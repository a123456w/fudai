package com.ruirong.chefang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.HomeSearchBean;

import java.util.ArrayList;

/**
 * Created by 16690 on 2018/3/27.
 * describe: 搜索结果 Adapter
 */

public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<HomeSearchBean.ListBean> list = new ArrayList<>();
    private RvItemClickListener rvItemClickListener;


    public SearchResultAdapter(Context mContext, ArrayList<HomeSearchBean.ListBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void setRvItemClickListener(RvItemClickListener rvItemClickListener) {
        this.rvItemClickListener = rvItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return Integer.parseInt(list.get(position).getType());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_search_result_shop, null);
            return new ShopSearchViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_dynamic, null);
            return new RoomSearchViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ShopSearchViewHolder) {
            ShopSearchViewHolder viewHolder = (ShopSearchViewHolder) holder;
            viewHolder.view.setTag(position);
            viewHolder.name.setText(list.get(position).getSp_name());
            viewHolder.rbGrade.setRating(Float.parseFloat(list.get(position).getGrades()));
            viewHolder.grade.setText(list.get(position).getGrades() + "分");
            viewHolder.region.setText(list.get(position).getSp_address());
            viewHolder.distance.setText(list.get(position).getDistance() + "km");
            GlideUtil.display(mContext, Constants.IMG_HOST + list.get(position).getCover(), viewHolder.ivPic);
        } else {
            RoomSearchViewHolder viewHolder = (RoomSearchViewHolder) holder;
            viewHolder.view.setTag(position);
            viewHolder.title.setText(list.get(position).getSp_name());
            viewHolder.address.setText(list.get(position).getSp_address());
            viewHolder.content.setText(list.get(position).getContent());
            GlideUtil.display(mContext, Constants.IMG_HOST + list.get(position).getCover(), viewHolder.ivPic);
        }
    }

    @Override
    public int getItemCount() {
        if (list == null){
            return 0;
        }else {
            return list.size();
        }
    }

    public HomeSearchBean.ListBean getItem(int position){
        return  list.get(position);
    }

    public void setData(ArrayList<HomeSearchBean.ListBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void addMoreData(ArrayList<HomeSearchBean.ListBean> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }



    public class ShopSearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public View view;
        public TextView name, classify, region, distance, grade;
        public RatingBar rbGrade;
        public ImageView ivPic;

        public ShopSearchViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = (TextView) itemView.findViewById(R.id.classify_title);
            classify = (TextView) itemView.findViewById(R.id.shop_classify);
            region = (TextView) itemView.findViewById(R.id.shop_region);
            distance = (TextView) itemView.findViewById(R.id.shop_distance);
            grade = (TextView) itemView.findViewById(R.id.rb_grade_branch);
            rbGrade = (RatingBar) itemView.findViewById(R.id.rb_grade);
            ivPic = (ImageView) itemView.findViewById(R.id.iv_pic);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            if (rvItemClickListener != null){
                rvItemClickListener.onClick(v,position,0);
            }

        }
    }

    public class RoomSearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public View view;
        public TextView title, address, content;
        public ImageView ivPic;

        public RoomSearchViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            title = (TextView) itemView.findViewById(R.id.tv_title);
            address = (TextView) itemView.findViewById(R.id.tv_address);
            content = (TextView) itemView.findViewById(R.id.tv_content);
            ivPic = (ImageView) itemView.findViewById(R.id.iv_pic);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            if (rvItemClickListener != null){
                rvItemClickListener.onClick(v,position,1);
            }
        }
    }


    public interface RvItemClickListener{
        void onClick(View view,int position,int itemType);
    }
}
