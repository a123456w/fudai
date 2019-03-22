package com.ruirong.chefang.adapter;

import android.graphics.Paint;
import android.widget.ListView;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.PurchaseBean;

/**
 * Created by Administrator on 2018/3/19.
 */

public class OrderGoodsListAdapter extends BaseListAdapter<PurchaseBean.ListBean> {

    public OrderGoodsListAdapter(ListView listView) {
        super(listView, R.layout.item_order_goods);
    }

    @Override
    public void fillData(ViewHolder holder, int position, PurchaseBean.ListBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getPic(), holder.getImageView(R.id.goods_pic));
        holder.setText(R.id.goods_name, model.getName());
        holder.setText(R.id.goods_price, "￥" + model.getNow_price());
        TextView txt1=holder.getView(R.id.goods_old_price);
        txt1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.setText(R.id.goods_old_price, "￥" + model.getBefore_price());
        holder.setText(R.id.goods_count, "x" + model.getNum());


    }
}
