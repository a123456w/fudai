package com.ruirong.chefang.adapter;

import android.widget.GridView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.AllBussinessBean;
import com.ruirong.chefang.bean.AllIndustriesBean;

/**
 * Created by chenlipeng on 2018/1/3 0003
 * describe:  商城中  全部行业页面的适配器
 */
public class AllBussinessGridviewAdapter extends BaseListAdapter<AllIndustriesBean> {
    public AllBussinessGridviewAdapter(GridView gridView) {
        super(gridView, R.layout.item_function);
    }

    @Override
    public void fillData(ViewHolder holder, int position, AllIndustriesBean model) {
        holder.setText(R.id.tv_name, model.getName());
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getPic(), holder.getImageView(R.id.iv_pic));
    }
}
