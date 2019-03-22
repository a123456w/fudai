package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.CarHouseBean;

/**
 * Created by xuxx on 2017/12/26 0026
   describe:  吃住玩
 */
public class GoodTitleIetmAdapter extends RecyclerViewAdapter<CarHouseBean.FangchanBean> {
    public GoodTitleIetmAdapter(RecyclerView listView) {
        super(listView, R.layout.good_item_layout);
    }



    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, CarHouseBean.FangchanBean model) {
        viewHolderHelper.setText(R.id.tv_text,model.getSp_name());
        //viewHolderHelper.setText(R.id.tv_address,model.getAddress1()+" "+model.getAddress2());
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getCover(),viewHolderHelper.getImageView(R.id.iv_image));
    }
}
