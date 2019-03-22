package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.RatingBar;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.ShopItemBean;
import com.ruirong.chefang.bean.ShopMallNearByShopBean;

/**
 * 商城分类一级通用适配器
 * Created by dillon on 2017/12/27.
 */

public class ShopMallClassifyAdapter extends RecyclerViewAdapter<ShopItemBean> {
    public ShopMallClassifyAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_shop_mall);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, ShopItemBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getCover(), helper.getImageView(R.id.iv_pic));
        helper.setText(R.id.classify_title, model.getSp_name());

        RatingBar rb_grade = helper.getView(R.id.rb_grade);
        if (model.getGrades() != null) {
            rb_grade.setRating(Float.parseFloat(model.getGrades()));
        }
        helper.setText(R.id.rb_grade_branch, model.getGrades() + "分");
        helper.setText(R.id.shop_region, model.getSp_address());


    }
}
