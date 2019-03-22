package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.DateUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.AlreadyExchangeBean;

/**
 * Created by chenlipeng on 2017/12/28 0028
   describe:  我的模块    已兑换
 */

public class AlreadyExchangeListAdapter extends RecyclerViewAdapter<AlreadyExchangeBean> {
    public AlreadyExchangeListAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_yet_exchange_listview );
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, AlreadyExchangeBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getCover(),viewHolderHelper.getImageView(R.id.iv_cover));
        viewHolderHelper.setText(R.id.tv_shop_name,model.getSp_name());
        viewHolderHelper.setText(R.id.tv_creat_time, DateUtil.toDate(model.getCreate_time(), DateUtil.FORMAT_YMD));
    }
}
