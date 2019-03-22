package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.LuckBackRoomListviewBean;
import com.ruirong.chefang.bean.MyCollectionBean;

import java.text.DecimalFormat;

/**
 * Created by chenlipeng on 2017/12/28 0028
 * describe: w我的收藏适配器
 */

public class MyCollectionAdapter extends RecyclerViewAdapter<MyCollectionBean.ListBean> {
    private boolean canEdit = false;
    public MyCollectionAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_my_collection);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, MyCollectionBean.ListBean model) {
//
        if (canEdit){
            viewHolderHelper.getImageView(R.id.iv_check).setVisibility(View.VISIBLE);
            viewHolderHelper.getImageView(R.id.iv_check).setSelected(model.isChecked());
        }else {
            viewHolderHelper.getImageView(R.id.iv_check).setVisibility(View.GONE);
            model.setChecked(false);
        }
        DecimalFormat df = new DecimalFormat("#0.0");
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getPic(),viewHolderHelper.getImageView(R.id.iv_pic));
        viewHolderHelper.setText(R.id.tv_name,model.getName());
        viewHolderHelper.setText(R.id.tv_fenshu,df.format(model.getFenshu())+"分");
        viewHolderHelper.setText(R.id.tv_money,model.getMoney()+"元起");
        ((RatingBar)viewHolderHelper.getView(R.id.rb_grade)).setRating((float) model.getFenshu());

        viewHolderHelper.setItemChildClickListener(R.id.iv_check);

    }

    public void setCanEdit(boolean b){
        this.canEdit = b;
    }

    public boolean getCanEdit(){
        return canEdit;
    }

}
