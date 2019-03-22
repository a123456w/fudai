package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.RoomRegisterBean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/5.
 */

//public class RoomRegisterAdapter extends RecyclerViewAdapter<RoomRegisterBean> {
//    public RoomRegisterAdapter(RecyclerView listView) {
//        super(listView, R.layout.itme_check_people);
//    }
//
//    @Override
//    protected void fillData(ViewHolderHelper holder, final int position, RoomRegisterBean model) {
//        holder.setText(R.id.check_people_tv, model.getName());
//        EditText check_people_et = holder.getView(R.id.check_people_et);
//
//        if(!TextUtils.isEmpty(model.getContent())){
//            check_people_et.setText(model.getContent());
//        }
//
//        check_people_et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                getData().get(position).setContent(s.toString());
//            }
//        });
//    }
//}
public class RoomRegisterAdapter extends BaseListAdapter<RoomRegisterBean> {
    public RoomRegisterAdapter(ListView listView) {
        super(listView, R.layout.itme_check_people);
    }


    @Override
    public void fillData(ViewHolder holder, final int position, RoomRegisterBean model) {
        holder.setText(R.id.check_people_tv, model.getName());
        EditText check_people_et = holder.getView(R.id.check_people_et);
        check_people_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getData().get(position).setContent(s.toString());
            }
        });
    }
}