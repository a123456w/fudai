package com.ruirong.chefang.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.SpecialtyOrderDetailsActivity;
import com.ruirong.chefang.bean.SpecialtyOrderBean;


/**
 * describe:待付款
 * Created by chenlipeng on 2017/12/21 11:09
 */


public class MyOrderListviewWaitPayMoneyAdapter extends RecyclerViewAdapter<SpecialtyOrderBean> {
    private SpecialtyOrderGoodsListAdapter specialtyOrderGoodsListAdapter;

    public MyOrderListviewWaitPayMoneyAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.my_order_listview_item_waitpay_money);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, final SpecialtyOrderBean model) {

        NoScrollListView goodsList = helper.getView(R.id.goods_list);
        specialtyOrderGoodsListAdapter = new SpecialtyOrderGoodsListAdapter(goodsList);
        goodsList.setAdapter(specialtyOrderGoodsListAdapter);
        if (model.getGoods_List() != null && model.getGoods_List().size() > 0) {
            specialtyOrderGoodsListAdapter.setData(model.getGoods_List());
        }

        if ("1".equals(model.getGq_status())) {//未过期
            helper.getView(R.id.goods_rl).setVisibility(View.VISIBLE);
            helper.getView(R.id.customer_del_lr).setVisibility(View.GONE);

            helper.setText(R.id.goods_type, "等待买家付款");
        } else {
            helper.getView(R.id.goods_rl).setVisibility(View.GONE);
            helper.getView(R.id.customer_del_lr).setVisibility(View.VISIBLE);

            helper.setText(R.id.goods_type, "已过期");
        }

        goodsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, SpecialtyOrderDetailsActivity.class);
                intent.putExtra("number_bh", model.getNumber_bh());
                mContext.startActivity(intent);
            }
        });

        helper.setText(R.id.goods_information, "共" + model.getNumber() + "件商品 小计:￥" + model.getOrder_amount() + "(含运费"+model.getDistri_price()+"元)");


        helper.setItemChildClickListener(R.id.tv_contact_seller);
        helper.setItemChildClickListener(R.id.goods_cancel);
        helper.setItemChildClickListener(R.id.order_all_tv_js);

        helper.setItemChildClickListener(R.id.customer_del);


    }
}
