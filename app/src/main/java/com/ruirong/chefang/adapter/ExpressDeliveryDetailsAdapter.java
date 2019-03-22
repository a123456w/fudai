package com.ruirong.chefang.adapter;

import android.view.View;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.Showlogistics;

/**
 * Created by Administrator on 2018/3/26.
 */

public class ExpressDeliveryDetailsAdapter extends BaseListAdapter<Showlogistics.TracesBean> {

    public ExpressDeliveryDetailsAdapter(ListView listView) {
        super(listView, R.layout.item_express);
    }

    @Override
    public void fillData(ViewHolder holder, int position, Showlogistics.TracesBean model) {

        holder.setText(R.id.title, model.getAcceptStation());
        holder.setText(R.id.item, model.getAcceptTime());


        if (position == 0) {
            holder.setImageResource(R.id.arrive_pic, R.drawable.arrive);
            holder.getView(R.id.isTop).setVisibility(View.GONE);
            holder.getView(R.id.goods_botton).setVisibility(View.VISIBLE);
        } else {
            holder.setImageResource(R.id.arrive_pic, R.drawable.transportation);
            if(position==getData().size()-1){
                holder.getView(R.id.goods_botton).setVisibility(View.GONE);
            }
        }
    }
}
