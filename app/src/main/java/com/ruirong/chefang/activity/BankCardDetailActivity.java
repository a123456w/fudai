package com.ruirong.chefang.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DialogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.TextUtils;
import com.ruirong.chefang.util.ToolUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 银行卡详情，主要用于对银行卡的解绑操作
 * Created by dillon on 2017/5/3.
 */

public class BankCardDetailActivity extends BaseActivity {

    @BindView(R.id.tv_bankname)
    TextView tvBankname;
    @BindView(R.id.tv_banknumber)
    TextView tvBanknumber;
    @BindView(R.id.btn_unbind)
    Button btnUnbind;
    @BindView(R.id.rl_bankcard)
    RelativeLayout rlBankcard;
    private String bankcardid;

    private String bankName;
    private String bankNumber;
    private int color;

    private BaseSubscriber<BaseBean<String>> unbindSubscriber;
    private DialogUtil dialogUtil = new DialogUtil(BankCardDetailActivity.this);

    @Override
    public int getContentView() {
        return R.layout.activity_bankcarddetail;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("银行卡");

        bankName = getIntent().getStringExtra("bankName");
        bankNumber = getIntent().getStringExtra("bankNumber");
        color = getIntent().getIntExtra("bgColor",0);

        tvBankname.setText(bankName);
        tvBanknumber.setText(TextUtils.hideText(bankNumber));
        rlBankcard.setBackgroundResource(color);

      //  titleBar.setRightImageRes(R.drawable.unbind);
        bankcardid = getIntent().getStringExtra("bankcardid");
 /*       titleBar.setRightImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUtil.confirm("提示", "确定解除当前绑定银行卡？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        unbindBankCard(bankcardid);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
        });*/
        btnUnbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUtil.confirm("提示", "确定解除当前绑定银行卡？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        unbindBankCard(bankcardid);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
        });
    }

    @Override
    public void getData() {

    }

    public void unbindBankCard(String id) {
        unbindSubscriber = new BaseSubscriber<BaseBean<String>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<String> stringBaseBean) {
                super.onNext(stringBaseBean);
                ToastUtil.showToast(BankCardDetailActivity.this, stringBaseBean.message);
                if (stringBaseBean.code == 0) {
                    Intent intent = new Intent(BankCardDetailActivity.this, BankCardActivity.class);
                    setResult(RESULT_OK, intent);
                    finish();
                }else if (stringBaseBean.code==4){
                    ToolUtil.loseToLogin(BankCardDetailActivity.this);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                finish();
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).deleteBankCard(id, new PreferencesHelper(BankCardDetailActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(unbindSubscriber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbindSubscriber != null && unbindSubscriber.isUnsubscribed()) {
            unbindSubscriber.unsubscribe();
        }
    }

}
