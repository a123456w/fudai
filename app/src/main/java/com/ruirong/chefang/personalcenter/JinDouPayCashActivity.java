package com.ruirong.chefang.personalcenter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DialogUtil;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.payDialog.PayPwdView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.BankCardActivity;
import com.ruirong.chefang.activity.GoldWithDrawActivity;
import com.ruirong.chefang.activity.PayMentCodeActivity;
import com.ruirong.chefang.activity.WebActivity;
import com.ruirong.chefang.bean.BankCard;
import com.ruirong.chefang.bean.ConfigBean;
import com.ruirong.chefang.bean.UserInforBean;
import com.ruirong.chefang.common.Constants;
import com.ruirong.chefang.event.WalletEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2018/1/9 0009
 * describe:  金豆兑换
 */
public class JinDouPayCashActivity extends BaseActivity implements PayPwdView.InputCallBack {
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.ll_tixian)
    LinearLayout llTixian;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_bankcard)
    TextView tvBankcard;
    @BindView(R.id.ll_exchange_money_num)
    LinearLayout llExchangeMoneyNum;
    @BindView(R.id.tv_shop_business)
    TextView tvShopBusiness;
    @BindView(R.id.rl_choseBank)
    RelativeLayout rlChoseBank;
    @BindView(R.id.tv_myconvert)
    TextView myConvert;   //   我的余额
    @BindView(R.id.et_withdrawsum)
    EditText etWithdrawsum;

    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.tv_shop_businesse)
    TextView tvShopBusinesse;
    @BindView(R.id.tv_myconverte)
    TextView tvMyconverte;

    private String bankid;
    private BaseSubscriber<BaseBean<Object>> goldCashSubscriber;
    private BaseSubscriber<BaseBean<ConfigBean>> configCashSubscriber;
    private double confignum;
    public AlertDialog alertDialog;

    private BaseSubscriber<BaseBean<BankCard>> bankCardSubscriber;
    private BaseSubscriber<BaseBean<UserInforBean>> userInforSubscriber;


    @Override
    public int getContentView() {
        return R.layout.activity_jindou_pay_cash;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("提现");
        titleBar.setRightImageRes(R.drawable.ic_questen);
        btnCommit.setEnabled(false);
        btnCommit.setPressed(false);
        titleBar.setRightImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity.startActivity(JinDouPayCashActivity.this, "提现规则", Constants.EXCHANGERULE);
            }
        });

        rlChoseBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JinDouPayCashActivity.this, BankCardActivity.class);
                intent.putExtra(Constants.ISBACK, true);
                startActivityForResult(intent, Constants.BANKCARKDETAILREQUESTCODE);
            }
        });

        etWithdrawsum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    btnCommit.setEnabled(false);
                    btnCommit.setPressed(false);
                } else {
                    btnCommit.setPressed(true);
                    btnCommit.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void getData() {
        getBank();
        getedu();
        isCommercial();   // 判断是否是商家
        configCashSubscriber = new BaseSubscriber<BaseBean<ConfigBean>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<ConfigBean> configBeanBaseBean) {
                super.onNext(configBeanBaseBean);
                if (configBeanBaseBean.code == 0) {
                    confignum = configBeanBaseBean.data.getWithdraw_user_cash();
                } else {
                    ToastUtil.showToast(JinDouPayCashActivity.this, configBeanBaseBean.message);
                }

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).configCash()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(configCashSubscriber);
    }

    /**
     * 获取银行卡
     */
    public void getBank() {
        bankCardSubscriber = new BaseSubscriber<BaseBean<BankCard>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<BankCard> bankCardBeanBaseBean) {
                super.onNext(bankCardBeanBaseBean);
                if (bankCardBeanBaseBean.code == 0) {
                    bankid = bankCardBeanBaseBean.data.getId();
                    tvBankcard.setText(bankCardBeanBaseBean.data.getBank() + "尾号" + bankCardBeanBaseBean.data.getBank_card_number());
                    if (bankCardBeanBaseBean.data.getMoney() != null && !"".equals(bankCardBeanBaseBean.data.getMoney())) {
                        myConvert.setText(bankCardBeanBaseBean.data.getMoney());
                    } else {
                        myConvert.setText("0.00");
                    }
                } else if (bankCardBeanBaseBean.code == 4) {
                    ToolUtil.loseToLogin(JinDouPayCashActivity.this);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).bankCard(new PreferencesHelper(JinDouPayCashActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bankCardSubscriber);
    }


    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        if (TextUtils.isEmpty(tvBankcard.getText().toString())) {
            ToastUtil.showToast(JinDouPayCashActivity.this, "请先选择银行卡");
            return;
        }
        if (TextUtils.isEmpty(etWithdrawsum.getText().toString())) {
            ToastUtil.showToast(JinDouPayCashActivity.this, "请输入提现金额");
            return;
        }
        // 是否设置密码，如果没有设置的话，就去设置
        isSetPwd();
    }

    /**
     * 是否是商户
     */
    public void isCommercial(){
        HttpHelp.getInstance().create(RemoteApi.class).userInfor(new PreferencesHelper(JinDouPayCashActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<UserInforBean>>(this,null){
                    @Override
                    public void onNext(BaseBean<UserInforBean> userInforBeanBaseBean) {
                        super.onNext(userInforBeanBaseBean);
                        if (userInforBeanBaseBean.code==0){
                            // 2 是商家   1是用户    商家展示
                            if ("2".equals(userInforBeanBaseBean.data.getRole())){
                                llTixian.setVisibility(View.GONE);
                            }else if ("1".equals(userInforBeanBaseBean.data.getRole())){
                                llTixian.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 是否设置密码
     */
    public void isSetPwd() {

        userInforSubscriber = new BaseSubscriber<BaseBean<UserInforBean>>(this, null) {
            @Override
            public void onNext(BaseBean<UserInforBean> userInforBeanBaseBean) {
                super.onNext(userInforBeanBaseBean);

                if (userInforBeanBaseBean.code == 0) {
                    UserInforBean userInforBean = userInforBeanBaseBean.data;

                    String role = userInforBean.getRole();
                    int etWithsum = Integer.parseInt(etWithdrawsum.getText().toString());
                    if ("1".equals(role)) {
                        if (etWithsum < 100 || etWithsum % 100 != 0) {
                            ToastUtil.showToast(JinDouPayCashActivity.this, "提现金额为大于100的整数");
                            return;
                        }
                    } else if ("2".equals(role)) {
                        if (etWithsum < 500 || etWithsum % 500 != 0) {
                            ToastUtil.showToast(JinDouPayCashActivity.this, "提现金额为大于500的倍数");
                            return;
                        }
                    }
                    if (userInforBean.getIs_has_pay_pass() == 0) {
                        startActivity(JinDouPayCashActivity.this, PayMentCodeActivity.class);
                    } else if (userInforBean.getIs_has_pay_pass() == 1) {
                        //TODO   换成从接口获取数据。
                        getWithDrawMoney(etWithdrawsum.getText().toString(),new PreferencesHelper(JinDouPayCashActivity.this).getToken());
                    }
                } else if (userInforBeanBaseBean.code == 4) {
                    ToolUtil.loseToLogin(JinDouPayCashActivity.this);
                }

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).userInfor(new PreferencesHelper(JinDouPayCashActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userInforSubscriber);
    }

    /**
     * 金豆提现
     *
     * @param money
     * @param cardid
     * @param paypass
     */
    public void goldWithDraw(String money, String cardid, String paypass ,String edition) {
        goldCashSubscriber = new BaseSubscriber<BaseBean<Object>>(this, null) {
            @Override
            public void onNext(BaseBean<Object> stringBaseBean) {
                super.onNext(stringBaseBean);
                DialogUtil.dismissPayDialog();
                if (stringBaseBean.code == 0) {
                    EventBusUtil.post(new WalletEvent());
                    showDialog(JinDouPayCashActivity.this, stringBaseBean.code, "24小时到账，节假日顺延", "继续提现");
                } else if (stringBaseBean.code == 4) {
                    ToolUtil.loseToLogin(JinDouPayCashActivity.this);
                } else {
                    ToastUtil.showToast(JinDouPayCashActivity.this,stringBaseBean.message);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).goldCash(money, cardid, paypass,edition, new PreferencesHelper(JinDouPayCashActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(goldCashSubscriber);

    }
    /**
     * 获取版本号
     * @return
     */
    public String getVersionName(){
        String versionName = "";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName ;
    }
    /**
     * 获取体现到账金额
     * @param money
     * @param token
     */
    public void getWithDrawMoney(String money,String token){
        HttpHelp.getInstance().create(RemoteApi.class).getWithDrawMoney(money,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this,null){
                    @Override
                    public void onNext(BaseBean<String> stringBaseBean) {
                        super.onNext(stringBaseBean);
                        if (stringBaseBean.code==0){
                            DialogUtil.showPayDialog(JinDouPayCashActivity.this, "请输入支付密码", etWithdrawsum.getText().toString(), "实际到账金额为" + stringBaseBean.data, getSupportFragmentManager());
                        }else {
                            ToastUtil.showToast(JinDouPayCashActivity.this,stringBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (goldCashSubscriber != null && goldCashSubscriber.isUnsubscribed()) {
            goldCashSubscriber.unsubscribe();
        }
        if (configCashSubscriber != null && configCashSubscriber.isUnsubscribed()) {
            configCashSubscriber.unsubscribe();
        }
        if (userInforSubscriber != null && userInforSubscriber.isUnsubscribed()) {
            userInforSubscriber.unsubscribe();
        }
        if (bankCardSubscriber != null && bankCardSubscriber.isUnsubscribed()) {
            bankCardSubscriber.unsubscribe();
        }

    }

    @Override
    public void onInputFinish(String result) {
        goldWithDraw(etWithdrawsum.getText().toString(), bankid, result,getVersionName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.BANKCARKDETAILREQUESTCODE) {
            bankid = data.getStringExtra(Constants.BANKCARDID);

            String banknum = data.getStringExtra(Constants.BANKCARDNUM);
            String num = "1234";

            if (banknum.length() > 4) {
                num = banknum.substring(banknum.length() - 4);
            }
            tvBankcard.setText(data.getStringExtra(Constants.BANKCARDNAME) + "尾号是" + num);
        }
    }

    public void showDialog(final Context context, final int icon, String msg, String btncontinue) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_dialogd, null, false);
        ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        TextView tvHint = (TextView) view.findViewById(R.id.tv_hint);
        TextView tvContinue = (TextView) view.findViewById(R.id.tv_continue);
        if (icon == 1) {
            GlideUtil.display(JinDouPayCashActivity.this, R.drawable.icon_6, ivIcon);
        }
        if (icon == 2) {
            GlideUtil.display(JinDouPayCashActivity.this, R.drawable.icon_4, ivIcon);
        }
        tvHint.setText(msg);
        tvContinue.setText(btncontinue);
        view.findViewById(R.id.tv_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (icon == 2) {
                    etWithdrawsum.getText().clear();
                }
                alertDialog.dismiss();


            }
        });
        view.findViewById(R.id.tv_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (icon) {
                    case 0:
                        alertDialog.dismiss();
                        finish();
                        break;
                    case 1:
                        alertDialog.dismiss();
                        getWithDrawMoney(etWithdrawsum.getText().toString(),new PreferencesHelper(JinDouPayCashActivity.this).getToken());
                        break;
                    case 2:
                        alertDialog.dismiss();
                        etWithdrawsum.getText().clear();
                        break;
                }


            }
        });
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();

    }

    /**
     * 获取提现额度
     */
    public void getedu() {
        HttpHelp.getInstance().create(RemoteApi.class).getedu(new PreferencesHelper(JinDouPayCashActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this, null) {
                    @Override
                    public void onNext(BaseBean<String> stringBaseBean) {
                        super.onNext(stringBaseBean);
                        if (stringBaseBean.code == 0) {
                            if (!TextUtils.isEmpty(stringBaseBean.data)) {
                                tvMyconverte.setText(stringBaseBean.data);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }
}
