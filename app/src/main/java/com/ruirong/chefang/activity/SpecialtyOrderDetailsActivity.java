package com.ruirong.chefang.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DialogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.TimeUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.qlzx.mylibrary.widget.payDialog.PayPwdView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.OrderCommodityAdapter;
import com.ruirong.chefang.adapter.ReasonAdapter;
import com.ruirong.chefang.bean.SelectedStateBean;
import com.ruirong.chefang.bean.SpecialtyOrderDetailBean;
import com.ruirong.chefang.event.PendingPaymentEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.widget.PasswordInputView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/26.
 * 特产订单详情页面
 */

public class SpecialtyOrderDetailsActivity extends BaseActivity implements PayPwdView.InputCallBack {

    @BindView(R.id.order_state)
    TextView orderState;
    @BindView(R.id.order_time)
    TextView orderTime;
    @BindView(R.id.cv_countdownView)
    CountdownView cvCountdownView;
    @BindView(R.id.tv_consignee)
    TextView tvConsignee;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.order_details_image_position)
    ImageView orderDetailsImagePosition;
    @BindView(R.id.detailed_address)
    TextView detailedAddress;
    @BindView(R.id.goods_address)
    RelativeLayout goodsAddress;
    @BindView(R.id.goods_list)
    NoScrollListView goodsList;
    @BindView(R.id.real_price)
    TextView realPrice;
    @BindView(R.id.contact)
    TextView contact;
    @BindView(R.id.order_number)
    TextView orderNumber;
    @BindView(R.id.order_establish_time)
    TextView orderEstablishTime;
    @BindView(R.id.order_all_tv_payment)
    TextView orderAllTvPayment;
    @BindView(R.id.order_all_tv_cancel_order)
    TextView orderAllTvCancelOrder;
    @BindView(R.id.tv_contact_seller)
    TextView tvContactSeller;
    @BindView(R.id.pending_payment)
    RelativeLayout pendingPayment;
    @BindView(R.id.order_all_tv_deliver)
    TextView orderAllTvDeliver;
    @BindView(R.id.pending_delivery)
    RelativeLayout pendingDelivery;
    @BindView(R.id.order_all_tv_confirm)
    TextView orderAllTvConfirm;
    @BindView(R.id.tv_look_logistics)
    TextView tvLookLogistics;
    @BindView(R.id.goods_receipt)
    RelativeLayout goodsReceipt;
    @BindView(R.id.order_all_tv_evaluate)
    TextView orderAllTvEvaluate;
    @BindView(R.id.tv_look_logistics_collect)
    TextView tvLookLogisticsCollect;
    @BindView(R.id.to_be_evaluated)
    RelativeLayout toBeEvaluated;
    @BindView(R.id.order_all_tv_details)
    TextView orderAllTvDetails;
    @BindView(R.id.customer_service)
    RelativeLayout customerService;
    @BindView(R.id.customer_del)
    TextView customerDel;
    @BindView(R.id.customer_del_lr)
    RelativeLayout customerDelLr;
    @BindView(R.id.customer_complete_collect)
    TextView customerCompleteCollect;
    @BindView(R.id.customer_complete_rl)
    RelativeLayout customerCompleteRl;
    private OrderCommodityAdapter orderCommodityAdapter;
    private String number_bh;
    private SpecialtyOrderDetailBean specialtyOrderDetailBean;

    private Dialog mDialog;
    private TextView no, yes;
    private ListView id_reason;
    private List<SelectedStateBean> reasonList = new ArrayList<>();
    private String reasonText;
    private ReasonAdapter reasonAdapter;
    private Dialog mDialogs;

    @Override
    public int getContentView() {
        return R.layout.activity_specialty_order_details;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("特产订单详情");

        number_bh = getIntent().getStringExtra("number_bh");
        if (number_bh == null) {
            finish();
        }
//        showLoadingDialog("加载中...");
        orderCommodityAdapter = new OrderCommodityAdapter(goodsList);
        goodsList.setAdapter(orderCommodityAdapter);
        orderCommodityAdapter.setNumber_bh(number_bh);
    }

    @Override
    public void getData() {
        specialtyOrderDetail(new PreferencesHelper(SpecialtyOrderDetailsActivity.this).getToken(), number_bh);
    }

    /**
     * 订单信息
     *
     * @param token
     * @param number_bh
     */
    private void specialtyOrderDetail(String token, String number_bh) {
        HttpHelp.getInstance().create(RemoteApi.class).specialtyOrderDetail(token, number_bh)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<SpecialtyOrderDetailBean>>(SpecialtyOrderDetailsActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<SpecialtyOrderDetailBean> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {
                            specialtyOrderDetailBean = baseBean.data;
                            if (specialtyOrderDetailBean != null) {

                                setVisibilityView();

                                tvConsignee.setText(specialtyOrderDetailBean.getAddr().getRec_name());
                                phone.setText(specialtyOrderDetailBean.getAddr().getMobile());
                                detailedAddress.setText(specialtyOrderDetailBean.getAddr().getAddress());


                                orderNumber.setText("订单编号:" + specialtyOrderDetailBean.getList().getNumber_bh());
                                orderEstablishTime.setText("创建时间:" + TimeUtil.timedate(specialtyOrderDetailBean.getList().getPay_time()));
                                realPrice.setText("￥" + baseBean.data.getList().getShi_money());

                                if ("1".equals(specialtyOrderDetailBean.getList().getPay_state())) {//已付款

                                    if ("1".equals(specialtyOrderDetailBean.getList().getState())) {//待收货
                                        orderState.setText("等待买家收货");
                                        goodsReceipt.setVisibility(View.VISIBLE);
                                    } else if ("2".equals(specialtyOrderDetailBean.getList().getState())) {//已收货
                                        orderState.setText("交易成功");
                                        toBeEvaluated.setVisibility(View.VISIBLE);
                                    } else if ("3".equals(specialtyOrderDetailBean.getList().getState())) {//售后
                                        orderState.setText("申请售后");
                                        customerService.setVisibility(View.GONE);
                                        orderCommodityAdapter.setIsT(false);
                                    } else if ("0".equals(specialtyOrderDetailBean.getList().getState())) {//未发货
                                        orderState.setText("等待卖家发货");
                                        pendingDelivery.setVisibility(View.VISIBLE);
                                    } else {
                                        orderState.setText("订单已完成");
                                        customerCompleteRl.setVisibility(View.VISIBLE);
                                    }
                                } else if ("0".equals(specialtyOrderDetailBean.getList().getPay_state())) {//待付款
                                    orderState.setText("等待买家付款");
                                    pendingPayment.setVisibility(View.VISIBLE);
                                    orderCommodityAdapter.setIsT(false);
                                    orderTime.setVisibility(View.GONE);
                                    cvCountdownView.setVisibility(View.GONE);

                                    if ("1".equals(specialtyOrderDetailBean.getList().getGq_status())) {//未过期

                                        int iitem = Integer.parseInt(TimeUtil.getTime()) - Integer.parseInt(specialtyOrderDetailBean.getList().getPay_time());
                                        int titem = Integer.parseInt(specialtyOrderDetailBean.getGqTime()) - iitem;


                                        orderTime.setVisibility(View.VISIBLE);
                                        cvCountdownView.setVisibility(View.VISIBLE);

                                        cvCountdownView.start((titem * 1000));

                                        cvCountdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                                            @Override
                                            public void onEnd(CountdownView cv) {
                                                setVisibilityView();
                                                customerDelLr.setVisibility(View.VISIBLE);
                                                orderTime.setText("商品已过期");
                                            }
                                        });

                                    } else {
                                        setVisibilityView();
                                        customerDelLr.setVisibility(View.VISIBLE);
                                        orderTime.setText("商品已过期");
                                    }


                                }

                                if (specialtyOrderDetailBean.getGoods_info().size() > 0 && specialtyOrderDetailBean.getGoods_info() != null) {
                                    orderCommodityAdapter.setData(specialtyOrderDetailBean.getGoods_info());
                                }
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(SpecialtyOrderDetailsActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.i("XXX", throwable.getLocalizedMessage());
                        hideLoadingDialog();
                    }
                });
    }


    @OnClick({R.id.contact, R.id.order_all_tv_payment, R.id.order_all_tv_cancel_order,
            R.id.tv_contact_seller, R.id.order_all_tv_deliver, R.id.order_all_tv_confirm, R.id.tv_look_logistics,
            R.id.order_all_tv_evaluate, R.id.tv_look_logistics_collect, R.id.order_all_tv_details, R.id.customer_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.contact://联系卖家
//                ToastUtil.showToast(SpecialtyOrderDetailsActivity.this, "联系卖家");
                WebActivity.startActivity(SpecialtyOrderDetailsActivity.this, "客服热线", com.ruirong.chefang.common.Constants.SEARVICE);
                break;
            case R.id.order_all_tv_payment://待付款 付款
//                ToastUtil.showToast(SpecialtyOrderDetailsActivity.this, "待付款 付款");
                Intent intentf = new Intent(SpecialtyOrderDetailsActivity.this, ImmediatelyPaymentActivity.class);
                intentf.putExtra("mode_type", "1");
                intentf.putExtra("place_type", "4");
                intentf.putExtra("shop_price", specialtyOrderDetailBean.getList().getShi_money() + "");
                intentf.putExtra("order_sn", number_bh);
                startActivity(intentf);
                break;
            case R.id.customer_del://待付款 过期
                ToastUtil.showToast(SpecialtyOrderDetailsActivity.this, "待付款 过期");
                delspecialty(new PreferencesHelper(SpecialtyOrderDetailsActivity.this).getToken(), specialtyOrderDetailBean.getList().getNumber_bh(), number_bh);

                break;
            case R.id.order_all_tv_cancel_order://待付款 取消订单
                storeValueDialog();
//                ToastUtil.showToast(SpecialtyOrderDetailsActivity.this, "待付款 取消订单");
                break;
            case R.id.tv_contact_seller://待付款 联系卖家
//                ToastUtil.showToast(SpecialtyOrderDetailsActivity.this, "待付款 联系卖家");
                WebActivity.startActivity(SpecialtyOrderDetailsActivity.this, "客服热线", com.ruirong.chefang.common.Constants.SEARVICE);
                break;
            case R.id.order_all_tv_deliver://待发货 提醒发货
                remind(new PreferencesHelper(SpecialtyOrderDetailsActivity.this).getToken(), number_bh, "1");
                break;
            case R.id.order_all_tv_confirm://待收货 确认收货
                showPop();
                break;
            case R.id.tv_look_logistics://待收货 联系卖家
//                ToastUtil.showToast(SpecialtyOrderDetailsActivity.this, "待收货 联系卖家");
                WebActivity.startActivity(SpecialtyOrderDetailsActivity.this, "客服热线", com.ruirong.chefang.common.Constants.SEARVICE);
                break;
            case R.id.order_all_tv_evaluate://待评价 评价
                Intent intent = new Intent(SpecialtyOrderDetailsActivity.this, SpecialProductReview.class);
                intent.putExtra("order_id", number_bh);
                if (TextUtils.isEmpty(new PreferencesHelper(this).getToken())) {
                    ToolUtil.goToLogin(this);
                } else {
                    startActivity(intent);
                }
                break;
            case R.id.tv_look_logistics_collect://已完成 查看物流
//                ToastUtil.showToast(SpecialtyOrderDetailsActivity.this, "已完成 查看物流");
                Intent intentw = new Intent(SpecialtyOrderDetailsActivity.this, ExpressDeliveryDetailsActivity.class);
                intentw.putExtra("number_bh", number_bh);
                startActivity(intentw);
                break;
            case R.id.order_all_tv_details://售后 查看详情
                ToastUtil.showToast(SpecialtyOrderDetailsActivity.this, "查看详情");
                break;
        }
    }

    private void setVisibilityView() {
        pendingPayment.setVisibility(View.GONE);
        pendingDelivery.setVisibility(View.GONE);
        goodsReceipt.setVisibility(View.GONE);
        toBeEvaluated.setVisibility(View.GONE);
        customerService.setVisibility(View.GONE);
        customerDelLr.setVisibility(View.GONE);
        orderTime.setVisibility(View.GONE);
        cvCountdownView.setVisibility(View.GONE);
    }


    /**
     * 取消的弹框
     */
    private void storeValueDialog() {
        mDialog = new Dialog(SpecialtyOrderDetailsActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(SpecialtyOrderDetailsActivity.this).inflate(R.layout.cancel_reason, null);

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
                    ToastUtil.showToast(SpecialtyOrderDetailsActivity.this, "请选择原因");
                } else {
                    delspecialty(new PreferencesHelper(SpecialtyOrderDetailsActivity.this).getToken(), specialtyOrderDetailBean.getList().getNumber_bh(), reasonText);
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
     * 取消订单
     *
     * @param token
     * @param id
     * @param cancel
     */
    private void delspecialty(String token, String id, String cancel) {
        HttpHelp.getInstance().create(RemoteApi.class).delspecialty(token, id, cancel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(SpecialtyOrderDetailsActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            EventBus.getDefault().post(new PendingPaymentEvent());
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(SpecialtyOrderDetailsActivity.this);
                        }
                        ToastUtil.showToast(SpecialtyOrderDetailsActivity.this, baseBean.message);
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });


    }


    /****************确认订单****************/
    private void showPop() {
//
//        View mContentView = null;
//        if(mContentView == null){
//            mContentView = LayoutInflater.from(mContext).inflate(R.layout.pop_confirm, null);
//        }
//
//        if(mPopUpWindow == null){
//            mPopUpWindow = new PopupWindow(mContentView, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//            mPopUpWindow.setBackgroundDrawable(new BitmapDrawable());
//            mPopUpWindow.setOutsideTouchable(true);
//            mPopUpWindow.setFocusable(true);
//        }
//        mPopUpWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);


        mDialog = new Dialog(SpecialtyOrderDetailsActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(SpecialtyOrderDetailsActivity.this).inflate(R.layout.pop_confirm, null);

        TextView quxiao, queren;
        quxiao = (TextView) inflate.findViewById(R.id.quxiao);
        queren = (TextView) inflate.findViewById(R.id.queren);

        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
//                showMima();
                showPayDialog();
            }
        });


        mDialog.setContentView(inflate);
        Window dialogwindow = mDialog.getWindow();

        dialogwindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
//        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        dialogwindow.setAttributes(lp);
        mDialog.show();

    }

    private void goodsreceipt(String token, String id, String password) {
        HttpHelp.getInstance().create(RemoteApi.class).goodsreceipt(token, id, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(SpecialtyOrderDetailsActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            EventBus.getDefault().post(new PendingPaymentEvent());
                            ToastUtil.showToast(SpecialtyOrderDetailsActivity.this, baseBean.message);
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(SpecialtyOrderDetailsActivity.this);
                        }
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }


    /**
     * 提醒发货
     *
     * @param token
     * @param order_id
     * @param type
     */
    private void remind(String token, String order_id, String type) {
        HttpHelp.getInstance().create(RemoteApi.class).remind(token, order_id, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(SpecialtyOrderDetailsActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(SpecialtyOrderDetailsActivity.this, baseBean.message);
                        } else {
                            ToastUtil.showToast(SpecialtyOrderDetailsActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    private void showPayDialog() {
        //弹出支付密码输入框
        DialogUtil.showPayDialog(this, "请输入支付密码", "", "", getSupportFragmentManager());
    }

    @Override
    public void onInputFinish(String result) {
        goodsreceipt(new PreferencesHelper(SpecialtyOrderDetailsActivity.this).getToken(), number_bh, result);
        DialogUtil.dismissPayDialog();
    }
}
