package com.ruirong.chefang.adapter;

import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.FuBackHotelDetailsBean;
import com.ruirong.chefang.bean.SpecialtyDetailsBean;

/**
 * Created by chenlipeng on 2017/12/29 0029
 * describe:
 */

public class HotelDetailsReserveAdapter extends BaseListAdapter<FuBackHotelDetailsBean.HotelsBean.AttributeBean> {
    public HotelDetailsReserveAdapter(ListView listView) {
        super(listView, R.layout.item_specialty_details_productpara);
    }


    @Override
    public void fillData(ViewHolder holder, int position, FuBackHotelDetailsBean.HotelsBean.AttributeBean model) {
        holder.setText(R.id.title, model.getKey());
        holder.setText(R.id.content, model.getValue());
    }
}
