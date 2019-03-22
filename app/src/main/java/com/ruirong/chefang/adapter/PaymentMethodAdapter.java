package com.ruirong.chefang.adapter;

import android.view.View;
import android.widget.ListView;

import com.qlzx.mylibrary.base.BaseListAdapter;
import com.ruirong.chefang.R;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



/**
 * 作者-->蜕变~成蝶
 * 创建于-->2018/1/10
 * 作用-->
 */

public class PaymentMethodAdapter extends BaseListAdapter<String> {
    private Map<String,Boolean> map=new LinkedHashMap<>();
    int[] image = new int[]{R.drawable.image_wechat, R.drawable.image_alipay, R.drawable.image_nionpay,R.drawable.image_zhanghu, R.drawable.image_chuzhi};
    public PaymentMethodAdapter(ListView listView) {
        super(listView, R.layout.dialog_item_mode);
        String[] strings = new String[]{"微信支付", "支付宝支付","银联支付","账户余额支付","储值余额支付"};

        List<String> list = Arrays.asList(strings);

        setData(list);
        setFalse();
    }

    @Override
    public void fillData(final ViewHolder holder, int position, final String model) {

        holder.setText(R.id.cancel_titlea,model);
        if(map.get(model)){
            holder.setImageResource(R.id.select_ischeck_pic,R.drawable.ischeck);
        }else{
            holder.setImageResource(R.id.select_ischeck_pic,R.drawable.check);
        }
        holder.setImageResource(R.id.iv_pica,image[position]);
        holder.getImageView(R.id.select_ischeck_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFalse();
                map.put(model,true);
                holder.setImageResource(R.id.select_ischeck_pic,R.drawable.ischeck);
                notifyDataSetChanged();
            }
        });

    }

    private void setFalse(){
        for(int i=0;i<getData().size();i++){
            map.put(getData().get(i),false);
        }
    }

}
