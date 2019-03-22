package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.util.DateUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.SilverTransferBean;

/**
 * Created by dillon on 2017/5/5.
 */

public class SilverTransferRecodeAdapter extends RecyclerViewAdapter<SilverTransferBean> {
    public SilverTransferRecodeAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_silvertransfer_list);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, SilverTransferBean model) {
                viewHolderHelper.setText(R.id.tv_time, DateUtil.toDate(model.getCreate_time(), DateUtil.FORMAT_YMDHM));
                if (model.getType()==1){
                    viewHolderHelper.setText(R.id.tv_type,"转出");
                }else if (model.getType()==2){
                    viewHolderHelper.setText(R.id.tv_type,"转入");
                }
                viewHolderHelper.setText(R.id.tv_count,model.getTransfer_money());
    }
}
