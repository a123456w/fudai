package com.ruirong.chefang.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.LuckyBagBean;

/**
 * Created by 16690 on 2018/4/3.
 * describe:
 */

public class LuckyBagListAdapter extends RecyclerViewAdapter<LuckyBagBean> {

    public LuckyBagListAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_luckybag_order_list);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, LuckyBagBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getCover(),helper.getImageView(R.id.goods_pic));
        helper.setText(R.id.goods_name,model.getTitle());
        helper.setText(R.id.goods_content,model.getAttr());
        helper.setText(R.id.goods_price,model.getNow_price());
        helper.setText(R.id.goods_old_price,"￥"+model.getBefore_price());
        helper.setText(R.id.goods_count,"x"+model.getNumber());
        helper.setText(R.id.goods_information,"共"+model.getNumber()+"件商品  小计:￥"+model.getOrder_amount());
        helper.getTextView(R.id.goods_old_price).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        setVisible(helper);

        if ("0".equals(model.getPay_state())){
            //未支付
            if("1".equals(model.getGq_status())){
                //未过期
                helper.setText(R.id.goods_type,"等待买家付款");
                helper.getView(R.id.goods_rl).setVisibility(View.VISIBLE);
            }else if ("0".equals(model.getGq_status())){
                //过期
                helper.setText(R.id.goods_type,"已过期");
                helper.setText(R.id.tv_del,"删除订单");
                helper.getTextView(R.id.goods_address).setVisibility(View.GONE);
                helper.getView(R.id.customer_del_lr).setVisibility(View.VISIBLE);
            }
        }else {
            //已支付
            if ("0".equals(model.getState())){
                //待发货
                helper.setText(R.id.goods_type,"待发货");
                helper.setText(R.id.goods_hint,"提醒发货");
                helper.getView(R.id.customer_hint_lr).setVisibility(View.VISIBLE);
            }else if ("1".equals(model.getState())){
                //待收货
                helper.setText(R.id.goods_type,"待收货");
                helper.setText(R.id.tv_del,"确认收货");
                helper.getTextView(R.id.goods_address).setVisibility(View.VISIBLE);
                helper.getView(R.id.customer_del_lr).setVisibility(View.VISIBLE);
            }else if ("2".equals(model.getState())){
                //待评价
                helper.setText(R.id.goods_type,"待评价");
                helper.setText(R.id.tv_del,"评价");
                helper.getTextView(R.id.goods_address).setVisibility(View.VISIBLE);
                helper.getView(R.id.customer_del_lr).setVisibility(View.VISIBLE);
            }else if ("3".equals(model.getState())){
                //售后
                helper.setText(R.id.goods_type,"售后");
                helper.setText(R.id.goods_hint,"查看详情");
                helper.getView(R.id.customer_hint_lr).setVisibility(View.VISIBLE);
            }else if ("4".equals(model.getState())){
                //已评价
                helper.setText(R.id.goods_type,"已评价");
                helper.setText(R.id.goods_hint,"查看详情");
                helper.getView(R.id.customer_hint_lr).setVisibility(View.VISIBLE);
            }
        }



        helper.setItemChildClickListener(R.id.tv_pay);
        helper.setItemChildClickListener(R.id.goods_cancel);
        helper.setItemChildClickListener(R.id.tv_contact_seller);

        helper.setItemChildClickListener(R.id.tv_del);
        helper.setItemChildClickListener(R.id.goods_address);

        helper.setItemChildClickListener(R.id.goods_hint);

    }

    /**
     * 隐藏下方按钮
     * @param helper
     */
    private void setVisible(ViewHolderHelper helper){
        helper.getView(R.id.goods_rl).setVisibility(View.GONE);
        helper.getView(R.id.customer_del_lr).setVisibility(View.GONE);
        helper.getView(R.id.customer_hint_lr).setVisibility(View.GONE);
    }
}
