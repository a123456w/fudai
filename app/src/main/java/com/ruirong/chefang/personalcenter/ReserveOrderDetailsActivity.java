package com.ruirong.chefang.personalcenter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.TimeUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.ImmediatelyPaymentActivity;
import com.ruirong.chefang.bean.ReserveOrderDetailsBean;
import com.ruirong.chefang.event.ReservationOrderEvent;
import com.ruirong.chefang.event.UpdateBeforeUIDateEvent;
import com.ruirong.chefang.event.UpdateInformationEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.widget.FlowLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2017/12/27 0027
 * describe:  预约订单  订单详情
 */
public class ReserveOrderDetailsActivity
        extends BaseActivity {


    //    @BindView(R.id.iv1)
//    ImageView iv1;
//    @BindView(R.id.iv2)
//    ImageView iv2;
//    @BindView(R.id.iv3)
//    ImageView iv3;
//    @BindView(R.id.iv4)
//    ImageView iv4;
//    @BindView(R.id.view)
//    View view;
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_go_pay)
    TextView tvGoPay;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sp_name)
    TextView tvSpName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_ruzhu_time)
    TextView tvRuzhuTime;
    @BindView(R.id.tv_li_time)
    TextView tvLiTime;
    @BindView(R.id.tv_book_num)
    TextView tvBookNum;
    @BindView(R.id.tv_check_name)
    TextView tvCheckName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_dao_time)
    TextView tvDaoTime;
    @BindView(R.id.tv_ruzhu_direct)
    TextView tvRuzhuDirect;
    @BindView(R.id.tv_order_id)
    TextView tvOrderId;
    @BindView(R.id.tv_creat_time)
    TextView tvCreatTime;
    @BindView(R.id.fl_hotel_configuration)
    FlowLayout flHotelConfiguration;
    @BindView(R.id.ll_hotel_details)
    LinearLayout llHotelDetails;
    @BindView(R.id.rl_button)
    RelativeLayout rlButton;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.ll_specif)
    LinearLayout llSpecif;
    @BindView(R.id.tv_specif)
    TextView tvSpecif;
    @BindView(R.id.ll_hotel)
    RelativeLayout llHotel;
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;
    //    private MyAlbumAdapter adapter;
    private PreferencesHelper helper;
    private ReserveOrderDetailsBean detail;
    private String orderId;


    @Override
    public int getContentView() {
        return R.layout.activity_reserve_order_details;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
//        titleBar.setTitleText("预订订单");
        EventBusUtil.register(this);
//        View inflate = ReserveOrderDetailsActivity.this.getLayoutInflater().inflate(R.layout.reserve_order_details_room_layout, null);
//        llHotelDetails.addView(inflate, 2);

        helper = new PreferencesHelper(this);
        // TODO: 2018/1/10 0010  4中状态，用了图片去替换了。 
        orderId = getIntent().getStringExtra("order_id");

//        流式布局。根据后台，动态添加控件
//        View inflate1 = ReserveOrderDetailsActivity.this.getLayoutInflater().inflate(R.layout.hotel_configuration_item_layout, null);
//        ViewGroup parent = (ViewGroup) inflate1.getParent();
//        if (parent != null) {
//            parent.removeAllViews();
//
//        }
//        for (int i = 0; i < 4; i++) {
//            flHotelConfiguration.addView(inflate1);
//        }
    }

    @Override
    public void getData() {
        getOrderDetailsData(helper.getToken(), orderId);
    }

    public void getOrderDetailsData(String token, String number_bh) {
        showLoadingDialog("加载中...");
        HttpHelp.getInstance().create(RemoteApi.class).getReserveDetails(token, number_bh)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ReserveOrderDetailsBean>>(ReserveOrderDetailsActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<ReserveOrderDetailsBean> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {
                            detail = baseBean.data;
                            updataUI(detail);
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(ReserveOrderDetailsActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                    }
                });

//        for (int i = 0; i < 12; i++) {
//            MyOrderListviewBean myOrderListviewBean = new MyOrderListviewBean();
//            myOrderListviewBean.setTitle("农业商圈发展电商");
//            baseList.add(myOrderListviewBean);
//        }
//        adapter = new MyAlbumAdapter(baseList);
//
//        //调用方法,传入一个接口回调
//        adapter.setOnItemClickListener(new MyAlbumAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view) {
//            Intent intent = new Intent(MyPhoneActivity.this, MyFriendCricleDetailsActivity.class);
//            intent.putExtra("string_data", "hello");
//            intent.putExtra("int_data", 100);
//            startActivity(intent);
//            }
//        });
//        canContentView.setAdapter(adapter);


    }


    /**
     * 更新界面
     *
     * @param bean
     */
    public void updataUI(ReserveOrderDetailsBean bean) {
        String qrCode = bean.getCode_path();
        if (!android.text.TextUtils.isEmpty(qrCode)){
            GlideUtil.display(ReserveOrderDetailsActivity.this, com.qlzx.mylibrary.common.Constants.IMG_HOST+qrCode,ivQrcode);
        }
        if ("1".equals(bean.getPay_status())) {
            //TODO 初始化未支付布局
            ivPic.setVisibility(View.GONE);
            rlButton.setVisibility(View.VISIBLE);
            if ("1".equals(bean.getGq_status())) {
                //未过期
                tvStatus.setText("未支付");
                tvHint.setText("订单未支付，请在24小时内支付该订单");
                tvGoPay.setText("去支付");
            } else {
                //已过期
                tvStatus.setText("已过期");
                tvHint.setVisibility(View.INVISIBLE);
                tvGoPay.setText("删除订单");
            }
        } else {
            if ("1".equals(detail.getCancel())) {
                //退款申请
                tvStatus.setText("退款处理中");
                tvHint.setVisibility(View.INVISIBLE);
                ivPic.setImageResource(R.drawable.daishiyong);
                ivPic.setVisibility(View.VISIBLE);
                rlButton.setVisibility(View.GONE);
            } else if ("2".equals(detail.getCancel())) {
                //同意退款
                tvStatus.setText("已同意退款");
                tvHint.setVisibility(View.INVISIBLE);
                ivPic.setImageResource(R.drawable.daishiyong);
                ivPic.setVisibility(View.VISIBLE);
                rlButton.setVisibility(View.GONE);
            } else if ("3".equals(detail.getCancel())) {
                //拒绝退款
                tvStatus.setText("已拒绝退款");
                tvHint.setVisibility(View.INVISIBLE);
                ivPic.setImageResource(R.drawable.daishiyong);
                ivPic.setVisibility(View.VISIBLE);
                rlButton.setVisibility(View.GONE);
            } else {
                switch (detail.getStatus()) {
                    case "1":
                        //等待使用
                        tvStatus.setText("待入住");
                        tvHint.setText("已支付订单，请在使用时间范围内使用该订单");
                        tvGoPay.setText("去使用");
                        ivPic.setVisibility(View.VISIBLE);
                        rlButton.setVisibility(View.GONE);
                        break;
                    case "2":
                        //正在使用
                        tvStatus.setText("已入住");
                        tvHint.setText("已支付订单，请在使用时间范围内使用该订单");
                        ivPic.setImageResource(R.drawable.shiyongzhong);
                        ivPic.setVisibility(View.VISIBLE);
                        rlButton.setVisibility(View.GONE);
                        break;
                    case "3":
//                        if ("0".equals(detail.getComment_state())){
//                            //等待评价
//
//                        }else {
//                            //已评价
//
//                        }
                        tvStatus.setText("已完成");
                        tvHint.setText("当前订单已完成，谢谢您的使用");
                        ivPic.setImageResource(R.drawable.image_yiwancheng);
                        ivPic.setVisibility(View.VISIBLE);
                        rlButton.setVisibility(View.GONE);
                        break;
                    case "4":
                        //正在使用
                        tvStatus.setText("已入住");
                        tvHint.setText("已支付订单，请在使用时间范围内使用该订单");
                        ivPic.setImageResource(R.drawable.shiyongzhong);
                        ivPic.setVisibility(View.VISIBLE);
                        rlButton.setVisibility(View.GONE);
                        break;
                }
            }
        }
        tvPrice.setText("￥" + bean.getApyment_money());
        tvSpName.setText(bean.getSp_name());
        tvName.setText(bean.getName());
        tvAddress.setText(bean.getSp_address());
        tvRuzhuTime.setText("入住:  " + TimeUtil.getFormatData(bean.getDao(), "MM-dd")
                + " （" + TimeUtil.getWeek(bean.getDao()) + "）");
        tvLiTime.setText("离店:  " + TimeUtil.getFormatData(bean.getLi_time(), "MM-dd")
                + " （" + TimeUtil.getWeek(bean.getLi_time()) + "）");
        tvBookNum.setText("共 " + bean.getNightnum() + " 晚  共 " + bean.getBook_num() + " 间");
        ArrayList<String> str = (ArrayList<String>) bean.getSpecif();
        if (str != null && str.size() > 0) {
            String specif = "";
            for (int i = 0; i < str.size(); i++) {
                if (i == 0) {
                    specif = str.get(i);
                } else {
                    specif = specif + " | " + str.get(i);
                }

            }
            llSpecif.setVisibility(View.VISIBLE);
            tvSpecif.setText(specif);
        } else {
            llSpecif.setVisibility(View.GONE);
        }
        tvPhone.setText(bean.getPhone());
        String checkName = "";
        for (String s : bean.getCheck_name()) {
            checkName = checkName + " " + s;
        }
        tvCheckName.setText(checkName);
        tvDaoTime.setText(bean.getYu_time());
        tvRuzhuDirect.setText(bean.getRuzhu_direct());
        tvOrderId.setText(bean.getOrder_id());
        tvCreatTime.setText(TimeUtil.getFormatData(bean.getCreate_time(), "yyyy年MM月dd日 HH:mm"));

    }


    @OnClick({R.id.tv_go_pay, R.id.ll_hotel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_go_pay:
                if ("去支付".equals(tvGoPay.getText().toString())) {
                    Intent intent = new Intent(ReserveOrderDetailsActivity.this, ImmediatelyPaymentActivity.class);
                    intent.putExtra("mode_type", "1");
                    intent.putExtra("place_type", "3");
                    intent.putExtra("shop_price", detail.getApyment_money());
                    intent.putExtra("order_sn", detail.getOrder_id());
                    startActivity(intent);
                } else if ("去使用".equals(tvGoPay.getText().toString())) {
                    ToastUtil.showToast(ReserveOrderDetailsActivity.this, "点击了去使用");
                } else if ("删除订单".equals(tvGoPay.getText().toString())) {
                    delReserveOrder(helper.getToken(), detail.getOrder_id(), "订单过期");
                }
                break;
        }

    }


    private void delReserveOrder(String token, String id, String cancel) {
        HttpHelp.getInstance().create(RemoteApi.class).delReserveOrder(token, id, cancel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(ReserveOrderDetailsActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(ReserveOrderDetailsActivity.this, "删除成功");
                            finishAndUpdate();
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(ReserveOrderDetailsActivity.this);
                        } else {
                            ToastUtil.showToast(ReserveOrderDetailsActivity.this, baseBean.message);
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
    public void onUpdateEvent(UpdateInformationEvent event) {
        finishAndUpdate();
    }

    /**
     * 付款事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateEvent(ReservationOrderEvent event) {
        finishAndUpdate();
    }


    /**
     * 关闭activity 发送消息刷新上个页面数据
     */
    private void finishAndUpdate() {
        EventBusUtil.post(new UpdateBeforeUIDateEvent());
        finish();
    }
}
