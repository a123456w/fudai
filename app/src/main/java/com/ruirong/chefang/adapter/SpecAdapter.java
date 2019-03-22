package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;

/*
 *  Created by 李  on 2018/11/19.
 */
public class SpecAdapter extends RecyclerViewAdapter<SpecBean>{

    public SpecAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_spec);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, SpecBean model) {
        TextView tv_name = helper.getTextView(R.id.tv_name);
        View v = helper.getView(R.id.v_lin);
        tv_name.setText(model.getName());
        if (model.isSelect()){
            v.setBackground(mContext.getResources().getDrawable(R.color.text_color_spec1));
            tv_name.setTextColor(mContext.getResources().getColor(R.color.text_color_spec1));
         }else {
            v.setBackground(mContext.getResources().getDrawable(R.color.line1));
            tv_name.setTextColor(mContext.getResources().getColor(R.color.text_color_spec));
        }
    }


}
