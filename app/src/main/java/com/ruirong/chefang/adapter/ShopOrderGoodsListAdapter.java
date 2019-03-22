package com.ruirong.chefang.adapter;

import android.graphics.Paint;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.ShopOrderBean;

/**
 * Created by 16690 on 2018/3/28.
 * describe:
 */

public class ShopOrderGoodsListAdapter extends BaseListAdapter<ShopOrderBean.GoodslistBean> {

    public ShopOrderGoodsListAdapter(ListView listView) {
        super(listView, R.layout.item_shop_order_good);
    }

    @Override
    public void fillData(ViewHolder holder, int position, ShopOrderBean.GoodslistBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getPic(), holder.getImageView(R.id.goods_pic));
        holder.setText(R.id.goods_name, model.getGoods_name());
        holder.setText(R.id.goods_price, "￥"+model.getGood_price());
        holder.setText(R.id.goods_content,model.getSpecif());
//        //添加删除线
//        holder.setText(R.id.goods_old_price, model.getBefore_price());
//        holder.getTextView(R.id.goods_old_price).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.setText(R.id.goods_count, "x" + model.getNum());
    }
}
