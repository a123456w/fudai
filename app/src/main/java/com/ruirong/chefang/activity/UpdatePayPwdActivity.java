package com.ruirong.chefang.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.qlzx.mylibrary.bean.BaseBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.widget.PasswordInputView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 修改支付密码
 * Created by dillon on 2017/5/9.
 */

public class UpdatePayPwdActivity extends BaseActivity {

    @BindView(R.id.pass_newpwd)
    PasswordInputView passNewpwd;
    @BindView(R.id.pass_surepwd)
    PasswordInputView passSurepwd;
    @BindView(R.id.tv_wrongpwd)
    TextView tvWrongpwd;
    @BindView(R.id.btn_commit)
    Button btnCommit;

    private String mobile="";
    private String payCode="";
    private BaseSubscriber<BaseBean<String>> updatePayPwdSubscriber;

    @Override
    public int getContentView() {
        return R.layout.activity_updatepaypwd;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("修改支付密码");
        mobile = getIntent().getStringExtra("mypursemobile");
        payCode = getIntent().getStringExtra("mypursepaycode");
        passSurepwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < 6) {
                    tvWrongpwd.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (passNewpwd.getText().toString().length() == 6 && passSurepwd.getText().toString().length() == 6) {
                    if (passNewpwd.getText().toString().equals(passSurepwd.getText().toString())) {
                        tvWrongpwd.setVisibility(View.GONE);
                    } else {
                        tvWrongpwd.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
    @Override
    public void getData() {

    }
    @OnClick(R.id.btn_commit)
    public void onViewClicked() {

        Log.e("mmmmmmmmmsssss",passNewpwd.getText().toString()+"以防为空");
        Log.e("mmmmmmmmmsssss",passSurepwd.getText().toString()+"以防为空");

        if (TextUtils.isEmpty(passNewpwd.getText().toString())) {
            ToastUtil.showToast(UpdatePayPwdActivity.this, "请输入新密码");
            return;
        }
        if (passNewpwd.getText().toString().length() != 6){
            ToastUtil.showToast(UpdatePayPwdActivity.this, "请输入6位新密码");
            return;
        }
        if (TextUtils.isEmpty(passSurepwd.getText().toString())) {
            ToastUtil.showToast(UpdatePayPwdActivity.this, "请输入确认密码");
            return;
        }
        if (passSurepwd.getText().toString().length() != 6){
            ToastUtil.showToast(UpdatePayPwdActivity.this, "请输入6位确认密码");
            return;
        }

        if (passSurepwd.getText().toString().equals(passNewpwd.getText().toString())) {
            updatePayPwd(mobile,payCode,passNewpwd.getText().toString());
        } else {
            tvWrongpwd.setVisibility(View.VISIBLE);
            ToastUtil.showToast(UpdatePayPwdActivity.this, "新密码和确认密码不一致");
        }
    }
    /**
     * 修改支付密码
     *
     * @param phone
     * @param upcode
     * @param newPassword
     */
    public void updatePayPwd(String phone,String upcode, String newPassword) {
        updatePayPwdSubscriber = new BaseSubscriber<BaseBean<String>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<String> stringBaseBean) {
                super.onNext(stringBaseBean);
                if (stringBaseBean.code==0){
                    setResult(RESULT_OK);
                    finish();
                } else if (stringBaseBean.code==4){
                    ToolUtil.loseToLogin(UpdatePayPwdActivity.this);
                }
                ToastUtil.showToast(UpdatePayPwdActivity.this, stringBaseBean.message);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).updatePayPwd(phone,upcode,newPassword, new PreferencesHelper(UpdatePayPwdActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updatePayPwdSubscriber);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updatePayPwdSubscriber != null && updatePayPwdSubscriber.isUnsubscribed()) {
            updatePayPwdSubscriber.unsubscribe();
        }
    }
    public static void startActivityResult(Activity context, String mobile, String code, int requestCode) {
        Intent intent = new Intent(context, UpdatePayPwdActivity.class);
        intent.putExtra("mypursemobile",mobile);
        intent.putExtra("mypursepaycode", code);
        context.startActivityForResult(intent,requestCode);
    }

}
