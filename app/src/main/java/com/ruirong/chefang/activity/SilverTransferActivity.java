package com.ruirong.chefang.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.CustomerBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 金豆转账申请和提交
 * Created by dillon on 2017/4/10.
 */

public class SilverTransferActivity extends BaseActivity {

    @BindView(R.id.et_oppositename)
    EditText etOppositename;
    @BindView(R.id.btn_suretransfers)
    Button btnSure;

    private BaseSubscriber<BaseBean<CustomerBean>> customerSubcriber;


    @Override
    public int getContentView() {
        return R.layout.activity_silvertransfer;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("转账");
        btnSure.setEnabled(false);
        btnSure.setPressed(false);
        etOppositename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()<1){
                    btnSure.setEnabled(false);
                    btnSure.setPressed(false);
                }else {
                    btnSure.setEnabled(true);
                    btnSure.setPressed(true);
                }
            }
        });

    }

    @Override
    public void getData() {

    }


    public void getCustomer(String mobile) {
        customerSubcriber = new BaseSubscriber<BaseBean<CustomerBean>>(this, null) {
            @Override
            public void onNext(BaseBean<CustomerBean> customerBeanBaseBean) {
                super.onNext(customerBeanBaseBean);
                if (customerBeanBaseBean.code == 0) {
                    CustomerBean customerBean = customerBeanBaseBean.data;

                    SilverTransferActivitytwo.startActivityResult(SilverTransferActivity.this, customerBean.getNickname(), customerBean.getRole(), customerBean.getRealname(), customerBean.getPic(), customerBean.getMobile());
                    finish();
                }else if (customerBeanBaseBean.code==4){
                    ToolUtil.loseToLogin(SilverTransferActivity.this);
                } else {
                    ToastUtil.showToast(SilverTransferActivity.this, customerBeanBaseBean.message);
                }
            }

            @Override
            public void onError(Throwable throwable) {

                super.onError(throwable);
            }
        };

        HttpHelp.getInstance().create(RemoteApi.class).customerMessage(mobile, new PreferencesHelper(SilverTransferActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(customerSubcriber);

    }


    @OnClick(R.id.btn_suretransfers)
    public void onViewClicked() {
        if (TextUtils.isEmpty(etOppositename.getText().toString())) {
            ToastUtil.showToast(SilverTransferActivity.this, "对方账户不能为空");
            return;
        } else {
           getCustomer(etOppositename.getText().toString().trim());

        }
    }
}
