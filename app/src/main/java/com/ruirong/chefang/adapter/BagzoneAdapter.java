package com.ruirong.chefang.adapter;

import android.widget.GridView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.HomePageBean;

/**
 * 首页福袋专区适配器
 * Created by dillon on 2017/12/26.
 */

public class BagzoneAdapter extends BaseListAdapter<HomePageBean.FudaiBean> {
    public BagzoneAdapter(GridView gridView) {
        super(gridView, R.layout.item_eachchild);
    }

    @Override
    public void fillData(ViewHolder holder, int position, HomePageBean.FudaiBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getCover(), holder.getImageView(R.id.iv_pic));
        holder.setText(R.id.tv_name, model.getTitle());
        holder.setText(R.id.tv_price, model.getNow_price());
    }
}
