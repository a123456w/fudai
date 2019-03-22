package com.ruirong.chefang.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.event.CommentPicDeleteEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Djr on 2018/1/13.
 */

public class CommentPicListAdapter extends BaseAdapter {

    private List<String> list = new ArrayList<>();
    private Context context;
    private boolean show = true;
    private String goodsId;

    public CommentPicListAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<String> list) {
        if (list != null) {
            this.list = list;
        }
        notifyDataSetChanged();
    }

    public int getList() {
        if (list != null && list.size() > 0) {
            return list.size();
        }
        return 1;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size() + 1;
        }
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_publish_pic, null);
        ImageView item_publish_pic_img = (ImageView) convertView.findViewById(R.id.item_publish_pic_img);
        ImageView item_publish_pic_del = (ImageView) convertView.findViewById(R.id.item_publish_pic_del);
        ImageView item_publish_pic_img_occupied = (ImageView) convertView.findViewById(R.id.item_publish_pic_img_occupied);
        TextView item_publish_pic_text_occupied = (TextView) convertView.findViewById(R.id.item_publish_pic_text_occupied);

        if (position < list.size()) {
            item_publish_pic_img.setVisibility(View.VISIBLE);
            Glide.with(context).load(list.get(position)).asBitmap().dontAnimate().fitCenter()
                    .placeholder(com.qlzx.mylibrary.R.drawable.ic_placeload)
                    .error(com.qlzx.mylibrary.R.drawable.ic_load_error)
                    .into(item_publish_pic_img);
            item_publish_pic_del.setVisibility(View.VISIBLE);
            item_publish_pic_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    EventBusUtil.post(new CommentPicDeleteEvent(goodsId, position));
                    notifyDataSetChanged();
                }
            });
        } else {
            item_publish_pic_img_occupied.setVisibility(View.VISIBLE);
            item_publish_pic_text_occupied.setVisibility(View.VISIBLE);
            item_publish_pic_text_occupied.setText(list.size() + "/3");
            if (show) {
                item_publish_pic_img_occupied.setVisibility(View.GONE);
                item_publish_pic_text_occupied.setVisibility(View.GONE);
            } else {
                item_publish_pic_img_occupied.setVisibility(View.VISIBLE);
                item_publish_pic_text_occupied.setVisibility(View.VISIBLE);
            }
        }
        if (list.size() >= 3) {
            show = true;
        } else {
            show = false;
        }

        return convertView;
    }

}
