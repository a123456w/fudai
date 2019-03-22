package com.ruirong.chefang.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.DateUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.TimeUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.MyOrderListviewBean;
import com.ruirong.chefang.bean.ReserveOrderBean;

import java.util.Calendar;

import cn.iwgang.countdownview.CountdownView;

import static com.qlzx.mylibrary.util.DateUtil.FORMAT_MD_CN;


/**
 * Created by chenlipeng on 2017/12/27 0027
 * describe:  预定订单  待付款
 */

public class ReserveOrderListviewWaitPayMoneyAdapter extends RecyclerViewAdapter<ReserveOrderBean.ListBean> {
    private int gqTime = 0;

    public ReserveOrderListviewWaitPayMoneyAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.reserve_order_listview_item_wait_pay_money);
    }

    public void setGqTime(int gqTime) {
        this.gqTime = gqTime;
    }

    @Override
    protected void fillData(final ViewHolderHelper helper, int position, ReserveOrderBean.ListBean model) {
        helper.setText(R.id.tv_content, model.getSp_name());
        helper.setText(R.id.tv_specif, model.getSpecif());
        helper.setText(R.id.tv_money, "￥" + model.getMoney());
        helper.setText(R.id.tv_time, DateUtil.toDate(model.getDao_time(), FORMAT_MD_CN) + " - " + DateUtil.toDate(model.getLi_time(), FORMAT_MD_CN));
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getCover(), helper.getImageView(R.id.js_image_title));


        int time = (int) ((Integer.parseInt(model.getCreate_time()) + gqTime) - (Calendar.getInstance().getTimeInMillis() / 1000));
        if (time > 0) {
            helper.setText(R.id.tv_state, "预订成功");
            helper.getView(R.id.customer_del_lr).setVisibility(View.GONE);
            helper.getView(R.id.rl_wait_pay_button).setVisibility(View.VISIBLE);

            ((CountdownView) helper.getView(R.id.count_time)).start(time * 1000);
            ((CountdownView) helper.getView(R.id.count_time)).setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    helper.setText(R.id.tv_state, "订单过期");
                    helper.getView(R.id.customer_del_lr).setVisibility(View.VISIBLE);
                    helper.getView(R.id.rl_wait_pay_button).setVisibility(View.GONE);
                }
            });

        } else {
            helper.setText(R.id.tv_state, "订单过期");
            helper.getView(R.id.customer_del_lr).setVisibility(View.VISIBLE);
            helper.getView(R.id.rl_wait_pay_button).setVisibility(View.GONE);
        }

        helper.setItemChildClickListener(R.id.order_all_tv_js);
        helper.setItemChildClickListener(R.id.goods_cancel);
        helper.setItemChildClickListener(R.id.customer_del);

    }

}
