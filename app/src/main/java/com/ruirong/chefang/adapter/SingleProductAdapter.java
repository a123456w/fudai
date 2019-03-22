package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.FuBackHotelDetailsBean;
import com.ruirong.chefang.bean.SingleProductBean;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 热销产品  单品选项
 * Created by BX on 2018/2/24.
 */

public class SingleProductAdapter extends RecyclerViewAdapter<FuBackHotelDetailsBean.GoodsBean> {
    public SingleProductAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_singleproduct);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, FuBackHotelDetailsBean.GoodsBean model) {

        Glide.with(mContext).load(Constants.IMG_HOST + model.getCover())
                .bitmapTransform(new RoundedCornersTransformation(mContext, 6, 0, RoundedCornersTransformation.CornerType.ALL))
                .crossFade(1000)
                .into(helper.getImageView(R.id.single_pic));

        helper.setText(R.id.single_name, model.getName());
        helper.setText(R.id.single_price, "￥" + model.getPrice());

    }
}
