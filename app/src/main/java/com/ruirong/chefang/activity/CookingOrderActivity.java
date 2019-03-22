package com.ruirong.chefang.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.ShopCartConfirmAdapter;
import com.ruirong.chefang.bean.AddressItemBean;
import com.ruirong.chefang.bean.JiesuanBean;
import com.ruirong.chefang.event.JiesuanEvent;
import com.ruirong.chefang.event.UpdateAllGoodsEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 餐饮的确定订单
 * Created by dillon on 2018/5/6.
 */

public class CookingOrderActivity extends BaseActivity {

    @BindView(R.id.order_head)
    RelativeLayout orderHead;
    @BindView(R.id.address_content)
    TextView addressContent;
    @BindView(R.id.address_user)
    TextView addressUser;
    @BindView(R.id.address_title)
    LinearLayout addressTitle;
    @BindView(R.id.shop_headpic)
    ImageView shopHeadpic;
    @BindView(R.id.shop_headname)
    TextView shopHeadname;
    @BindView(R.id.shop_goodslist)
    NoScrollListView shopGoodslist;
    @BindView(R.id.shop_remarks)
    EditText shopRemarks;
    @BindView(R.id.purchase)
    TextView purchase;
    @BindView(R.id.shopping_bean)
    TextView shoppingBean;
    @BindView(R.id.distribution_price)
    TextView distributionPrice;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.goods_price)
    TextView goodsPrice;
    @BindView(R.id.iv_left)
    ImageView ivLeft;

    private String myShopId = "";
    private JiesuanBean jiesuanBean;
    private ShopCartConfirmAdapter adapter;
    private List<JiesuanBean.GoodsBean> list = new ArrayList<>();


    private boolean isTrue = false;
    private final int REQUEST_CODE_CHOOSE_AREA = 200;
    private AddressItemBean addressItemBean;
    private int type = 1;//1是到店自取 2是店内配送 3是店外配送
    private AlertDialog dialog;


    private static final String SHOPCARTTVCALL = "SHOPCARTTVCALL";
    private static final String SHOPCARTTVPHONE = "SHOPCARTTVPHONE";
    private static final String SHOPCARTTVTIME = "SHOPCARTTVTIME";
    private static final String SHOPCARTTVPEOPLENUM = "SHOPCARTTVPEOPLENUM";
    private static final String SHOPMYCANZHUONNUM = "SHOPMYCANZHUONNUM";
    private String mTvCall  = "";
    private String mTvPhone  = "";
    private String mTvTime = "";
    private String mTvPeople = "";
    private String mCanZhuoNum = "" ;
    @Override
    public int getContentView() {
        return R.layout.activity_cookingorder;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();/***/

        myShopId = getIntent().getStringExtra("shop_id");
   /*     if (myShopId == null) {
            finish();
        }*/

        Log.i("XXX", myShopId);

        mTvCall = getIntent().getStringExtra(SHOPCARTTVCALL);
        mTvPeople = getIntent().getStringExtra(SHOPCARTTVPEOPLENUM);
        mTvTime = getIntent().getStringExtra(SHOPCARTTVTIME);
        mTvPhone = getIntent().getStringExtra(SHOPCARTTVPHONE);

        mCanZhuoNum = getIntent().getStringExtra(SHOPMYCANZHUONNUM);
        adapter = new ShopCartConfirmAdapter(shopGoodslist);
        shopGoodslist.setAdapter(adapter);

    }

    @Override
    public void getData() {
        jiesuan(new PreferencesHelper(CookingOrderActivity.this).getToken(), myShopId);
    }

    /**
     * 订单详情
     *
     * @param token
     * @param shop_id
     */
    private void jiesuan(String token, String shop_id) {
        HttpHelp.getInstance().create(RemoteApi.class).jiesuan(token, shop_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<JiesuanBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<JiesuanBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            jiesuanBean = baseBean.data;
                            if (jiesuanBean != null) {
                                if (jiesuanBean.getAddress() != null && jiesuanBean.getAddress().size() > 0) {
                                    addressContent.setVisibility(View.VISIBLE);
                                    addressUser.setVisibility(View.VISIBLE);

                                    addressContent.setText(jiesuanBean.getAddress().get(0).getAddress());
                                    addressUser.setText(jiesuanBean.getAddress().get(0).getRec_name() + " " + jiesuanBean.getAddress().get(0).getMobile());

                                } else {
                                    addressContent.setVisibility(View.GONE);
                                    addressUser.setVisibility(View.GONE);
                                }

                                GlideUtil.displayAvatar(CookingOrderActivity.this, Constants.IMG_HOST + jiesuanBean.getLogo(), shopHeadpic);
                                shopHeadname.setText(jiesuanBean.getShop_name());

                                list = jiesuanBean.getGoods();
                                if (list != null && list.size() > 0) {
                                    adapter.setData(list);
                                }

                                distributionPrice.setText("￥" + jiesuanBean.getYun_fee());
                                goodsPrice.setText("￥" + jiesuanBean.getTotal_price());
                                total.setText("￥" + jiesuanBean.getAmount_price());


                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(CookingOrderActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.i("XXXX", throwable.getLocalizedMessage());
                    }
                });

    }


    @OnClick({ R.id.purchase, R.id.shopping_bean,
            R.id.iv_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.purchase:
                    suborder(new PreferencesHelper(CookingOrderActivity.this).getToken(), "0", myShopId, shopRemarks.getText().toString().trim(), "0");

                break;

            case R.id.shopping_bean:
                Intent intent11 = new Intent(CookingOrderActivity.this, RechargeActivity.class);
                intent11.putExtra("int_data", 100);
                startActivity(intent11);
                break;
            case R.id.iv_left:
                finish();
                break;
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    /**
     * 提交订单
     *
     * @param token
     * @param address_id
     * @param shop_id
     * @param trim
     */
    private void suborder(final String token, String address_id, String shop_id, String trim, String is_since ) {
        HttpHelp.getInstance().create(RemoteApi.class).suborder(token, address_id, shop_id, trim, is_since,mCanZhuoNum,mTvTime,mTvPeople,mTvCall,mTvPhone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        Log.i("XXX", baseBean.code + " " + baseBean.message);
                        if (baseBean.code == 0) {
                            if (jiesuanBean != null && baseBean.data != null) {
                                Intent intent = new Intent(CookingOrderActivity.this, ImmediatelyPaymentActivity.class);
                                intent.putExtra("place_type", "1");
                                intent.putExtra("shop_price", jiesuanBean.getAmount_price() + "");
                                intent.putExtra("order_sn", baseBean.data);
                                if (TextUtils.isEmpty(token)) {
                                    ToolUtil.goToLogin(CookingOrderActivity.this);
                                } else {
                                    startActivity(intent);
                                }
                                EventBusUtil.post(new UpdateAllGoodsEvent());
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.i("XXX", throwable.getLocalizedMessage());
                    }
                });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hello(JiesuanEvent event) {
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    /**
     * 传递数据给这个页面
     *
     * @param context
     * @param tvCall    称呼
     * @param tvPhone   电话
     * @param tvTime    时间
     * @param peopleNum 人数
     */
    public static void startActivityWithParmeter(Context context, String shopid, String tvCall, String tvPhone, String tvTime, String peopleNum,String myzhuonum) {
        Intent intent = new Intent(context, CookingOrderActivity.class);
        intent.putExtra("shop_id",shopid);
        intent.putExtra(SHOPCARTTVCALL, tvCall);
        intent.putExtra(SHOPCARTTVPHONE, tvPhone);
        intent.putExtra(SHOPCARTTVTIME, tvTime);
        intent.putExtra(SHOPCARTTVPEOPLENUM, peopleNum);
        intent.putExtra(SHOPMYCANZHUONNUM,myzhuonum);

        context.startActivity(intent);
    }

}
