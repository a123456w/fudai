package com.ruirong.chefang.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
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
 * Created by Administrator on 2018/3/20.
 * 商城 确认订单
 */

public class ShopCartConfirmActivity extends BaseActivity {

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
    @BindView(R.id.shop_arrive)
    ImageView shopArrive;
    @BindView(R.id.shop_remarks)
    EditText shopRemarks;
    @BindView(R.id.purchase)
    TextView purchase;
    @BindView(R.id.shopping_bean)
    TextView shoppingBean;
    @BindView(R.id.add_address)
    TextView addAddress;
    @BindView(R.id.distribution_price)
    TextView distributionPrice;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.goods_price)
    TextView goodsPrice;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.shop_arrive_nei)
    ImageView shopArriveNei;
    @BindView(R.id.shop_arrive_wai)
    ImageView shopArriveWai;
    @BindView(R.id.dainwai)
    RelativeLayout dainwai;
    @BindView(R.id.rl_peisongfei)
    RelativeLayout rlPeisongfei;
    private String myShopId = "";
    private JiesuanBean jiesuanBean;
    private ShopCartConfirmAdapter adapter;
    private List<JiesuanBean.GoodsBean> list = new ArrayList<>();
    private String address_id = "";
    private boolean isTrue = false;
    private final int REQUEST_CODE_CHOOSE_AREA = 200;
    private AddressItemBean addressItemBean;
    private int type = 1;//1是到店自取 2是店内配送 3是店外配送
    private AlertDialog dialog;

    private String isStart = "3";

    @Override
    public int getContentView() {
        return R.layout.activity_shop_cart_confirm;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();/***/

        myShopId = getIntent().getStringExtra("shop_id");
        isStart = getIntent().getStringExtra("is_start");


        if ("3".equals(isStart)) {
            rlPeisongfei.setVisibility(View.VISIBLE);
        } else {
            rlPeisongfei.setVisibility(View.GONE);
        }
   /*     if (myShopId == null) {
            finish();
        }*/

        Log.i("XXX", myShopId);


        adapter = new ShopCartConfirmAdapter(shopGoodslist);
        shopGoodslist.setAdapter(adapter);

    }

    @Override
    public void getData() {
        jiesuan(new PreferencesHelper(ShopCartConfirmActivity.this).getToken(), myShopId);
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
                                    addAddress.setVisibility(View.GONE);

                                    addressContent.setText(jiesuanBean.getAddress().get(0).getAddress());
                                    addressUser.setText(jiesuanBean.getAddress().get(0).getRec_name() + " " + jiesuanBean.getAddress().get(0).getMobile());
                                    address_id = jiesuanBean.getAddress().get(0).getId();

                                } else {
                                    addressContent.setVisibility(View.GONE);
                                    addressUser.setVisibility(View.GONE);
                                    addAddress.setVisibility(View.VISIBLE);
                                }

                                GlideUtil.displayAvatar(ShopCartConfirmActivity.this, Constants.IMG_HOST + jiesuanBean.getLogo(), shopHeadpic);
                                shopHeadname.setText(jiesuanBean.getShop_name());

                                list = jiesuanBean.getGoods();
                                if (list != null && list.size() > 0) {
                                    adapter.setData(list);
                                }

                                distributionPrice.setText("￥" + jiesuanBean.getYun_fee());
                                goodsPrice.setText("￥" + jiesuanBean.getTotal_price());
                                total.setText("￥" + jiesuanBean.getAmount_price());

                                if ("1".equals(jiesuanBean.getIs_distribute())) {
                                    dainwai.setVisibility(View.VISIBLE);
                                } else if ("0".equals(jiesuanBean.getIs_distribute())) {
                                    dainwai.setVisibility(View.GONE);
                                }

                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(ShopCartConfirmActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.i("XXXX", throwable.getLocalizedMessage());
                    }
                });

    }


    @OnClick({R.id.add_address, R.id.purchase, R.id.shopping_bean, R.id.shop_arrive,
            R.id.address_title, R.id.iv_left, R.id.shop_arrive_nei, R.id.shop_arrive_wai})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_address:

                Intent intent1 = new Intent(ShopCartConfirmActivity.this, AddAddressActivity.class);
                intent1.putExtra("type_choice", 1);
                intent1.putExtra("type", 1);
                startActivity(intent1);

                break;

            case R.id.shop_arrive_nei:
                type = 2;
                setPic();
                addressTitle.setVisibility(View.INVISIBLE);
                shopArriveNei.setImageResource(R.drawable.orange_choosed_icon);

                final AlertDialog.Builder builder = new AlertDialog.Builder(ShopCartConfirmActivity.this);
                builder.setTitle("温馨提示");
                builder.setMessage("请在备注中填写店内地址\n" +
                        "如（房间号、桌牌号等信息以便服务人员及时送达）");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();


                break;

            case R.id.shop_arrive_wai:
                type = 3;
                setPic();
                addressTitle.setVisibility(View.VISIBLE);
                shopArriveWai.setImageResource(R.drawable.orange_choosed_icon);

                if (jiesuanBean.getAddress() != null && jiesuanBean.getAddress().size() > 0) {
                    addressContent.setVisibility(View.VISIBLE);
                    addressUser.setVisibility(View.VISIBLE);
                    addAddress.setVisibility(View.GONE);

                    addressContent.setText(jiesuanBean.getAddress().get(0).getAddress());
                    addressUser.setText(jiesuanBean.getAddress().get(0).getRec_name() + " " + jiesuanBean.getAddress().get(0).getMobile());
                    address_id = jiesuanBean.getAddress().get(0).getId();

                } else {
                    addressContent.setVisibility(View.GONE);
                    addressUser.setVisibility(View.GONE);
                    addAddress.setVisibility(View.VISIBLE);
                }


                break;

            case R.id.purchase:
                if (type == 3) {
                    if (TextUtils.isEmpty(address_id)) {
                        ToastUtil.showToast(ShopCartConfirmActivity.this, "请选择收货地址！ ");
                        return;
                    }
                    suborder(new PreferencesHelper(ShopCartConfirmActivity.this).getToken(), address_id, myShopId, shopRemarks.getText().toString().trim(), type + "");
                } else {
                    suborder(new PreferencesHelper(ShopCartConfirmActivity.this).getToken(), "0", myShopId, shopRemarks.getText().toString().trim(), type + "");
                }


                break;

            case R.id.shopping_bean:

                Intent intent11 = new Intent(ShopCartConfirmActivity.this, RechargeActivity.class);
                intent11.putExtra("int_data", 100);
                startActivity(intent11);

                break;
            case R.id.address_title://修改地址
                Intent intentX = new Intent(ShopCartConfirmActivity.this, AddressActivity.class);
                intentX.putExtra("type_choice", 1);
                startActivity(intentX);

                break;

            case R.id.shop_arrive:
                type = 1;
                setPic();
                addressTitle.setVisibility(View.INVISIBLE);
                shopArrive.setImageResource(R.drawable.orange_choosed_icon);

                break;
            case R.id.iv_left:
                finish();
                break;
        }
    }

    private void setPic() {
        shopArrive.setImageResource(R.drawable.no_choosed);
        shopArriveNei.setImageResource(R.drawable.no_choosed);
        shopArriveWai.setImageResource(R.drawable.no_choosed);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //选择地区
        String address = intent.getStringExtra("address");
        if (address != null) {
            addressContent.setVisibility(View.VISIBLE);
            addressUser.setVisibility(View.VISIBLE);
            addAddress.setVisibility(View.GONE);
            addressItemBean = new Gson().fromJson(address, AddressItemBean.class);
            address_id = addressItemBean.getId();
            addressUser.setText("收货人:" + addressItemBean.getRec_name() + " " + addressItemBean.getMobile());
            addressContent.setText(addressItemBean.getAddress());
        } else {
            ToastUtil.showToast(ShopCartConfirmActivity.this, "请重新选择地址~");
        }
    }

    /**
     * 提交订单
     *
     * @param token
     * @param address_id
     * @param shop_id
     * @param trim
     */
    private void suborder(final String token, String address_id, String shop_id, String trim, String is_since) {
        HttpHelp.getInstance().create(RemoteApi.class).suborder(token, address_id, shop_id, trim, is_since, "0", "0", "0", "0", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        Log.i("XXX", baseBean.code + " " + baseBean.message);
                        if (baseBean.code == 0) {
                            if (jiesuanBean != null && baseBean.data != null) {
                                Intent intent = new Intent(ShopCartConfirmActivity.this, ImmediatelyPaymentActivity.class);
                                intent.putExtra("place_type", "1");
                                intent.putExtra("shop_price", jiesuanBean.getAmount_price() + "");
                                intent.putExtra("order_sn", baseBean.data);
                                if (TextUtils.isEmpty(token)) {
                                    ToolUtil.goToLogin(ShopCartConfirmActivity.this);
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


}