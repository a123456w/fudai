package com.ruirong.chefang.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.ShopOrderDetailsActivity;
import com.ruirong.chefang.bean.ShopOrderBean;

/**
 * Created by 16690 on 2018/3/28.
 * describe: 商城订单 售后
 */

public class ShopOrderDeliveringAdapter extends RecyclerViewAdapter<ShopOrderBean> {
    private ShopOrderGoodsListAdapter shopOrderGoodsListAdapter;

    public ShopOrderDeliveringAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_my_shop_order_delivering);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, final int position, ShopOrderBean model) {
        NoScrollListView goodsList = helper.getView(R.id.goods_list);
        shopOrderGoodsListAdapter = new ShopOrderGoodsListAdapter(goodsList);
        goodsList.setAdapter(shopOrderGoodsListAdapter);
        if (model.getGoodslist() != null && model.getGoodslist().size() > 0) {
            shopOrderGoodsListAdapter.setData(model.getGoodslist());
        }

        goodsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Intent intent = new Intent(mContext, ShopOrderDetailsActivity.class);
                intent.putExtra("number_bh",getItem(position).getOrder_id());
                mContext.startActivity(intent);
            }
        });
        helper.setText(R.id.tv_shop_name,model.getShop_name());
        if ("1".equals(model.getCancel())) {
            helper.setText(R.id.tv_state, "退款申请");
        } else if ("2".equals(model.getCancel())) {
            helper.setText(R.id.tv_state, "同意退款");
        } else if ("3".equals(model.getCancel())) {
            helper.setText(R.id.tv_state, "拒绝退款");
        } else {
            helper.setText(R.id.tv_state, "售后");
        }
        if ("1".equals(model.getGoods_type())){
            //商品
            if ("1".equals(model.getIs_hx())){     // 1的时候点餐核销，2的时候配送
                helper.setText(R.id.goods_information, "共" + model.getGoodsnum() + "件商品 小计:￥" + model.getShi_money());
            }else {
                helper.setText(R.id.goods_information, "共" + model.getGoodsnum() + "件商品 小计:￥" + model.getShi_money() + "(含配送费"+model.getDistri_price()+"元)");
            }
        }else {
            helper.setText(R.id.goods_information, "小计:￥" + model.getShi_money());
        }

        helper.setItemChildClickListener(R.id.tv_look_logistics);
    }
}
