package com.ruirong.chefang.adapter;

import android.widget.GridView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.ShopHomeBean;

/**商城标签适配器
 * Created by dillon on 2017/12/26.
 */

public class ShopMallLableAdapter extends BaseListAdapter<ShopHomeBean.TradeBean> {
    public ShopMallLableAdapter(GridView gridView) {
        super(gridView, R.layout.item_function);
    }

    @Override
    public void fillData(ViewHolder holder, int position, ShopHomeBean.TradeBean model) {
        holder.setText(R.id.tv_name,model.getName());
        GlideUtil.displayAvatar(mContext, Constants.IMG_HOST+model.getPic(),holder.getImageView(R.id.iv_pic));
    }
}
