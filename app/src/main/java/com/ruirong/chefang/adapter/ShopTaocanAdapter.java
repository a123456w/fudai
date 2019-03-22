package com.ruirong.chefang.adapter;

import android.graphics.Paint;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.FuBackHotelDetailsBean;

/** 店铺套餐
 * Created by dillon on 2018/5/11.
 */

public class ShopTaocanAdapter extends BaseListAdapter<FuBackHotelDetailsBean.PackageBean> {
    public ShopTaocanAdapter(ListView listView) {
        super(listView, R.layout.item_shopreserve);
    }

    @Override
    public void fillData(ViewHolder helper, int position, FuBackHotelDetailsBean.PackageBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getPic(), helper.getImageView(R.id.shop_pic));
        helper.setText(R.id.shop_name, model.getName());
        helper.setText(R.id.new_price, "￥" + model.getAll_price());
        helper.setText(R.id.who_price, "￥" + model.getShop_price());
        helper.getTextView(R.id.who_price).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

    }
}
