package com.ruirong.chefang.personalcenter;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.util.SmsButtonUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.PayMentCodeActivity;
import com.ruirong.chefang.activity.UpdatePayPwdActivity;
import com.ruirong.chefang.util.ToolUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 修改支付密码第一步
 * Created by dillon on 2017/8/18.
 *
 * 支付密码设置
 */

public class UpdatePayPassActivity extends BaseActivity {
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_getcode)
    TextView tvGetcode;
    @BindView(R.id.tv_next)
    TextView tvNext;
    private final int REQUEST_CODE_NEXT = 100;
    @BindView(R.id.et_phone)
    EditText etPhone;

    private SmsButtonUtil mSmsButtonUtil;
    private BaseSubscriber<BaseBean<String>> getCodeSubscriber;
    private BaseSubscriber<BaseBean<String>> InputPassCodeSubscriber;


    @Override
    public int getContentView() {
        return R.layout.activity_updatepaypass;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("支付密码设置");

        tvNext.setEnabled(false);

        // 验证码倒计时工具
        mSmsButtonUtil = new SmsButtonUtil(tvGetcode);
        mSmsButtonUtil.setCountDownText("重新获取（%ds）");

        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    tvNext.setBackgroundResource(R.drawable.round_cap_gray2);
                    tvNext.setEnabled(false);
                } else {
                    tvNext.setBackgroundResource(R.drawable.bg_get_verify_code);
                    tvNext.setEnabled(true);
                }
            }
        });
    }

    /**
     * 获取验证码
     *
     * @param view
     */
    @OnClick(R.id.tv_getcode)
    public void getVerficationCode(View view) {
        if (TextUtils.isEmpty(etPhone.getText().toString().trim())){
            ToastUtil.showToast(UpdatePayPassActivity.this,"请输入手机号");
            return;
        }
        String str = "signzdht" + System.currentTimeMillis() / 1000;

        String[] stringsArray = new String[2];
        stringsArray[0] = "signzdht";
        stringsArray[1] = System.currentTimeMillis() / 1000 + "";

        String sign = ToolUtil.getMD5(str + ToolUtil.getMD5(stringsArray[1].substring(stringsArray[1].length() - 4, stringsArray[1].length()) + stringsArray[0]));

        view.setEnabled(false);

        getMessageCode(view, etPhone.getText().toString().trim(), str, sign, 4);
    }

    /**
     * 获取短信验证码
     *
     * @param view
     * @param phone 手机号
     * @param str
     * @param sign
     * @param type  短信类型    1 注册短信   3  修改手机号
     */
    public void getMessageCode(final View view, String phone, String str, String sign, int type) {
        getCodeSubscriber = new BaseSubscriber<BaseBean<String>>(this, null) {
            @Override
            public void onNext(BaseBean<String> stringBaseBean){
                ToastUtil.showToast(UpdatePayPassActivity.this, stringBaseBean.message);
                if (stringBaseBean.code == 0) {
                    mSmsButtonUtil.startCountDown();
                }else if (stringBaseBean.code==4){
                    ToolUtil.loseToLogin(UpdatePayPassActivity.this);
                } else {
                    view.setEnabled(true);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                ToastUtil.showToast(UpdatePayPassActivity.this, "获取验证码失败");
                view.setEnabled(true);
            }
        };
        //发送验证码的接口，type 是1的时候是注册的发送验证码。
//        HttpHelp.getRetrofit(this).create(RemoteApi.class).getUpdatePasswordCode(phone, str, sign, type, new PreferencesHelper(UpdatePayPassActivity.this).getToken())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(getCodeSubscriber);
    }


    /**
     * 下一步
     */
    @OnClick(R.id.tv_next)
    public void gotoNext() {
// TODO: 2018/1/10 0010 主要参考那个app 

        startActivity(new Intent(this, PayMentCodeActivity.class));
        
//        InputPassCodeSubscriber = new BaseSubscriber<BaseBean<String>>(this, null) {
//            @Override
//            public void onNext(BaseBean<String> stringBaseBean) {
//                super.onNext(stringBaseBean);
////                if (stringBaseBean.code == 0) {
////                    InputPayPassActivity.startActivityWithParameter(UpdatePayPassActivity.this, etPhone.getText().toString().trim(), etCode.getText().toString().trim(), REQUEST_CODE_NEXT);
////                }else {
////                    ToastUtil.showToast(UpdatePayPassActivity.this,stringBaseBean.message);
////                }
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                super.onError(throwable);
//            }
//        };

//        HttpHelp.getRetrofit(this).create(RemoteApi.class).updatePhoneOne(etPhone.getText().toString().trim(), etCode.getText().toString().trim(), 4, new PreferencesHelper(UpdatePayPassActivity.this).getToken())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(InputPassCodeSubscriber);


    }

    @Override
    public void getData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_NEXT && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

}
