package com.ruirong.chefang.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.LargeCategoryBean;

/**
 * Created by Administrator on 2018/3/6.
 */

public class CategoryLeftAdapter extends RecyclerViewAdapter<LargeCategoryBean.CateBean> {

    public CategoryLeftAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.left_list_item);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, LargeCategoryBean.CateBean model) {
        LinearLayout linearLayout = helper.getView(R.id.category_loose);

        if (model.isTrue()) {
            linearLayout.setBackgroundResource(R.color.white);
        } else {
            linearLayout.setBackgroundResource(R.color.color_eeeeee);
        }

        if (position == 0) {
            helper.getImageView(R.id.category_type_pic).setImageResource(R.drawable.selling);
            helper.getImageView(R.id.category_type_pic).setVisibility(View.VISIBLE);
        } else if (position == 1) {
            helper.getImageView(R.id.category_type_pic).setImageResource(R.drawable.discount);
            helper.getImageView(R.id.category_type_pic).setVisibility(View.VISIBLE);
        } else {
            helper.getImageView(R.id.category_type_pic).setVisibility(View.GONE);
        }

        helper.setText(R.id.category_name, model.getName());

    }
}
