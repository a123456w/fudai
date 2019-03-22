package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.GoldBuyBean;

/**
 * Created by dillon on 2017/9/13.
 */

public class GoldBuyAdapter extends RecyclerViewAdapter<GoldBuyBean> {
    public GoldBuyAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_buy_list);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, GoldBuyBean model) {
           viewHolderHelper.setText(R.id.tv_moneyo,model.getMoney());
           viewHolderHelper.setText(R.id.tv_profit,model.getProfit());
    }
}
