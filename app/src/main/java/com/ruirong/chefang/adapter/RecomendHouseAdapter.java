package com.ruirong.chefang.adapter;

import android.widget.GridView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.HomePageBean;

/**
 * 推荐店铺适配器
 * Created by dillon on 2017/12/26.
 */

public class RecomendHouseAdapter extends BaseListAdapter<HomePageBean.FangchanBean> {
    public RecomendHouseAdapter(GridView gridView) {
        super(gridView, R.layout.item_house);
    }

    @Override
    public void fillData(ViewHolder holder, int position, HomePageBean.FangchanBean model) {
        holder.setText(R.id.tv_title, model.getSp_name());
        holder.setText(R.id.tv_city, model.getAddress1());
        holder.setText(R.id.tv_area, model.getAddress2());
        holder.setText(R.id.tv_address, model.getTitle());
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getCover(),holder.getImageView(R.id.iv_pic));

    }
}
