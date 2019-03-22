package com.ruirong.chefang.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.util.TimeUtil;
import com.qlzx.mylibrary.widget.NoScrollGridView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.FuBackHotelDetailsBean;

/**
 * Created by chenlipeng on 2018/1/2 0002
 * describe:  商家头条评论
 */
public class ShopHeadlineCommentAdapter extends BaseListAdapter<FuBackHotelDetailsBean.PinglunaBean> {
    private EvaluateListAdapter adapter;

    public ShopHeadlineCommentAdapter(ListView listView) {
        super(listView, R.layout.list_item_entertainment_details_comment);
    }

    @Override
    public void fillData(ViewHolder holder, int position, FuBackHotelDetailsBean.PinglunaBean model) {
        holder.setText(R.id.tv_user_name, model.getUsername());
        Log.i("XXX",model.getCreate_time());
        Log.i("XXX",TimeUtil.timedate(model.getCreate_time()));
        holder.setText(R.id.tv_time, TimeUtil.timedate(model.getCreate_time()));
        holder.setText(R.id.tv_number_of_orders, model.getContent());


        if (!TextUtils.isEmpty(model.getReply())) {
            holder.getView(R.id.ll_reply).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_reply, model.getReply());
        } else {
            holder.getView(R.id.ll_reply).setVisibility(View.GONE);
        }

        if (model.getStart_num()!=null&&(!TextUtils.isEmpty(model.getStart_num()))){
            RatingBar rb_grade = holder.getView(R.id.rb_grade);
            rb_grade.setRating(Float.parseFloat(model.getStart_num()));
        }


        NoScrollGridView noScrollGridView = holder.getView(R.id.ngr);
        adapter = new EvaluateListAdapter(noScrollGridView);
        noScrollGridView.setAdapter(adapter);


        if (model.getPics() != null && model.getPics().size() > 0) {
            adapter.setData(model.getPics());
        }

    }
}
