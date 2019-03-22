package com.ruirong.chefang.adapter;

import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.CarHouseBean;

/**
 * Created by xuxx on 2017/12/26 0026
   describe:  吃住玩
 */
public class LifeIetmAdapter extends BaseListAdapter<CarHouseBean.FangchanBean> {
    public LifeIetmAdapter(ListView listView) {
        super(listView, R.layout.life_item_layout);
    }

    @Override
    public void fillData(ViewHolder holder, int position, CarHouseBean.FangchanBean model) {
        holder.setText(R.id.tv_title,model.getSp_name());
        holder.setText(R.id.tv_address,model.getAddress1()+" "+model.getAddress2());
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getCover(),holder.getImageView(R.id.iv_pic));
    }
}
