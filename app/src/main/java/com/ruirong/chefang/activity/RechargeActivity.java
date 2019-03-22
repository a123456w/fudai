package com.ruirong.chefang.activity;

import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.common.Constants;
import com.ruirong.chefang.event.BalanceEvent;
import com.ruirong.chefang.event.OrderEvent;
import com.ruirong.chefang.event.RechargeSuccessEvent;
import com.ruirong.chefang.event.WxPayEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.AliPayUtil;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.util.WxPayUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 充值  买金豆
 * Created by BX on 2017/12/29.
 */

public class RechargeActivity extends BaseActivity {
    @BindView(R.id.et_sum)
    EditText etSum;
    @BindView(R.id.iv_wx)
    ImageView ivWx;
    @BindView(R.id.ll_wx)
    LinearLayout llWx;
    @BindView(R.id.iv_zfb)
    ImageView ivZfb;
    @BindView(R.id.ll_zfb)
    LinearLayout llZfb;
    @BindView(R.id.iv_yl)
    ImageView ivYl;
    @BindView(R.id.ll_yl)
    LinearLayout llYl;

    private String sum = "";
    private String tag = "RechargeActivity";

    /**
     * 2是微信，1是支付宝
     */
    private int type = 2;
    private int maxLen = 10;
    private PreferencesHelper preferencesHelper;


    @Override
    public int getContentView() {
        return R.layout.activity_rechanges;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("充值");
        ivWx.setSelected(true);

        etSum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = etSum.getText();
                int len = editable.length();

                //输完0x后把0删了，仿支付宝输入金额规则
                if (len >= 2 && s.charAt(0) == '0' && s.charAt(1) != '0') {

                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(1, len);
                    etSum.setText(newStr);
                    editable = etSum.getText();
                    //新字符串的长度
                    int newLen = editable.length();
                    //设置新光标所在的位置
                    Selection.setSelection(editable, newLen);

                }

                //输完00x后不能再输入
                if (len >= 3 && s.charAt(0) == '0' && s.charAt(1) == '0') {

                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, 2);
                    etSum.setText(newStr);
                    editable = etSum.getText();
                    //设置新光标所在的位置
                    Selection.setSelection(editable, 2);

                }


                if (len > maxLen) {
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, maxLen);
                    etSum.setText(newStr);
                    editable = etSum.getText();

                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        preferencesHelper = new PreferencesHelper(this);

        EventBusUtil.register(this);
    }

    @Override
    public void getData() {

    }

    /**
     * 选择微信支付
     */
    @OnClick(R.id.ll_wx)
    public void selectWx() {
        if (type != 2) {
            ivWx.setSelected(true);
            ivZfb.setSelected(false);
            ivYl.setSelected(false);
            type = 2;
        }
    }

    /**
     * 选择支付宝支付
     */
    @OnClick(R.id.ll_zfb)
    public void selectZfb() {
        if (type != 1) {
            ivZfb.setSelected(true);
            ivWx.setSelected(false);
            ivYl.setSelected(false);
            type = 1;
        }
    }
    /**
     *   银联支付
     */
    @OnClick(R.id.ll_yl)
    public void selectYl() {
        if (type != 3) {
            ivZfb.setSelected(false);
            ivWx.setSelected(false);
            ivYl.setSelected(true);
            type = 3;
        }
    }
    /**
     * 充值
     */
    @OnClick(R.id.tv_recharge)
    public void recharge() {
        sum = etSum.getText().toString().trim();
        if (TextUtils.isEmpty(sum)) {
            ToastUtil.showToast(this, "请先输入金额");
            return;
        } else if (sum.startsWith("0")) {
            ToastUtil.showToast(this, "金额必须大于0");
            return;
        }
        if (type == 1) {
            aliPay();
        } else if (type == 2) {
            Constants.WEIXIN_PAY_TYPE = tag;
            WxPayUtil.wxPay(preferencesHelper.getToken(), RechargeActivity.this, sum, type);
        }else if (type==3){
            ToastUtil.showToast(RechargeActivity.this,"银联支付暂未开通");
        }
    }

    /**
     * 支付宝支付
     */
    public void aliPay() {
        showLoadingDialog("");
        HttpHelp.getInstance().create(RemoteApi.class).recharge(preferencesHelper.getToken(), sum, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this, null) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {
                            new AliPayUtil().pay(baseBean.data, RechargeActivity.this, tag);
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(RechargeActivity.this);
                        } else {
                            ToastUtil.showToast(RechargeActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                        ToastUtil.showToast(RechargeActivity.this, getString(R.string.net_error));
                    }
                });
    }

    /**
     * 支付宝支付回调
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void aliPaySuccess(OrderEvent event) {
        if (event.tag.equals(tag)) {
            if (event.success) {
                EventBusUtil.post(new RechargeSuccessEvent());
                showRechargeSuccess();
            } else {
                ToastUtil.showToast(this, "支付失败");
            }

        }
    }

    /**
     * 微信支付回调
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void wxPaySuccess(WxPayEvent event) {
        if (event.tag.equals(tag)) {
            switch (event.code) {
                case 0:
                    EventBusUtil.post(new RechargeSuccessEvent());
                    showRechargeSuccess();
                    break;
                case -1:
                    ToastUtil.showToast(this, "支付失败");
                    break;
                case -2:
                    ToastUtil.showToast(this, "支付取消");
                    break;
            }
        }
    }


    /**
     * 支付成功弹窗
     */
    private void showRechargeSuccess() {
        EventBus.getDefault().postSticky(new BalanceEvent());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_recharge_successs, null, false);

        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        view.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
        view.findViewById(R.id.tv_recharge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSum.setText("");
                alertDialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }
}
