package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.HousePropertyBean;
import com.ruirong.chefang.bean.LuckBackRoomListviewBean;

/**
 * Created by chenlipeng on 2017/12/28 0028
 * describe:  福返房产
 */

public class LuckBackRoomAdapter extends RecyclerViewAdapter<HousePropertyBean.ListBean> {
    public LuckBackRoomAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_luckback_room);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, HousePropertyBean.ListBean model) {

        GlideUtil.display(mContext, Constants.IMG_HOST + model.getPic(), helper.getImageView(R.id.house_pic));
        helper.setText(R.id.house_title, model.getSp_name());
        helper.setText(R.id.house_region, model.getSp_address());
        helper.setText(R.id.region_content, model.getContent());

    }
}
