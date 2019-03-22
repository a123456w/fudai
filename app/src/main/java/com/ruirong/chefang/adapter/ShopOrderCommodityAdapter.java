package com.ruirong.chefang.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.ShopOrderDetailBean;

/**
 * Created by wu on 2018/3/29.
 * describe: 商城订单详情 商品adapter
 */

public class ShopOrderCommodityAdapter extends BaseListAdapter<ShopOrderDetailBean.GoodsInfoBean> {
    public ShopOrderCommodityAdapter(ListView listView) {
        super(listView, R.layout.item_shop_order_detail_good);
    }

    @Override
    public void fillData(ViewHolder holder, int position, ShopOrderDetailBean.GoodsInfoBean model) {

        GlideUtil.display(mContext, Constants.IMG_HOST + model.getCover(), holder.getImageView(R.id.goods_pic));
        holder.setText(R.id.goods_name, model.getGoods_name());
        holder.setText(R.id.goods_type, model.getSpecif());
        holder.setText(R.id.new_price, "￥" + model.getPrice());

        if (TextUtils.isEmpty(model.getYuan_price()) || "0".equals(model.getYuan_price())) {
            holder.getTextView(R.id.old_price).setVisibility(View.GONE);
        } else {
            //添加删除线
            holder.setText(R.id.old_price, "￥" + model.getYuan_price());
            holder.getTextView(R.id.old_price).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        }
        holder.setText(R.id.goods_num, "x" + model.getNum());
    }
}
