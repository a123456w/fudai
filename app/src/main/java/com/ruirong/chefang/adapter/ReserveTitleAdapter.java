package com.ruirong.chefang.adapter;

import android.widget.GridView;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.ReserceRoomNumberBean;
import com.ruirong.chefang.bean.ReserveTitleBean;

/**
 * Created by BX on 2018/3/1.
 */

public class ReserveTitleAdapter extends BaseListAdapter<String> {
    public ReserveTitleAdapter(GridView gridView) {
        super(gridView, R.layout.item_reservetitle);
    }

    @Override
    public void fillData(ViewHolder holder, int position, String model) {
        holder.setText(R.id.title, model);
    }
}
