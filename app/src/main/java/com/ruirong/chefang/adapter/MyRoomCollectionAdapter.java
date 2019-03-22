package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.MyRoomCollectionBean;

import java.text.DecimalFormat;

/**
 * Created by 16690 on 2018/3/26.
 */

public class MyRoomCollectionAdapter extends RecyclerViewAdapter<MyRoomCollectionBean.ListBean> {
    private boolean canEdit = false;
    public MyRoomCollectionAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_my_room_collection);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, MyRoomCollectionBean.ListBean model) {

        if (canEdit){
            viewHolderHelper.getImageView(R.id.iv_check).setVisibility(View.VISIBLE);
            viewHolderHelper.getImageView(R.id.iv_check).setSelected(model.isChecked());
        }else {
            viewHolderHelper.getImageView(R.id.iv_check).setVisibility(View.GONE);
            model.setChecked(false);
        }
        DecimalFormat df = new DecimalFormat("#0.0");
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getPic(),viewHolderHelper.getImageView(R.id.iv_pic));
        viewHolderHelper.setText(R.id.tv_name,model.getSp_name());
        viewHolderHelper.setText(R.id.tv_fenshu,model.getDp_grade()+"分");
        viewHolderHelper.setText(R.id.tv_address,model.getSp_address());
        viewHolderHelper.setText(R.id.tv_distances,model.getDistance()+"km");
        ((RatingBar)viewHolderHelper.getView(R.id.rb_grade)).setRating(Float.parseFloat(model.getDp_grade()));
        viewHolderHelper.setItemChildClickListener(R.id.iv_check);
    }


    public void setCanEdit(boolean b){
        this.canEdit = b;
    }

    public boolean getCanEdit(){
        return canEdit;
    }
}
