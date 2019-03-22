package com.ruirong.chefang.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.RatingBar;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.CarAndRoomsBean;

/**
 * 车房详情评论
 * Created by BX on 2017/12/27.
 */

public class CarpinglunAdapter extends BaseListAdapter<CarAndRoomsBean.PinglunBean> {
    public CarpinglunAdapter(ListView listView) {
        super(listView, R.layout.list_item_garade_details_comment);
    }


    @Override
    public void fillData(ViewHolder holder, int position, CarAndRoomsBean.PinglunBean model) {
        holder.setText(R.id.tv_user_name, model.getUsername());
        holder.setText(R.id.tv_time, model.getCreate_time());
        holder.setText(R.id.tv_number_of_orders, model.getContent());
        // holder.setText(R.id.tv_replay,model.)
        if (model.getStart_num()!=null){
            RatingBar ratingBar = holder.getView(R.id.rb_grade);
            ratingBar.setRating(Float.parseFloat(model.getStart_num()));
        }
        RecyclerView recyclerView = holder.getView(R.id.rv_Carpinglun);
        CarpinglunImageAdapter adapter = new CarpinglunImageAdapter(recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView.setAdapter(adapter);
        if (model.getPicsd()!=null&&model.getPicsd().size()>0){
            adapter.setData(model.getPicsd());
        }
    }
}
