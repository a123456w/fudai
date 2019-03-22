package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.HotelBean;

/**酒店住宿适配器
 * Created by dillon on 2017/12/26.
 */

public class HotelAdapter extends RecyclerViewAdapter<HotelBean> {
    public HotelAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_hotel);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, HotelBean model) {

    }
}
