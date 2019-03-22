package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;

import butterknife.BindView;

public class NearbyCityAdapter extends RecyclerViewAdapter<Object> {




    public NearbyCityAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.nearby_item_city);


    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, Object model) {
        this.getData().size();



        helper.setText(R.id.tv_nearby_city, model.toString());


    }




}
