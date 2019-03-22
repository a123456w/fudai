package com.ruirong.chefang.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.ShopAndHouseBean;


/**
 * Created by chenlipeng on 2018/4/8.
 */

public class ShopAndHouseAdapter extends BaseListAdapter<ShopAndHouseBean> {
    public ShopAndHouseAdapter(GridView gridView) {
        super(gridView, R.layout.item_shopandhouse);
    }

    @Override
    public void fillData(ViewHolder holder, int position, ShopAndHouseBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getCover(), holder.getImageView(R.id.iv_pic));
        LinearLayout shopLayout = holder.getLinearLayout(R.id.ll_shop);
        LinearLayout houseLayout = holder.getLinearLayout(R.id.ll_house);
        if (TextUtils.isEmpty(model.getAddress1()) && TextUtils.isEmpty(model.getAddress2()) && TextUtils.isEmpty(model.getTitle())) {
            shopLayout.setVisibility(View.VISIBLE);
            houseLayout.setVisibility(View.GONE);

            holder.setText(R.id.tv_shoptitle, model.getSp_name());
            RatingBar ratingBar = holder.getView(R.id.rb_grade);
            if ((!TextUtils.isEmpty(model.getDp_grade())) && (model.getDp_grade() != null)) {
                ratingBar.setRating(Float.parseFloat(model.getDp_grade()));
            }
            holder.setText(R.id.tv_score, model.getDp_grade() + "分");
            holder.setText(R.id.tv_type, model.getCate());
            holder.setText(R.id.tv_shoparea, model.getArea());
            holder.setText(R.id.tv_distance, model.getDistance() + "km");
        } else {
            shopLayout.setVisibility(View.GONE);
            houseLayout.setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_title, model.getSp_name());
            holder.setText(R.id.tv_city, model.getAddress1());
            holder.setText(R.id.tv_area, model.getAddress2());
            holder.setText(R.id.tv_address, model.getTitle());
        }
    }
}
