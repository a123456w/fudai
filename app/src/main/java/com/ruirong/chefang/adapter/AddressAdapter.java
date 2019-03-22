package com.ruirong.chefang.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.AddressBean;
import com.ruirong.chefang.bean.AddressItemBean;

/**
 * 我的地址
 * Created by BX on 2017/12/29.
 */

//public class AddressAdapter extends RecyclerViewAdapter<AddressBean> {
public class AddressAdapter extends RecyclerViewAdapter<AddressItemBean> {
    public AddressAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_address);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, AddressItemBean model) {
        helper.setText(R.id.address_name, model.getRec_name());
        helper.setText(R.id.address_phone, model.getMobile());
        helper.setText(R.id.address_content, model.getAddress());

        if ("0".equals(model.getIs_defa())) {
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.check);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            helper.getTextView(R.id.tv_address_default).setCompoundDrawables(drawable, null, null, null);
        } else if ("1".equals(model.getIs_defa())) {
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.checked);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            helper.getTextView(R.id.tv_address_default).setCompoundDrawables(drawable, null, null, null);
        }

        helper.setItemChildClickListener(R.id.tv_address_default);
        helper.setItemChildClickListener(R.id.tv_edit);
        helper.setItemChildClickListener(R.id.tv_delete);


    }
}
