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
 * describe:  待评价
 */

public class ShopOrderDeliverAdapter extends RecyclerViewAdapter<ShopOrderBean> {
    private ShopOrderGoodsListAdapter shopOrderGoodsListAdapter;

    public ShopOrderDeliverAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_my_shop_order_deliver);
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
        helper.setText(R.id.tv_state,"待评价");
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
        helper.setItemChildClickListener(R.id.order_all_tv_js);
    }
}
