package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;

/*
 *  Created by 李  on 2018/11/13.
 */
public class OrderDetailAdapter extends RecyclerViewAdapter<Object> {


    public OrderDetailAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_order_commit);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, Object model) {

    }
}
