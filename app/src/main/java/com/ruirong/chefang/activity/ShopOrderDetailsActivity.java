package com.ruirong.chefang.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.qlzx.mylibrary.util.TimeUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.qlzx.mylibrary.widget.payDialog.PayPwdView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.ReasonAdapter;
import com.ruirong.chefang.adapter.ShopOrderCommodityAdapter;
import com.ruirong.chefang.bean.SelectedStateBean;
import com.ruirong.chefang.bean.ShopOrderDetailBean;
import com.ruirong.chefang.event.LoginSuccessEvent;
import com.ruirong.chefang.event.ShopGoodsReceiptEvent;
import com.ruirong.chefang.event.ShopOrderCommentEvent;
import com.ruirong.chefang.event.ShopOrderRefundEvent;
import com.ruirong.chefang.event.UpdateBeforeUIDateEvent;
import com.ruirong.chefang.event.UpdateShopOrderEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.personalcenter.ShopOrderCommentActivity;
import com.ruirong.chefang.personalcenter.ShopOrderRefundActivity;
import com.ruirong.chefang.util.ToolUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.iwgang.countdownview.CountdownView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 16690 on 2018/3/29.
 * describe: 商城订单详情
 */

public class ShopOrderDetailsActivity extends BaseActivity implements PayPwdView.InputCallBack {


    @BindView(R.id.order_state)
    TextView orderState;
    @BindView(R.id.order_time) //支付剩余时间
            TextView orderTime;
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
    RelativeLayout pendingPayment;  //待付款   （联系卖家  取消订单  付款）
    @BindView(R.id.order_all_tv_deliver)
    TextView orderAllTvDeliver;
    @BindView(R.id.service)
    RelativeLayout service; // 售后
    @BindView(R.id.order_all_tv_confirm)
    TextView orderAllTvConfirm;
    @BindView(R.id.goods_receipt)
    RelativeLayout goodsReceipt; // 确认收货 （提醒收货 确认收货）
    @BindView(R.id.order_all_tv_evaluate)
    TextView orderAllTvEvaluate;
    @BindView(R.id.to_be_evaluated)
    RelativeLayout toBeEvaluated; //待评价 （ 评价）
    @BindView(R.id.cv_countdownView)
    CountdownView cvCountdownView; // 倒计时
    @BindView(R.id.customer_del)
    TextView customerDel;
    @BindView(R.id.customer_del_lr)
    RelativeLayout customerDelLr; //（删除订单）
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.order_refund)
    TextView orderRefund;
    @BindView(R.id.ll_code_pic) //核销码
            LinearLayout llCodePic;
    @BindView(R.id.iv_code)
    ImageView ivCode;

    private String number_bh;
    private String gqTime;
    private ShopOrderDetailBean detail;

    private ShopOrderCommodityAdapter adapter;
    private PreferencesHelper helper;

    private Dialog mDialog;
    private TextView no, yes;
    private ListView id_reason;
    private List<SelectedStateBean> reasonList = new ArrayList<>();
    private String reasonText;
    private ReasonAdapter reasonAdapter;

    private Dialog querenDialog;//确认收货

    @Override
    public int getContentView() {
        return R.layout.activity_shop_order_details;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("商城订单详情");

        EventBusUtil.register(this);

        helper = new PreferencesHelper(this);
        number_bh = getIntent().getStringExtra("number_bh");
        if (number_bh == null) {
            finish();
        }
        adapter = new ShopOrderCommodityAdapter(goodsList);
        goodsList.setAdapter(adapter);

    }

    @Override
    public void getData() {
//        showLoadingDialog("加载中...");
        getShopDetail(helper.getToken(), number_bh);
    }

    public void getShopDetail(String token, String number_bh) {
        HttpHelp.getInstance().create(RemoteApi.class).getShopOrderDetail(token, number_bh)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ShopOrderDetailBean>>(ShopOrderDetailsActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<ShopOrderDetailBean> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {
                            detail = baseBean.data;
                            gqTime = detail.getGqTime();
                            updateUI(detail.getList(), detail.getGoods_info());
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(ShopOrderDetailsActivity.this);
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                        ToastUtil.showToast(ShopOrderDetailsActivity.this, "获取数据错误");
                    }
                });
    }

    private void updateUI(final ShopOrderDetailBean.ListBean order, List<ShopOrderDetailBean.GoodsInfoBean> goodsList) {
        setVisibilityView();
        if ("3".equals(order.getIs_since())) {
            //显示地址
            goodsAddress.setVisibility(View.VISIBLE);
        } else {

            if ("2".equals(order.getGoods_type())) {
                //显示核销码
                llCodePic.setVisibility(View.VISIBLE);
                orderAllTvConfirm.setVisibility(View.GONE);
                final String imgUrl = Constants.IMG_HOST + order.getCode_path() ;
                GlideUtil.display(ShopOrderDetailsActivity.this, Constants.IMG_HOST + order.getCode_path(), ivCode);
                ivCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(BGAPhotoPreviewActivity.newIntent(ShopOrderDetailsActivity.this, null, imgUrl));
                    }
                });
            } else if ("1".equals(order.getGoods_type())) {
                if ("1".equals(order.getIs_hx())){
                    //显示核销码
                    llCodePic.setVisibility(View.VISIBLE);
                    orderAllTvConfirm.setVisibility(View.GONE);
                    final String imgUrl = Constants.IMG_HOST + order.getCode_path() ;
                    GlideUtil.display(ShopOrderDetailsActivity.this, Constants.IMG_HOST + order.getCode_path(), ivCode);
                    ivCode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(BGAPhotoPreviewActivity.newIntent(ShopOrderDetailsActivity.this, null, imgUrl));
                        }
                    });
                }
            }
        }

        tvShopName.setText(order.getShopname());

        tvConsignee.setText("收货人：" + order.getName());
        phone.setText(order.getMobile());
        detailedAddress.setText(order.getAddress());
        orderNumber.setText("订单编号:" + order.getOrder_id());
        orderEstablishTime.setText("创建时间:" + TimeUtil.timedate(order.getCreate_time()));
        realPrice.setText("￥" + order.getTotal_money());
        adapter.setData(goodsList);

        if ("1".equals(order.getStatus())) {//待付款
            orderState.setText("等待买家付款");
            orderRefund.setVisibility(View.INVISIBLE);
            if ("1".equals(order.getGq_status())) {//未过期
                orderTime.setVisibility(View.VISIBLE);
                pendingPayment.setVisibility(View.VISIBLE);
                cvCountdownView.setVisibility(View.VISIBLE);

                int iitem = Integer.parseInt(TimeUtil.getTime()) - Integer.parseInt(order.getCreate_time());
                int titem = Integer.parseInt(gqTime) - iitem;
                cvCountdownView.start((titem * 1000));
                cvCountdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        orderTime.setText("商品已过期");
                        pendingPayment.setVisibility(View.GONE);
                        customerDelLr.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                orderTime.setText("商品已过期");
                customerDelLr.setVisibility(View.VISIBLE);
            }

        } else if ("2".equals(order.getStatus())) {
            //已付款

            if ("1".equals(order.getGoods_type())) {
                //商品

                if ("0".equals(order.getCancel())) {
                    //未售后

                    if ("2".equals(order.getIs_hx())) {

                        if ("5".equals(order.getShop_status())) {//已确认订单
                            if ("0".equals(order.getComment_state())) {
                                orderState.setText("待评论");
                                toBeEvaluated.setVisibility(View.VISIBLE);
                                orderRefund.setVisibility(View.INVISIBLE);
                            } else {
                                orderState.setText("已评论");
                                orderRefund.setVisibility(View.INVISIBLE);

                            }
                        } else if ("0".equals(order.getShop_status())) {//待接单
                            orderState.setText("待接单");
                            orderRefund.setVisibility(View.VISIBLE);
                        } else if ("1".equals(order.getShop_status())) {//待发货
                            orderState.setText("待发货");
                            orderRefund.setVisibility(View.VISIBLE);
                        } else if ("2".equals(order.getShop_status())) {//配送中
                            orderState.setText("配送中");
                            orderRefund.setVisibility(View.VISIBLE);
                            goodsReceipt.setVisibility(View.VISIBLE);
                        } else if ("3".equals(order.getShop_status())) {// 已送达
                            orderState.setText("已送达");
                            orderRefund.setVisibility(View.VISIBLE);
                            goodsReceipt.setVisibility(View.VISIBLE);
                        } else {
                            orderState.setText("已取消");
                            orderRefund.setVisibility(View.INVISIBLE);
                        }
                    }else {
                        if ("5".equals(order.getShop_status())) {//已确认订单
                            if ("0".equals(order.getComment_state())) {
                                orderState.setText("待评论");
                                toBeEvaluated.setVisibility(View.VISIBLE);
                                orderRefund.setVisibility(View.INVISIBLE);
                            } else {
                                orderState.setText("已评论");
                                orderRefund.setVisibility(View.INVISIBLE);

                            }
                        } else if ("0".equals(order.getShop_status())) {//待接单
                            orderState.setText("待接单");
                            orderRefund.setVisibility(View.VISIBLE);
                        }else {
                           if ("1".equals(order.getIs_hexiao())){
                               orderState.setText("已核销");
                           }else {
                               orderState.setText("待核销");
                           }
                        }
                    }


                } else {
                    //售后
                    service.setVisibility(View.GONE);
                    orderRefund.setVisibility(View.INVISIBLE);
                    if ("1".equals(order.getCancel())) {
                        orderState.setText("退款处理中");
                    } else if ("2".equals(order.getCancel())) {
                        orderState.setText("同意退款");
                    } else if ("3".equals(order.getCancel())) {
                        orderState.setText("拒绝退款");

                    }
                }

            } else {
                //套餐
                if ("0".equals(order.getCancel())) {
                    //未售后

                    if ("5".equals(order.getShop_status())) {//已确认订单
                        if ("0".equals(order.getComment_state())) {
                            orderState.setText("待评论");
                            toBeEvaluated.setVisibility(View.VISIBLE);
                            orderRefund.setVisibility(View.INVISIBLE);
                        } else {
                            orderState.setText("已评论");
                            orderRefund.setVisibility(View.INVISIBLE);

                        }
                    } else if ("0".equals(order.getShop_status()) ||
                            "1".equals(order.getShop_status()) ||
                            "2".equals(order.getShop_status()) ||
                            "3".equals(order.getShop_status())) {
                        //使用中
                        orderState.setText("使用中");
                        orderRefund.setVisibility(View.VISIBLE);
                        goodsReceipt.setVisibility(View.VISIBLE);
                    } else {
                        orderState.setText("已取消");
                        orderRefund.setVisibility(View.INVISIBLE);
                    }

                } else {
                    //售后
                    service.setVisibility(View.GONE);
                    orderRefund.setVisibility(View.INVISIBLE);
                    if ("1".equals(order.getCancel())) {
                        orderState.setText("退款处理中");
                    } else if ("2".equals(order.getCancel())) {
                        orderState.setText("同意退款");
                    } else if ("3".equals(order.getCancel())) {
                        orderState.setText("拒绝退款");

                    }
                }

            }


        }
    }


    @OnClick({R.id.contact, R.id.order_all_tv_payment, R.id.order_all_tv_cancel_order,
            R.id.tv_contact_seller, R.id.order_all_tv_confirm, R.id.goods_address,
            R.id.order_all_tv_evaluate, R.id.customer_del, R.id.order_refund})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.contact://联系卖家
                WebActivity.startActivity(this, "客服热线", com.ruirong.chefang.common.Constants.SEARVICE);
                break;
            case R.id.order_all_tv_payment://付款
                Intent intent2 = new Intent(ShopOrderDetailsActivity.this, ImmediatelyPaymentActivity.class);
                intent2.putExtra("mode_type", "1");
                intent2.putExtra("place_type", detail.getList().getGoods_type());
                intent2.putExtra("shop_price", detail.getList().getTotal_money());
                intent2.putExtra("order_sn", detail.getList().getOrder_id());
                startActivity(intent2);
                break;
            case R.id.customer_del://删除
                delShopOrder(helper.getToken(), detail.getList().getOrder_id(), "");
                break;
            case R.id.order_all_tv_cancel_order://待付款 取消订单
                storeValueDialog();
                break;
            case R.id.tv_contact_seller://待付款 联系卖家
                WebActivity.startActivity(this, "客服热线", com.ruirong.chefang.common.Constants.SEARVICE);
                break;
            case R.id.order_all_tv_confirm://待收货 确认收货
                showPop();
                break;
            case R.id.order_all_tv_evaluate://待评价 评价
                Intent intent = new Intent(ShopOrderDetailsActivity.this, ShopOrderCommentActivity.class);
                intent.putExtra("order_id", detail.getList().getOrder_id());
                intent.putExtra("shop_id", detail.getGoods_info().get(0).getShop_id());
                startActivity(intent);
                break;
            case R.id.order_refund://退款
                Intent intent1 = new Intent(ShopOrderDetailsActivity.this, ShopOrderRefundActivity.class);
                Gson gson = new Gson();
                String s = gson.toJson(detail.getGoods_info());
                intent1.putExtra("order_id", detail.getList().getOrder_id());
                intent1.putExtra("goods", s);
                intent1.putExtra("all_price", detail.getList().getTotal_money());
                startActivity(intent1);
                break;
            case R.id.goods_address://我的地址
                Intent intent3 = new Intent(ShopOrderDetailsActivity.this, AddressActivity.class);
                startActivity(intent3);
                break;
        }
    }


    private void setVisibilityView() {
        pendingPayment.setVisibility(View.GONE);
        service.setVisibility(View.GONE);
        goodsReceipt.setVisibility(View.GONE);
        toBeEvaluated.setVisibility(View.GONE);
        customerDelLr.setVisibility(View.GONE);
        orderTime.setVisibility(View.GONE);
        cvCountdownView.setVisibility(View.GONE);
    }

    /**
     * 取消原因
     */
    private void storeValueDialog() {
        mDialog = new Dialog(ShopOrderDetailsActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(ShopOrderDetailsActivity.this).inflate(R.layout.cancel_reason, null);
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
                    ToastUtil.showToast(ShopOrderDetailsActivity.this, "请选择原因");
                } else {
                    mDialog.dismiss();
                    delShopOrder(new PreferencesHelper(ShopOrderDetailsActivity.this).getToken(), detail.getList().getOrder_id(), reasonText);
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
        for (int i = 0; i < 3; i++) {
            SelectedStateBean selectedStateBean = new SelectedStateBean();
            selectedStateBean.setTrue(false);
            switch (i) {
                case 0:
                    selectedStateBean.setTitle("不想要了");
                    break;
                case 1:
                    selectedStateBean.setTitle("信息填写错误,重新拍");
                    break;
                case 2:
                    selectedStateBean.setTitle("其他原因");
                    break;
            }
            reasonList.add(selectedStateBean);
        }

        reasonAdapter = new ReasonAdapter(id_reason);
        id_reason.setAdapter(reasonAdapter);
        reasonAdapter.setData(reasonList);


    }


    /**
     * 删除商城订单
     *
     * @param token
     * @param id
     * @param cancel
     */
    private void delShopOrder(String token, String id, String cancel) {
        HttpHelp.getInstance().create(RemoteApi.class).delShopOrder(token, id, cancel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(ShopOrderDetailsActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(ShopOrderDetailsActivity.this, "删除成功");
                            finishAndUpdate();
                            EventBusUtil.post(new UpdateShopOrderEvent());
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(ShopOrderDetailsActivity.this);
                        } else {
                            ToastUtil.showToast(ShopOrderDetailsActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });


    }

    private void setFalse() {
        for (int i = 0; i < reasonList.size(); i++) {
            reasonList.get(i).setTrue(false);
        }
    }


    private void showPop() {
        querenDialog = new Dialog(ShopOrderDetailsActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(ShopOrderDetailsActivity.this).inflate(R.layout.pop_shop_confirm, null);
        TextView quxiao, queren;
        quxiao = (TextView) inflate.findViewById(R.id.quxiao);
        queren = (TextView) inflate.findViewById(R.id.queren);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                querenDialog.dismiss();
            }
        });
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                querenDialog.dismiss();
                showPayDialog();
            }
        });
        querenDialog.setContentView(inflate);
        Window dialogwindow = querenDialog.getWindow();
        dialogwindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogwindow.setAttributes(lp);
        querenDialog.show();

    }


    private void showPayDialog() {
        //弹出支付密码输入框
        DialogUtil.showPayDialog(this, "请输入支付密码", "", "", getSupportFragmentManager());
    }

    @Override
    public void onInputFinish(String result) {
        goodsreceipt(new PreferencesHelper(ShopOrderDetailsActivity.this).getToken(), detail.getList().getOrder_id(), result);
        DialogUtil.dismissPayDialog();
    }


    private void goodsreceipt(String token, String id, String password) {
        HttpHelp.getInstance().create(RemoteApi.class).shopGoodsreceipt(token, id, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(ShopOrderDetailsActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            EventBusUtil.post(new ShopGoodsReceiptEvent());
                            finishAndUpdate();
                            ToastUtil.showToast(ShopOrderDetailsActivity.this, baseBean.message);
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(ShopOrderDetailsActivity.this);
                        } else {
                            ToastUtil.showToast(ShopOrderDetailsActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }


    /**
     * 评论事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateEvent(ShopOrderCommentEvent event) {
        finishAndUpdate();
    }

    /**
     * 退款事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateEvent(ShopOrderRefundEvent event) {
        finishAndUpdate();
    }

    /**
     * 付款事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateEvent(LoginSuccessEvent event) {
        finishAndUpdate();
    }


    /**
     * 关闭activity 发送消息刷新上个页面数据
     */
    private void finishAndUpdate() {
        EventBusUtil.post(new UpdateBeforeUIDateEvent());
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }
}
