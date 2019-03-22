package com.ruirong.chefang.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.ruirong.chefang.bean.DoBuyConfirmBean;
import com.ruirong.chefang.bean.PersionMoneyBean;
import com.ruirong.chefang.bean.StatusBean;
import com.ruirong.chefang.bean.TiYanBean;
import com.ruirong.chefang.bean.TimesBean;
import com.ruirong.chefang.common.Constants;
import com.ruirong.chefang.event.OrderEvent;
import com.ruirong.chefang.event.RechargeSuccessEvent;
import com.ruirong.chefang.event.UpdateInformationEvent;
import com.ruirong.chefang.event.WxPayEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.AliPayUtil;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.util.WxPayUtil;
import com.ruirong.chefang.view.TiyanDialog;
import com.ruirong.chefang.view.TiyanInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.http.HTTP;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/8.
 * 我要买单
 */

public class GoToBuyABillActivity extends BaseActivity implements PayPwdView.InputCallBack {
    @BindView(R.id.ll_order)
    LinearLayout llOrder;
    @BindView(R.id.money_type)
    TextView moneyType;
    @BindView(R.id.goods_price)
    EditText goodsPrice;
    @BindView(R.id.money_surplus)
    TextView moneySurplus;
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
    private int type_x = 1;//0金币银币,1支付宝,2微信,3银联,4店铺储值  线下支付
    private BaseSubscriber<BaseBean<PersionMoneyBean>> persionMoneySubscriber;
    private Dialog mDialogs;
    private ImageView del_pic;
    private String shop_id;//商品id
    private String mode_name;//店铺名称
    private String tag = "GoToBuyABillActivity";
    PersionMoneyBean persionMoneyBean;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private AlertDialog.Builder tybuilder;
    private AlertDialog tyalertDialog;
    PopupWindow popwindow;

    @Override
    public int getContentView() {
        return R.layout.activity_go_to_buy_a_bill;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        EventBusUtil.register(this);
        titleBar.setTitleText("我要买单");
        titleBar.getIbLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        shop_id = getIntent().getStringExtra("shop_id");
        mode_name = getIntent().getStringExtra("mode_name");
        if (shop_id == null || mode_name == null) {
            finish();
        } else {
            moneyType.setText(mode_name);
        }
    }

    @Override
    public void getData() {
        persionMoney(new PreferencesHelper(GoToBuyABillActivity.this).getToken());
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
        tybuilder = new AlertDialog.Builder(this);
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
                AlertDialog tyalertDialog = (AlertDialog) v.getTag();

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
     * 获取余额
     *
     * @param token
     */
    private void persionMoney(String token) {

        persionMoneySubscriber = new BaseSubscriber<BaseBean<PersionMoneyBean>>(GoToBuyABillActivity.this, null) {
            @Override
            public void onNext(BaseBean<PersionMoneyBean> persionMoneyBeanBaseBean) {
                super.onNext(persionMoneyBeanBaseBean);
                if (persionMoneyBeanBaseBean.code == 0 && persionMoneyBeanBaseBean.data != null) {
                    persionMoneyBean = persionMoneyBeanBaseBean.data;
                    String text = "可用余额<font color = '#ff0000'>" + persionMoneyBean.getBack_money() + "</font>元";
                    moneySurplus.setText(Html.fromHtml(text));
                } else if (persionMoneyBeanBaseBean.code == 4) {
                    ToolUtil.loseToLogin(GoToBuyABillActivity.this);
                    finish();
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
                type_x = 1;
                setFalse();
                moneyZfb.setImageResource(R.drawable.orange_choosed_icon);

                break;
            case R.id.money_wx_ll:
                type_x = 2;
                setFalse();
                moneyWx.setImageResource(R.drawable.orange_choosed_icon);

                break;
            case R.id.money_yl_ll:
                type_x = 3;
                setFalse();
                moneyYl.setImageResource(R.drawable.orange_choosed_icon);

                break;
            case R.id.money_ye_ll:
                type_x = 0;
                setFalse();
                moneyYe.setImageResource(R.drawable.orange_choosed_icon);

                break;
            case R.id.money_cz_ll:
                type_x = 4;
                setFalse();
                moneyCz.setImageResource(R.drawable.orange_choosed_icon);

                break;
            case R.id.purchase:


                if (TextUtils.isEmpty(goodsPrice.getText().toString().trim())) {
                    ToastUtil.showToast(GoToBuyABillActivity.this, "请输入支付金额");
                    return;
                }

                if (type_x == 0 || type_x == 4) {
//                    showMima();
                    showPayDialog();

                } else if (type_x == 1) {
                    doBuyConfirmZ(new PreferencesHelper(GoToBuyABillActivity.this).getToken(),
                            shop_id, goodsPrice.getText().toString().trim(), type_x + "");
                } else if (type_x == 2) {
                    Constants.WEIXIN_PAY_TYPE = tag;
                    WxPayUtil.doBuyConfirm(GoToBuyABillActivity.this,
                            new PreferencesHelper(GoToBuyABillActivity.this).getToken(),
                            shop_id, goodsPrice.getText().toString().trim(), type_x + "");
                } else if (type_x == 3) {
                    ToastUtil.showToast(GoToBuyABillActivity.this, getResources().getString(R.string.yinlian_hint));
                }


                break;

            case R.id.shopping_bean:
                Intent intent1 = new Intent(GoToBuyABillActivity.this, RechargeActivity.class);
                startActivity(intent1);
                break;
        }
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
                        //刷新余额
                        // 调用打印机
                        sendPrint();

                        EventBus.getDefault().post(new UpdateInformationEvent());
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
                        //刷新余额
                        // 调用打印机
                        sendPrint();

                        EventBus.getDefault().post(new UpdateInformationEvent());
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
     * 弹出体验金的钱数和时间
     */
    public void showMoneyAndDay(String money,String day,  final String token) {

        LayoutInflater inflater = LayoutInflater.from(GoToBuyABillActivity.this);
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
                        // popwindow.dismiss();
                        ToastUtil.showToast(GoToBuyABillActivity.this,stringBaseBean.message);
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
                //  ToastUtil.showToast(this, "支付成功");
                showRechargeSuccess("支付成功");
                EventBusUtil.post(new RechargeSuccessEvent());
                getIsFirstOrder(new PreferencesHelper(GoToBuyABillActivity.this).getToken());

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
                    showRechargeSuccess("支付成功");
                    EventBusUtil.post(new RechargeSuccessEvent());
                    getIsFirstOrder(new PreferencesHelper(GoToBuyABillActivity.this).getToken());
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
        builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_recharge_success, null, false);
        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
        getIsFirstOrder(new PreferencesHelper(GoToBuyABillActivity.this).getToken());

        final Intent intent = new Intent(GoToBuyABillActivity.this, BusinessReviewsActivity.class);
        intent.putExtra("shop_id", shop_id);
        intent.putExtra("type", "7");
        TextView tvHint = (TextView)view.findViewById(R.id.tv_hint);
        tvHint.setText(msg);
        view.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                startActivity(intent);
                finish();
            }
        });
        view.findViewById(R.id.tv_recharge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                startActivity(intent);
                finish();
            }
        });
    }


    private void showPayDialog() {
        //弹出支付密码输入框
        DialogUtil.showPayDialog(GoToBuyABillActivity.this, "请输入支付密码", "￥" + goodsPrice.getText().toString().trim(), "余额:" + persionMoneyBean.getBack_money(), getSupportFragmentManager());
    }

    @Override
    public void onInputFinish(String result) {
        DialogUtil.dismissPayDialog();

        doBuyConfirm(new PreferencesHelper(GoToBuyABillActivity.this).getToken(),
                shop_id, goodsPrice.getText().toString().trim(), type_x + "",
                result, "1");

    }

    /**
     * 线下支付
     *
     * @param token
     * @param shop_id
     * @param money
     * @param pay_type
     * @param pay_pass
     * @param is_support
     */
    private void doBuyConfirm(String token, String shop_id, String money,
                              String pay_type, String pay_pass, String is_support) {

        HttpHelp.getInstance().create(RemoteApi.class).doBuyConfirm(token, shop_id, money, pay_type, pay_pass, is_support)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<DoBuyConfirmBean>>(GoToBuyABillActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<DoBuyConfirmBean> baseBean) {
                        super.onNext(baseBean);
//                        mDialogs.dismiss();
                        Log.i("XXXdddd", baseBean.code + "  " + baseBean.message);
                        if (baseBean.code == 0) {
                            if ((type_x == 0) || (type_x == 4)) {//0是余额支付 4是储值余额支付
                                showRechargeSuccess("支付成功");

                                //支付成功弹框
                            }
//                            finish();
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(GoToBuyABillActivity.this);
                        } else if (baseBean.message.contains("请先设置支付密码")) {
                            startActivity(new Intent(GoToBuyABillActivity.this, PayMentCodeActivity.class));
                        } else if (baseBean.message.contains("支付密码错误")) {
                            ToastUtil.showToast(GoToBuyABillActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
//                        mDialogs.dismiss();
                        Log.i("XXX", throwable.getLocalizedMessage());
                    }
                });
    }

    /**
     * 调用打印机
     */
    public void sendPrint() {
        HttpHelp.getInstance().create(RemoteApi.class).sendPrint()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this, null) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {

                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 线下支付 支付宝
     *
     * @param token
     * @param shop_id
     * @param money
     * @param pay_type
     */
    private void doBuyConfirmZ(String token, String shop_id, String money,
                               String pay_type) {
        showLoadingDialog("");
        HttpHelp.getInstance().create(RemoteApi.class).doBuyConfirmZ(token, shop_id, money, pay_type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(GoToBuyABillActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {
                            new AliPayUtil().pay(baseBean.data, GoToBuyABillActivity.this, tag);
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(GoToBuyABillActivity.this);
                        } else {
                            ToastUtil.showToast(GoToBuyABillActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.i("XXX", throwable.getLocalizedMessage());
                    }
                });
    }

    private void setFalse() {
        moneyZfb.setImageResource(R.drawable.no_choosed);
        moneyWx.setImageResource(R.drawable.no_choosed);
        moneyYl.setImageResource(R.drawable.no_choosed);
        moneyYe.setImageResource(R.drawable.no_choosed);
        moneyCz.setImageResource(R.drawable.no_choosed);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }


}
