package com.ruirong.chefang.adapter;

import android.graphics.Paint;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.ComboBean;

/**
 * Created by BX on 2018/2/26.
 */

public class ShopReserveAdapter extends BaseListAdapter<ComboBean> {
    public ShopReserveAdapter(ListView listView) {
        super(listView, R.layout.item_shopreserve);
    }

/*

    @Override
    protected void fillData(ViewHolderHelper helper, int position, final ComboBean model) {


        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int heigth = dm.heightPixels;
        int width = dm.widthPixels;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) helper.getView(R.id.lls).getLayoutParams();

        if (getData().size() == 1) {

            layoutParams.width = width - 80;
            helper.getView(R.id.lls).setLayoutParams(layoutParams);

        }else{

            layoutParams.width = width - 160;
            helper.getView(R.id.lls).setLayoutParams(layoutParams);

        }
*//*
*/
/*


        GlideUtil.display(mContext, Constants.IMG_HOST + model.getPic(), helper.getImageView(R.id.shop_pic));
        helper.setText(R.id.shop_name, model.getName());
        helper.setText(R.id.new_price, "￥" + model.getAll_price());
        helper.setText(R.id.who_price, "￥" + model.getShop_price());
        helper.getTextView(R.id.who_price).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


        helper.setItemChildClickListener(R.id.tv_reserve);

    }

*/

    @Override
    public void fillData(ViewHolder helper, int position, ComboBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getPic(), helper.getImageView(R.id.shop_pic));
        helper.setText(R.id.shop_name, model.getName());
        helper.setText(R.id.new_price, "￥" + model.getAll_price());
        helper.setText(R.id.who_price, "￥" + model.getShop_price());
        helper.getTextView(R.id.who_price).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


       // helper.setItemChildClickListener(R.id.tv_reserve);

    }
}
