package com.ruirong.chefang.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.EditTextClearAble;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.qlzx.mylibrary.bean.BaseBean;
import com.ruirong.chefang.http.RemoteApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by guo on 2017/4/11.
 * 修改密码第二步
 */

public class ModifyPasswordActivity2 extends BaseActivity {
    @BindView(R.id.et_pw)
    EditTextClearAble etPw;
    @BindView(R.id.et_confirm_pw)
    EditTextClearAble etConfirmPw;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_commit)
    TextView tvCommit;

    private String mobile;
    private String code;
    private BaseSubscriber<BaseBean<String>> updatePwdSubscriber;

    @Override
    public int getContentView() {
        return R.layout.activity_modify_password2;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("修改密码");
        mobile = getIntent().getStringExtra("mobile");
        code = getIntent().getStringExtra("code");
    }

    public static void startActivityResult(Activity context, String mobile, String code, int requestCode) {
        Intent intent = new Intent(context, ModifyPasswordActivity2.class);
        intent.putExtra("mobile", mobile);
        intent.putExtra("code", code);
        context.startActivityForResult(intent, requestCode);
    }

    @OnClick(R.id.tv_commit)
    public void commit(View view) {
        String pwd = etPw.getText().toString().trim();
        String confirmPwd = etConfirmPw.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.showToast(ModifyPasswordActivity2.this, "密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(confirmPwd)) {
            ToastUtil.showToast(ModifyPasswordActivity2.this, "确认密码不能为空");
            return;
        }
        if (pwd.equals(confirmPwd)) {

            Log.i("XXX", mobile + " " + code + " " + pwd);

            updatePwdSubscriber = new BaseSubscriber<BaseBean<String>>(this, null) {
                @Override
                public void onNext(BaseBean<String> stringBaseBean) {
                    super.onNext(stringBaseBean);
                    Log.i("XXX", stringBaseBean.message + " " + stringBaseBean.code );
                    if (stringBaseBean.code == 0) {
                        ToastUtil.showToast(ModifyPasswordActivity2.this, stringBaseBean.message);
                        setResult(RESULT_OK);
                        startActivity(new Intent(ModifyPasswordActivity2.this,LoginActivity.class));
                    }

                }

                @Override
                public void onError(Throwable throwable) {
                    super.onError(throwable);
                    ToastUtil.showToast(ModifyPasswordActivity2.this, getString(R.string.net_error));
                }
            };

            HttpHelp.getInstance().create(RemoteApi.class).updatePwd(mobile, code, pwd)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(updatePwdSubscriber);
        } else {
            ToastUtil.showToast(ModifyPasswordActivity2.this, "密码和确认密码不一致");
        }

    }

    @Override
    public void getData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updatePwdSubscriber != null && updatePwdSubscriber.isUnsubscribed()) {
            updatePwdSubscriber.unsubscribe();
        }
    }
}
