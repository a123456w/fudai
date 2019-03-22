package com.ruirong.chefang.shoppingcart.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.shoppingcart.bean.ShoppingCartRecommendRean;


/**
 * Created by guo on 2017/8/9.
 */

public class ShoppingCartRecommendAdapter extends RecyclerViewAdapter<ShoppingCartRecommendRean.ListBean> {
    public ShoppingCartRecommendAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_shopping_cart_recommend);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, ShoppingCartRecommendRean.ListBean model) {
        GlideUtil.display(mContext,model.getIndex_pic(),viewHolderHelper.getImageView(R.id.iv_goods));
        viewHolderHelper.setText(R.id.tv_goods_name,model.getGoods_name());
        viewHolderHelper.setText(R.id.tv_price,model.getShop_price());

    }
}
