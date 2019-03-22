package com.ruirong.chefang.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.maning.calendarlibrary.view.GridItemDividerDecoration;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;

import java.util.ArrayList;

/*
 *  Created by 李  on 2018/11/19.
 */
public class SearchAdapter extends RecyclerViewAdapter<String>{
    public SearchAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_search);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, String model) {
        initRecyclerView(helper);
    }

    private void initRecyclerView(ViewHolderHelper helper) {
        RecyclerView rv = helper.getView(R.id.rv_item_search);
        ItemSearchAdapter itemSearchAdapter = new ItemSearchAdapter(rv);
        GridLayoutManager gm=new GridLayoutManager(mContext,4);
        rv.setLayoutManager(gm);
        GridItemDividerDecoration gridItemDividerDecoration = new GridItemDividerDecoration(mContext.getResources().getDrawable(R.drawable.sarech_item_line), GridItemDividerDecoration.VERTICAL, 2);
        rv.addItemDecoration(gridItemDividerDecoration);
        rv.setAdapter(itemSearchAdapter);
        ArrayList<String> strings = new ArrayList<>();
        for (int i=0;i<12;i++){
            strings.add("");
        }
        itemSearchAdapter.setData(strings);
    }
}
