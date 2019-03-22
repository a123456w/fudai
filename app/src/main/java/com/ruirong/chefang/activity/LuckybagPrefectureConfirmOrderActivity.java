package com.ruirong.chefang.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.OrderGoodsListAdapter;
import com.ruirong.chefang.bean.AddressItemBean;
import com.ruirong.chefang.bean.ObjectBean;
import com.ruirong.chefang.bean.PurchaseBean;
import com.ruirong.chefang.event.JiesuanEvent;
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
 * Created by chenlipeng on 2017/12/29 0029
 * describe:  特产 跳转到的  确认订单
 */
public class LuckybagPrefectureConfirmOrderActivity extends BaseActivity {

    @BindView(R.id.add_address)
    TextView addAddress;
    @BindView(R.id.goods_address_empty)
    RelativeLayout goodsAddressEmpty;
    @BindView(R.id.tv_consignee)
    TextView tvConsignee;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.order_details_image_position)
    ImageView orderDetailsImagePosition;
    @BindView(R.id.detailed_address)
    TextView detailedAddress;
    @BindView(R.id.goods_address)
    RelativeLayout goodsAddress;
    @BindView(R.id.goods_list)
    NoScrollListView goodsList;
    @BindView(R.id.express_price)
    TextView expressPrice;
    @BindView(R.id.goods_total)
    TextView goodsTotal;
    @BindView(R.id.remarks)
    EditText remarks;
    @BindView(R.id.tv_showings)
    TextView tvShowings;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    private PurchaseBean purchaseBean;
    private OrderGoodsListAdapter orderGoodsListAdapter;
    private List<PurchaseBean.ListBean> listBeans = new ArrayList<>();
    private int number = 0;
    private final int REQUEST_CODE_CHOOSE_AREA = 200;
    private AddressItemBean addressItemBean;
    private boolean isModify = false;
    private float zj;//总价
    private String specialty_id;//商品id;
    private String address_id = "";//地址id;
    private String goods_id = "";//生成订单的商品id;
    private String Specialty_Type = "";

    @Override
    public int getContentView() {
        return R.layout.activity_luckybag_prefecture_confirm_order;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("确认订单");

        orderGoodsListAdapter = new OrderGoodsListAdapter(goodsList);
        goodsList.setAdapter(orderGoodsListAdapter);


        Specialty_Type = getIntent().getStringExtra("Specialty_Type");
        specialty_id = getIntent().getStringExtra("specialty_id");
        if (TextUtils.isEmpty(specialty_id)) {
            finish();
        }
        Log.i("XXX", specialty_id + "");
    }

    @Override
    public void getData() {

        if (TextUtils.isEmpty(Specialty_Type)) {
            purchase(new PreferencesHelper(LuckybagPrefectureConfirmOrderActivity.this).getToken(),
                    specialty_id, null);
        } else {
            purchase(new PreferencesHelper(LuckybagPrefectureConfirmOrderActivity.this).getToken(),
                    specialty_id, "1");
        }
    }

    private void purchase(String token, String id, String types) {

        HttpHelp.getInstance().create(RemoteApi.class).purchase(token, id, types)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<PurchaseBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<PurchaseBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            purchaseBean = baseBean.data;
                            if (purchaseBean != null) {
                                if (purchaseBean.getAddress() != null && purchaseBean.getAddress().size() > 0) {
                                    goodsAddressEmpty.setVisibility(View.GONE);
                                    goodsAddress.setVisibility(View.VISIBLE);
                                    tvConsignee.setText(purchaseBean.getAddress().get(0).getRec_name());
                                    phone.setText(purchaseBean.getAddress().get(0).getMobile());
                                    detailedAddress.setText(purchaseBean.getAddress().get(0).getAddress());
                                    address_id = purchaseBean.getAddress().get(0).getId();
                                } else {
                                    goodsAddressEmpty.setVisibility(View.VISIBLE);
                                    goodsAddress.setVisibility(View.GONE);
                                }
                                listBeans = purchaseBean.getList();
                                if (listBeans != null && listBeans.size() > 0) {
                                    orderGoodsListAdapter.setData(listBeans);
                                }
                                if (listBeans != null && listBeans.size() > 0) {

                                    for (int i = 0; i < listBeans.size(); i++) {
                                        if (i != listBeans.size() - 1) {
                                            goods_id += listBeans.get(i).getId() + ",";
                                        } else {
                                            goods_id += listBeans.get(i).getId();
                                        }
                                        zj += Float.parseFloat(listBeans.get(i).getNow_price()) * Float.parseFloat(listBeans.get(i).getNum());
                                        Log.i("XXX", listBeans.get(i).getNow_price() + " " + listBeans.get(i).getNum());
                                    }

                                    if (zj >= Float.parseFloat(purchaseBean.getBanyou())) {
                                        expressPrice.setText("0.00");
                                    } else {
                                        expressPrice.setText(purchaseBean.getYunfen());
                                        zj = zj + Float.parseFloat(purchaseBean.getYunfen());
                                    }

                                    Log.i("XXX", zj + "");

                                    goodsTotal.setText(zj + "");
                                    tvTotalMoney.setText(zj + "");

                                }
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(LuckybagPrefectureConfirmOrderActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    @OnClick({R.id.tv_showings, R.id.goods_address, R.id.add_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_showings:

                if (TextUtils.isEmpty(goods_id)) {
                    ToastUtil.showToast(LuckybagPrefectureConfirmOrderActivity.this, "商品信息错误");
                    return;
                }
                if (zj == 0.00) {
                    ToastUtil.showToast(LuckybagPrefectureConfirmOrderActivity.this, "价格有误");
                    return;
                }

                if (TextUtils.isEmpty(address_id)) {
                    ToastUtil.showToast(LuckybagPrefectureConfirmOrderActivity.this, "暂无收货地址");
                    return;
                }


                if (TextUtils.isEmpty(Specialty_Type)) {
                    Log.i("XXX", "111111111111111");
                    addOrder(new PreferencesHelper(LuckybagPrefectureConfirmOrderActivity.this).getToken(), goods_id,
                            listBeans.get(0).getNum(), address_id,
                            zj + "", remarks.getText().toString().trim(), "1", expressPrice.getText().toString().trim());
                } else {
                    Log.i("XXX", "222222222222222");
                    addOrder(new PreferencesHelper(LuckybagPrefectureConfirmOrderActivity.this).getToken(), goods_id,
                            listBeans.get(0).getNum(), address_id,
                            zj + "", remarks.getText().toString().trim(), null, expressPrice.getText().toString().trim());
                }


                break;

            case R.id.goods_address://修改地址
                Intent intent = new Intent(LuckybagPrefectureConfirmOrderActivity.this, AddressActivity.class);
                intent.putExtra("type_choice", 2);
                startActivity(intent);
                break;
            case R.id.add_address://修改地址
                Intent intent1 = new Intent(LuckybagPrefectureConfirmOrderActivity.this, AddAddressActivity.class);
                intent1.putExtra("type_choice", 2);
                intent1.putExtra("type", 1);
                startActivity(intent1);
                break;
        }
    }

    private void addOrder(String token, String id, String number, String rid,
                          String price, String remarks, String is_cart, String distri_price) {

        HttpHelp.getInstance().create(RemoteApi.class).addOrder(token, id, number, rid, price, remarks, is_cart, distri_price)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ObjectBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<ObjectBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            Intent intent = new Intent(LuckybagPrefectureConfirmOrderActivity.this, ImmediatelyPaymentActivity.class);
                            intent.putExtra("place_type", "4");
                            intent.putExtra("shop_price", zj + "");
                            intent.putExtra("order_sn", baseBean.data.getNumber_bh());
                            startActivity(intent);
                            finish();
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(LuckybagPrefectureConfirmOrderActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //选择地区
        String address = intent.getStringExtra("address");
        if (address != null) {
            goodsAddressEmpty.setVisibility(View.GONE);
            goodsAddress.setVisibility(View.VISIBLE);
            addAddress.setVisibility(View.GONE);
            addressItemBean = new Gson().fromJson(address, AddressItemBean.class);
            address_id = addressItemBean.getId();
            tvConsignee.setText(addressItemBean.getRec_name());
            phone.setText(addressItemBean.getMobile());
            detailedAddress.setText(addressItemBean.getAddress());
        } else {
            ToastUtil.showToast(LuckybagPrefectureConfirmOrderActivity.this, "请重新选择地址~");
        }
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
