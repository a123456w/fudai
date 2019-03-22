package com.ruirong.chefang.adapter;

import android.widget.GridView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.ConfirmationOrderBean;

/**
 * 确认订单
 * Created by BX on 2017/12/29.
 */

public class ConfirmationOrderAdapter extends BaseListAdapter<ConfirmationOrderBean> {
    public ConfirmationOrderAdapter(GridView gridView) {
        super(gridView, R.layout.item_package);
    }

    @Override
    public void fillData(ViewHolder holder, int position, ConfirmationOrderBean model) {

    }
}
