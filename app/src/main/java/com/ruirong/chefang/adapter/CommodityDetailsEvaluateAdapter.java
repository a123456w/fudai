package com.ruirong.chefang.adapter;

import android.widget.ListView;
import android.widget.RatingBar;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.TimeUtil;
import com.qlzx.mylibrary.widget.NoScrollGridView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.FuBackHotelDetailsBean;
import com.ruirong.chefang.bean.GoodDetailsBean;
import com.ruirong.chefang.util.TextUtils;

/**
 * Created by chenlipeng on 2018/1/2 0002
 * describe:  商家头条评论
 */
public class CommodityDetailsEvaluateAdapter extends BaseListAdapter<GoodDetailsBean.PllistBean> {
    private EvaluateListAdapter adapter;

    public CommodityDetailsEvaluateAdapter(ListView listView) {
        super(listView, R.layout.item_commodity_details_evaluate);
    }

    @Override
    public void fillData(ViewHolder holder, int position, GoodDetailsBean.PllistBean model) {
        GlideUtil.displayAvatar(mContext, Constants.IMG_HOST + model.getUser_pic(), holder.getImageView(R.id.commodity_pic));
        holder.setText(R.id.commodity_name, model.getUsername());
        holder.setText(R.id.commodity_time, TimeUtil.timedate(model.getCreate_time()));
        holder.setText(R.id.commodity_content, model.getContent());

        RatingBar ratingBar = holder.getView(R.id.commodity_grade);
        ratingBar.setRating(Float.parseFloat(model.getStart_num()));

        NoScrollGridView noScrollGridView = holder.getView(R.id.commodity_gv);
        adapter = new EvaluateListAdapter(noScrollGridView);
        noScrollGridView.setAdapter(adapter);
        if (model.getPics() != null && model.getPics().size() > 0) {
            adapter.setData(model.getPics());
        }


    }
}
