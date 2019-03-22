package com.ruirong.chefang.adapter;

import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.JiesuanBean;

/**
 * Created by Administrator on 2018/3/21.
 */

public class ShopCartConfirmAdapter extends BaseListAdapter<JiesuanBean.GoodsBean> {
    public ShopCartConfirmAdapter(ListView listView) {
        super(listView, R.layout.item_shop_cart_confirm);
    }

    @Override
    public void fillData(ViewHolder holder, int position, JiesuanBean.GoodsBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getCover(), holder.getImageView(R.id.goods_pid));
        holder.setText(R.id.goods_name, model.getName());
        holder.setText(R.id.goods_num, "x"+model.getNum());
        holder.setText(R.id.goods_price, "￥" + model.getPrice());


    }
}
