package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.SmallCategoryBean;

/**
 * Created by Administrator on 2018/3/6.
 */

public class CategoryRightAdapter extends RecyclerViewAdapter<SmallCategoryBean.ListBean> {

    public CategoryRightAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.right_list_item);
    }

    @Override
    protected void fillData(final ViewHolderHelper helper, int position, final SmallCategoryBean.ListBean model) {
        GlideUtil.display(mContext,
                Constants.IMG_HOST + model.getCover(),
                helper.getImageView(R.id.iv_goods_pic));

        helper.setText(R.id.tv_goods_name, model.getName());

        if (model.getSpecif() != null) {
            if (model.getSpecif().size() >= 1) {
                if (model.getSpecif().get(0) != null) {
                    helper.setText(R.id.content_one, model.getSpecif().get(0));
                }
            } else {
                helper.setText(R.id.content_one, "");
            }
            if (model.getSpecif().size() >= 2) {
                if (model.getSpecif().get(1) != null) {
                    helper.setText(R.id.content_tow, model.getSpecif().get(1));
                }
            } else {
                helper.setText(R.id.content_tow, "");
            }
        }

        helper.setText(R.id.tv_price, "￥" + model.getPrice());

        if ("0".equals(model.getCartcount())) {
            helper.getImageView(R.id.iv_reduce_less).setVisibility(View.GONE);
            helper.getTextView(R.id.tv_count).setVisibility(View.GONE);
        } else {
            helper.getImageView(R.id.iv_reduce_less).setVisibility(View.VISIBLE);
            helper.getTextView(R.id.tv_count).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_count, model.getCartcount());
        }

        helper.setItemChildClickListener(R.id.iv_plus_add);
        helper.setItemChildClickListener(R.id.iv_reduce_less);

//        helper.getImageView(R.id.iv_plus_add).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                model.setCartcount((Integer.parseInt(model.getCartcount()) + 1) + "");
//                addcart(mContext.token, model.getId(), model.getCartcount());
//                notifyDataSetChanged();
//            }
//        });
//
//        helper.getImageView(R.id.iv_reduce_less).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Integer.parseInt(model.getCartcount()) - 1 <= 0) {
//                    model.setCartcount("0");
//                } else {
//                    model.setCartcount(Integer.parseInt(model.getCartcount()) - 1 + "");
//                }
//                addcart(mContext.token, model.getId(), model.getCartcount());
//                notifyDataSetChanged();
//            }
//        });
    }


}
