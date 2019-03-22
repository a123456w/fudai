package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.FuBackHotelBean;

/**
 * Created by chenlipeng on 2017/12/28 0028
 * describe:  福返酒店
 */

public class LuckBackHotelAdapter extends RecyclerViewAdapter<FuBackHotelBean.ListBean> {
    public LuckBackHotelAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_luckback_hotel);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, FuBackHotelBean.ListBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getCover(), helper.getImageView(R.id.fuback_pic));
        helper.setText(R.id.fuback_name, model.getSp_name());
        helper.setText(R.id.fuback_distance, "(距离" + model.getDistances() + "km)");
        helper.setText(R.id.fuback_score, model.getDp_grade()+"分");
        helper.setText(R.id.fuback_evaluate, model.getDp_num() + "条点评");
        helper.setText(R.id.fuback_price, "￥" + model.getQ_price() + "起");


    }
}
