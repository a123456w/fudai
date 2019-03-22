package com.ruirong.chefang.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
 * describe: 商城订单  全部
 */

public class ShopOrderWaitReceiveAdapter extends RecyclerViewAdapter<ShopOrderBean> {
    private ShopOrderGoodsListAdapter shopOrderGoodsListAdapter;

    public ShopOrderWaitReceiveAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_my_shop_order_wait_reciive);
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
                intent.putExtra("number_bh", getItem(position).getOrder_id());
                mContext.startActivity(intent);
            }
        });


        helper.setText(R.id.tv_shop_name, model.getShop_name());
        if ("1".equals(model.getGoods_type())){
            //商品
            if ("1".equals(model.getIs_hx())){     // 1的时候点餐核销，2的时候配送
                helper.setText(R.id.goods_information, "共" + model.getGoodsnum() + "件商品 小计:￥" + model.getShi_money());
            }else {
                helper.setText(R.id.goods_information, "共" + model.getGoodsnum() + "件商品 小计:￥" + model.getShi_money() + "(含配送费"+model.getDistri_price()+"元)");
            }

        }else {
            //套餐
            helper.setText(R.id.goods_information, "小计:￥" + model.getShi_money());

        }

        if (!TextUtils.isEmpty(model.getShop_status())) {
            if ("1".equals(model.getStatus())) {
                //未支付
                if ("1".equals(model.getGq_status())) {//未过期
                    helper.setText(R.id.tv_state, "等待买家付款");
                    helper.getTextView(R.id.tv_three).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_three, "付款");
                    helper.getTextView(R.id.tv_two).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_two, "取消订单");
                    helper.getTextView(R.id.tv_one).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_one, "联系卖家");

                } else {
                    helper.setText(R.id.tv_state, "已过期");
                    helper.getTextView(R.id.tv_one).setVisibility(View.GONE);
                    helper.getTextView(R.id.tv_two).setVisibility(View.GONE);
                    helper.getTextView(R.id.tv_three).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_three, "删除订单");
                }
            } else {
                //已支付

                helper.getTextView(R.id.tv_one).setVisibility(View.GONE);
                helper.getTextView(R.id.tv_two).setVisibility(View.GONE);
                helper.getTextView(R.id.tv_three).setVisibility(View.GONE);

                if ("0".equals(model.getCancel())) {
                    //未退款

                    if ("5".equals(model.getShop_status())) {
                        helper.getTextView(R.id.tv_three).setVisibility(View.VISIBLE);
                        if ("0".equals(model.getComment_state())){
                            helper.setText(R.id.tv_state, "待评价");
                            helper.setText(R.id.tv_three, "评价");
                        }else {
                            helper.setText(R.id.tv_state, "已评价");
                            helper.setText(R.id.tv_three, "查看详情");
                        }

                    } else if ("0".equals(model.getShop_status())){
                        helper.getTextView(R.id.tv_three).setVisibility(View.VISIBLE);
                        if ("1".equals(model.getGoods_type())){
                            //商品
                            helper.setText(R.id.tv_state, "待接单");
                            helper.setText(R.id.tv_three, "查看详情");
                        }else {
                            //套餐
                            helper.setText(R.id.tv_state, "使用中");
                            helper.setText(R.id.tv_three, "查看详情");
                        }

                    } else if ("1".equals(model.getShop_status())){
                        helper.getTextView(R.id.tv_three).setVisibility(View.VISIBLE);
                        if ("1".equals(model.getGoods_type())){

                            if ("1".equals(model.getIs_hx())){
                                helper.setText(R.id.tv_three, "去核销");
                            }else {
                                helper.setText(R.id.tv_three, "提醒配送");
                            }
                            helper.setText(R.id.tv_state, "已接单");

                        }else {
                            helper.setText(R.id.tv_state, "使用中");
                            helper.setText(R.id.tv_three, "查看详情");
                        }
                    }else if ("2".equals(model.getShop_status())){
                        helper.getTextView(R.id.tv_three).setVisibility(View.VISIBLE);
                        if ("1".equals(model.getGoods_type())){
                            helper.setText(R.id.tv_state, "配送中");
                            helper.setText(R.id.tv_three, "确认订单");
                        }else {
                            helper.setText(R.id.tv_state, "使用中");
                            helper.setText(R.id.tv_three, "确认订单");
                        }

                    }else if ("3".equals(model.getShop_status())){
                        helper.getTextView(R.id.tv_three).setVisibility(View.VISIBLE);
                        if ("1".equals(model.getGoods_type())){
                            helper.setText(R.id.tv_state, "已送达");
                            helper.setText(R.id.tv_three, "确认订单");
                        }else {
                            helper.setText(R.id.tv_state, "使用中");
                            helper.setText(R.id.tv_three, "确认订单");
                        }
                    }else {
                        helper.getTextView(R.id.tv_three).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tv_state, "已取消");
                        helper.setText(R.id.tv_three, "删除订单");
                    }
                } else {
                    //售后
                    helper.getTextView(R.id.tv_three).setVisibility(View.VISIBLE);
                    helper.setText(R.id.tv_three, "查看详情");
                    if ("1".equals(model.getCancel())) {
                        helper.setText(R.id.tv_state, "退款申请");
                    } else if ("2".equals(model.getCancel())) {
                        helper.setText(R.id.tv_state, "同意退款");
                    } else if ("3".equals(model.getCancel())) {
                        helper.setText(R.id.tv_state, "拒绝退款");
                    } else {
                        helper.setText(R.id.tv_state, "售后");
                    }

                }


            }

        } else {
            helper.getTextView(R.id.tv_one).setVisibility(View.GONE);
            helper.getTextView(R.id.tv_two).setVisibility(View.GONE);
            helper.getTextView(R.id.tv_three).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_three, "查看详情");
        }

        helper.setItemChildClickListener(R.id.tv_one);
        helper.setItemChildClickListener(R.id.tv_two);
        helper.setItemChildClickListener(R.id.tv_three);

    }
}
