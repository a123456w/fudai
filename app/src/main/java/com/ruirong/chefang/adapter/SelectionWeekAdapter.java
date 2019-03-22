package com.ruirong.chefang.adapter;

import android.graphics.Color;
import android.widget.GridView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.SelectedStateBean;

/**
 * Created by Administrator on 2018/3/23.
 */

public class SelectionWeekAdapter extends BaseListAdapter<SelectedStateBean> {

    public SelectionWeekAdapter(GridView gridView) {
        super(gridView, R.layout.item_selection_week);
    }

    @Override
    public void fillData(ViewHolder holder, int position, SelectedStateBean model) {

        if (model.isTrue()) {
            //选中是蓝色加粗
            holder.getTextView(R.id.tv_week).setTextColor(Color.parseColor("#3abc98"));
            holder.getTextView(R.id.tv_week).getPaint().setFakeBoldText(true);
        } else {
            // 没有选中是黑色
            holder.getTextView(R.id.tv_week).setTextColor(Color.parseColor("#000000"));
            holder.getTextView(R.id.tv_week).getPaint().setFakeBoldText(true);
        }

        holder.setText(R.id.tv_week, model.getName());

    }
}
