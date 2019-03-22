package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;

/**
 * create by xuxx on 2018/11/1
 * 美食类型适配器
 */
public class ClassificationItemAdapter extends RecyclerViewAdapter<Object>{

    public ClassificationItemAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.classification_item_layout);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, Object model) {

    }
}
