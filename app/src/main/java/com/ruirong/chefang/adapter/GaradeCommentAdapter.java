package com.ruirong.chefang.adapter;

import android.widget.GridView;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.CarAndRoomsBean;
import com.ruirong.chefang.bean.GaradeCommentBean;

/**
 * 车房详情评论
 * Created by BX on 2017/12/27.
 */

public class GaradeCommentAdapter extends BaseListAdapter<GaradeCommentBean> {
    public GaradeCommentAdapter(ListView listView) {
        super(listView, R.layout.list_item_garade_details_comment);
    }


    @Override
    public void fillData(ViewHolder holder, int position, GaradeCommentBean model) {

    }
}
