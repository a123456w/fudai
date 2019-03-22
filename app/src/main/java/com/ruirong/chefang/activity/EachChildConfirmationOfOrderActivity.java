package com.ruirong.chefang.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DialogUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.payDialog.PayPwdView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.AddressItemBean;
import com.ruirong.chefang.bean.ImmediatelyBean;
import com.ruirong.chefang.bean.ObjectBean;
import com.ruirong.chefang.event.JiesuanEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.widget.PasswordInputView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/2.
 * 福袋专区 提交订单接口
 */

public class EachChildConfirmationOfOrderActivity extends BaseActivity implements PayPwdView.InputCallBack {
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
    @BindView(R.id.goods_pic)
    ImageView goodsPic;
    @BindView(R.id.goods_name)
    TextView goodsName;
    @BindView(R.id.goods_price)
    TextView goodsPrice;
    @BindView(R.id.goods_old_price)
    TextView goodsOldPrice;
    @BindView(R.id.goods_count)
    TextView goodsCount;
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
    @BindView(R.id.p_ll)
    RelativeLayout pLl;
    private String specialty_id;
    private float zj;
    private ImmediatelyBean immediatelyBean;
    private String address_id;
    private AddressItemBean addressItemBean;
    private Dialog mDialogs;
    private PasswordInputView pass_pwd;
    private String number_bh;

    @Override
    public int getContentView() {
        return R.layout.activity_each_child_confirmation_of_prder;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("确认订单");

        specialty_id = getIntent().getStringExtra("specialty_id");
        Log.i("XXX", specialty_id);
        if (specialty_id == null) {
            finish();
        }


    }

    @Override
    public void getData() {

        if (TextUtils.isEmpty(new PreferencesHelper(this).getToken())) {
            ToolUtil.goToLogin(this);
        } else {
            immediately(new PreferencesHelper(this).getToken(), specialty_id);
        }

    }

    private void immediately(String token, String id) {
        HttpHelp.getInstance().create(RemoteApi.class).immediately(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ImmediatelyBean>>(this, null) {

                    @Override
                    public void onNext(BaseBean<ImmediatelyBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            immediatelyBean = baseBean.data;
                            if (immediatelyBean != null) {
                                if (immediatelyBean.getAddress() != null && immediatelyBean.getAddress().size() > 0) {
                                    goodsAddressEmpty.setVisibility(View.GONE);
                                    goodsAddress.setVisibility(View.VISIBLE);
                                    tvConsignee.setText(immediatelyBean.getAddress().get(0).getRec_name());
                                    phone.setText(immediatelyBean.getAddress().get(0).getMobile());
                                    detailedAddress.setText(immediatelyBean.getAddress().get(0).getAddress());
                                    address_id = immediatelyBean.getAddress().get(0).getId();
                                    Log.i("XXX", address_id);
                                } else {
                                    goodsAddressEmpty.setVisibility(View.VISIBLE);
                                    goodsAddress.setVisibility(View.GONE);
                                }

                                GlideUtil.display(EachChildConfirmationOfOrderActivity.this, Constants.IMG_HOST + immediatelyBean.getPic(), goodsPic);
                                goodsName.setText(immediatelyBean.getTitle());
                                goodsPrice.setText(immediatelyBean.getNow_price());
                                goodsOldPrice.setText("￥" + immediatelyBean.getBefore_price());
                                goodsOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                goodsCount.setText("x" + immediatelyBean.getNum() + "");


                                if (immediatelyBean.getNow_price() != null) {

                                    zj = Float.parseFloat(immediatelyBean.getNow_price()) * Float.parseFloat(immediatelyBean.getNum() + "");

                                    Log.i("XXX", zj + "");

                                    goodsTotal.setText(zj + "");
                                    tvTotalMoney.setText(zj + "");

                                }
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(EachChildConfirmationOfOrderActivity.this);
                        }
                        ToastUtil.showToast(EachChildConfirmationOfOrderActivity.this, baseBean.message);
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

                if (TextUtils.isEmpty(specialty_id)) {
                    ToastUtil.showToast(EachChildConfirmationOfOrderActivity.this, "商品信息错误");
                    return;
                }
                if (zj == 0.00) {
                    ToastUtil.showToast(EachChildConfirmationOfOrderActivity.this, "价格有误");
                    return;
                }

                if (TextUtils.isEmpty(address_id)) {
                    ToastUtil.showToast(EachChildConfirmationOfOrderActivity.this, "暂无收货地址");
                    return;
                }

                 orderChild(new PreferencesHelper(EachChildConfirmationOfOrderActivity.this).getToken(),
                        specialty_id, "1", address_id, remarks.getText().toString().trim());

                break;

            case R.id.goods_address://修改地址
                Intent intent = new Intent(EachChildConfirmationOfOrderActivity.this, AddressActivity.class);
                intent.putExtra("type_choice", 3);
                startActivity(intent);
                break;
            case R.id.add_address://新增地址
                Intent intent1 = new Intent(EachChildConfirmationOfOrderActivity.this, AddAddressActivity.class);
                intent1.putExtra("type_choice", 3);
                intent1.putExtra("type", 1);
                startActivity(intent1);
                break;
        }
    }

    private void orderChild(String token, String id, String number,
                            String rid, String remarks) {
        HttpHelp.getInstance().create(RemoteApi.class).orderChild(token, id, number, rid, remarks)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ObjectBean>>(this, null) {

                    @Override
                    public void onNext(BaseBean<ObjectBean> baseBean) {
                        super.onNext(baseBean);
                        ToastUtil.showToast(EachChildConfirmationOfOrderActivity.this, baseBean.message);
                        if (baseBean.code == 0) {
                            if (!TextUtils.isEmpty(baseBean.data.getNumber_bh())) {
                                number_bh = baseBean.data.getNumber_bh();
                                showPayDialog();
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(EachChildConfirmationOfOrderActivity.this);
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
            ToastUtil.showToast(EachChildConfirmationOfOrderActivity.this, "请重新选择地址~");
        }
    }


    /**
     * 福币支付接口
     */
    private void paychild(String token, String order_number, String pay_pass) {
        HttpHelp.getInstance().create(RemoteApi.class).paychild(token, order_number, pay_pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(EachChildConfirmationOfOrderActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        Log.i("XXX", baseBean.code + "  " + baseBean.message);
                        if (baseBean.code == 0) {
                            DialogUtil.dismissPayDialog();
                            finish();
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(EachChildConfirmationOfOrderActivity.this);
                        }
                        ToastUtil.showToast(EachChildConfirmationOfOrderActivity.this, baseBean.message);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });

    }


    private void showPayDialog() {
        //弹出支付密码输入框
        DialogUtil.showPayDialog(this, "请输入支付密码",  "", "", getSupportFragmentManager());
    }

    @Override
    public void onInputFinish(String result) {
        paychild(new PreferencesHelper(EachChildConfirmationOfOrderActivity.this).getToken(),
                this.number_bh, result);
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
