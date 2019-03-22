package com.ruirong.chefang.adapter;

import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.SelectedStateBean;

/**
 * Created by Administrator on 2018/3/26.
 */

public class ReasonAdapter extends BaseListAdapter<SelectedStateBean> {
    public ReasonAdapter(ListView listView) {
        super(listView, R.layout.item_reason);
    }

    @Override
    public void fillData(ViewHolder holder, int position, SelectedStateBean model) {
        holder.setText(R.id.reason_name, model.getTitle());
        if (model.isTrue()) {
            holder.setImageResource(R.id.reason_choice, R.drawable.orange_choosed_icon);
        } else {
            holder.setImageResource(R.id.reason_choice, R.drawable.no_choosed);
        }


    }
}
