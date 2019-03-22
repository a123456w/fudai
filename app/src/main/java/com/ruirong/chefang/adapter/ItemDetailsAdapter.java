package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

public class ItemDetailsAdapter extends RecyclerViewAdapter<Object> {
    public ItemDetailsAdapter(RecyclerView recyclerView, int itemLayoutId) {
        super(recyclerView, itemLayoutId);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, Object model) {

    }
}
