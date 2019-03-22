package com.ruirong.chefang.adapter;

import android.widget.GridView;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.RankingListBean;

/**
 * Created by BX on 2017/12/29.
 */

public class RankingListAdatper extends BaseListAdapter<RankingListBean> {

    public RankingListAdatper(ListView listView) {
        super(listView, R.layout.item_ranking_list);
    }

    @Override
    public void fillData(ViewHolder holder, int position, RankingListBean model) {
        holder.setText(R.id.tv_num,position+1+"");

    }
}
