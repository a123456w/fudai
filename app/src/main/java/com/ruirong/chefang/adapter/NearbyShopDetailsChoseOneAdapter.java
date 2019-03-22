package com.ruirong.chefang.adapter;

import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.HotelDetailChoseOneBean;

/**
 * Created by chenlipeng on 2018/1/2 0002
   describe: 附近店铺详情  单品
 */
public class NearbyShopDetailsChoseOneAdapter extends BaseListAdapter<HotelDetailChoseOneBean> {
    public NearbyShopDetailsChoseOneAdapter(ListView listView) {
        super(listView, R.layout.list_item_nearby_shop_details_one_product);
    }

    @Override
    public void fillData(ViewHolder holder, int position, HotelDetailChoseOneBean model) {
//        holder.setText(R.id.tv_oldprice, "583");
//        holder.getTextView(R.id.tv_oldprice).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }
}
