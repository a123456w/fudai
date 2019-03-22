package com.ruirong.chefang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.ComBinBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlipeng on 2018/1/3 0003
 * describe:  商家详情里边套餐详情的数据
 */
public class ComBolAdaper extends BaseExpandableListAdapter {
    private List<ComBinBean.GoodidsBean> iData = new ArrayList<>();
    private Context mContext ;
    private List<String> gData = new ArrayList<>();

    public ComBolAdaper(List<ComBinBean.GoodidsBean> iData, Context mContext, List<String> gData) {
        this.iData = iData;
        this.mContext = mContext;
        this.gData = gData;
    }

    @Override
    public int getGroupCount() {
        return gData.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return iData.get(i).getContent().size();
    }

    @Override
    public Object getGroup(int i) {
        return gData.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return iData.get(i).getContent().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ComBinBean.GoodidsBean shoppingCartBean = iData.get(groupPosition);
        ShopHolder holder = null;
        if (convertView == null) {
            holder = new ShopHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_combin_group_layout, parent, false);
            holder.tvShopName = (TextView) convertView.findViewById(R.id.tv_expense_month);
            convertView.setTag(holder);
        } else {
            holder = (ShopHolder) convertView.getTag();
        }
        try {
            holder.tvShopName.setText(gData.get(groupPosition));
        } catch (Exception e) {

        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ComBinBean.GoodidsBean.ContentBean goodsBean = iData.get(groupPosition).getContent().get(childPosition);

        GoodsHolder holder = null;
        if (convertView == null) {
            holder = new GoodsHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_combin_layout, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);

            convertView.setTag(holder);
        } else {
            holder = (GoodsHolder) convertView.getTag();
        }

        holder.tvNum.setText(goodsBean.getNum());
        holder.tvPrice.setText(goodsBean.getPrice());
        holder.tvName.setText(goodsBean.getName());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class ShopHolder {
        TextView tvShopName;
    }

    class GoodsHolder {
        TextView tvName;
        TextView tvNum;
        TextView tvPrice;

    }


    public List<ComBinBean.GoodidsBean> getData() {
        return iData;
    }



}
