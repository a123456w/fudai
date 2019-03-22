package com.ruirong.chefang.personalcenter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.util.StringUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenlipeng on 2017/12/27 0027
 * describe:  订单详情
 */
public class OrderDetailsActivity extends BaseActivity

{

    @BindView(R.id.tv_wait_apply_money)
    TextView tvWaitApplyMoney;
    @BindView(R.id.tv_consignee)
    TextView tvConsignee;
    @BindView(R.id.order_details_image_position)
    ImageView orderDetailsImagePosition;
    @BindView(R.id.tv_remind_deliver_goods)
    TextView tvRemindDeliverGoods;
    @BindView(R.id.tv_product)
    TextView tvProduct;
    @BindView(R.id.tv_cancle_order)
    TextView tvCancleOrder;
    @BindView(R.id.rl_product)
    RelativeLayout rlProduct;
    private String type;

    @Override
    public int getContentView() {
        return R.layout.activity_order_details;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitleText("订单详情");


        type = getIntent().getStringExtra("type");
        if (!StringUtil.isEmpty(type)) {
            if (type.equals("待收货")) {
                tvProduct.setText("退货");
            } else if (type.equals("待发货")) {
                tvProduct.setText("退款");
            } else if (type.equals("待付款")) {
                rlProduct.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void getData() {

    }

    @OnClick({R.id.tv_remind_deliver_goods, R.id.tv_cancle_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_remind_deliver_goods:
                showConfirmationOfReceiptDialog();

//                    Toast.makeText(this, "提醒买家发货成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_cancle_order:
                canlleOrderDialog();

                break;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    /**
     * 这个是   您是否收到该订单商品的弹框
     */
    private void showConfirmationOfReceiptDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(OrderDetailsActivity.this);

        View inflate = LayoutInflater.from(OrderDetailsActivity.this).inflate(R.layout.dialog_confirmation_of_receipt, null);
        normalDialog.setView(inflate);
        normalDialog.show();


    }


    /**
     * 取消订单的弹框
     */
    private void canlleOrderDialog() {
        Dialog mDialog = new Dialog(OrderDetailsActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(OrderDetailsActivity.this).inflate(R.layout.dialog_reason_to_cancel_the_order, null);
        mDialog.setContentView(inflate);
        Window dialogwindow = mDialog.getWindow();
        dialogwindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogwindow.setAttributes(lp);
        mDialog.show();
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}

//
//    // TODO: 2016/5/17 构建一个popupwindow的布局
//    View popupView = getLayoutInflater().inflate(R.layout.dialog_confirmation_of_receipt, null);
//
//    // TODO: 2016/5/17 创建PopupWindow对象，指定宽度和高度
//    PopupWindow window = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//// TODO: 2016/5/17 设置背景颜色
//        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
//                // TODO: 2016/5/17 设置可以获取焦点
//                window.setFocusable(true);
//                // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
//                window.setOutsideTouchable(true);
//                // TODO: 2016/5/17 以下拉的方式显示，并且可以设置显示的位置
//                window.showAtLocation(view, Gravity.CENTER, 0, 0);
//
//                window.setOnDismissListener(new PopupWindow.OnDismissListener() {
//@Override
//public void onDismiss() {
//        backgroundAlpha(1f);
//        }
//        });
//
//
//        backgroundAlpha(0.3f);