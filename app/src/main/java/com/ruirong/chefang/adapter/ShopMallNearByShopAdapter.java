package com.ruirong.chefang.adapter;

import android.widget.ListView;
import android.widget.RatingBar;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.ShopHomeBean;

/**
 * 商城附近的店
 * Created by dillon on 2017/12/26.
 */

public class ShopMallNearByShopAdapter extends BaseListAdapter<ShopHomeBean.ShopBean> {
    public ShopMallNearByShopAdapter(ListView listView) {
        super(listView, R.layout.item_nearbyshops);
    }

    @Override
    public void fillData(ViewHolder holder, int position, ShopHomeBean.ShopBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getCover(), holder.getImageView(R.id.iv_pic));
        holder.setText(R.id.classify_title, model.getSp_name());

        if (model.getDp_grade()!=null){
            RatingBar rbGrade = holder.getView(R.id.rb_grade);
            rbGrade.setRating(Float.parseFloat(model.getDp_grade()));
        }
        holder.setText(R.id.rb_grade_branch, model.getDp_grade() + "分");
        holder.setText(R.id.shop_classify, model.getClassify_id());
        holder.setText(R.id.shop_region, model.getArea_id());
        holder.setText(R.id.shop_distance, model.getDistances() + "km");

    }
}
