package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.ScreenItem;

/**
 *
 *  Created by 李  on 2018/11/20.
 */
class ScreenItemAdapter extends RecyclerViewAdapter<ScreenItem> {
    public int getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(int lastPosition) {
        this.lastPosition = lastPosition;
    }

    private int lastPosition=-1;
    public ScreenItemAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_adapter_screen);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, ScreenItem model) {
        helper.setText(R.id.tv_name,model.getName());
        switch (model.getIsSelect()){
            case 0://未选择
                helper.getTextView(R.id.tv_name).setBackground(mContext.getResources().getDrawable(R.drawable.bg_search_item));
                helper.getTextView(R.id.tv_name).setTextColor(mContext.getResources().getColor(R.color.color_333333));
                break;
            case 1://已选择
                helper.getTextView(R.id.tv_name).setBackground(mContext.getResources().getDrawable(R.drawable.bg_search_item_is_select));
                break;
            case 2://没有选中状态
                helper.getTextView(R.id.tv_name).setBackground(mContext.getResources().getDrawable(R.drawable.bg_search_item_is_no_select));
                helper.getTextView(R.id.tv_name).setTextColor(mContext.getResources().getColor(R.color.color_999999));
                break;
        }
    }

}
