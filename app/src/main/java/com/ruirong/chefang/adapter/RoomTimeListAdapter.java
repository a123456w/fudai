package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.LuckBackRoomListviewBean;
import com.ruirong.chefang.bean.RoomTimeBean;

/**
 * Created by chenlipeng on 2017/12/28 0028
 * describe:  我的模块    房时
 */

public class RoomTimeListAdapter extends RecyclerViewAdapter<RoomTimeBean> {
    public RoomTimeListAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_room_time_listview);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, RoomTimeBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getLogo(),viewHolderHelper.getImageView(R.id.iv_logo));
        viewHolderHelper.setText(R.id.tv_shop_name,model.getShop_name());
        viewHolderHelper.setText(R.id.tv_sumtime,model.getSumtimes()+"小时");
        ProgressBar view = (ProgressBar) viewHolderHelper.getView(R.id.progress_bar);
        view.setMax(Integer.parseInt(model.getDui_time()));
        view.setProgress(Integer.parseInt(model.getSumtimes()));

        viewHolderHelper.setItemChildClickListener(R.id.tv_exchange);

    }
}
