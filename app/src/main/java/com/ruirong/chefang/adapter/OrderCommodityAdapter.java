package com.ruirong.chefang.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.RefundChoiceActivity;
import com.ruirong.chefang.activity.SpecialtyOrderDetailsActivity;
import com.ruirong.chefang.bean.SpecialtyOrderDetailBean;

/**
 * Created by Administrator on 2018/3/28.
 */

public class OrderCommodityAdapter extends BaseListAdapter<SpecialtyOrderDetailBean.GoodsInfoBean> {
    private boolean isT = true;
    private String number_bh;

    public void setNumber_bh(String number_bh) {
        this.number_bh = number_bh;
    }


    public void setIsT(boolean isT) {
        this.isT = isT;
    }

    public boolean getIsT() {
        return isT;
    }

    public OrderCommodityAdapter(ListView listView) {
        super(listView, R.layout.item_order_commodity);
    }

    @Override
    public void fillData(ViewHolder holder, int position, final SpecialtyOrderDetailBean.GoodsInfoBean model) {
        if (isT) {
            holder.getView(R.id.order_refund_rl).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.order_refund_rl).setVisibility(View.GONE);
        }

        GlideUtil.display(mContext, Constants.IMG_HOST + model.getPic(), holder.getImageView(R.id.goods_pic));
        holder.setText(R.id.goods_name, model.getName());
        if ("1".equals(model.getTui_status())) {
            holder.setText(R.id.goods_type, "已申请退款");
        } else if ("2".equals(model.getTui_status())) {
            holder.setText(R.id.goods_type, "2退款成功");
        } else if ("3".equals(model.getTui_status())) {
            holder.setText(R.id.goods_type, "3拒绝退款");
        } else if ("0".equals(model.getTui_status())) {
            holder.getView(R.id.order_refund_rl).setVisibility(View.VISIBLE);
            if (model.getAttr() != null && model.getAttr().size() > 0) {
                holder.setText(R.id.goods_type, model.getAttr().get(0));
            }
        }
        holder.setText(R.id.new_price, "￥" + model.getNow_price());
        holder.setText(R.id.old_price, "￥" + model.getBefore_price());
        holder.setText(R.id.goods_num, "x" + model.getNum());

        holder.getView(R.id.order_refund).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(number_bh)) {
                    Intent intent = new Intent(mContext, RefundChoiceActivity.class);
                    String goodsContent = new Gson().toJson(model);
                    intent.putExtra("goodsContent", goodsContent);
                    intent.putExtra("number_bh", number_bh);
                    mContext.startActivity(intent);
                    SpecialtyOrderDetailsActivity mContext = (SpecialtyOrderDetailsActivity) OrderCommodityAdapter.this.mContext;
                    mContext.finish();
                }
            }
        });

    }
}
