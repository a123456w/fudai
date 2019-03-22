package com.ruirong.chefang.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.SpecialtyOrderDetailsActivity;
import com.ruirong.chefang.bean.MyOrderListviewBean;
import com.ruirong.chefang.bean.SpecialtyOrderBean;

import java.util.Map;


/**
 * Created by chenlipeng on 2017/12/27 0027
 * describe:  全部订单的适配器
 */

public class MyOrderAllListviewAdapter extends RecyclerViewAdapter<SpecialtyOrderBean> {
    private SpecialtyOrderGoodsListAdapter specialtyOrderGoodsListAdapter;

    public MyOrderAllListviewAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.my_order_listview_item_deliver_good_all);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, final SpecialtyOrderBean model) {
        NoScrollListView goodsList = helper.getView(R.id.goods_list);
        specialtyOrderGoodsListAdapter = new SpecialtyOrderGoodsListAdapter(goodsList);
        goodsList.setAdapter(specialtyOrderGoodsListAdapter);

        if (model.getGoods_List() != null && model.getGoods_List().size() > 0) {
            specialtyOrderGoodsListAdapter.setData(model.getGoods_List());
        }

        goodsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, SpecialtyOrderDetailsActivity.class);
                intent.putExtra("number_bh", model.getNumber_bh());
                Log.i("XXX", model.getNumber_bh());
                mContext.startActivity(intent);
            }
        });

        setV(helper);
        if ("1".equals(model.getPay_state())) {//已付款

            if ("1".equals(model.getState())) {//待收货
                helper.setText(R.id.goods_type, "等待买家收货");
                helper.getView(R.id.goods_receipt).setVisibility(View.VISIBLE);
            } else if ("2".equals(model.getState())) {//已收货
                helper.setText(R.id.goods_type, "交易成功");
                helper.getView(R.id.to_be_evaluated).setVisibility(View.VISIBLE);
            } else if ("3".equals(model.getState())) {//售后
                helper.setText(R.id.goods_type, "申请售后");
                helper.setText(R.id.goods_information, "申请退款");
                helper.getView(R.id.customer_service).setVisibility(View.VISIBLE);

            } else if ("0".equals(model.getState())) {//待发货
                helper.setText(R.id.goods_type, "等待卖家发货");
                helper.getView(R.id.pending_delivery).setVisibility(View.VISIBLE);
            } else {
                helper.setText(R.id.goods_type, "订单已完成");
                helper.getView(R.id.customer_complete_rl).setVisibility(View.VISIBLE);
            }


        } else if ("0".equals(model.getPay_state())) {//待付款
            if ("1".equals(model.getGq_status())) {//未过期
                helper.getView(R.id.pending_payment).setVisibility(View.VISIBLE);
                helper.getView(R.id.customer_del_lr).setVisibility(View.GONE);
                helper.setText(R.id.goods_type, "等待买家付款");
            } else {
                helper.getView(R.id.pending_payment).setVisibility(View.GONE);
                helper.getView(R.id.customer_del_lr).setVisibility(View.VISIBLE);

                helper.setText(R.id.goods_type, "已过期");
            }


        }

        if ("3".equals(model.getState())) {

        } else {
            helper.setText(R.id.goods_information, "共" + model.getNumber() + "件商品 小计:￥" + model.getOrder_amount() + "(含运费" + model.getDistri_price() + "元)");
        }


        helper.setItemChildClickListener(R.id.order_all_tv_payment);
        helper.setItemChildClickListener(R.id.order_all_tv_cancel_order);
        helper.setItemChildClickListener(R.id.tv_contact_seller);


        helper.setItemChildClickListener(R.id.order_all_tv_deliver);
        helper.setItemChildClickListener(R.id.customer_del);

        helper.setItemChildClickListener(R.id.order_all_tv_confirm);
        helper.setItemChildClickListener(R.id.tv_look_logistics);

        helper.setItemChildClickListener(R.id.order_all_tv_evaluate);
        helper.setItemChildClickListener(R.id.tv_look_logistics_collect);

        helper.setItemChildClickListener(R.id.order_all_tv_details);

        helper.setItemChildClickListener(R.id.tv_contact_seller_details);
        helper.setItemChildClickListener(R.id.customer_del_details);
        helper.setItemChildClickListener(R.id.order_all_tv_deliver_details);
        helper.setItemChildClickListener(R.id.customer_complete);
        helper.setItemChildClickListener(R.id.tv_look_logistics_details);
        helper.setItemChildClickListener(R.id.tv_look_logistics_collect_details);
        helper.setItemChildClickListener(R.id.customer_complete_collect);

    }

    private void setV(ViewHolderHelper helper) {
        helper.setText(R.id.goods_type, "等待买家收货");
        helper.getView(R.id.pending_payment).setVisibility(View.GONE);
        helper.getView(R.id.pending_delivery).setVisibility(View.GONE);
        helper.getView(R.id.goods_receipt).setVisibility(View.GONE);
        helper.getView(R.id.to_be_evaluated).setVisibility(View.GONE);
        helper.getView(R.id.customer_service).setVisibility(View.GONE);
        helper.getView(R.id.customer_del_lr).setVisibility(View.GONE);
        helper.getView(R.id.customer_complete_rl).setVisibility(View.GONE);
    }

}
