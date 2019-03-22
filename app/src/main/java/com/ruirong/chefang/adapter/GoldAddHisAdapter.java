package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.util.DateUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.GoldAddDetailBean;


/**金豆增值记录
 * Created by dillon on 2017/9/10.
 */

public class GoldAddHisAdapter extends RecyclerViewAdapter<GoldAddDetailBean.DetailBean> {
    public GoldAddHisAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_goldaddhis_list);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, GoldAddDetailBean.DetailBean model) {
        viewHolderHelper.setText(R.id.tv_date, DateUtil.toDate( model.getCreate_time(), DateUtil.FORMAT_YMDHM));
        viewHolderHelper.setText(R.id.tv_type, "第"+model.getTime()+"天收益");
        viewHolderHelper.setText(R.id.tv_moneyhong, model.getTotal());
    }
}
