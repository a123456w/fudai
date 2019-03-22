package com.ruirong.chefang.adapter;

import android.graphics.Paint;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.HotelDetailChoseOneBean;

/**酒店详情选项一的适配器
 * Created by dillon on 2017/12/27.
 */

public class HotelDetailChoseOneAdapter extends BaseListAdapter<HotelDetailChoseOneBean> {
    public HotelDetailChoseOneAdapter(ListView listView) {
        super(listView, R.layout.item_hoteldetail_chosetylpeone);
    }

    @Override
    public void fillData(ViewHolder holder, int position, HotelDetailChoseOneBean model) {
        holder.setText(R.id.tv_oldprice, "583");
        holder.getTextView(R.id.tv_oldprice).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }
}
