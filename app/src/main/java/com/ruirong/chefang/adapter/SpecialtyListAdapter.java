package com.ruirong.chefang.adapter;


import android.graphics.Paint;
import android.widget.GridView;


import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.SpecialListListviewBean;


/**
 * Created by chenlipeng on 2017/12/28 0028
 * describe:  特产列表的适配器
 */
public class SpecialtyListAdapter extends BaseListAdapter<SpecialListListviewBean.ListBean> {
    public SpecialtyListAdapter(GridView gridView) {
        super(gridView, R.layout.specialty_list_gridview_item1);
    }

    @Override
    public void fillData(ViewHolder holder, int position, SpecialListListviewBean.ListBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getPic(), holder.getImageView(R.id.image));
        holder.setText(R.id.tv_name, model.getName());
        holder.setText(R.id.tv_now_price, "￥" + model.getNow_price());
        //添加删除线
        holder.getTextView(R.id.tv_oldprice).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.setText(R.id.tv_oldprice, "￥" + model.getBefore_price());
        holder.setText(R.id.tv_xiaoliang, "销量" + model.getXnum());
    }

}
