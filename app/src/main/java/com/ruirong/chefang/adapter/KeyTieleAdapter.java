package com.ruirong.chefang.adapter;

import android.support.v7.widget.RecyclerView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.FuBackHotelDetailsBean;

/**酒店套餐的标题
 * Created by dillon on 2018/5/10.
 */

public class KeyTieleAdapter extends RecyclerViewAdapter<FuBackHotelDetailsBean.KeytitleBean> {
    public KeyTieleAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_key_text);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, FuBackHotelDetailsBean.KeytitleBean model) {
            viewHolderHelper.setText(R.id.tv_keytitle,model.getTitle());
    }
}
