package com.ruirong.chefang.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.FuBackHotelDetailsBean;

/**
 * Created by chenlipeng on 2018/1/2 0002
 * describe:  商家头条第一个选项的适配器
 */

public class ShopHeadlineChoseOneAdapter extends RecyclerViewAdapter<FuBackHotelDetailsBean.HotelsBean> {
    public ShopHeadlineChoseOneAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_hoteldetail_chosetylpeone);
    }


    @Override
    protected void fillData(ViewHolderHelper holder, int position, final FuBackHotelDetailsBean.HotelsBean model) {
/*

        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int heigth = dm.heightPixels;
        int width = dm.widthPixels;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.getView(R.id.lls).getLayoutParams();

        if (getData().size() == 1) {

            layoutParams.width = width - 80;
            holder.getView(R.id.lls).setLayoutParams(layoutParams);

        } else {

            layoutParams.width = width - 160;
            holder.getView(R.id.lls).setLayoutParams(layoutParams);

        }
*/


        GlideUtil.display(mContext, Constants.IMG_HOST + model.getCover(), holder.getImageView(R.id.iv_pic));
        holder.setText(R.id.tv_name, model.getName());
        holder.setText(R.id.tv_presentprice, "￥" + model.getPrice());
        holder.setText(R.id.tv_oldprice, "￥" + model.getYuan_price());
        holder.getTextView(R.id.tv_oldprice).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

    /*    if (model.getSpecif() != null) {
            if (model.getSpecif().size() >= 1) {
                holder.setText(R.id.condition_1, model.getSpecif().get(0));
            }
            if (model.getSpecif().size() >= 2) {
                holder.setText(R.id.condition_2, model.getSpecif().get(1));
            }
            if (model.getSpecif().size() >= 3) {
                holder.setText(R.id.tv_evaluate, model.getSpecif().get(2));
            }
        }
*/

        holder.setItemChildClickListener(R.id.iv_orderonline);
        holder.setItemChildClickListener(R.id.tv_presentprice);
        holder.setItemChildClickListener(R.id.tv_oldprice);
        if (1 == model.getIs_full()) {
            holder.setImageResource(R.id.iv_orderonline, R.drawable.ic_orderonline);
        } else if (0 == model.getIs_full()) {//已满
            holder.setImageResource(R.id.iv_orderonline, R.drawable.iv_yi_orderonline);
        }

    }
}
