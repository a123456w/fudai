package com.ruirong.chefang.adapter;

import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.LuckBackRoomListviewBean;
import com.ruirong.chefang.bean.SpecialtyDetailsBean;

/**
 * Created by chenlipeng on 2017/12/29 0029
 * describe:
 */

public class SpecialtyDetailsProductParameterAdapter extends BaseListAdapter<SpecialtyDetailsBean.GoodsBean> {
    public SpecialtyDetailsProductParameterAdapter(ListView listView) {
        super(listView, R.layout.item_specialty_details_productpara);
    }


    @Override
    public void fillData(BaseListAdapter.ViewHolder holder, int position, SpecialtyDetailsBean.GoodsBean model) {
        holder.setText(R.id.title, model.getKey());
        holder.setText(R.id.content, model.getValue());
    }
}
