package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.DateUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.ReserveOrderBean;

import java.util.Calendar;

import cn.iwgang.countdownview.CountdownView;

/**
 * Created by 16690 on 2018/4/9.
 * describe:
 */

public class ReserveOrderListAllAdapter extends RecyclerViewAdapter<ReserveOrderBean.ListBean> {
    private int gqTime = 0;

    public ReserveOrderListAllAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_reserve_order_all);
    }

    @Override
    protected void fillData(final ViewHolderHelper helper, int position, ReserveOrderBean.ListBean model) {
        helper.setText(R.id.tv_content, model.getSp_name());
        helper.setText(R.id.tv_specif, model.getSpecif());
        helper.setText(R.id.tv_money, "￥" + model.getMoney());
        helper.setText(R.id.tv_time, DateUtil.toDate(model.getDao_time(), DateUtil.FORMAT_MD_CN) + " - " + DateUtil.toDate(model.getLi_time(), DateUtil.FORMAT_MD_CN));
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getCover(), helper.getImageView(R.id.js_image_title));

        helper.getView(R.id.rl_wait_use).setVisibility(View.GONE);
        helper.getView(R.id.rl_wait_pay_button).setVisibility(View.GONE);
        helper.getView(R.id.rl_confirm).setVisibility(View.GONE);
        helper.getView(R.id.rl_del).setVisibility(View.GONE);
        helper.getView(R.id.rl_detail).setVisibility(View.GONE);
        helper.getView(R.id.rl_comment).setVisibility(View.GONE);
        helper.getView(R.id.ll_count_time).setVisibility(View.GONE);


        if ("1".equals(model.getPay_status())) {
            //未支付
            int time = (int) ((Integer.parseInt(model.getCreate_time()) + gqTime) - (Calendar.getInstance().getTimeInMillis() / 1000));
            if (time > 0) {
                helper.setText(R.id.tv_state, "预订成功");
                helper.getView(R.id.rl_wait_pay_button).setVisibility(View.VISIBLE);
                helper.getView(R.id.ll_count_time).setVisibility(View.VISIBLE);
                ((CountdownView) helper.getView(R.id.count_time)).start(time * 1000);
                ((CountdownView) helper.getView(R.id.count_time)).setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        helper.setText(R.id.tv_state, "订单过期");
                        helper.getView(R.id.rl_del).setVisibility(View.VISIBLE);
                        helper.getView(R.id.rl_wait_pay_button).setVisibility(View.GONE);
                    }
                });

            } else {
                helper.setText(R.id.tv_state, "订单过期");
                helper.getView(R.id.rl_del).setVisibility(View.VISIBLE);
                helper.getView(R.id.ll_count_time).setVisibility(View.GONE);
            }
        } else {
            //已支付
            if ("1".equals(model.getCancel())) {
                helper.setText(R.id.tv_state, "退款处理中");
                helper.getView(R.id.rl_detail).setVisibility(View.VISIBLE);
            } else if ("2".equals(model.getCancel())) {
                helper.setText(R.id.tv_state, "同意退款");
                helper.getView(R.id.rl_detail).setVisibility(View.VISIBLE);
            } else if ("3".equals(model.getCancel())) {
                helper.setText(R.id.tv_state, "拒绝退款");
                helper.getView(R.id.rl_detail).setVisibility(View.VISIBLE);
            } else {
                switch (model.getStatus()) {
                    case "1":
                        helper.setText(R.id.tv_state, "等待使用");
                        helper.getView(R.id.rl_wait_use).setVisibility(View.VISIBLE);
                        break;
                    case "2":
                        helper.setText(R.id.tv_state, "正在使用");
                        helper.getView(R.id.rl_confirm).setVisibility(View.VISIBLE);
                        break;
                    case "3":
                        if ("0".equals(model.getComment_state())) {
                            helper.setText(R.id.tv_state, "等待评价");
                            helper.getView(R.id.rl_comment).setVisibility(View.VISIBLE);
                        } else {
                            helper.setText(R.id.tv_state, "已评价");
                            helper.getView(R.id.rl_detail).setVisibility(View.VISIBLE);
                        }

                        break;
                    case "4":
                        helper.setText(R.id.tv_state, "正在使用");
                        helper.getView(R.id.rl_confirm).setVisibility(View.VISIBLE);
                        break;
                }
            }

        }

        helper.setItemChildClickListener(R.id.tv_cancel);//取消订单
        helper.setItemChildClickListener(R.id.tv_pay);//付款
        helper.setItemChildClickListener(R.id.customer_del);//删除订单
        helper.setItemChildClickListener(R.id.tv_refund);//退款
        helper.setItemChildClickListener(R.id.tv_confirm);// 使用中  确认订单
        helper.setItemChildClickListener(R.id.tv_comment);// 评价
        helper.setItemChildClickListener(R.id.tv_go_detail);// 查看详情
    }

    public void setGqTime(int gqTime) {
        this.gqTime = gqTime;
    }
}
