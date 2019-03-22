package com.ruirong.chefang.adapter;

import android.widget.ListView;
import android.widget.RatingBar;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.util.TimeUtil;
import com.qlzx.mylibrary.widget.NoScrollGridView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.EachChildCommentBean;
import com.ruirong.chefang.bean.SpecialtyCommodityEvaluateBean;

/**
 * 特产详情评价
 * Created by BX on 2017/12/27.
 */

public class EachChildCommentAdapter extends BaseListAdapter<EachChildCommentBean.ListBean> {
    private EvaluateListAdapter adapter;

    public EachChildCommentAdapter(ListView listView) {
        super(listView, R.layout.list_item_specialty_evaluate);

    }


    @Override
    public void fillData(ViewHolder holder, int position, EachChildCommentBean.ListBean model) {
        holder.setText(R.id.tv_user_name, model.getUsername());
        holder.setText(R.id.tv_number_of_orders, model.getContent());
        holder.setText(R.id.tv_time, TimeUtil.timedate(model.getCreate_time()));
        RatingBar rb_grade = holder.getView(R.id.rb_grade);
        rb_grade.setRating(Float.parseFloat(model.getStart_num()));
        NoScrollGridView evaluate_gv = holder.getView(R.id.evaluate_gv);
        adapter = new EvaluateListAdapter(evaluate_gv);
        evaluate_gv.setAdapter(adapter);
        if (model.getPics() != null && model.getPics().size() > 0) {
            adapter.setData(model.getPics());
        }
    }
}
