package com.ruirong.chefang.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.ScreenItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  酒店住宿适配器
 *  Created by 李  on 2018/11/20.
 */
public class ScreenAdapter extends RecyclerViewAdapter<String>{
    /**
     * 分类筛选数据
     */
    private Map<String,List<ScreenItem>> map;
    /**
     * 上一次点击事件item的下标存储
     */
    private int[] lastPosition;
    /**
     *  已选择的item集合
     */
    private List<ScreenItem> isSelectList=new ArrayList();
    /**
     * 刷新ui的回调
     */
    private OnUpDataListener onUpDataListener;

    /**
     * 所有二级adapter
     * @return
     */
    private List<ScreenItemAdapter> adapters=new ArrayList<>();

    public int[] getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(int[] lastPosition) {
        this.lastPosition = lastPosition;
    }




    public OnUpDataListener getOnUpDataListener() {
        return onUpDataListener;
    }

    public void setOnUpDataListener(OnUpDataListener onUpDataListener) {
        this.onUpDataListener = onUpDataListener;
    }



    public ScreenAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_screen);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, String model) {
        initRecyclerView(helper,position,model);
    }

    /**
     * 初始化 RecyclerView
     * @param helper
     * @param position1
     * @param model
     */
    private void initRecyclerView(ViewHolderHelper helper, final int position1, String model) {
        TextView tv_title = helper.getTextView(R.id.tv_title);
        tv_title.setText(model);
        RecyclerView rv = helper.getView(R.id.rv_screen_item);
        final ScreenItemAdapter screenItemAdapter = new ScreenItemAdapter(rv);
        GridLayoutManager llm = new GridLayoutManager(mContext, 4);
        rv.setLayoutManager(llm);
        rv.setAdapter(screenItemAdapter);
        if (map!=null&&map.size()>0){
            screenItemAdapter.setData(map.get(model));
        }
        adapters.add(screenItemAdapter);
        screenItemAdapter.setOnRVItemClickListener(new OnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                ScreenItem item = screenItemAdapter.getItem(position);
                if (item.getIsSelect()!=2){
                    if (lastPosition[position1]==position){
                        if (item.getIsSelect()==1){
                            //再次点击
                            item.setIsSelect(0);
                            isSelectList.remove(item);
                            if (onUpDataListener!=null)onUpDataListener.notifyData(isSelectList);
                            lastPosition[position1]=-1;
                            notifyDataSetChanged();
                        }
                        return;
                    }
                    item.setIsSelect(1);
                    if (lastPosition[position1]!=-1){
                        screenItemAdapter.getItem(lastPosition[position1]).setIsSelect(0);
                        isSelectList.remove(screenItemAdapter.getItem(lastPosition[position1]));
                    }
                    item.setIsSelect(1);
                    isSelectList.add(screenItemAdapter.getItem(position));
                    if (onUpDataListener!=null)onUpDataListener.notifyData(isSelectList);
                    lastPosition[position1]=position;
                    notifyDataSetChanged();

                }
            }
        });
    }

    /**
     * 设置数据
     * @param map
     */
    public void setMapData(Map<String,List<ScreenItem>> map){

        if (map!=null){
            this.map=map;
            List<String> mList = Arrays.asList(map.keySet().toArray(new String[]{}));
            lastPosition=new int[mList.size()];
            for (int i = 0; i < lastPosition.length; i++) {
                lastPosition[i]=-1;
            }
            setData(mList);
        }else {
            for (ScreenItemAdapter adapter : adapters) {
                adapter.clear();
            }
            clear();
        }
    }
    public interface  OnUpDataListener{
        void notifyData(List<ScreenItem> isSelectList);
    }
}
