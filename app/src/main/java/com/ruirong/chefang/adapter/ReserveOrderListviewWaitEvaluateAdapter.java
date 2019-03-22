package com.ruirong.chefang.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.TimeUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.MyOrderListviewBean;
import com.ruirong.chefang.bean.ReserveOrderBean;


/**
 * Created by chenlipeng on 2017/12/27 0027
   describe:  预定订单 待评价
 */

public class ReserveOrderListviewWaitEvaluateAdapter extends RecyclerViewAdapter<ReserveOrderBean.ListBean> {
    public ReserveOrderListviewWaitEvaluateAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.reserve_order_listview_item_wait_evaluate);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, ReserveOrderBean.ListBean model) {
        helper.setText(R.id.tv_content, model.getSp_name());
        String time = TimeUtil.getSimpleTimeMini(model.getCreate_time());
        String [] times = time.split(" ");
        helper.setText(R.id.tv_day, times[0]);
        helper.setText(R.id.tv_time,times[1]+"下单");
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getCover(),helper.getImageView(R.id.js_image_title));


        helper.setItemChildClickListener(R.id.order_all_tv_js);

    }

}
