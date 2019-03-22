package com.ruirong.chefang.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.ReserceActivity;
import com.ruirong.chefang.bean.FuBackHotelDetailsBean;
import com.ruirong.chefang.bean.HotelReserveTaoCan;
import com.ruirong.chefang.util.TextUtils;
import com.ruirong.chefang.util.ToolUtil;

import java.util.List;

/**酒店套餐
 * Created by dillon on 2018/5/11.
 */

public class HotelTaocanAdapter extends BaseListAdapter<HotelReserveTaoCan>{
    List<HotelReserveTaoCan> list ;
    String shop_id ;
    FuBackHotelDetailsBean fuBackHotelDetailsBean;

    public HotelTaocanAdapter(ListView listView) {
        super(listView, R.layout.item_hoteldetail_chosetylpeone);
    }
    public void setMyDatas(List<HotelReserveTaoCan> list ,String shop_id,FuBackHotelDetailsBean fuBackHotelDetailsBean) {
        this.list = list;
        this.shop_id = shop_id;
        this.fuBackHotelDetailsBean = fuBackHotelDetailsBean;
    }
    @Override
    public void fillData(ViewHolder holder, final int position, HotelReserveTaoCan model) {

        GlideUtil.display(mContext, Constants.IMG_HOST + model.getCover(), holder.getImageView(R.id.iv_pic));
        holder.setText(R.id.tv_name, model.getName());
        holder.setText(R.id.tv_presentprice, "￥" + model.getPrice());
        holder.setText(R.id.tv_oldprice, "￥" + model.getYuan_price());
        holder.getTextView(R.id.tv_oldprice).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


        if (model.getSpecif() != null) {
            if (model.getSpecif().size() >= 1) {
                holder.setText(R.id.condition_1, model.getSpecif().get(0));
            }
            if (model.getSpecif().size() >= 2) {
                holder.setText(R.id.condition_2, model.getSpecif().get(1));
            }
            if (model.getSpecif().size() >= 3) {
                holder.setText(R.id.tv_evaluate, model.getSpecif().get(2));
            }
        }

        if (1 == model.getIs_full()) {
            holder.setImageResource(R.id.iv_orderonline, R.drawable.ic_orderonline);

        } else if (0 == model.getIs_full()) {//已满
            holder.setImageResource(R.id.iv_orderonline, R.drawable.iv_yi_orderonline);
        }
        RelativeLayout relativeLayout =(RelativeLayout)holder.getView(R.id.rl_reserve);

                relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (android.text.TextUtils.isEmpty(new PreferencesHelper(mContext).getToken())) {
                        ToolUtil.goToLogin(mContext);
                    } else {
                        if (list.get(position).getIs_full() == 1) {
                            if (fuBackHotelDetailsBean != null && !android.text.TextUtils.isEmpty(shop_id)) {
                                Intent intent1 = new Intent(mContext, ReserceActivity.class);
                                intent1.putExtra("house_id", list.get(position).getId());
                                intent1.putExtra("position", position);
                                intent1.putExtra("shop_id", shop_id);
                                String house_xq = new Gson().toJson(fuBackHotelDetailsBean);
                                intent1.putExtra("house_xq", house_xq);
                                mContext.startActivity(intent1);
                            }
                        } else if (list.get(position).getIs_full() == 1) {
                            ToastUtil.showToast(mContext, "房间已满");
                        }
                    }
                }
            });
       }
}
