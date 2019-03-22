package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.ProdeceBean;

/**
 * 金豆增值adapter
 * Created by dillon on 2017/9/8.
 */

public class GoldAddAdapter extends RecyclerViewAdapter<ProdeceBean.DataBean> {
    public GoldAddAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_grow_goldbean);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, ProdeceBean.DataBean model) {
        viewHolderHelper.setText(R.id.tv_dayprofit, model.getRate_interest() + "%");
        viewHolderHelper.setText(R.id.tv_yearprofit, model.getAnnual_interest_rate() + "%");
        viewHolderHelper.setText(R.id.tv_date, model.getDate_line() + "天");
        viewHolderHelper.setText(R.id.tv_explain, model.getExplain());
        viewHolderHelper.setItemChildClickListener(R.id.tv_addprofit);

    }
}
