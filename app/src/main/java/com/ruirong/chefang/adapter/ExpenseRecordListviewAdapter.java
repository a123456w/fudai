package com.ruirong.chefang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.GRecordBean;
import com.ruirong.chefang.bean.PurchaseHistoryInfo;
import com.ruirong.chefang.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlipeng on 2018/1/3 0003
   describe:  消费记录
 */
public class ExpenseRecordListviewAdapter extends BaseExpandableListAdapter {

    private List<GRecordBean.MonthBean> data=new ArrayList<>();
    private Context mContext;
    private OnItemChildClickListener onItemChildClickListener;
    private OnItemChildLongClickListener onItemChildLongClickListener;
    private List<Integer> headYears=new ArrayList<>();

    public ExpenseRecordListviewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getInfo().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getInfo().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GRecordBean.MonthBean shoppingCartBean = data.get(groupPosition);
        ShopHolder holder = null;
        if (convertView==null){
            holder=new ShopHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_expenserecord_group_layout,parent,false);
             holder.tvShopName= (TextView) convertView.findViewById(R.id.tv_expense_month);
            convertView.setTag(holder);
        }else {
            holder= (ShopHolder) convertView.getTag();
        }
        try {
            holder.tvShopName.setText(headYears.get(groupPosition)+"-"+shoppingCartBean.getMonth());
        }catch (Exception e){

        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        PurchaseHistoryInfo goodsBean = data.get(groupPosition).getInfo().get(childPosition);

        GoodsHolder holder = null;
        if (convertView==null){
            holder=new GoodsHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_expenserecord_child_layout,parent,false);
            holder.tvGoodsName= (TextView) convertView.findViewById(R.id.tv_shop_business);
            holder.tvPrice= (TextView) convertView.findViewById(R.id.tv_shop_price);
            holder.tvCount= (TextView) convertView.findViewById(R.id.tv_shop_time);
            holder.ivGoods= (ImageView) convertView.findViewById(R.id.circleview_headportrait);


            convertView.setTag(holder);
        }else {
            holder= (GoodsHolder) convertView.getTag();
        }
        String type=goodsBean.getType();
        if ("1".equals(type)){
            holder.ivGoods.setImageResource(R.drawable.ic_goods);
        }else if ("2".equals(type)){
            holder.ivGoods.setImageResource(R.drawable.ic_merchants);
        }else if ("3".equals(type)){
            holder.ivGoods.setImageResource(R.drawable.ic_merchants);
        }else if ("4".equals(type)){
            holder.ivGoods.setImageResource(R.drawable.ic_transfer);
        }else if ("5".equals(type)){
            holder.ivGoods.setImageResource(R.drawable.ic_wx);
        }
        holder.tvGoodsName.setText(goodsBean.getMsg());
        holder.tvPrice.setText(goodsBean.getSymbol()+goodsBean.getPrice());
        holder.tvCount.setText(ToolUtil.getStrTime(goodsBean.getCreate_time()));
        holder.ivGoods.setImageResource(R.drawable.image_oldhouse);

        setClick(convertView,groupPosition,childPosition);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class ShopHolder{
         TextView tvShopName;
    }

    class GoodsHolder{
         TextView tvGoodsName;
         TextView tvCount;
         TextView tvPrice;
         ImageView ivGoods;

    }



    public List<GRecordBean.MonthBean> getData() {
        return data;
    }

    public void setData(List<GRecordBean.MonthBean> data,List<Integer> headYears) {
        if (data!=null){
            this.data.clear();
            this.headYears.clear();
            this.data.addAll(data);
            this.headYears=headYears;
            notifyDataSetChanged();
        }else {
            this.data.clear();
            notifyDataSetChanged();
        }
    }

    public void addData(List<GRecordBean.MonthBean> data,List<Integer> headYears){
        this.data.addAll(data);
        this.headYears.addAll(headYears);
        notifyDataSetChanged();
    }
    private void setClick(final View view, final int groupPosition, final int childPosition){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemChildClickListener!=null){
                    onItemChildClickListener.onItemChildClick(view,groupPosition,childPosition);
                }
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemChildLongClickListener!=null){
                    onItemChildLongClickListener.onItemLongChildClick(view,groupPosition,childPosition);
                }
                return true;
            }
        });
    }



    public interface OnItemChildClickListener{
        void onItemChildClick(View view, int groupPosition, int childPosition);
    }


    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener){
        this.onItemChildClickListener=onItemChildClickListener;
    }

    public interface OnItemChildLongClickListener{
        void onItemLongChildClick(View view, int groupPosition, int childPosition);
    }

    public void setOnItemChildLongClickListener(OnItemChildLongClickListener onItemChildLongClickListener){
        this.onItemChildLongClickListener=onItemChildLongClickListener;
    }
}
