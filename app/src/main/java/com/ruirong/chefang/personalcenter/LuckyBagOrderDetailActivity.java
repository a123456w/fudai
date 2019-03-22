package com.ruirong.chefang.personalcenter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DateUtil;
import com.qlzx.mylibrary.util.DialogUtil;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.TimeUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.payDialog.PayPwdView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.AddressActivity;
import com.ruirong.chefang.activity.ExpressDeliveryDetailsActivity;
import com.ruirong.chefang.activity.ReserveOrderCommentActivity;
import com.ruirong.chefang.activity.SettingPaymentPasswordActivity;
import com.ruirong.chefang.activity.WebActivity;
import com.ruirong.chefang.adapter.ReasonAdapter;
import com.ruirong.chefang.bean.LuckyBagOrderDetailBean;
import com.ruirong.chefang.bean.SelectedStateBean;
import com.ruirong.chefang.bean.UserInforBean;
import com.ruirong.chefang.event.LuckyOrderCommnetEvent;
import com.ruirong.chefang.event.UpdateBeforeUIDateEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.widget.PasswordInputView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wu on 2018/4/4.
 * describe: 福袋订单详情
 */

public class LuckyBagOrderDetailActivity extends BaseActivity implements PayPwdView.InputCallBack {

    @BindView(R.id.order_state)
    TextView orderState;
    @BindView(R.id.order_time)
    TextView orderTime;
    @BindView(R.id.cv_countdownView)
    CountdownView cvCountdownView;

    @BindView(R.id.goods_address)
    RelativeLayout goodsAddress;
    @BindView(R.id.tv_consignee)
    TextView tvConsignee;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.detailed_address)
    TextView detailedAddress;

    @BindView(R.id.goods_pic)
    ImageView goodsPic;
    @BindView(R.id.goods_name)
    TextView goodsName;
    @BindView(R.id.goods_type)
    TextView goodsType;
    @BindView(R.id.new_price)
    TextView newPrice;
    @BindView(R.id.old_price)
    TextView oldPrice;
    @BindView(R.id.goods_num)
    TextView goodsNum;

    @BindView(R.id.order_refund)
    TextView orderRefund;
    @BindView(R.id.real_price)
    TextView realPrice;
    @BindView(R.id.contact)
    TextView contact;
    @BindView(R.id.order_number)
    TextView orderNumber;
    @BindView(R.id.order_establish_time)
    TextView orderEstablishTime;


    @BindView(R.id.pending_payment)
    RelativeLayout pendingPayment;//待付款
    @BindView(R.id.tv_payment)
    TextView tvPayment;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_contact_seller)
    TextView tvContactSeller;

    @BindView(R.id.pending_delivery)
    RelativeLayout pendingDelivery;//待发货
    @BindView(R.id.tv_deliver)
    TextView tvDeliver;

    @BindView(R.id.goods_receipt)
    RelativeLayout goodsReceipt; //待收货
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.tv_look_logistics)
    TextView tvLookLogistics;

    @BindView(R.id.to_be_evaluated)
    RelativeLayout toBeEvaluated;//待评价
    @BindView(R.id.tv_evaluate)
    TextView tvEvaluate;
    @BindView(R.id.tv_look_logistics_collect)
    TextView tvLookLogisticsCollect;

    @BindView(R.id.customer_del_lr)
    RelativeLayout customerDelLr;//删除订单
    @BindView(R.id.customer_del)
    TextView customerDel;

    private Context mContext;
    private String number_bh;
    private String gqTime;
    private LuckyBagOrderDetailBean detail;

    public static final int PAYCODE = 1;
    public static final int CONFIG = 2;

    private Dialog mDialog;
    private TextView no, yes;
    private ListView id_reason;
    private List<SelectedStateBean> reasonList = new ArrayList<>();
    private String reasonText;
    private ReasonAdapter reasonAdapter;

    private Dialog qrDialog;//确认收货

    private int type;

    @Override
    public int getContentView() {
        return R.layout.activity_luckybag_order_detail;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        mContext = this;
        EventBusUtil.register(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("优客订单详情");

        number_bh = getIntent().getStringExtra("number_bh");
        if (number_bh == null) {
            finish();
        }
    }

    @Override
    public void getData() {
        getLuckyBagDetails(new PreferencesHelper(LuckyBagOrderDetailActivity.this).getToken(), number_bh);
    }

    /**
     * 获取详情
     *
     * @param token
     * @param orderId
     */
    private void getLuckyBagDetails(String token, String orderId) {
//        showLoadingDialog("加载中...");
        HttpHelp.getInstance().create(RemoteApi.class).getLuckyBagDetail(token, orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<LuckyBagOrderDetailBean>>(LuckyBagOrderDetailActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<LuckyBagOrderDetailBean> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {
                            detail = baseBean.data;
                            gqTime = detail.getGqTime();
                            updateUI(detail.getList(), detail.getGoods_info(), detail.getAddr());
                        } else if (baseBean.code==4){
                            ToolUtil.loseToLogin(LuckyBagOrderDetailActivity.this);
                        }else {
                            loadingLayout.setStatus(LoadingLayout.Empty);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                    }
                });
    }

    /**
     * 设置UI
     *
     * @param order
     * @param goodsInfo
     * @param people
     */
    private void updateUI(LuckyBagOrderDetailBean.ListBean order, LuckyBagOrderDetailBean.GoodsInfoBean goodsInfo, LuckyBagOrderDetailBean.AddrBean people) {
        if ("0".equals(order.getPay_state())) {
            //未支付
            orderState.setText("等待卖家付款");
            if ("1".equals(order.getGq_status())) {
                //未过期
                orderTime.setVisibility(View.VISIBLE);
                cvCountdownView.setVisibility(View.VISIBLE);
                int iitem = Integer.parseInt(TimeUtil.getTime()) - Integer.parseInt(order.getPay_time());
                int titem = Integer.parseInt(gqTime) - iitem;
                cvCountdownView.start((titem * 1000));
                cvCountdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        orderTime.setText("商品已过期");
                        cvCountdownView.setVisibility(View.GONE);
                    }
                });

                pendingPayment.setVisibility(View.VISIBLE);

            } else if ("0".equals(order.getGq_status())) {
                //过期
                orderTime.setVisibility(View.VISIBLE);
                orderTime.setText("商品已过期");

                customerDelLr.setVisibility(View.VISIBLE);
            }
        } else {
            //已支付
            if ("0".equals(order.getState())) {
                //待发货
                orderState.setText("待发货");


                pendingDelivery.setVisibility(View.VISIBLE);
            } else if ("1".equals(order.getState())) {
                //待收货
                orderState.setText("待收货");

                goodsReceipt.setVisibility(View.VISIBLE);
            } else if ("2".equals(order.getState())) {
                //待评价
                orderState.setText("待评价");

                toBeEvaluated.setVisibility(View.VISIBLE);
            } else if ("3".equals(order.getState())) {
                //售后
                orderState.setText("售后");
            } else if ("4".equals(order.getState())){
                //已评价
                orderState.setText("已评价");
            }
        }

        tvConsignee.setText(people.getRec_name());
        phone.setText(people.getMobile());
        detailedAddress.setText(people.getAddress());

        GlideUtil.display(LuckyBagOrderDetailActivity.this, Constants.IMG_HOST + goodsInfo.getCover(), goodsPic);
        goodsName.setText(goodsInfo.getTitle());
        goodsType.setText(goodsInfo.getAttr());
        newPrice.setText(goodsInfo.getNow_price());
        oldPrice.setText("￥" + goodsInfo.getBefore_price());
        oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        goodsNum.setText("x" + goodsInfo.getNum());

        realPrice.setText("￥"+goodsInfo.getOrder_amount());
        orderNumber.setText("订单编号：" + goodsInfo.getId());
        orderEstablishTime.setText("创建时间：" + DateUtil.toDate(order.getPay_time(), DateUtil.FORMAT_YMDHMS));

    }


    @OnClick({R.id.tv_payment, R.id.tv_cancel, R.id.tv_contact_seller, R.id.tv_deliver, R.id.tv_confirm,
            R.id.tv_look_logistics, R.id.tv_evaluate, R.id.tv_look_logistics_collect, R.id.customer_del,
            R.id.goods_address,R.id.contact})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_payment:// 付款
                showPayDialog();
                break;
            case R.id.tv_cancel:// 取消订单
                storeValueDialog();
                break;
            case R.id.tv_contact_seller:// 联系卖家
                WebActivity.startActivity(this, "客服热线", com.ruirong.chefang.common.Constants.SEARVICE);
                break;
            case R.id.tv_deliver:// 提醒发货
                remind(new PreferencesHelper(LuckyBagOrderDetailActivity.this).getToken(),detail.getList().getNumber_bh(),"2");
                break;
            case R.id.tv_confirm:// 确认收货
                showPop();
                break;
            case R.id.tv_look_logistics://查看物流
                Intent intent = new Intent(LuckyBagOrderDetailActivity.this, ExpressDeliveryDetailsActivity.class);
                intent.putExtra("number_bh", detail.getList().getNumber_bh());
                startActivity(intent);
                break;
            case R.id.tv_evaluate://评价
                Intent intent1 = new Intent(mContext, ReserveOrderCommentActivity.class);
                intent1.putExtra("order_id", detail.getList().getNumber_bh());
                intent1.putExtra("type", 2);
                startActivity(intent1);
                break;
            case R.id.tv_look_logistics_collect://评价中 查看物流
                Intent intent2 = new Intent(LuckyBagOrderDetailActivity.this, ExpressDeliveryDetailsActivity.class);
                intent2.putExtra("number_bh", detail.getList().getNumber_bh());
                startActivity(intent2);
                break;
            case R.id.customer_del://删除订单
                delLuckyBagOrder(new PreferencesHelper(mContext).getToken(), detail.getList().getNumber_bh(), "");
                break;
            case R.id.goods_address://地址
                Intent intent3 = new Intent(LuckyBagOrderDetailActivity.this, AddressActivity.class);
                startActivity(intent3);
                break;
            case R.id.contact:
                WebActivity.startActivity(this, "客服热线", com.ruirong.chefang.common.Constants.SEARVICE);
                break;
        }
    }

    /**
     * 支付弹窗
     */
    private void showPayDialog() {
        String userInfo = new PreferencesHelper(mContext).getUserInfo();
        Gson gson = new Gson();
        UserInforBean userInforBean = gson.fromJson(userInfo, UserInforBean.class);

        if (userInforBean.getIs_has_pay_pass() == 0) {
            //没有pay_pass
            final Intent intent = new Intent(mContext, SettingPaymentPasswordActivity.class);
            //ToastUtil.showToast(MyRoomTimeActivity.this, "没有设置key_pass");
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("提示");
            builder.setMessage("您还设置支付密码，前去设置");
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(intent);
                }
            });
            builder.create().show();

        } else {
            //弹出密码输入框
            showmima(PAYCODE);
        }
    }

    private void showmima(int type) {
        this.type = type;
        if (type == PAYCODE) {
            DialogUtil.showPayDialog(this, "请输入支付密码", detail.getGoods_info().getOrder_amount(), "", getSupportFragmentManager());
        } else if (type == CONFIG) {
            DialogUtil.showPayDialog(this, "请输入支付密码", "", "", getSupportFragmentManager());
        }
    }


    /**
     * 付款
     *
     * @param token
     * @param orderId
     * @param password
     */
    private void pay(String token, String orderId, String password) {
        HttpHelp.getInstance().create(RemoteApi.class).paychild(token, orderId, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(mContext, baseBean.message);
                            DialogUtil.dismissPayDialog();
                            finishAndUpdate();
                        }else if (baseBean.code==4){
                            ToolUtil.loseToLogin(LuckyBagOrderDetailActivity.this);
                        } else {
                            ToastUtil.showToast(mContext, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    /**
     * 确认收货弹窗
     */
    private void showPop() {
        qrDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.pop_confirm, null);
        TextView quxiao, queren;
        quxiao = (TextView) inflate.findViewById(R.id.quxiao);
        queren = (TextView) inflate.findViewById(R.id.queren);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrDialog.dismiss();
            }
        });
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrDialog.dismiss();
                showmima(CONFIG);
            }
        });
        qrDialog.setContentView(inflate);
        Window dialogwindow = qrDialog.getWindow();
        dialogwindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogwindow.setAttributes(lp);
        qrDialog.show();

    }

    /**
     * 确认收货
     *
     * @param token
     * @param orderId
     * @param password
     */
    private void config(String token, String orderId, String password) {
        HttpHelp.getInstance().create(RemoteApi.class).LukyBagsreceipt(token, orderId, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(mContext, baseBean.message);
                            DialogUtil.dismissPayDialog();
                            finishAndUpdate();
                        }else if (baseBean.code==4){
                            ToolUtil.loseToLogin(LuckyBagOrderDetailActivity.this);
                        } else {
                            ToastUtil.showToast(mContext, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }


    /**
     * 取消原因
     */
    private void storeValueDialog() {
        mDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.cancel_reason, null);
        initDialogView(inflate);
        id_reason.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setFalse();
                reasonList.get(position).setTrue(true);
                reasonText = reasonList.get(position).getTitle();
                reasonAdapter.notifyDataSetChanged();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(reasonText)) {
                    ToastUtil.showToast(mContext, "请选择原因");
                } else {
                    mDialog.dismiss();
                    delLuckyBagOrder(new PreferencesHelper(mContext).getToken(), detail.getList().getNumber_bh(), reasonText);
                }
            }
        });

        mDialog.setContentView(inflate);
        Window dialogwindow = mDialog.getWindow();
        dialogwindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogwindow.setAttributes(lp);
        mDialog.show();
    }

    private void initDialogView(View inflate) {
        no = (TextView) inflate.findViewById(R.id.no);
        yes = (TextView) inflate.findViewById(R.id.yes);
        id_reason = (ListView) inflate.findViewById(R.id.id_reason);


        reasonList.clear();
        for (int i = 0; i < 6; i++) {
            SelectedStateBean selectedStateBean = new SelectedStateBean();
            selectedStateBean.setTrue(false);
            switch (i) {
                case 0:
                    selectedStateBean.setTitle("不想买了");
                    break;
                case 1:
                    selectedStateBean.setTitle("信息填写错误,重新拍");
                    break;
                case 2:
                    selectedStateBean.setTitle("卖家缺货");
                    break;
                case 3:
                    selectedStateBean.setTitle("同城见面交易");
                    break;
                case 4:
                    selectedStateBean.setTitle("卖家服务态度不好");
                    break;
                case 5:
                    selectedStateBean.setTitle("其他原因");
                    break;
            }
            reasonList.add(selectedStateBean);
        }

        reasonAdapter = new ReasonAdapter(id_reason);
        id_reason.setAdapter(reasonAdapter);
        reasonAdapter.setData(reasonList);


    }

    private void setFalse() {
        for (int i = 0; i < reasonList.size(); i++) {
            reasonList.get(i).setTrue(false);
        }
    }

    /**
     * 删除订单
     *
     * @param token
     * @param id
     * @param cancel
     */
    private void delLuckyBagOrder(String token, String id, String cancel) {
        HttpHelp.getInstance().create(RemoteApi.class).delLuckyBagOrder(token, id, cancel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(mContext, baseBean.message);
                            finishAndUpdate();
                        }else if (baseBean.code==4){
                            ToolUtil.loseToLogin(LuckyBagOrderDetailActivity.this);
                        } else {
                            ToastUtil.showToast(mContext, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    /**
     * 提醒发货
     * @param token
     * @param order_id
     * @param type
     */
    private void remind(String token,String order_id,String type){
        HttpHelp.getInstance().create(RemoteApi.class).remind(token, order_id, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(mContext, baseBean.message);
                        } else {
                            ToastUtil.showToast(mContext, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void commentCallback(LuckyOrderCommnetEvent event) {
        finishAndUpdate();
    }

    /**
     * 关闭activity 发送消息刷新上个页面数据
     */
    private void finishAndUpdate(){
        EventBusUtil.post(new UpdateBeforeUIDateEvent());
        finish();
    }


    @Override
    public void onInputFinish(String result) {
        if (type == PAYCODE) {
            pay(new PreferencesHelper(mContext).getToken(), detail.getList().getNumber_bh(), result);
        } else if (type == CONFIG) {
            config(new PreferencesHelper(mContext).getToken(), detail.getList().getNumber_bh(), result);
        }
    }
}
