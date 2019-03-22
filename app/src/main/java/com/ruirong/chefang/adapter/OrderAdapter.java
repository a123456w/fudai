package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.OrderBean;

/**
 * Created by dillon on 2017/4/5.
 */

public class OrderAdapter extends RecyclerViewAdapter<OrderBean> {
  /*  public Set<OrderBean> selectList = new HashSet<>();
    public boolean edit;*/

    public OrderAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_exchange_record_listview);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, OrderBean model) {
   /*     viewHolderHelper.setVisibility(R.id.check_select, edit ? View.VISIBLE : View.GONE);
        if (edit) {
            viewHolderHelper.setChecked(R.id.check_select, selectList.contains(model));
        }*/
      //  viewHolderHelper.setText(R.id.tv_data, DateUtil.toDate(model.getCreate_time(), DateUtil.FORMAT_YMDHM));
        viewHolderHelper.setText(R.id.tv_cash, model.getMoney());
        viewHolderHelper.setText(R.id.tv_type,model.getTypeName());

    }
}
