package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.ShopCategoryBean;

/**
 * Created by guo on 2017/5/3.
 */

public class SelectShopCategoryAdapter extends RecyclerViewAdapter<ShopCategoryBean> {
    public int selectedPosition=-1;
    public SelectShopCategoryAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_select_shop_category);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, final int position, ShopCategoryBean model) {
        viewHolderHelper.setText(R.id.tv_name,model.getName());
        viewHolderHelper.getTextView(R.id.tv_name).setSelected(position==selectedPosition);
        viewHolderHelper.setVisibility(R.id.iv_label,position==selectedPosition? View.VISIBLE:View.GONE);
        viewHolderHelper.getView(R.id.rl_item_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position!=selectedPosition){
                    selectedPosition=position;
                    notifyDataSetChanged();
                }
            }
        });
    }
}
