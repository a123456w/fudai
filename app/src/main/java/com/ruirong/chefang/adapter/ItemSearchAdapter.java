package com.ruirong.chefang.adapter;

import android.preference.PreferenceActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;

/*
 *  Created by 李  on 2018/11/19.
 */
public class ItemSearchAdapter extends RecyclerViewAdapter<String> {

    public ItemSearchAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_search_item);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, String model) {
//        helper.setText(R.id.tv_name,model);

    }
}
