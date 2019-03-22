package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.DateUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.TimeUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.LuckBackRoomListviewBean;
import com.ruirong.chefang.bean.StoredRecordsBean;

import static com.qlzx.mylibrary.util.DateUtil.FORMAT_YMD;

/**
 * Created by chenlipeng on 2017/12/28 0028
 * describe:  我的模块    我的储值
 */

public class MyStoredValueListAdapter extends RecyclerViewAdapter<StoredRecordsBean> {
    public MyStoredValueListAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_my_storedvalue_listview);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, StoredRecordsBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getLogo(), viewHolderHelper.getImageView(R.id.iv_icon));
        viewHolderHelper.setText(R.id.tv_shop_name, model.getShop_name());
        viewHolderHelper.setText(R.id.tv_stored_money, "￥" + model.getActualPrice());
        viewHolderHelper.setText(R.id.tv_true_money, "￥" + model.getPrice());

    }
}
