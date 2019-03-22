package com.ruirong.chefang.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.UserInforBean;
import com.ruirong.chefang.event.PayPwdSuccedEvent;
import com.ruirong.chefang.event.RechargeSuccessEvent;
import com.ruirong.chefang.event.UserEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.widget.PasswordInputView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 支付密码设置
 * Created by dillon on 2017/4/10.
 */

public class PayMentCodeActivity extends BaseActivity {

    @BindView(R.id.pass_pwd)
    PasswordInputView passPwd;
    @BindView(R.id.pass_surepwd)
    PasswordInputView passSurepwd;
    @BindView(R.id.tv_wrongpwd)
    TextView tvWrongpwd;
    @BindView(R.id.btn_commit)
    Button btnCommit;

    private BaseSubscriber<BaseBean<String>> payPassSetSubscriber ;

    @Override
    public int getContentView() {
        return R.layout.activity_paymentcode;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("设置支付密码");

        passPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count<6){
                    tvWrongpwd.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passSurepwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (count<6){
                            tvWrongpwd.setVisibility(View.GONE);
                        }
            }
            @Override
            public void afterTextChanged(Editable s) {
                   if (passPwd.getText().toString().length()==6&&passSurepwd.getText().toString().length()==6){
                       if (!passPwd.getText().toString().equals(passSurepwd.getText().toString())){
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
           if (TextUtils.isEmpty(passPwd.getText().toString())){
               ToastUtil.showToast(PayMentCodeActivity.this,"请输入支付密码");
               return;
           }
           if (TextUtils.isEmpty(passSurepwd.getText().toString())){
               ToastUtil.showToast(PayMentCodeActivity.this,"请输入确认支付密码");
               return;
           }
           if (!passPwd.getText().toString().equals(passSurepwd.getText().toString())){
               ToastUtil.showToast(PayMentCodeActivity.this,"支付密码和确认支付密码不一致");
               tvWrongpwd.setVisibility(View.VISIBLE);
               return;
           }

           payPwdSet(passSurepwd.getText().toString());
    }

    /**
     * 设置支付密码
     * @param password
     */
    public void payPwdSet(String password){
        payPassSetSubscriber = new BaseSubscriber<BaseBean<String>>(this,null){
            @Override
            public void onNext(BaseBean<String> stringBaseBean) {
                ToastUtil.showToast(PayMentCodeActivity.this,stringBaseBean.message);
                if (stringBaseBean.code==0){
                    PreferencesHelper preferencesHelper = new PreferencesHelper(PayMentCodeActivity.this);
                    Gson gson = new Gson();
                    UserInforBean userInforBean =gson.fromJson(preferencesHelper.getUserInfo(), UserInforBean.class);
                    userInforBean.setIs_has_pay_pass(1);
                    preferencesHelper.saveUserInfo(gson.toJson(userInforBean));
                    EventBusUtil.post(new UserEvent());
                    EventBusUtil.post(new PayPwdSuccedEvent());
                    EventBusUtil.post(new RechargeSuccessEvent());
                    finish();
                }else if (stringBaseBean.code==4){
                    ToolUtil.loseToLogin(PayMentCodeActivity.this);
                }
            }
            @Override
            public void onError(Throwable throwable){
               ToastUtil.showToast(PayMentCodeActivity.this,getString(R.string.net_error));
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).setPayPwd(password,new PreferencesHelper(PayMentCodeActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(payPassSetSubscriber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (payPassSetSubscriber!=null&&payPassSetSubscriber.isUnsubscribed()){
            payPassSetSubscriber.unsubscribe();
        }
    }
}
