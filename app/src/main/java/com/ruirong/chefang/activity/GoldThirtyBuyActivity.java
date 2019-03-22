package com.ruirong.chefang.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
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
import com.qlzx.mylibrary.util.DialogUtil;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.payDialog.PayPwdView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.GoldBuyAdapter;
import com.ruirong.chefang.bean.BuyProfitBean;
import com.ruirong.chefang.bean.GoldBuyBean;
import com.ruirong.chefang.bean.ProdeceBean;
import com.ruirong.chefang.bean.UserInforBean;
import com.ruirong.chefang.bean.WalletBean;
import com.ruirong.chefang.event.PayPwdSuccedEvent;
import com.ruirong.chefang.event.RechargeSuccessEvent;
import com.ruirong.chefang.event.UpdateInformationEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2018/1/10 0010
 * describe:  种植365天定期
 */

public class GoldThirtyBuyActivity extends BaseActivity implements PayPwdView.InputCallBack {
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.et_cash)
    EditText etCash;
    @BindView(R.id.cash)
    RelativeLayout cash;
    @BindView(R.id.tv_balance)
    TextView tvBalance;

    @BindView(R.id.agreement)
    TextView agreement;
    @BindView(R.id.rb_choice)
    CheckBox rbChoice;
    @BindView(R.id.tv_aggrement)
    TextView tvAggrement;
    @BindView(R.id.ll_aggrement)
    LinearLayout llAggrement;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.tv_introduce)
    TextView tvIndroduce;
    @BindView(R.id.tv_moneyo1)
    TextView tvMoneyo1;
    @BindView(R.id.rl_chong1)
    RelativeLayout rlChong1;
    @BindView(R.id.tv_profit1)
    TextView tvProfit1;
    @BindView(R.id.rl_grow1)
    RelativeLayout rlGrow1;
    @BindView(R.id.tv_moneyo2)
    TextView tvMoneyo2;
    @BindView(R.id.tv_profit2)
    TextView tvProfit2;
    @BindView(R.id.rl_grow2)
    RelativeLayout rlGrow2;
    @BindView(R.id.tv_moneyo3)
    TextView tvMoneyo3;
    @BindView(R.id.rl_chong3)
    RelativeLayout rlChong3;
    @BindView(R.id.tv_profit3)
    TextView tvProfit3;
    @BindView(R.id.rl_grow3)
    RelativeLayout rlGrow3;
    @BindView(R.id.rv_buy)
    LinearLayout rvBuy;
    private ProdeceBean.DataBean prodeceBean;
    private BaseSubscriber<BaseBean<WalletBean>> walletSubscriber;
    private WalletBean walletBean;
    private List<GoldBuyBean> goldBuyBeanList = new ArrayList<>();
    private GoldBuyAdapter goldBuyAdapter;
    private BaseSubscriber<BaseBean<BuyProfitBean>> buyGoldSubscriber;
    private int isSetPwd;


    @Override
    public int getContentView() {
        return R.layout.activity_goldaddbuy;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        EventBusUtil.register(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        prodeceBean = (ProdeceBean.DataBean) getIntent().getSerializableExtra("PRODECEBEAN");
        PreferencesHelper preferencesHelper = new PreferencesHelper(GoldThirtyBuyActivity.this);
        Gson gson = new Gson();
        String userInfo = preferencesHelper.getUserInfo();
        UserInforBean userInforBean = gson.fromJson(userInfo, UserInforBean.class);
        isSetPwd = userInforBean.getIs_has_pay_pass();
        titleBar.setTitleText("种植" + prodeceBean.getDate_line() + "天定期");
        tvIndroduce.setText("种植" + prodeceBean.getDate_line() + "天(" + prodeceBean.getRate_interest() + "%/天)");

        etCash.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
              /*  if (!TextUtils.isEmpty(s.toString().trim())) {
                    if (Integer.parseInt(s.toString().trim()) > Float.parseFloat(walletBean.getGold())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(GoldThirtyBuyActivity.this);
                        builder.setTitle("提示");
                        builder.setMessage("余额不足，请去购买金豆");
                        builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RechargeActivity.startActivity(GoldThirtyBuyActivity.this, RechargeActivity.class);
                            }
                        });
                        builder.create().show();
                    }
                }*/
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void getData() {

        getMoney(new PreferencesHelper(GoldThirtyBuyActivity.this).getToken());
        initGoldData();
    }

    public void initGoldData() {
        tvMoneyo1.setText("1000");
        tvMoneyo2.setText("5000");
        tvMoneyo3.setText("10000");
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        tvProfit1.setText(decimalFormat.format(Float.parseFloat(prodeceBean.getRate_interest()) * 10 * Integer.parseInt(prodeceBean.getDate_line())));
        tvProfit2.setText(decimalFormat.format(Float.parseFloat(prodeceBean.getRate_interest()) * 50 * Integer.parseInt(prodeceBean.getDate_line())));
        tvProfit3.setText(decimalFormat.format(Float.parseFloat(prodeceBean.getRate_interest()) * 100 * Integer.parseInt(prodeceBean.getDate_line())));

/*
        GoldBuyBean goldBuyBean = new GoldBuyBean();
        goldBuyBean.setMoney("1000");
        goldBuyBean.setProfit(decimalFormat.format(Float.parseFloat(prodeceBean.getRate_interest()) * 10 * Integer.parseInt(prodeceBean.getDate_line())));
        GoldBuyBean goldBuyBean1 = new GoldBuyBean();
        goldBuyBean1.setMoney("5000");
        goldBuyBean1.setProfit(decimalFormat.format(Float.parseFloat(prodeceBean.getRate_interest()) * 50 * Integer.parseInt(prodeceBean.getDate_line())));
        GoldBuyBean goldBuyBean2 = new GoldBuyBean();
        goldBuyBean2.setMoney("10000");
        goldBuyBean2.setProfit(decimalFormat.format(Float.parseFloat(prodeceBean.getRate_interest()) * 100 * Integer.parseInt(prodeceBean.getDate_line())));
        goldBuyBeanList.add(goldBuyBean);
        goldBuyBeanList.add(goldBuyBean1);
        goldBuyBeanList.add(goldBuyBean2);
        goldBuyAdapter.setData(goldBuyBeanList);*/
    }

    public void getMoney(String token) {
        walletSubscriber = new BaseSubscriber<BaseBean<WalletBean>>(this, null) {
            @Override
            public void onNext(BaseBean<WalletBean> walletBeanBaseBean) {
                super.onNext(walletBeanBaseBean);
                if (walletBeanBaseBean.code == 0) {
                    walletBean = walletBeanBaseBean.data;
                    if (walletBean != null) {
                        tvBalance.setText(walletBean.getGold());
                    }
                } else if (walletBeanBaseBean.code == 4) {
                    ToolUtil.loseToLogin(GoldThirtyBuyActivity.this);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        };

        HttpHelp.getInstance().create(RemoteApi.class).wallet(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(walletSubscriber);
    }

    @OnClick({R.id.tv_aggrement, R.id.tv_buy, R.id.rl_grow1, R.id.rl_grow2, R.id.rl_grow3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_grow1:
                etCash.setText("1000");
                rlGrow1.setBackgroundResource(R.drawable.shape_growgoldy);
                rlGrow2.setBackgroundResource(R.drawable.shape_growgoldw);
                rlGrow3.setBackgroundResource(R.drawable.shape_growgoldw);
                break;
            case R.id.rl_grow2:
                etCash.setText("5000");
                rlGrow2.setBackgroundResource(R.drawable.shape_growgoldy);
                rlGrow1.setBackgroundResource(R.drawable.shape_growgoldw);
                rlGrow3.setBackgroundResource(R.drawable.shape_growgoldw);
                break;
            case R.id.rl_grow3:
                etCash.setText("10000");
                rlGrow3.setBackgroundResource(R.drawable.shape_growgoldy);
                rlGrow2.setBackgroundResource(R.drawable.shape_growgoldw);
                rlGrow1.setBackgroundResource(R.drawable.shape_growgoldw);
                break;

            case R.id.tv_aggrement:
                WebActivity.startActivity(GoldThirtyBuyActivity.this, "金豆增值服务协议", Constants.IMG_HOST + "/index.php/About/fund.html");
                break;
            case R.id.tv_buy:
                if (!rbChoice.isChecked()) {
                    ToastUtil.showToast(GoldThirtyBuyActivity.this, "请同意用户协议");
                    return;
                } else {
                    if (TextUtils.isEmpty(etCash.getText().toString().trim())) {
                        ToastUtil.showToast(GoldThirtyBuyActivity.this, "理财金额不能为空");
                        return;
                    } else {
                        if (Integer.parseInt(etCash.getText().toString().trim()) > Float.parseFloat(walletBean.getGold())) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(GoldThirtyBuyActivity.this);
                            builder.setTitle("提示");
                            builder.setMessage("余额不足，请去充值");
                            builder.setNegativeButton("取消", null);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    RechargeActivity.startActivity(GoldThirtyBuyActivity.this, RechargeActivity.class);
                                }
                            });
                            builder.create().show();
                        } else {
                            if (Integer.parseInt(etCash.getText().toString().trim()) / 1000 > 0 && Integer.parseInt(etCash.getText().toString().trim()) % 1000 == 0) {

                                if (isSetPwd == 1) {
                                    LogUtil.d("dddddddddddddddd", isSetPwd + "mim");
                                    DialogUtil.showPayDialog(GoldThirtyBuyActivity.this, "请输入支付密码", etCash.getText().toString().trim(), "增值金额是" + etCash.getText().toString().trim(), getSupportFragmentManager());
                                } else if (isSetPwd == 0) {
                                    LogUtil.d("dddddddddddddddd", isSetPwd + "mimddd");
                                    AlertDialog(2, "种植金豆前必先设置支付密码");
                                }

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(GoldThirtyBuyActivity.this);
                                builder.setTitle("提示");
                                builder.setMessage("理财金额必须是1000整倍数");
                                builder.setNegativeButton("取消", null);
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        etCash.getText().clear();
                                    }
                                });
                                builder.create().show();
                            }
                        }
                    }
                }
                break;
        }
    }


    public void buyProfit(String pass_word, String f_id, String pay_gold, String token) {
        buyGoldSubscriber = new BaseSubscriber<BaseBean<BuyProfitBean>>(this, null) {
            @Override
            public void onNext(BaseBean<BuyProfitBean> stringBaseBean) {
                super.onNext(stringBaseBean);
                DialogUtil.dismissPayDialog();
                if (stringBaseBean.code == 0) {

                    EventBus.getDefault().post(new RechargeSuccessEvent());
                    EventBus.getDefault().post(new UpdateInformationEvent());

                    AlertDialog.Builder builder = new AlertDialog.Builder(GoldThirtyBuyActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("您已购买成功");
                    builder.setNegativeButton("取消", null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.create().show();

                } else if (stringBaseBean.code == 1) {
                    int type = stringBaseBean.data.getStatus();
                    AlertDialog(type, stringBaseBean.message);
                } else if (stringBaseBean.code == 4) {
                    ToolUtil.loseToLogin(GoldThirtyBuyActivity.this);
                }

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                DialogUtil.dismissPayDialog();
                ToastUtil.showToast(GoldThirtyBuyActivity.this, "请稍后再试");
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).buyProfit(pass_word, f_id, pay_gold, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(buyGoldSubscriber);
    }

    /**
     * 充值成功后刷新金额
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void pwdSuccess(PayPwdSuccedEvent event) {
        isSetPwd = 1;
    }


    public void AlertDialog(final int type, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GoldThirtyBuyActivity.this);
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            /*    0：密码错误
                1：密码为空
                2：未设置支付密码
                3：商品不存在
                4：购买数量大于库存
                5：金豆不足
                6：支付失败
                7：没有实名认证
                */
                switch (type) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        startActivity(GoldThirtyBuyActivity.this, PayMentCodeActivity.class);
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                }
            }
        });
        builder.create().show();

    }

    @Override
    public void onInputFinish(String result) {
        buyProfit(result, prodeceBean.getId(), etCash.getText().toString().trim(), new PreferencesHelper(GoldThirtyBuyActivity.this).getToken());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
        if (buyGoldSubscriber != null && buyGoldSubscriber.isUnsubscribed()) {
            buyGoldSubscriber.unsubscribe();
        }
    }

}
