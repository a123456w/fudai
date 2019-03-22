package com.ruirong.chefang.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.SmsButtonUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.util.ToolUtil;
import com.qlzx.mylibrary.widget.EditTextClearAble;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.qlzx.mylibrary.bean.BaseBean;
import com.ruirong.chefang.common.Constants;
import com.ruirong.chefang.http.RemoteApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by guo on 2017/4/11.
 * 注册
 */

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.et_phone)
    EditTextClearAble etPhone;
    @BindView(R.id.et_verification_code)
    EditTextClearAble etVerificationCode;
    @BindView(R.id.tv_verification_code)
    TextView tvVerificationCode;
    @BindView(R.id.et_recommend_phone)
    EditTextClearAble etRecommendPhone;
    @BindView(R.id.et_pw)
    EditTextClearAble etPw;
    @BindView(R.id.et_confirm_pw)
    EditTextClearAble etConfirmPw;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;

    private BaseSubscriber<BaseBean<String>> getcodeSubscriber;
    private BaseSubscriber<BaseBean<String>> registerSubscriber;
    private SmsButtonUtil mSmsButtonUtil;

    private final int REQUEST_CODE_CHOOSE_AREA = 600;
    private String provinceId="";
    private String cityId="";
    private String countyId="";
    private String provinceName = "";
    private String cityName = "";
    private String countyName = "";


    @Override
    public int getContentView() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("注册");

        // 验证码倒计时工具
        mSmsButtonUtil = new SmsButtonUtil(tvVerificationCode);
        mSmsButtonUtil.setCountDownText("重新获取（%ds）");
    }

    @Override
    public void getData() {

    }

    /**
     * 注册
     */
    @OnClick(R.id.tv_register)
    public void register() {
        String mobile = etPhone.getText().toString().trim();
        String code = etVerificationCode.getText().toString().trim();
        String refer_mobile = etRecommendPhone.getText().toString().trim();
        String password = etPw.getText().toString().trim();
        String repassword = etConfirmPw.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtil.showToast(this, "账号不能为空");
            return;
        }
        if(!(ToolUtil.isChinaPhoneLegal(mobile)||ToolUtil.isEmail(mobile))){
            ToastUtil.showToast(this, "账号格式不正确");
            return;
        }

        if (TextUtils.isEmpty(code)) {
            ToastUtil.showToast(this, "验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(tvArea.getText().toString().trim())){
            ToastUtil.showToast(RegisterActivity.this,"请选择区域");
            return;
        }


        if (TextUtils.isEmpty(password)) {
            ToastUtil.showToast(this, "密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(repassword)) {
            ToastUtil.showToast(this, "确认密码不能为空");
            return;
        }


        if (!password.equals(repassword)) {
            ToastUtil.showToast(this, "两次密码不一致");
            return;
        }

  /*      if (!ToolUtil.ispsd(password)){
            ToastUtil.showToast(this, "密码不能为全英文或全数字");
            return;
        }*/

        if (!TextUtils.isEmpty(refer_mobile)){
            if(!(ToolUtil.isChinaPhoneLegal(refer_mobile)||ToolUtil.isEmail(refer_mobile))){
                ToastUtil.showToast(this, "推荐人格式不正确");
                return;
            }
        }

        registerSubscriber = new BaseSubscriber<BaseBean<String>>(this, null) {
            @Override
            public void onNext(BaseBean<String> stringBaseBean) {
                ToastUtil.showToast(RegisterActivity.this, stringBaseBean.message);
                if (stringBaseBean.code == 0) {
                    finish();
                }
            }
        };

        HttpHelp.getRetrofit(this).create(RemoteApi.class).register(mobile, code, password, refer_mobile, provinceId, cityId, countyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(registerSubscriber);
    }

    /**
     * 登录
     */
    @OnClick(R.id.tv_login)
    public void login() {
        finish();
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.tv_verification_code)
    public void getVerificationCode(final View view) {
        String mobile = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(mobile)) {
            ToastUtil.showToast(this, "账号不能为空");
            return;
        }
        view.setEnabled(false);

        if (ToolUtil.isChinaPhoneLegal(mobile)){
            getPhoneCode(view, mobile);
        } else if (ToolUtil.isEmail(mobile)) {
            getEmailCode(view, mobile);
        } else {
            view.setEnabled(true);
            ToastUtil.showToast(RegisterActivity.this, "输入信息错误");
        }
    }

    public void getPhoneCode(final View view, String mobile) {
        getcodeSubscriber = new BaseSubscriber<BaseBean<String>>(this, null) {
            @Override
            public void onNext(BaseBean<String> stringBaseBean) {
                ToastUtil.showToast(RegisterActivity.this, stringBaseBean.message);
                Log.d("XXX", "onNext: "+stringBaseBean.message);
                if (stringBaseBean.code == 0) {
                    mSmsButtonUtil.startCountDown();
                } else {
                    view.setEnabled(true);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                ToastUtil.showToast(RegisterActivity.this, "获取验证码失败");
                view.setEnabled(true);
            }
        };
        //发送验证码的接口，type 是1的时候是注册的发送验证码。
        HttpHelp.getRetrofit(this).create(RemoteApi.class).getcode(mobile, "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getcodeSubscriber);
    }

    public void getEmailCode(final View view, String mobile) {
        getcodeSubscriber = new BaseSubscriber<BaseBean<String>>(this, null) {
            @Override
            public void onNext(BaseBean<String> stringBaseBean) {
                ToastUtil.showToast(RegisterActivity.this, stringBaseBean.message);
                if (stringBaseBean.code == 0) {
                    mSmsButtonUtil.startCountDown();
                } else {
                    view.setEnabled(true);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                ToastUtil.showToast(RegisterActivity.this, "获取验证码失败");
                view.setEnabled(true);
            }
        };
        //发送验证码的接口，type 是1的时候是注册的发送验证码。
        HttpHelp.getRetrofit(this).create(RemoteApi.class).getEmailCode(mobile, "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getcodeSubscriber);

    }


    /**
     * 选择地区
     */
    @OnClick(R.id.ll_area)
    public void choiceArea() {

        startActivityForResult(new Intent(RegisterActivity.this, ProvinceActivity.class), REQUEST_CODE_CHOOSE_AREA);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case REQUEST_CODE_CHOOSE_AREA:
                    //选择地区
                    provinceId = data.getStringExtra("provinceId");
                    provinceName = data.getStringExtra("provinceName");
                    cityId = data.getStringExtra("cityId");
                    cityName = data.getStringExtra("cityName");
                    countyId = data.getStringExtra("countyId");
                    countyName = data.getStringExtra("countyName");
                    tvArea.setText(provinceName + " " + cityName + " " + countyName);
                    break;
            }
        }
    }


    /**
     * 用户协议
     */
    @OnClick(R.id.tv_agreement)
    public void userAgreement() {
        WebActivity.startActivity(this, "用户协议", Constants.AGREEMENT_URL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getcodeSubscriber != null && getcodeSubscriber.isUnsubscribed()) {
            getcodeSubscriber.unsubscribe();
        }
        if (registerSubscriber != null && registerSubscriber.isUnsubscribed()) {
            registerSubscriber.unsubscribe();
        }
    }
}
