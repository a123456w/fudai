package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.HotelDetailChoseOneBean;
import com.ruirong.chefang.bean.HotelDetailCommentBean;

/**
 * Created by chenlipeng on 2018/1/2 0002
   describe:    热销产品，套餐选项适配器
 */
public class HotSellProductDetailsComboChooseAdapter extends RecyclerViewAdapter<HotelDetailChoseOneBean> {
    public HotSellProductDetailsComboChooseAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.list_item_entertainment_details_combo);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, HotelDetailChoseOneBean model) {

    }
//    public HotSellProductDetailsComboChooseAdapter(ListView listView) {
//        super(listView, R.layout.list_item_entertainment_details_combo);
//    }
//
//    @Override
//    public void fillData(ViewHolder holder, int position, HotelDetailCommentBean model) {
//
//    }
}
