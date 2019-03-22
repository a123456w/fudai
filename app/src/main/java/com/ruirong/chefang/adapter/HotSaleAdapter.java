package com.ruirong.chefang.adapter;

import android.text.TextUtils;
import android.widget.ListView;
import android.widget.RatingBar;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.HomePageBean;


/**
 * 热销产品适配器
 * Created by dillon on 2017/12/26.
 */

public class HotSaleAdapter extends BaseListAdapter<HomePageBean.RexiaoBean> {
    public HotSaleAdapter(ListView listView) {
        super(listView, R.layout.item_selling);
    }

    @Override
    public void fillData(ViewHolder holder, int position, HomePageBean.RexiaoBean model) {
        holder.setText(R.id.tv_title, model.getName());
        RatingBar ratingBar = holder.getView(R.id.rb_grade);
        if ((!TextUtils.isEmpty(model.getPingfen())) && (model.getPingfen() != null)) {
            ratingBar.setRating(Float.parseFloat(model.getPingfen()));
        }
//        holder.setText(R.id.tv_price, "￥" + model.getPrice());
        holder.setText(R.id.tv_content, "￥" + model.getPrice());
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getCover(), holder.getImageView(R.id.iv_pic));

    }
}
