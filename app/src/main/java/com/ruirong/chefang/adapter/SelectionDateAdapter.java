package com.ruirong.chefang.adapter;

import android.graphics.Color;
import android.widget.GridView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.SelectedStateBean;

/**
 * Created by Administrator on 2018/3/23.
 */

public class SelectionDateAdapter extends BaseListAdapter<SelectedStateBean> {

    public SelectionDateAdapter(GridView gridView) {
        super(gridView, R.layout.item_selection_date);
    }

    @Override
    public void fillData(ViewHolder holder, int position, SelectedStateBean model) {

        if (model.isTrue()) {
            holder.getTextView(R.id.tv_week).setTextColor(Color.parseColor("#3abc98"));
            holder.getTextView(R.id.tv_date).setTextColor(Color.parseColor("#3abc98"));
            holder.getTextView(R.id.tv_week).getPaint().setFakeBoldText(true);
            holder.getTextView(R.id.tv_date).getPaint().setFakeBoldText(true);
        } else {
            holder.getTextView(R.id.tv_week).setTextColor(Color.parseColor("#000000"));
            holder.getTextView(R.id.tv_date).setTextColor(Color.parseColor("#000000"));
            holder.getTextView(R.id.tv_week).getPaint().setFakeBoldText(true);
            holder.getTextView(R.id.tv_date).getPaint().setFakeBoldText(true);
        }

        holder.setText(R.id.tv_week, model.getTitle());

        String[] split = model.getName().split("-");
        holder.setText(R.id.tv_date, split[1] + "-" + split[2]);

    }
}
