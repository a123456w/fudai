package com.ruirong.chefang.adapter;

import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.CarAndRoomsBean;

/**
 * Created by chenlipeng on 2017/12/26 0026
 * describe:  物流列表适配器
 */
public class LogisticsListviewAdapter extends BaseListAdapter<CarAndRoomsBean> {
    public LogisticsListviewAdapter(ListView listView) {


        super(listView, R.layout.listview_item_logistics);

    }


    @Override
    public void fillData(ViewHolder holder, int position, CarAndRoomsBean model) {


    }


}
