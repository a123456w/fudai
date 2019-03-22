package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;

public class ItemDanAdapter extends RecyclerViewAdapter<Object> {
    public ItemDanAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_dan);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, Object model) {

    }
}
