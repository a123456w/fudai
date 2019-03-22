package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;

/**
 * 智能排序DropDown适配器
 */
public class SortDropDownAdapter  extends RecyclerViewAdapter<Object>{
    public SortDropDownAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.nearby_item_citymessage);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, Object model) {
        helper.setText(R.id.tv_nearby_cityMessage,model.toString());

    }
}
