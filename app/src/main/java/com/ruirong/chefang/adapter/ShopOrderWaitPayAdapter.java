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
 * describe: 商城订单 待付款Adapter
 */

public class ShopOrderWaitPayAdapter extends RecyclerViewAdapter<ShopOrderBean>{

    private ShopOrderGoodsListAdapter shopOrderGoodsListAdapter;

    public ShopOrderWaitPayAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_my_shop_order_wait_pay);
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

        if ("1".equals(model.getGq_status())){
            //未过期
            helper.getView(R.id.rl_wait_pay_button).setVisibility(View.VISIBLE);
            helper.getView(R.id.customer_del_lr).setVisibility(View.GONE);
            helper.setText(R.id.tv_state,"等待买家付款");
        }else {
            helper.setText(R.id.tv_state,"已过期");
            helper.getView(R.id.rl_wait_pay_button).setVisibility(View.GONE);
            helper.getView(R.id.customer_del_lr).setVisibility(View.VISIBLE);
        }

        helper.setText(R.id.tv_shop_name,model.getShop_name());
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
        helper.setItemChildClickListener(R.id.tv_contact_seller);
        helper.setItemChildClickListener(R.id.goods_cancel);
        helper.setItemChildClickListener(R.id.order_all_tv_js);
        helper.setItemChildClickListener(R.id.customer_del);
    }
}
