package com.ruirong.chefang.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.EntertainmentAffirmOrderActivity;
import com.ruirong.chefang.bean.HotelDetailCommentBean;
import com.ruirong.chefang.util.ToolUtil;

/**
 * Created by chenlipeng on 2018/1/2 0002
 * describe:    热销产品，套餐选项适配器
 */
public class NearbyShopDetailsComboChooseAdapter extends BaseListAdapter<HotelDetailCommentBean> {
    public NearbyShopDetailsComboChooseAdapter(ListView listView) {
        super(listView, R.layout.list_item_nearby_shop_details_combo);
    }

    @Override
    public void fillData(ViewHolder holder, int position, HotelDetailCommentBean model) {
        holder.getView(R.id.tv_reserve).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(mContext, EntertainmentAffirmOrderActivity.class);
                if (TextUtils.isEmpty(new PreferencesHelper(mContext).getToken())) {
                    ToolUtil.goToLogin(mContext);
                } else {
                    mContext.startActivity(intent1);
                }

            }
        });


    }
}
