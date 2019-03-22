package com.ruirong.chefang.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruirong.chefang.R;

import java.util.ArrayList;
import java.util.List;

public class ChoiceBoxAdapter extends RecyclerView.Adapter<ChoiceBoxAdapter.ViewHolder> {
    //1、定义一个集合，用来记录选中
    private List<Boolean> isClicks;
    private Context context;
    private List<Object> list;
    //2、定义监听并设set方法
    private ChoiceBoxAdapter.OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(ChoiceBoxAdapter.OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public ChoiceBoxAdapter(Context context, List<Object> list) {
        this.context = context;
        this.list = list;
        //3、为集合添加值
        isClicks = new ArrayList<>();
        for(int i = 0;i<list.size();i++){
            isClicks.add(false);
        }
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ChoiceBoxAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.screen_item_box,viewGroup,false);
        ChoiceBoxAdapter.ViewHolder vh = new ChoiceBoxAdapter.ViewHolder(view);

        return vh;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ChoiceBoxAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tv_can.setText(list.get(position).toString());

        //4：设置点击事件
        if(mOnItemClickListener!=null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getLayoutPosition(); // 1
                    for(int i = 0; i <isClicks.size();i++){
                        isClicks.set(i,false);
                    }
                    isClicks.set(position,true);
                    notifyDataSetChanged();
                    mOnItemClickListener.onItemClick(viewHolder.itemView, position); // 2
                }
            });
        }
        //5、记录要更改属性的控件
        viewHolder.itemView.setTag(viewHolder.tv_can);
        //6、判断改变属性
        Resources resources = context.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.bg_box);
        Drawable drawable1 = resources.getDrawable(R.drawable.bg_unbox);
        if(isClicks.get(position)){

            viewHolder.tv_can.setBackground(drawable);

        }else{
            viewHolder.tv_can.setBackground(drawable1);
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_can;
        public ViewHolder(View view){
            super(view);
            tv_can = (TextView) view.findViewById(R.id.tv_can);
        }
    }
    //7、定义点击事件回调接口
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

}
