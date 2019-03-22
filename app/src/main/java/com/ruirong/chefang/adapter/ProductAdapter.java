package com.ruirong.chefang.adapter;

import android.opengl.Visibility;
import android.view.View;
import android.widget.GridView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.HousedetailsBean;

/**
 * Created by BX on 2018/2/28.
 */

public class ProductAdapter extends BaseListAdapter<HousedetailsBean.ContentBean> {


    public ProductAdapter(GridView gridView) {
        super(gridView, R.layout.item_carsteails);
    }

    @Override
    public void fillData(ViewHolder holder, int position, HousedetailsBean.ContentBean model) {
//        if(a%2 == 1){   //是奇数
//            return true;
//        }
//        return false;


        if(position%2 != 1){//是奇数
            holder.setVisibility(R.id.ll_top, View.VISIBLE);
            holder.setVisibility(R.id.ll_bottom, View.GONE);

            holder.setText(R.id.tv_title, model.getKey());
            holder.setText(R.id.tv_content, model.getValue());
        } else {

            holder.setVisibility(R.id.ll_top, View.GONE);
            holder.setVisibility(R.id.ll_bottom, View.VISIBLE);

            holder.setText(R.id.tv_title1, model.getKey());
            holder.setText(R.id.tv_content1, model.getValue());
        }

    }
}
