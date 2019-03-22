package com.ruirong.chefang.adapter;

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


/**
 * Created by chenlipeng on 2017/12/27 0027
 * describe:  预定订单 待使用
 */

public class ReserveOrderListviewUseAdapter extends RecyclerViewAdapter<ReserveOrderBean.ListBean> {
    public ReserveOrderListviewUseAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.reserve_order_listview_item_wait_use);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, ReserveOrderBean.ListBean model) {
        helper.setText(R.id.tv_content, model.getSp_name());
        helper.setText(R.id.tv_specif, model.getSpecif());
        helper.setText(R.id.tv_money, "￥" + model.getMoney());
        helper.setText(R.id.tv_time, DateUtil.toDate(model.getCreate_time(), DateUtil.FORMAT_MD_CN) + " - " + DateUtil.toDate(model.getCreate_time(), DateUtil.FORMAT_MD_CN));
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getCover(), helper.getImageView(R.id.js_image_title));

        helper.setItemChildClickListener(R.id.order_all_tv_js);

    }

}
