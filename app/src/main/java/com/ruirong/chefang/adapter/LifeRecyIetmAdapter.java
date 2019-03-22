package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;

/**
 * create by xuxx on 2018/10/31
 */
public class LifeRecyIetmAdapter extends RecyclerViewAdapter<Object> {
    public LifeRecyIetmAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.life_recy_item_layout);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, Object model) {

    }
}
