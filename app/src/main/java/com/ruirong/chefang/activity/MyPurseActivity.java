package com.ruirong.chefang.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.UserInforBean;
import com.ruirong.chefang.bean.WalletBean;
import com.ruirong.chefang.event.RechargeSuccessEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.personalcenter.ExchangeRecordActivity;
import com.ruirong.chefang.personalcenter.GoldBeanBuyActivity;
import com.ruirong.chefang.personalcenter.GoldBeanIncreaseListActivity;
import com.ruirong.chefang.personalcenter.JinDouPayCashActivity;
import com.ruirong.chefang.util.ToolUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的钱包
 * Created by dillon on 2018/1/3.
 */

public class MyPurseActivity extends BaseActivity {

    @BindView(R.id.tv_allmoney)
    TextView tvAllmoney;
    @BindView(R.id.tv_goldsum)
    TextView tvGoldsum;
    @BindView(R.id.tv_silversum)
    TextView tvSilversum;

    UserInforBean userInforBean;

    @Override
    public int getContentView() {
        return R.layout.activity_mypurse;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("我的钱包");
        showLoadingDialog("加载中...");
        EventBusUtil.register(this);
        String userStr = new PreferencesHelper(MyPurseActivity.this).getUserInfo();
        userInforBean = new Gson().fromJson(userStr, UserInforBean.class);
    }

    @Override
    public void getData() {
        HttpHelp.getInstance().create(RemoteApi.class).wallet(new PreferencesHelper(this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<WalletBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<WalletBean> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {
                            WalletBean walletBean = baseBean.data;
                            tvAllmoney.setText(walletBean.getTotal_money());
                            tvGoldsum.setText(walletBean.getGold());
                            tvSilversum.setText(walletBean.getSilver());
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(MyPurseActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                    }
                });


    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getData();
    }


    /**
     * 充值结束后更新金额。
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateSumMoney(RechargeSuccessEvent event) {
        getData();
    }

    @OnClick({R.id.ll_growbeans, R.id.ll_profit, R.id.ll_goldbeantrans, R.id.ll_transformhis, R.id.ll_buybean, R.id.ll_buybeanhis, R.id.ll_sellbeans, R.id.ll_sellbeanshis, R.id.ll_bankcard, R.id.ll_paypwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_growbeans:     //种金豆
                startActivity(new Intent(this, GoldAddListActivity.class));
                break;
            case R.id.ll_profit:   //增长收益
                startActivity(new Intent(this, GoldBeanIncreaseListActivity.class));
                break;
            case R.id.ll_goldbeantrans:  //金豆转账
                startActivity(new Intent(this, SilverTransferActivity.class));
                break;
            case R.id.ll_transformhis:   //金豆转账记录
                startActivity(new Intent(this, SilverTransferRecordActivity.class));
                break;
            case R.id.ll_buybean:     //  购买金豆
                startActivity(new Intent(this, RechargeActivity.class));
                break;
            case R.id.ll_buybeanhis:   // 金豆购买记录
                Intent intent1 = new Intent(this, GoldBeanBuyActivity.class);

                startActivity(intent1);

                break;
            case R.id.ll_sellbeans:    // 金豆兑换
                Intent intent13 = new Intent(this, JinDouPayCashActivity.class);
                startActivity(intent13);
                break;
            case R.id.ll_sellbeanshis:   // 兑换记录
                Intent intent12 = new Intent(this, ExchangeRecordActivity.class);
                startActivity(intent12);
                break;
            case R.id.ll_bankcard:      // 银行卡
                startActivity(new Intent(this, BankCardActivity.class));
                break;
            case R.id.ll_paypwd:
                //TODO  修改密码的样式。
                // 支付密码设置
//                startActivity(new Intent(this, PayMentCodeActivity.class));

                //    startActivity(new Intent(this, UpdatePayPassActivity.class));
                Intent intent = null;
                if (userInforBean.getIs_has_pay_pass() == 1) {
                    ModifyPasswordActivity.startActivityWithParmeter(MyPurseActivity.this, "修改支付密码");
                } else {
                    intent = new Intent(MyPurseActivity.this, PayMentCodeActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }
}
