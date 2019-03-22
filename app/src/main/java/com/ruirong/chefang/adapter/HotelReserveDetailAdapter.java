package com.ruirong.chefang.adapter;

import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.HotelReserveTaoCan;

/**
 * Created by dillon on 2018/5/11.
 */

public class HotelReserveDetailAdapter extends BaseListAdapter<HotelReserveTaoCan.AttributeBean> {
    public HotelReserveDetailAdapter(ListView listView) {
        super(listView, R.layout.item_specialty_details_productpara);
    }


    @Override
    public void fillData(BaseListAdapter.ViewHolder holder, int position, HotelReserveTaoCan.AttributeBean model) {
        holder.setText(R.id.title, model.getKey());
        holder.setText(R.id.content, model.getValue());
    }
}