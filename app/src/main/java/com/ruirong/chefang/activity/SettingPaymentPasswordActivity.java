package com.ruirong.chefang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.PasswordInputView;
import com.ruirong.chefang.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by BX on 2017/12/29.
 */

public class SettingPaymentPasswordActivity extends BaseActivity {


    @BindView(R.id.pass_pwd)
    PasswordInputView passPwd;
    @BindView(R.id.pass_surepwd)
    PasswordInputView passSurepwd;
    @BindView(R.id.tv_wrongpwd)
    TextView tvWrongpwd;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    private BaseSubscriber<BaseBean<String>> updatePayPwdSubscriber;
    private String mobile;
    private String code;


    @Override
    public int getContentView() {
        return R.layout.activity_setting_payment_password;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("输入支付密码");
        mobile = getIntent().getStringExtra("inputMobile");
        code = getIntent().getStringExtra("inputCode");


        passPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    tvCommit.setBackgroundResource(R.drawable.round_cap_gray2);
                    tvCommit.setEnabled(false);
                } else {
                    tvCommit.setBackgroundResource(R.drawable.round_cap_blue);
                    tvCommit.setEnabled(true);
                }
            }
        });

    }

    @Override
    public void getData() {

    }

    @OnClick(R.id.tv_commit)
    public void onViewClicked() {
//        if (passPwd.getText().length() != 6) {
//            ToastUtil.showToast(InputPayPassActivity.this, "请输入6位数字密码");
//            return;
//        }
//        if (!passPwd.getText().toString().trim().equals(passSurepwd.getText().toString().trim())) {
//            ToastUtil.showToast(InputPayPassActivity.this, "密码和确认密码要一致");
//            return;
//        }
//
//        updatePayPwdSubscriber = new BaseSubscriber<BaseBean<String>>(this, null) {
//            @Override
//            public void onNext(BaseBean<String> stringBaseBean) {
//                super.onNext(stringBaseBean);
//                if (stringBaseBean.code == 0) {
//                    PreferencesHelper preferencesHelper = new PreferencesHelper(SettingPaymentPasswordActivity.this);
//                    String userInfo = preferencesHelper.getUserInfo();
//                    Gson gson = new Gson();
//                    UserInforBean userInforBean = gson.fromJson(userInfo, UserInforBean.class);
//                    userInforBean.setIs_pay(1);
//                    preferencesHelper.saveUserInfo(gson.toJson(userInforBean));
//
//                    setResult(RESULT_OK);
//                    finish();
//                }
//                ToastUtil.showToast(SettingPaymentPasswordActivity.this, stringBaseBean.message);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                super.onError(throwable);
//            }
//
//        };
//        HttpHelp.getRetrofit(this).create(RemoteApi.class).updatePayPwd(mobile, code, passPwd.getText().toString().trim(), new PreferencesHelper(SettingPaymentPasswordActivity.this).getToken())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(updatePayPwdSubscriber);
    }

    public static void startActivityWithParameter(Activity context, String inputMobile, String inputCode, int requestCode) {
        Intent intent = new Intent(context, SettingPaymentPasswordActivity.class);
        intent.putExtra("inputMobile", inputMobile);
        intent.putExtra("inputCode", inputCode);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
