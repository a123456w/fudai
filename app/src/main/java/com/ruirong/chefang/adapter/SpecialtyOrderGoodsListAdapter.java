package com.ruirong.chefang.adapter;

import android.graphics.Paint;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.SpecialtyOrderBean;

/**
 * Created by Administrator on 2018/3/20.
 */

public class SpecialtyOrderGoodsListAdapter extends BaseListAdapter<SpecialtyOrderBean.GoodsListBean> {


    public SpecialtyOrderGoodsListAdapter(ListView listView) {
        super(listView, R.layout.item_specialty_order_goods);
    }

    @Override
    public void fillData(ViewHolder holder, int position, SpecialtyOrderBean.GoodsListBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getPic(), holder.getImageView(R.id.goods_pic));
        holder.setText(R.id.goods_name, model.getName());
        if (model.getAttr() != null && model.getAttr().size() > 0) {
            holder.setText(R.id.goods_content, model.getAttr().get(0));
        }
        holder.setText(R.id.goods_price, "￥" + model.getNow_price() );
        //添加删除线
        holder.setText(R.id.goods_old_price, "￥" + model.getBefore_price());
        holder.getTextView(R.id.goods_old_price).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.setText(R.id.goods_count, "x" + model.getNum());

    }
}
