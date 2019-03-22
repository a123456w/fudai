package com.ruirong.chefang.activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.EditTextClearAble;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.qlzx.mylibrary.bean.BaseBean;
import com.ruirong.chefang.bean.LoginBean;
import com.ruirong.chefang.bean.UserInforBean;
import com.ruirong.chefang.event.LoginSuccessEvent;
import com.ruirong.chefang.http.RemoteApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by guo on 2017/3/22.
 * 登录页
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditTextClearAble etPhone;
    @BindView(R.id.et_pw)
    EditTextClearAble etPw;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_lost_pw)
    TextView tvLostPw;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    private BaseSubscriber<BaseBean<LoginBean>> loginSubscriber;
    private BaseSubscriber<BaseBean<UserInforBean>> userInforSubscriber;
    private boolean fromSplash;

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("登录");

        fromSplash = getIntent().getBooleanExtra("fromSplash", false);
    }

    @Override
    public void getData() {
    }

    /**
     * 登录
     */
    @OnClick(R.id.tv_login)
    public void login() {

        // 手机号/用户名
        String userName = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            ToastUtil.showToast(this, "请输入账号");
            return;
        }

        // 密码
        String password = etPw.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showToast(this, "请输入密码");
            return;
        }

        showLoadingDialog("登录中...");
        loginSubscriber = new BaseSubscriber<BaseBean<LoginBean>>(this, null) {
            @Override
            public void onNext(BaseBean<LoginBean> baseBean) {
                hideLoadingDialog();
                if (baseBean.code==0) {
                    new PreferencesHelper(LoginActivity.this).saveToken(baseBean.data.getToken());
                    getUserInfor(baseBean.data.getToken());
                    EventBusUtil.post(new LoginSuccessEvent());

                    if (fromSplash){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    finish();
                } else {
                    ToastUtil.showToast(LoginActivity.this, baseBean.message);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                hideLoadingDialog();
            }
        };

        HttpHelp.getRetrofit(this).create(RemoteApi.class).login(userName, password,new PreferencesHelper(LoginActivity.this).getRegistrationID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginSubscriber);

    }

    /**
     * 获取用户信息。
     * @param token
     */
    public void getUserInfor(String token){
        userInforSubscriber = new BaseSubscriber<BaseBean<UserInforBean>>(this,loadingLayout){
            @Override
            public void onNext(BaseBean<UserInforBean> userInforBeanBaseBean) {
                super.onNext(userInforBeanBaseBean);
                UserInforBean userInforBean = userInforBeanBaseBean.data;
                new PreferencesHelper(LoginActivity.this).savePhoneNum(userInforBean.getMobile());
                new PreferencesHelper(LoginActivity.this).saveUserName(userInforBean.getMobile());
                new PreferencesHelper(LoginActivity.this).saveUserInfo(new Gson().toJson(userInforBean));
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).userInfor(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userInforSubscriber);
    }



    /**
     * 忘记密码
     */
    @OnClick(R.id.tv_lost_pw)
    public void lostPw(){
        ModifyPasswordActivity.startActivityWithParmeter(LoginActivity.this,"忘记密码");
    }
    /**
     * 注册
     */
    @OnClick(R.id.tv_register)
    public void register(){
        RegisterActivity.startActivity(this,RegisterActivity.class);
    }

    @OnClick(R.id.iv_titlebar_left)
    public void back(){
        if (fromSplash){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (fromSplash){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loginSubscriber != null && loginSubscriber.isUnsubscribed()){
            loginSubscriber.unsubscribe();
        }
        if (userInforSubscriber!=null&&userInforSubscriber.isUnsubscribed()){
            userInforSubscriber.unsubscribe();
        }
    }

    public static void startActivity(Context context,boolean fromSplash){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("fromSplash",fromSplash);
        context.startActivity(intent);
    }

}
