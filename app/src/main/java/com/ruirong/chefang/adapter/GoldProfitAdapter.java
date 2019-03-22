package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.util.DateUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.GoldProfitBean;

/**
 * Created by dillon on 2017/9/13.
 */

public class GoldProfitAdapter extends RecyclerViewAdapter<GoldProfitBean> {
    public GoldProfitAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_profit_list);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, GoldProfitBean model) {
        viewHolderHelper.setText(R.id.tv_dateline,model.getDate_line()+"天种植期");
        viewHolderHelper.setText(R.id.tv_starttime, DateUtil.toDate(model.getPay_time(), DateUtil.FORMAT_YMD));
        viewHolderHelper.setText(R.id.tv_endtime, DateUtil.toDate(model.getExpire_time(), DateUtil.FORMAT_YMD));
        viewHolderHelper.setText(R.id.tv_cash, model.getPay_gold());
        viewHolderHelper.setText(R.id.tv_profit, model.getTotal_rebate());
        viewHolderHelper.setItemChildClickListener(R.id.tv_checkhis);
    }
}
