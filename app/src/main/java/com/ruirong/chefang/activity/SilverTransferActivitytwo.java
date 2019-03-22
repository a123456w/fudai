package com.ruirong.chefang.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DialogUtil;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.payDialog.PayPwdView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.UserInforBean;
import com.ruirong.chefang.event.LoginSuccessEvent;
import com.ruirong.chefang.event.WalletEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 金豆转账申请和提交
 * Created by dillon on 2017/4/10.
 */

public class SilverTransferActivitytwo extends BaseActivity implements PayPwdView.InputCallBack {


    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.tv_roletype)
    TextView tvRoletype;
    @BindView(R.id.transfer)
    TextView transfer;
    @BindView(R.id.et_transfersum)
    EditText etTransfersum;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    private BaseSubscriber<BaseBean<String>> silverTranferSubscriber;
    private BaseSubscriber<BaseBean<Object>> silverTranferCommitSubscriber;
    public AlertDialog alertDialog;
    private String orderid;

    private String nicknam;
    private String rol;
    private String relnam;
    private String pic;
    private String mobil;

    @Override
    public int getContentView() {
        return R.layout.activity_goldtransfer;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        etTransfersum.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        Intent intent = getIntent();
        nicknam = intent.getStringExtra("nickname");
        rol = intent.getStringExtra("role");
        relnam = intent.getStringExtra("realname");
        pic = intent.getStringExtra("pic");
        mobil = intent.getStringExtra("mobile");

        if (!TextUtils.isEmpty(nicknam) && !TextUtils.isEmpty(relnam)) {
            tvName.setText(nicknam + "(" + relnam + ")");
        }
        if (!TextUtils.isEmpty(rol)) {
            tvRoletype.setText(rol);
        }

        if (!TextUtils.isEmpty(mobil)) {
            tvTel.setText(mobil);
        }
        GlideUtil.display(SilverTransferActivitytwo.this, Constants.IMG_HOST + pic, ivHead);

        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("金豆转账");
        btnConfirm.setEnabled(false);
        btnConfirm.setPressed(false);
        etTransfersum.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        etTransfersum.setText(s);
                        etTransfersum.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    etTransfersum.setText(s);
                    etTransfersum.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        etTransfersum.setText(s.subSequence(0, 1));
                        etTransfersum.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 1) {
                    btnConfirm.setEnabled(false);
                    btnConfirm.setPressed(false);
                } else {
                    btnConfirm.setEnabled(true);
                    btnConfirm.setPressed(true);
                }
            }
        });
    }

    @Override
    public void getData() {

    }

    public static void startActivityResult(Context context, String nickname, String role, String realname, String pic, String mobile) {
        Intent intent = new Intent(context, SilverTransferActivitytwo.class);
        intent.putExtra("nickname", nickname);
        intent.putExtra("role", role);
        intent.putExtra("realname", realname);
        intent.putExtra("pic", pic);
        intent.putExtra("mobile", mobile);

        context.startActivity(intent);


    }

    @OnClick(R.id.btn_confirm)
    public void onClick() {

        if (TextUtils.isEmpty(etTransfersum.getText().toString())) {
            ToastUtil.showToast(SilverTransferActivitytwo.this, "请输入转账金额");
            return;
        }
        silverTransferApply(mobil, etTransfersum.getText().toString());

    }

    @Override
    public void onInputFinish(String result) {

        silverTransferCommit(orderid, result);

    }

    /**
     * 金豆转账申请
     *
     * @param mobile
     * @param cashmoney
     */
    public void silverTransferApply(String mobile, String cashmoney) {
        silverTranferSubscriber = new BaseSubscriber<BaseBean<String>>(this, null) {
            @Override
            public void onNext(BaseBean<String> stringBaseBean) {
                super.onNext(stringBaseBean);

                if (stringBaseBean.code == 0) {
                    UserInforBean userInforBean = new Gson().fromJson(new PreferencesHelper(SilverTransferActivitytwo.this).getUserInfo(), UserInforBean.class);
                    if (userInforBean != null) {
                        if (userInforBean.getIs_has_pay_pass() == 0) {
                            startActivity(SilverTransferActivitytwo.this, PayMentCodeActivity.class);
                        } else if (userInforBean.getIs_has_pay_pass() == 1) {
                            orderid = stringBaseBean.data;
                            DialogUtil.showPayDialog(SilverTransferActivitytwo.this, "请输入支付密码", etTransfersum.getText().toString(), "转账金额是" + etTransfersum.getText().toString(), getSupportFragmentManager());
                        }
                    }

                } else if (stringBaseBean.code == 4) {
                    ToolUtil.loseToLogin(SilverTransferActivitytwo.this);
                } else {
                    showDialog(SilverTransferActivitytwo.this, stringBaseBean.code, stringBaseBean.message, "取消");
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).silverTransfer(mobile, cashmoney, new PreferencesHelper(SilverTransferActivitytwo.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(silverTranferSubscriber);
    }

    /**
     * 转账申请提交
     *
     * @param orderId
     * @param pay_pass
     */
    public void silverTransferCommit(String orderId, String pay_pass) {
        silverTranferCommitSubscriber = new BaseSubscriber<BaseBean<Object>>(this, null) {
            @Override
            public void onNext(BaseBean<Object> stringBaseBean) {
                super.onNext(stringBaseBean);
                DialogUtil.dismissPayDialog();
                if (stringBaseBean.code == 0) {
                    EventBusUtil.post(new WalletEvent());
                    EventBus.getDefault().post(new LoginSuccessEvent());
                    showDialog(SilverTransferActivitytwo.this, stringBaseBean.code, stringBaseBean.message, "继续转账");
                } else if (stringBaseBean.code == 4) {
                    ToolUtil.loseToLogin(SilverTransferActivitytwo.this);
                } else {
                    showDialog(SilverTransferActivitytwo.this, 3, stringBaseBean.message, "取消");
                }

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                DialogUtil.dismissPayDialog();
                ToastUtil.showToast(SilverTransferActivitytwo.this, "请稍后再试");
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).silverTransferCommit(orderId, pay_pass, new PreferencesHelper(SilverTransferActivitytwo.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(silverTranferCommitSubscriber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (silverTranferCommitSubscriber != null && silverTranferCommitSubscriber.isUnsubscribed()) {
            silverTranferCommitSubscriber.unsubscribe();
        }
        if (silverTranferSubscriber != null && silverTranferSubscriber.isUnsubscribed()) {
            silverTranferSubscriber.unsubscribe();
        }
    }

    public void showDialog(final Context context, final int icon, String msg, String btncontinue) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_dialogd, null, false);
        ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        TextView tvHint = (TextView) view.findViewById(R.id.tv_hint);
        TextView tvContinue = (TextView) view.findViewById(R.id.tv_continue);

        switch (icon) {
            case 1:
                GlideUtil.display(SilverTransferActivitytwo.this, R.drawable.icon_6, ivIcon);
                break;
            case 2:
                GlideUtil.display(SilverTransferActivitytwo.this, R.drawable.icon_3, ivIcon);
                break;
            case 3:
                GlideUtil.display(SilverTransferActivitytwo.this, R.drawable.icon_6, ivIcon);
                break;
        }

        tvHint.setText(msg);
        tvContinue.setText(btncontinue);

        view.findViewById(R.id.tv_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (icon) {
                    case 0:
                        etTransfersum.getText().clear();
                        break;
                    case 1:
                        //  etOppositeaccount.getText().clear();
                        break;
                    case 2:
                        etTransfersum.getText().clear();
                        break;
                    case 3:
                        break;
                }
                alertDialog.dismiss();
            }
        });
        view.findViewById(R.id.tv_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                switch (icon) {
                    case 0:
                        finish();
                        break;
                    case 1:
                        //   etOppositeaccount.getText().clear();
                        break;
                    case 2:
                        etTransfersum.getText().clear();
                        break;
                    case 3:
                        DialogUtil.showPayDialog(SilverTransferActivitytwo.this, "请输入支付密码", etTransfersum.getText().toString(), "转账金额是" + etTransfersum.getText().toString(), getSupportFragmentManager());
                        break;
                }

            }
        });
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();

    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {

    }
}
