package com.ruirong.chefang.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DialogUtil;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.payDialog.PayPwdView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.InfoWBean;
import com.ruirong.chefang.bean.PersionMoneyBean;
import com.ruirong.chefang.bean.StatusBean;
import com.ruirong.chefang.bean.TiYanBean;
import com.ruirong.chefang.common.Constants;
import com.ruirong.chefang.event.BalanceEvent;
import com.ruirong.chefang.event.LoginSuccessEvent;
import com.ruirong.chefang.event.OrderEvent;
import com.ruirong.chefang.event.PendingPaymentEvent;
import com.ruirong.chefang.event.RechargeSuccessEvent;
import com.ruirong.chefang.event.ReservationOrderEvent;
import com.ruirong.chefang.event.WxPayEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.AliPayUtil;
import com.ruirong.chefang.util.CashierInputFilter;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.util.WxPayUtil;
import com.ruirong.chefang.view.TiyanDialog;
import com.ruirong.chefang.view.TiyanInterface;
import com.ruirong.chefang.wheel.WheelView;
import com.ruirong.chefang.wheel.adapter.ArrayWheelAdapter;
import com.ruirong.chefang.widget.PasswordInputView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/21.
 * 立即支付页面
 */

public class ImmediatelyPaymentActivity extends BaseActivity implements PayPwdView.InputCallBack {
    @BindView(R.id.ll_order)
    LinearLayout llOrder;
    @BindView(R.id.money_type)
    TextView moneyType;
    @BindView(R.id.money_zfb)
    ImageView moneyZfb;
    @BindView(R.id.money_zfb_ll)
    RelativeLayout moneyZfbLl;
    @BindView(R.id.money_wx)
    ImageView moneyWx;
    @BindView(R.id.money_wx_ll)
    RelativeLayout moneyWxLl;
    @BindView(R.id.money_yl)
    ImageView moneyYl;
    @BindView(R.id.money_yl_ll)
    RelativeLayout moneyYlLl;
    @BindView(R.id.money_ye)
    ImageView moneyYe;
    @BindView(R.id.money_ye_ll)
    RelativeLayout moneyYeLl;
    @BindView(R.id.money_cz)
    ImageView moneyCz;
    @BindView(R.id.money_cz_ll)
    RelativeLayout moneyCzLl;
    @BindView(R.id.purchase)
    TextView purchase;
    @BindView(R.id.shopping_bean)
    TextView shoppingBean;
    @BindView(R.id.money_surplus)
    TextView moneySurplus;
    @BindView(R.id.goods_price)
    EditText goodsPrice;

    private android.app.AlertDialog.Builder tybuilder;
    private android.app.AlertDialog tyalertDialog;
    private int type = 2;//1 金豆银豆 2支付宝 3微信 4银联 5储值   线上支付
    private BaseSubscriber<BaseBean<PersionMoneyBean>> persionMoneySubscriber;
    private String place_type;//1购买店铺商品 2预定店铺套餐 3预定酒店房间 4特产订单
    private String shop_price;
    private String order_sn;
    private Dialog mDialogs;
    private PasswordInputView pass_pwd;
    private String shop_id;
    private String mode_name;
    private String tag = "ImmediatelyPaymentActivity";
    PersionMoneyBean persionMoneyBean;
    PopupWindow popwindow;
    @Override
    public int getContentView() {
        return R.layout.activity_immediately_payment;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("我要买单");

        EventBusUtil.register(this);
        InputFilter[] filters = {new CashierInputFilter()};
        goodsPrice.setFilters(filters);
        titleBar.getIbLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        place_type = getIntent().getStringExtra("place_type");
        shop_price = getIntent().getStringExtra("shop_price");
        order_sn = getIntent().getStringExtra("order_sn");
        if (shop_price == null || order_sn == null || place_type == null) {
            ToastUtil.showToast(ImmediatelyPaymentActivity.this, "订单错误！");
            finish();
        } else {
            moneyType.setVisibility(View.GONE);
            goodsPrice.setText(shop_price + "");
            goodsPrice.setEnabled(false);
        }

        if ("4".equals(place_type)) {
            moneyCzLl.setVisibility(View.GONE);
        }


    }

    @Override
    public void getData() {
        persionMoney(new PreferencesHelper(ImmediatelyPaymentActivity.this).getToken());
    }

    /**
     * 获取余额
     *
     * @param token
     */
    private void persionMoney(String token) {
        persionMoneySubscriber = new BaseSubscriber<BaseBean<PersionMoneyBean>>(ImmediatelyPaymentActivity.this, null) {
            @Override
            public void onNext(BaseBean<PersionMoneyBean> persionMoneyBeanBaseBean) {
                super.onNext(persionMoneyBeanBaseBean);
                if (persionMoneyBeanBaseBean.code == 0 && persionMoneyBeanBaseBean.data != null) {
                    persionMoneyBean = persionMoneyBeanBaseBean.data;
                    String text = "可用额度<font color = '#ff0000'>" + persionMoneyBean.getBack_money() + "</font>元";
                    moneySurplus.setText(Html.fromHtml(text));
                } else if (persionMoneyBeanBaseBean.code == 4) {
                    ToolUtil.loseToLogin(ImmediatelyPaymentActivity.this);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).persionMoney(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(persionMoneySubscriber);

    }

    @OnClick({R.id.money_zfb_ll, R.id.money_wx_ll, R.id.money_yl_ll, R.id.money_ye_ll, R.id.money_cz_ll, R.id.purchase, R.id.shopping_bean})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.money_zfb_ll:
                type = 2;
                setFalse();
                moneyZfb.setImageResource(R.drawable.orange_choosed_icon);

                break;
            case R.id.money_wx_ll:
                type = 3;
                setFalse();
                moneyWx.setImageResource(R.drawable.orange_choosed_icon);

                break;
            case R.id.money_yl_ll:
                type = 4;
                setFalse();
                moneyYl.setImageResource(R.drawable.orange_choosed_icon);

                break;
            case R.id.money_ye_ll:
                type = 1;
                setFalse();
                moneyYe.setImageResource(R.drawable.orange_choosed_icon);

                break;
            case R.id.money_cz_ll:
                type = 5;
                setFalse();
                moneyCz.setImageResource(R.drawable.orange_choosed_icon);

                break;
            case R.id.purchase:

                if (type == 1 || type == 5) {
//                    showMima();
                    showPayDialog();
                } else if (type == 2) {
                    infoPreciZ(new PreferencesHelper(ImmediatelyPaymentActivity.this).getToken(),
                            type + "", order_sn, place_type);
                } else if (type == 3) {
                    Constants.WEIXIN_PAY_TYPE = tag;
                    WxPayUtil.infoPreci(ImmediatelyPaymentActivity.this,
                            new PreferencesHelper(ImmediatelyPaymentActivity.this).getToken(),
                            type + "", order_sn, place_type);
                } else if (type == 4) {
                    ToastUtil.showToast(ImmediatelyPaymentActivity.this, getResources().getString(R.string.yinlian_hint));
                }


                break;

            case R.id.shopping_bean:
                Intent intent1 = new Intent(ImmediatelyPaymentActivity.this, RechargeActivity.class);
                startActivity(intent1);
                break;
        }
    }

    private void setFalse() {
        moneyZfb.setImageResource(R.drawable.no_choosed);
        moneyWx.setImageResource(R.drawable.no_choosed);
        moneyYl.setImageResource(R.drawable.no_choosed);
        moneyYe.setImageResource(R.drawable.no_choosed);
        moneyCz.setImageResource(R.drawable.no_choosed);
    }


    private void showPayDialog() {
        //弹出支付密码输入框
        DialogUtil.showPayDialog(this, "请输入支付密码", "￥" + goodsPrice.getText().toString().trim(), "余额:" + persionMoneyBean.getBack_money(), getSupportFragmentManager());
    }


    /**
     * 支付宝付款
     *
     * @param token
     * @param paytype
     * @param order_sn
     * @param type
     */
    private void infoPreciZ(String token, String paytype, String order_sn,
                            final String type) {
        showLoadingDialog("");
        HttpHelp.getInstance().create(RemoteApi.class).infoPreciZ(token, paytype, order_sn, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(ImmediatelyPaymentActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {
                            new AliPayUtil().pay(baseBean.data, ImmediatelyPaymentActivity.this, tag);
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(ImmediatelyPaymentActivity.this);
                        } else {
                            ToastUtil.showToast(ImmediatelyPaymentActivity.this, baseBean.message);
                        }
//                        finish();
//                        ToastUtil.showToast(ImmediatelyPaymentActivity.this, baseBean.message);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mDialogs.dismiss();
                        Log.i("XXX", throwable.getLocalizedMessage());
                    }
                });
    }
    /**
     * 是否是首次消费
     * @param token
     */
    public void getIsFirstOrder(final String token){
        HttpHelp.getInstance().create(RemoteApi.class).isFirstOrder(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<StatusBean>>(this,null){
                    @Override
                    public void onNext(BaseBean<StatusBean> statusBeanBaseBean) {
                        super.onNext(statusBeanBaseBean);
                        if (statusBeanBaseBean.code==0){
                            StatusBean statusBean = statusBeanBaseBean.data;
                            if (statusBean!=null){
                                // TODO  1 的时候是首次消费   2 的时候不是首次消费

                                if ("1".equals(statusBean.getStatus())){

                                    getTiyanNumber(token);

                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 获取体验金数量
     * @param token
     */
    public void getTiyanNumber(final String token){
        HttpHelp.getInstance().create(RemoteApi.class).getTiYanNum(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<TiYanBean>>(this,null){
                    @Override
                    public void onNext(BaseBean<TiYanBean> tiYanBeanBaseBean) {
                        super.onNext(tiYanBeanBaseBean);
                        if (tiYanBeanBaseBean.code==0){
                            TiYanBean tiYanBean = tiYanBeanBaseBean.data;
                            if (tiYanBean!=null){
                                // 弹出体验金的时间和天   popupwindow形式
                                // showMoneyAndDay(tiYanBean.getMoney(),tiYanBean.getDay(),token);
                                // 弹出体验金的时间和天   Alertdialog 形式
                                showDialogTiyan(tiYanBean.getMoney(),tiYanBean.getDay(),token);

                            /*
                                final TiyanDialog tiyanDialog = new TiyanDialog(ImmediatelyPaymentActivity.this);
                                tiyanDialog.setData(tiYanBean);
                                tiyanDialog.setTiyanInterface(new TiyanInterface() {
                                    @Override
                                    public void getTiyan() {
                                        holdTiyan(token);
                                        tiyanDialog.dismiss();
                                    }
                                });
                                tiyanDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                tiyanDialog.show();*/
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }
    /**
     * 弹出体验单
     * @param money
     * @param day
     * @param token
     */
    public void showDialogTiyan(String money,String day,  final String token){
        if(tyalertDialog!=null)
            return;
        tybuilder = new android.app.AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_tiyan, null, false);
        tybuilder.setView(view);
        tybuilder.setCancelable(false);
        tyalertDialog = tybuilder.create();

        tyalertDialog.show();

        TextView tvTiyan = (TextView) view.findViewById(R.id.tv_tiyannum);
        TextView tvTiyannum = (TextView) view.findViewById(R.id.tv_tiyangoldnum);
        TextView tvTiyanDay = (TextView) view.findViewById(R.id.tvhint);

        TextView tvGetTiyan = ((TextView) view.findViewById(R.id.tv_gettiyan));
        tvGetTiyan.setTag(tyalertDialog);
        ImageView ivClose = ((ImageView) view.findViewById(R.id.iv_tiyan));
        tvTiyan.setText(money);
        tvTiyannum.setText(money);
        tvTiyanDay.setText(day);
        tvGetTiyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog tyalertDialog = (android.app.AlertDialog) v.getTag();

                tyalertDialog.dismiss();
                tyalertDialog = null;
                holdTiyan(token);

            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tyalertDialog.dismiss();
                tyalertDialog = null;
            }
        });
    }

    /**
     * 弹出行业种类的行业类型选择器
     */
    public void showMoneyAndDay(String money,String day,  final String token) {

        LayoutInflater inflater = LayoutInflater.from(ImmediatelyPaymentActivity.this);
        final View inflate = inflater.inflate(R.layout.dialog_tiyan, null);

        popwindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popwindow.setBackgroundDrawable(new BitmapDrawable());
       // popwindow.setAnimationStyle(R.style.ActionSheetDialogStyle); // 设置动画
        popwindow.setFocusable(true);
        popwindow.setOutsideTouchable(false);
        popwindow.setTouchable(true);
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = (float) 0.5;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);

        popwindow.showAtLocation(llOrder, Gravity.CENTER, 0, 0);
        TextView tvTiyan = (TextView) inflate.findViewById(R.id.tv_tiyannum);
        TextView tvTiyannum = (TextView) inflate.findViewById(R.id.tv_tiyangoldnum);
        TextView tvTiyanDay = (TextView) inflate.findViewById(R.id.tvhint);

        TextView tvGetTiyan = ((TextView) inflate.findViewById(R.id.tv_gettiyan));

        ImageView ivClose = ((ImageView) inflate.findViewById(R.id.iv_tiyan));
        tvTiyan.setText(money);
        tvTiyannum.setText(money);
        tvTiyanDay.setText(day);
        tvGetTiyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holdTiyan(token);

            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popwindow.dismiss();
            }
        });
        popwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = (float) 1.0;
                getWindow().setAttributes(lp);
            }
        });


    }

    /**
     * 获取体验金
     * @param token
     */
    public void holdTiyan(String token){
        HttpHelp.getInstance().create(RemoteApi.class).holdTiyan(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this,null){
                    @Override
                    public void onNext(BaseBean<String> stringBaseBean) {
                        super.onNext(stringBaseBean);
                        ToastUtil.showToast(ImmediatelyPaymentActivity.this,stringBaseBean.message);
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
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
                showRechargeSuccess("支付成功");
                getIsFirstOrder(new PreferencesHelper(ImmediatelyPaymentActivity.this).getToken());

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
                    showRechargeSuccess("支付成功");
                    getIsFirstOrder(new PreferencesHelper(ImmediatelyPaymentActivity.this).getToken());
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
    private void showRechargeSuccess(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_recharge_success, null, false);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        TextView tvHint = (TextView)view.findViewById(R.id.tv_hint);
        tvHint.setText(msg);
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
                alertDialog.dismiss();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        EventBusUtil.unregister(this);
    }


    private void infoPreci(final String token, String paytype, String order_sn,
                           final String type, String is_support, String pay_pass) {

        HttpHelp.getInstance().create(RemoteApi.class).infoPreci(token, paytype, order_sn, type, is_support, pay_pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<InfoWBean>>(ImmediatelyPaymentActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<InfoWBean> baseBean) {
                        super.onNext(baseBean);
//                        mDialogs.dismiss();
                        if (baseBean.code == 0) {
                            switch (place_type) {
                                case "4":

                                    //特产
                                    EventBus.getDefault().post(new PendingPaymentEvent());
                                    getIsFirstOrder(token);
                                    break;
                                case "3":

                                    //预订
                                    EventBus.getDefault().post(new ReservationOrderEvent());
                                    getIsFirstOrder(token);
                                    break;

                            }
                            //刷新余额
                            EventBus.getDefault().post(new LoginSuccessEvent());
                            EventBus.getDefault().postSticky(new BalanceEvent());

                            /*finish();*/
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(ImmediatelyPaymentActivity.this);
                        } else if (baseBean.message.contains("请先设置支付密码")) {
                            startActivity(new Intent(ImmediatelyPaymentActivity.this, PayMentCodeActivity.class));
                        } else if (baseBean.message.contains("支付密码错误")) {
                            ToastUtil.showToast(ImmediatelyPaymentActivity.this, baseBean.message);
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    //do something
                                    showPayDialog();
                                }
                            }, 300);    //延时1s执行
                        }
                        ToastUtil.showToast(ImmediatelyPaymentActivity.this, baseBean.message);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
//                        mDialogs.dismiss();
                        Log.i("XXX", throwable.getLocalizedMessage());
                    }
                });
    }


    public void showKeyboard() {
        if (pass_pwd != null) {
            //设置可获得焦点
            pass_pwd.setFocusable(true);
            pass_pwd.setFocusableInTouchMode(true);
            //请求获得焦点
            pass_pwd.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) pass_pwd
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(pass_pwd, 0);
        }
    }


    @Override
    public void onInputFinish(String result) {
        infoPreci(new PreferencesHelper(ImmediatelyPaymentActivity.this).getToken(), type + "",
                order_sn, place_type, "1", result);

        DialogUtil.dismissPayDialog();
    }


}
