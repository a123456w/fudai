package com.ruirong.chefang.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.RefundGoodsInfoBean;

/**
 * Created by 16690 on 2018/4/3.
 * describe: 商城订单 退款 商品Adapter
 */

public class ShopOrderGoodsAdapter extends BaseListAdapter<RefundGoodsInfoBean> {

    public ShopOrderGoodsAdapter(ListView listView) {
        super(listView, R.layout.item_shop_order_detail_good);
    }

    @Override
    public void fillData(ViewHolder holder, int position, RefundGoodsInfoBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getCover(), holder.getImageView(R.id.goods_pic));
        holder.setText(R.id.goods_name, model.getGoods_name());
        holder.setText(R.id.goods_type,model.getSpecif());
        holder.setText(R.id.new_price, "￥"+model.getPrice());

        if (TextUtils.isEmpty(model.getYuan_price())|| "0".equals(model.getYuan_price())){
            holder.getTextView(R.id.old_price).setVisibility(View.GONE);
        }else {
            //添加删除线
            holder.setText(R.id.old_price, "￥"+model.getYuan_price());
            holder.getTextView(R.id.old_price).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        }
        holder.setText(R.id.goods_num, "x" + model.getNum());
    }
}
