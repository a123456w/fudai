package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.AddressItemBean;

/**
 * Created by guo on 2017/5/5.
 */

public class SelectAddressAdapter extends RecyclerViewAdapter<AddressItemBean> {

    public String selectedAddressId;
    public int selectedPosition;

    public SelectAddressAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_select_address);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, AddressItemBean model) {
        viewHolderHelper.setText(R.id.tv_name,model.getRec_name());
        viewHolderHelper.setText(R.id.tv_phone,model.getMobile());
        viewHolderHelper.setText(R.id.tv_address,model.getAddress());
        if (model.getId().equals(selectedAddressId)){
            viewHolderHelper.setVisibility(R.id.iv_selected_mark,View.VISIBLE);
            selectedPosition=position;
        }else {
            viewHolderHelper.setVisibility(R.id.iv_selected_mark,View.INVISIBLE);
        }

        viewHolderHelper.setItemChildClickListener(R.id.iv_modify);
    }
}
