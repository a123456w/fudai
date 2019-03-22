package com.ruirong.chefang.adapter;

import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.LuckBackRoomListviewBean;
/**
 * Created by chenlipeng on 2018/1/2 0002
   describe:  商家头条中点击每个listiview的item弹出dialog
 */
public class ShopHeadlineDialogAdapter extends BaseListAdapter<LuckBackRoomListviewBean> {
    public ShopHeadlineDialogAdapter(ListView listView) {
        super(listView, R.layout.item_shop_headline_dialog_listview);
    }



    @Override
    public void fillData(ViewHolder holder, int position, LuckBackRoomListviewBean model) {

    }
}
