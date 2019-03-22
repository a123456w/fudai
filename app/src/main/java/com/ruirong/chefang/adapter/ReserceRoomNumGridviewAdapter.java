package com.ruirong.chefang.adapter;

import android.graphics.Color;
import android.widget.GridView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.ReserceRoomNumberBean;

/**
 * Created by chenlipeng on 2018/1/4 0004
 * describe:  商家头条  房间预定
 */
public class ReserceRoomNumGridviewAdapter extends BaseListAdapter<ReserceRoomNumberBean> {

    public ReserceRoomNumGridviewAdapter(GridView gridView) {
        super(gridView, R.layout.item_reserce_room_number);
    }

    @Override
    public void fillData(ViewHolder holder, int position, ReserceRoomNumberBean model) {
        holder.setText(R.id.tv_content, model.getName());

        if (model.isChoice()) {
            holder.getTextView(R.id.tv_content).setBackgroundResource(R.drawable.bg_room);
            holder.getTextView(R.id.tv_content).setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.getTextView(R.id.tv_content).setBackgroundResource(R.drawable.bg_room_no);
            holder.getTextView(R.id.tv_content).setTextColor(Color.parseColor("#666666"));
        }

    }
}
