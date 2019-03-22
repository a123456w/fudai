package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.FoodBean;

public class FoodAdapter extends RecyclerViewAdapter<FoodBean> {
    public FoodAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_function);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, FoodBean model) {

    }
}
