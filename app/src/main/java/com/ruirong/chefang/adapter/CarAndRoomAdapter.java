package com.ruirong.chefang.adapter;

import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.CarAndRoomsBean;

/**
 * Created by chenlipeng on 2017/12/26 0026
   describe:  车房的适配器
 */
public class CarAndRoomAdapter extends BaseListAdapter<CarAndRoomsBean.GoodsBean> {
    public CarAndRoomAdapter(ListView listView) {
        super(listView, R.layout.item_dynamic);
    }

    @Override
    public void fillData(ViewHolder holder, int position, CarAndRoomsBean.GoodsBean model) {
        holder.setText(R.id.tv_title,model.getTitle());
        holder.setText(R.id.tv_address,model.getCanshu());
        holder.setText(R.id.tv_content,model.getDesc());
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getPics(),holder.getImageView(R.id.iv_pic));
    }
}
