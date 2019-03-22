package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.CarHouseBean;

/**
 * Created by chenlipeng on 2017/12/26 0026
   describe:  车房水平的recycleview的适配器
 */

public class CarAndRoomsLevelAdapter extends RecyclerViewAdapter<CarHouseBean.CatesBean> {
    public CarAndRoomsLevelAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.carandroons_level_recycleview_item_function );
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, CarHouseBean.CatesBean model) {
        viewHolderHelper.setText(R.id.tv_name,model.getName());
        if(model.isFile()){
            try{
                GlideUtil.display(mContext,Integer.parseInt(model.getPic()),viewHolderHelper.getImageView(R.id.iv_pic));
            }catch (Exception e){
                e.printStackTrace();
            }

        }else {
            GlideUtil.displayAvatar(mContext, Constants.IMG_HOST+model.getPic(),viewHolderHelper.getImageView(R.id.iv_pic));
        }
    }
}
