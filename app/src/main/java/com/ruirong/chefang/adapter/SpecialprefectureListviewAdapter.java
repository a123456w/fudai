package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.SpecialPrefectureListviewBean;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by chenlipeng on 2017/12/28 0028
 * describe:  特产专区
 */

public class SpecialprefectureListviewAdapter extends RecyclerViewAdapter<SpecialPrefectureListviewBean> {
    public SpecialprefectureListviewAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_special_prefecture);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, SpecialPrefectureListviewBean model) {

//        //原图处理成圆角，如果是四周都是圆角则是RoundedCornersTransformation.CornerType.ALL
//        Glide.with(mContext).load(Constants.IMG_HOST + model.getPic())
//                .bitmapTransform(new RoundedCornersTransformation(mContext, 20, 0, RoundedCornersTransformation.CornerType.ALL))
//                .crossFade(1000).into(viewHolderHelper.getImageView(R.id.iv_pic));

        //如果是四周已经是圆角则RoundedCornersTransformation.CornerType.ALL
        Glide.with(mContext)
                .load(Constants.IMG_HOST + model.getPic())
                .placeholder(com.qlzx.mylibrary.R.drawable.ic_placeload)
                .error(com.qlzx.mylibrary.R.drawable.ic_load_error)
                .bitmapTransform(new RoundedCornersTransformation(mContext, 8, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(viewHolderHelper.getImageView(R.id.iv_pic));


        viewHolderHelper.setText(R.id.goods_name, model.getNames());
    }
}
