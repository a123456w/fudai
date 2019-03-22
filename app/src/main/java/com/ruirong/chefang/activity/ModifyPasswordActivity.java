package com.ruirong.chefang.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.SmsButtonUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.util.ToolUtil;
import com.qlzx.mylibrary.widget.EditTextClearAble;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.UserInforBean;
import com.ruirong.chefang.http.RemoteApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ruirong.chefang.common.Constants.UPDATESETPAY;

/**
 * Created by guo on 2017/4/11.
 * 修改密码
 */

public class ModifyPasswordActivity extends BaseActivity {
    @BindView(R.id.et_phone)
    EditTextClearAble etPhone;
    @BindView(R.id.et_verification_code)
    EditTextClearAble etVerificationCode;
    @BindView(R.id.tv_verification_code)
    TextView tvVerificationCode;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_next)
    TextView tvNext;
    private SmsButtonUtil mSmsButtonUtil;

    private BaseSubscriber<BaseBean<String>> updateCodeSubscriber;
    private BaseSubscriber<BaseBean<String>> codeIsTrueSubscriber;
    private BaseSubscriber<BaseBean<String>> updateCodeIsTrueSubscriber;

    private String updateFlag;
    private String codeType = "2";

    private final int REQUEST_CODE_NEXT = 0;
    private final int REQUEST_CODE_NODIFYPASSWORD = 100;
    private static final String MODIFYPASSWORD = "MODIFYPASSWORD";
    private String ModifyTitle;
    private PreferencesHelper helper;

    private String UPDATEFLAGE = "UPDATEFLAGE";

    @Override
    public int getContentView() {
        return R.layout.activity_modify_password;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(this);
        ModifyTitle = getIntent().getStringExtra(MODIFYPASSWORD);
        if (!TextUtils.isEmpty(ModifyTitle)) {
            titleBar.setTitleText(ModifyTitle);
            if ("修改支付密码".equals(ModifyTitle)) {
                codeType = "4";
                updateFlag = "UPDATESETPAY";
            }
        }

        titleBar.setTitleText(getIntent().getStringExtra(MODIFYPASSWORD));
        // 验证码倒计时工具
        mSmsButtonUtil = new SmsButtonUtil(tvVerificationCode);
        mSmsButtonUtil.setCountDownText("重新获取（%ds）");
    }

    @Override
    public void getData() {

    }

    /**
     * 传递项目的标题
     *
     * @param context
     * @param title
     */
    public static void startActivityWithParmeter(Context context, String title) {
        Intent intent = new Intent(context, ModifyPasswordActivity.class);
        intent.putExtra(MODIFYPASSWORD, title);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_verification_code)
    public void getVerificationCode(final View view) {
        String mobile = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(mobile)) {
            ToastUtil.showToast(ModifyPasswordActivity.this, "输入账号不能为空");
            return;
        }


        if (!TextUtils.isEmpty(helper.getToken())) {
            // 登录状态
            UserInforBean userInfor = new Gson().fromJson(helper.getUserInfo(), UserInforBean.class);
            if (!mobile.equals(userInfor.getMobile())) {
                ToastUtil.showToast(ModifyPasswordActivity.this, "请输入注册时的号码");
                return;
            }
        }

        view.setEnabled(false);

        if (ToolUtil.isChinaPhoneLegal(mobile)) {
            getPhoneCode(view, mobile);
        } else if (ToolUtil.isEmail(mobile)) {
            getEmailCode(view, mobile);
        } else {
            view.setEnabled(true);
            ToastUtil.showToast(ModifyPasswordActivity.this, "输入信息错误");
        }
    }

    public void getEmailCode(final View view, String mobile) {

        updateCodeSubscriber = new BaseSubscriber<BaseBean<String>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<String> stringBaseBean) {
                super.onNext(stringBaseBean);
                ToastUtil.showToast(ModifyPasswordActivity.this, stringBaseBean.message);
                if (stringBaseBean.code == 0) {
                    mSmsButtonUtil.startCountDown();
                } else {
                    view.setEnabled(true);
                    ToastUtil.showToast(ModifyPasswordActivity.this, stringBaseBean.message);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                ToastUtil.showToast(ModifyPasswordActivity.this, "获取验证码失败");
                view.setEnabled(true);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).getEmailCode(mobile, codeType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updateCodeSubscriber);
    }

    public void getPhoneCode(final View view, String mobile) {

        updateCodeSubscriber = new BaseSubscriber<BaseBean<String>>(this, null) {
            @Override
            public void onNext(BaseBean<String> stringBaseBean) {
                super.onNext(stringBaseBean);
                ToastUtil.showToast(ModifyPasswordActivity.this, stringBaseBean.message);
                if (stringBaseBean.code == 0) {
                    mSmsButtonUtil.startCountDown();
                } else {
                    view.setEnabled(true);
                    ToastUtil.showToast(ModifyPasswordActivity.this, stringBaseBean.message);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                ToastUtil.showToast(ModifyPasswordActivity.this, "获取验证码失败");
                view.setEnabled(true);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).getcode(mobile, codeType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updateCodeSubscriber);
    }

    @OnClick(R.id.tv_next)
    public void next() {
        final String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast(ModifyPasswordActivity.this, "手机号或邮箱不能为空");
            return;
        }

        final String code = etVerificationCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtil.showToast(ModifyPasswordActivity.this, "验证码不能为空");
            return;
        }
        Log.i("XXX", UPDATESETPAY + " " + updateFlag);
        if (UPDATESETPAY.equals(updateFlag)) {
            Log.i("XXX", "111111111111111");
            updateCodeIsTrue(phone, code);
        } else {
            Log.i("XXX", "22222222222222");
            codeIsTrue(phone, code);
        }
    }

    public void codeIsTrue(final String mobile, final String code) {
        codeIsTrueSubscriber = new BaseSubscriber<BaseBean<String>>(this, null) {
            @Override
            public void onNext(BaseBean<String> stringBaseBean) {
                super.onNext(stringBaseBean);
                if (stringBaseBean.code == 0) {
                    ModifyPasswordActivity2.startActivityResult(ModifyPasswordActivity.this, mobile, code, REQUEST_CODE_NEXT);
                } else {
                    ToastUtil.showToast(ModifyPasswordActivity.this, stringBaseBean.message);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                ToastUtil.showToast(ModifyPasswordActivity.this, getString(R.string.net_error));
            }
        };

        LogUtil.i("token: " + new PreferencesHelper(ModifyPasswordActivity.this).getToken() + "/n mob:" + etPhone.getText().toString());

        HttpHelp.getInstance().create(RemoteApi.class).codeIsTrue(etPhone.getText().toString().trim(), etVerificationCode.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(codeIsTrueSubscriber);
    }

    public void updateCodeIsTrue(String mob, String co) {
        updateCodeIsTrueSubscriber = new BaseSubscriber<BaseBean<String>>(this, null) {
            @Override
            public void onNext(BaseBean<String> stringBaseBean) {
                super.onNext(stringBaseBean);
                ToastUtil.showToast(ModifyPasswordActivity.this, stringBaseBean.message);
                if (stringBaseBean.code == 0) {
                    UpdatePayPwdActivity.startActivityResult(ModifyPasswordActivity.this, etPhone.getText().toString().trim(), etVerificationCode.getText().toString().trim(), REQUEST_CODE_NODIFYPASSWORD);
                } else if (stringBaseBean.code == 4) {
                    com.ruirong.chefang.util.ToolUtil.loseToLogin(ModifyPasswordActivity.this);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                ToastUtil.showToast(ModifyPasswordActivity.this, getString(R.string.net_error));
            }
        };

        LogUtil.i("token: " + new PreferencesHelper(ModifyPasswordActivity.this).getToken() + "/n mob:" + mob);

        HttpHelp.getInstance().create(RemoteApi.class).updateCodeIsTrue(mob, co, new PreferencesHelper(ModifyPasswordActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updateCodeIsTrueSubscriber);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateCodeSubscriber != null && updateCodeSubscriber.isUnsubscribed()) {
            updateCodeSubscriber.unsubscribe();
        }
        if (codeIsTrueSubscriber != null && codeIsTrueSubscriber.isUnsubscribed()) {
            codeIsTrueSubscriber.unsubscribe();
        }
        if (updateCodeIsTrueSubscriber != null && updateCodeIsTrueSubscriber.isUnsubscribed()) {
            updateCodeIsTrueSubscriber.unsubscribe();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_NEXT && resultCode == RESULT_OK) {
            LogUtil.d("密码修改成功，马上关闭本页面");
            finish();
        } else if (requestCode == REQUEST_CODE_NODIFYPASSWORD && resultCode == RESULT_OK) {
            LogUtil.d("支付密码修改成功，马上关闭本页面");
            finish();
        }
    }
}
