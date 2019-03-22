package com.ruirong.chefang.adapter;

import android.widget.GridView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.HomeLableBean;

/**首页标签的适配器
 * Created by dillon on 2017/12/26.
 */

public class HomeLableAdapter extends BaseListAdapter<HomeLableBean> {

    public HomeLableAdapter(GridView gridView) {
        super(gridView, R.layout.item_function_list);
    }

    @Override
    public void fillData(ViewHolder holder, int position, HomeLableBean model) {
        holder.setText(R.id.tv_name,model.getName());
        holder.setText(R.id.tv_content,model.getContent());
        GlideUtil.displayAvatar(mContext,model.getPic(),holder.getImageView(R.id.iv_pic));
    }
}
