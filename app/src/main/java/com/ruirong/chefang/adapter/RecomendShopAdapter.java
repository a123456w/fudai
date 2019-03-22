package com.ruirong.chefang.adapter;

import android.text.TextUtils;
import android.widget.GridView;
import android.widget.RatingBar;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.HomePageBean;

/**
 * 推荐店铺适配器
 * Created by dillon on 2017/12/26.
 */

public class RecomendShopAdapter extends BaseListAdapter<HomePageBean.TuishopBean> {
    public RecomendShopAdapter(GridView gridView) {
        super(gridView, R.layout.item_shop);
    }

    @Override
    public void fillData(ViewHolder holder, int position, HomePageBean.TuishopBean model) {
         holder.setText(R.id.tv_title,model.getSp_name());
        RatingBar ratingBar = holder.getView(R.id.rb_grade);
        if ((!TextUtils.isEmpty(model.getDp_grade()))&&(model.getDp_grade()!=null)){
            ratingBar.setRating(Float.parseFloat(model.getDp_grade()));
        }
        holder.setText(R.id.tv_score,model.getDp_grade()+"分");
        holder.setText(R.id.tv_type,model.getCate());
        holder.setText(R.id.tv_area,model.getArea());
        holder.setText(R.id.tv_distance,model.getDistance()+"km");
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getCover(),holder.getImageView(R.id.iv_pic));

    }
}
