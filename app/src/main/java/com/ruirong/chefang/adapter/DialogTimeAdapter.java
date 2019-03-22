package com.ruirong.chefang.adapter;

import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.DialogTimeListviewBean;
import com.ruirong.chefang.bean.LuckBackRoomListviewBean;

/**
 * 到店时间
 * Created by jbx on 2017/12/29 0029
 * describe:
 */

public class DialogTimeAdapter extends BaseListAdapter<String> {

    public DialogTimeAdapter(ListView listView) {
        super(listView, R.layout.dialog_timea);
    }

    @Override
    public void fillData(ViewHolder holder, int position, String model) {
        holder.setText(R.id.time, model);
    }
}
