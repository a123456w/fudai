package com.ruirong.chefang.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.common.Constants;
import com.ruirong.chefang.event.OrderEvent;
import com.ruirong.chefang.event.RechargeSuccessEvent;
import com.ruirong.chefang.event.WxPayEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.AliPayUtil;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.util.WxPayUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2017/12/26 0026
 * describe:  储值页面
 */
public class StoredValueActivity extends BaseActivity {

    @BindView(R.id.c_price)
    TextView cPrice;
    @BindView(R.id.money_zfb)
    ImageView moneyZfb;
    @BindView(R.id.money_zfb_ll)
    RelativeLayout moneyZfbLl;
    @BindView(R.id.money_wx)
    ImageView moneyWx;
    @BindView(R.id.money_wx_ll)
    RelativeLayout moneyWxLl;
    @BindView(R.id.money_yl)
    ImageView moneyYl;
    @BindView(R.id.money_yl_ll)
    RelativeLayout moneyYlLl;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    private int type=1;
    private String shop_price;
    private String tag = "StoredValueActivity";
    private String shop_id;


    @Override
    public int getContentView() {
        return R.layout.activity_stored_value;
    }


    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("储值");
        EventBusUtil.register(this);
        shop_price = getIntent().getStringExtra("shop_price");
        shop_id = getIntent().getStringExtra("shop_id");
        if (shop_price == null || shop_id == null) {
            finish();
        }
        cPrice.setText(shop_price);

    }

    @Override
    public void getData() {


    }


    @OnClick({R.id.money_zfb_ll, R.id.money_wx_ll, R.id.money_yl_ll, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.money_zfb_ll:
                type = 1;
                moneyZfb.setImageResource(R.drawable.orange_choosed_icon);
                moneyWx.setImageResource(R.drawable.no_choosed);
                moneyYl.setImageResource(R.drawable.no_choosed);

                break;
            case R.id.money_wx_ll:
                type = 2;
                moneyZfb.setImageResource(R.drawable.no_choosed);
                moneyWx.setImageResource(R.drawable.orange_choosed_icon);
                moneyYl.setImageResource(R.drawable.no_choosed);

                break;
            case R.id.money_yl_ll:
                type = 3;
                moneyZfb.setImageResource(R.drawable.no_choosed);
                moneyWx.setImageResource(R.drawable.no_choosed);
                moneyYl.setImageResource(R.drawable.orange_choosed_icon);

                break;

            case R.id.tv_submit:

                if (type == 1) {
                    storageZ(new PreferencesHelper(StoredValueActivity.this).getToken(), shop_id, shop_price, type + "");
//                    storageZ(new PreferencesHelper(StoredValueActivity.this).getToken(), shop_id, "0.01", type + "");
                } else if (type == 2) {
                    Constants.WEIXIN_PAY_TYPE=tag;
//                    WxPayUtil.storage(StoredValueActivity.this,
//                            new PreferencesHelper(StoredValueActivity.this).getToken(),
//                            shop_id, "0.03", type + "");
                    WxPayUtil.storage(StoredValueActivity.this,
                            new PreferencesHelper(StoredValueActivity.this).getToken(),
                            shop_id, shop_price, type + "");
                }

                break;

        }

    }

    /**
     * 支付宝支付
     */
    public void  storageZ(String token, String shop_id, String money, String paytype) {
        showLoadingDialog("");
        HttpHelp.getInstance().create(RemoteApi.class).storageZ(token, shop_id, money, paytype)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this, null) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {
                            new AliPayUtil().pay(baseBean.data, StoredValueActivity.this, tag);
                        } else if (baseBean.code==4){
                            ToolUtil.loseToLogin(StoredValueActivity.this);
                        } else {
                            ToastUtil.showToast(StoredValueActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                        ToastUtil.showToast(StoredValueActivity.this, getString(R.string.net_error));
                    }
                });
    }


    /**
     * 支付宝支付回调
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void aliPaySuccess(OrderEvent event) {
        if (event.tag.equals(tag)) {
            if (event.success) {
                EventBusUtil.post(new RechargeSuccessEvent());
                showRechargeSuccess();
            } else {
                ToastUtil.showToast(this, "支付失败");
            }

        }
    }

    /**
     * 微信支付回调
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void wxPaySuccess(WxPayEvent event) {
        if (event.tag.equals(tag)) {
            switch (event.code) {
                case 0:

                    EventBusUtil.post(new RechargeSuccessEvent());
                    showRechargeSuccess();
                    break;
                case -1:
                    ToastUtil.showToast(this, "支付失败");
                    break;
                case -2:
                    ToastUtil.showToast(this, "支付取消");
                    break;
            }
        }
    }


    /**
     * 支付成功弹窗
     */
    private void showRechargeSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_storage_successs, null, false);

        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final Intent intent = new Intent(StoredValueActivity.this, BusinessReviewsActivity.class);
        intent.putExtra("shop_id", shop_id);
        intent.putExtra("type", "8");
        TextView tvHint = (TextView)view.findViewById(R.id.tv_hint);
        tvHint.setText("储值成功");
        view.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                startActivity(intent);
                finish();
            }
        });
        view.findViewById(R.id.tv_recharge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (type == 1) {
                    storageZ(new PreferencesHelper(StoredValueActivity.this).getToken(), shop_id, shop_price, type + "");
//                    storageZ(new PreferencesHelper(StoredValueActivity.this).getToken(), shop_id, "0.01", type + "");
                } else if (type == 2) {
                    Constants.WEIXIN_PAY_TYPE=tag;
//                    WxPayUtil.storage(StoredValueActivity.this,
//                            new PreferencesHelper(StoredValueActivity.this).getToken(),
//                            shop_id, "0.03", type + "");
                    WxPayUtil.storage(StoredValueActivity.this,
                            new PreferencesHelper(StoredValueActivity.this).getToken(),
                            shop_id, shop_price, type + "");
                }

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }


}
