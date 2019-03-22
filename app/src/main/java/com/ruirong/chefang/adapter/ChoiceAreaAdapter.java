package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.AreaBean;


/**
 * Created by guo on 2017/8/18.
 */

public class ChoiceAreaAdapter extends RecyclerViewAdapter<AreaBean> {

    public ChoiceAreaAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_choice_area);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, AreaBean model) {
        viewHolderHelper.setText(R.id.tv_area_name,model.getName());

    }
}
