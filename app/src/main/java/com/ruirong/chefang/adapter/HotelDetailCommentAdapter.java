package com.ruirong.chefang.adapter;

import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.HotelDetailCommentBean;

/**酒店详情评论的适配器
 * Created by dillon on 2017/12/27.
 */

public class HotelDetailCommentAdapter extends BaseListAdapter<HotelDetailCommentBean> {
    public HotelDetailCommentAdapter(ListView listView) {
        super(listView, R.layout.list_item_entertainment_details_comment);
    }

    @Override
    public void fillData(ViewHolder holder, int position, HotelDetailCommentBean model) {

    }
}
