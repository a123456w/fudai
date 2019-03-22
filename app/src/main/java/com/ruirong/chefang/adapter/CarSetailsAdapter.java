package com.ruirong.chefang.adapter;

import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.CarAndRoomsBean;

/**
 * Created by BX on 2018/2/28.
 */

public class CarSetailsAdapter extends BaseListAdapter<CarAndRoomsBean.CanshuBean> {
    public CarSetailsAdapter(ListView listView) {
        super(listView, R.layout.item_carsteails);
    }

    @Override
    public void fillData(ViewHolder holder, int position, CarAndRoomsBean.CanshuBean model) {
        holder.setText(R.id.tv_title, model.getKey());
        holder.setText(R.id.tv_content, model.getValue());
    }
}
