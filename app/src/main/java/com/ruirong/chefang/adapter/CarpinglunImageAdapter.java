package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;


/**
 * Created by BX on 2018/2/28.
 */

public class CarpinglunImageAdapter extends RecyclerViewAdapter<String> {
    public CarpinglunImageAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_carpingluninage);
    }


    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, final String model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model, viewHolderHelper.getImageView(R.id.tv_content));
        viewHolderHelper.getImageView(R.id.tv_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(BGAPhotoPreviewActivity.newIntent(mContext,
                        null, Constants.IMG_HOST + model));
            }
        });
    }
}
