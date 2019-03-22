package com.ruirong.chefang.adapter;

import android.view.View;
import android.widget.GridView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;

/**
 * Created by Administrator on 2018/3/1.
 */

public class EvaluateListAdapter extends BaseListAdapter<String> {
    public EvaluateListAdapter(GridView gridView) {
        super(gridView, R.layout.item_evaluate_list);
    }

    @Override
    public void fillData(ViewHolder holder, int position, final String model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model, holder.getImageView(R.id.evaluate_pic));
        holder.getImageView(R.id.evaluate_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(BGAPhotoPreviewActivity.newIntent(mContext,
                        null, Constants.IMG_HOST + model));
            }
        });
    }
}
