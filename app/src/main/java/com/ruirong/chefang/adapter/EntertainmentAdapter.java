package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.EntertainmentBean;


/**
 * Created by chenlipeng on 2017/12/26 0026
 * describe:  娱乐
 */

public class EntertainmentAdapter extends RecyclerViewAdapter<EntertainmentBean> {



    public EntertainmentAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.list_item_entertainment_details_one_product);
//        list_item_entertainment_details_combo
//                list_item_entertainment_details_comment
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, EntertainmentBean model) {
//        viewHolderHelper.setText(R.id.tv_area_name,model.getName());
    }
}
