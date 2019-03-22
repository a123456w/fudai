package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.util.DateUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.MessageItemBean;

import java.util.HashSet;
import java.util.Set;

import static com.qlzx.mylibrary.util.DateUtil.FORMAT_YMD;

/**
 * Created by guo on 2017/4/6.
 */

public class MessageAdapter extends RecyclerViewAdapter<MessageItemBean> {
    public Set<String> selectList=new HashSet<>();
    public boolean edit;
    public MessageAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_message);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, MessageItemBean model) {
        viewHolderHelper.setVisibility(R.id.iv_selector,edit? View.VISIBLE:View.GONE);
        viewHolderHelper.setVisibility(R.id.view_point,edit? View.GONE:View.VISIBLE);

        if (edit){
            viewHolderHelper.getView(R.id.iv_selector).setSelected(selectList.contains(model.getId()));
        }else {
            viewHolderHelper.setVisibility(R.id.view_point,model.getIs_read().equals("0")? View.VISIBLE:View.INVISIBLE);
        }
        viewHolderHelper.setText(R.id.tv_message,model.getContent());
        viewHolderHelper.setText(R.id.tv_date, DateUtil.toDate(model.getCreate_time(),FORMAT_YMD));

    }
}
