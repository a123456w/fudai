package com.ruirong.chefang.adapter;

import android.view.View;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.RankingBean;

/**
 * 排行榜
 * Created by BX on 2018/2/26.
 */

public class RankingAdapter extends BaseListAdapter<RankingBean.ListBean> {
    public RankingAdapter(ListView listView) {
        super(listView, R.layout.item_ranking);
    }

    @Override
    public void fillData(ViewHolder holder, int position, RankingBean.ListBean model) {

        holder.setText(R.id.ranking_name, "NO." + (position + 1));

        if (position == 0) {
            holder.setImageResource(R.id.ranking_name_pic, R.drawable.first_one);
        } else if (position == 1) {
            holder.setImageResource(R.id.ranking_name_pic, R.drawable.second_tow);
        } else if (position == 2) {
            holder.setImageResource(R.id.ranking_name_pic, R.drawable.third_three);
        } else {
            holder.setImageResource(R.id.ranking_name_pic, R.drawable.unknown_x);
        }

        GlideUtil.displayAvatar(mContext, Constants.IMG_HOST + model.getUidname(), holder.getImageView(R.id.ranking_pic));
        holder.setText(R.id.tv_name, model.getSp_name());
        if (model.getContent() == null) {
            holder.getTextView(R.id.tv_content).setVisibility(View.GONE);
        } else {
            holder.getTextView(R.id.tv_content).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_content, model.getContent());
        }

    }
}
