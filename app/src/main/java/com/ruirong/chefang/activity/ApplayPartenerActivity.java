package com.ruirong.chefang.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
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

/**
 * 申请合伙人
 * Created by dillon on 2017/9/8.
 */

public class ApplayPartenerActivity extends BaseActivity {
    @BindView(R.id.et_name)
    EditTextClearAble etName;
    @BindView(R.id.et_phone)
    EditTextClearAble etPhone;
    @BindView(R.id.tv_applay)
    TextView tvApplay;

    private BaseSubscriber<BaseBean<String>> applayPartenerSubscriber;

    @Override
    public int getContentView() {
        return R.layout.activity_applypartener;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("申请合伙人");
    }

    @Override
    public void getData() {

    }

    @OnClick(R.id.tv_applay)
    public void onViewClicked() {
        String userInforBean = new PreferencesHelper(ApplayPartenerActivity.this).getUserInfo();
        UserInforBean userInforBean1 = new Gson().fromJson(userInforBean, UserInforBean.class);
        if (TextUtils.isEmpty(etName.getText().toString().trim())) {
            ToastUtil.showToast(ApplayPartenerActivity.this, "请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
            ToastUtil.showToast(ApplayPartenerActivity.this, "请输入手机号");
            return;
        }

        if (!ToolUtil.isChinaPhoneLegal(etPhone.getText().toString().trim())) {
            ToastUtil.showToast(ApplayPartenerActivity.this, "手机号格式不正确");
            return;
        }
        applayPartener(etPhone.getText().toString().trim(), etName.getText().toString().trim(), new PreferencesHelper(ApplayPartenerActivity.this).getToken());


    }

    public void applayPartener(String mobile, String name, String token) {
        applayPartenerSubscriber = new BaseSubscriber<BaseBean<String>>(this, null) {
            @Override
            public void onNext(BaseBean<String> stringBaseBean) {
                super.onNext(stringBaseBean);
                if (stringBaseBean.code == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ApplayPartenerActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage(stringBaseBean.message);
                    builder.setNegativeButton("取消", null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.create().show();
                }else if (stringBaseBean.code==4){
                    com.ruirong.chefang.util.ToolUtil.loseToLogin(ApplayPartenerActivity.this);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };

        HttpHelp.getInstance().create(RemoteApi.class).applayPartener(mobile, name, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(applayPartenerSubscriber);
    }

}
