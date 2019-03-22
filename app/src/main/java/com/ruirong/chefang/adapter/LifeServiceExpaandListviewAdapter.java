package com.ruirong.chefang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruirong.chefang.R;
import com.ruirong.chefang.shoppingcart.bean.ShoppingCartBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlipeng on 2018/1/3 0003
   describe:  生活服务适配器
 */
public class LifeServiceExpaandListviewAdapter extends BaseExpandableListAdapter {

    private List<ShoppingCartBean> data=new ArrayList<>();
    private Context mContext;
    private OnItemChildClickListener onItemChildClickListener;
    private OnItemChildLongClickListener onItemChildLongClickListener;

    public LifeServiceExpaandListviewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getGoods().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getGoods().get(childPosition);
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
        ShoppingCartBean shoppingCartBean = data.get(groupPosition);
        ShopHolder holder = null;
        if (convertView==null){
            holder=new ShopHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_lifeservice_group_layout,parent,false);
//            holder.checkTag=convertView.findViewById(R.id.iv_shop_status);
//            holder.tvShopName= (TextView) convertView.findViewById(R.id.tv_shop_name);
            convertView.setTag(holder);
        }else {
            holder= (ShopHolder) convertView.getTag();
        }
//        holder.tvShopName.setText(shoppingCartBean.getName());
//        holder.checkTag.setSelected(shoppingCartBean.isChecked);
//        holder.checkTag.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onItemChildClickListener!=null){
//                    onItemChildClickListener.onItemChildClick(v,groupPosition,0);
//                }
//            }
//        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ShoppingCartBean.GoodsBean goodsBean = data.get(groupPosition).getGoods().get(childPosition);

        GoodsHolder holder = null;
        if (convertView==null){
            holder=new GoodsHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_lifeservice_child_layout,parent,false);
//            holder.itemView=convertView.findViewById(R.id.item_view);
//            holder.rlLoseEfficacy=convertView.findViewById(R.id.rl_lose_efficacy);
//            holder.checkTag=convertView.findViewById(R.id.iv_status);
//            holder.ivSubtract=convertView.findViewById(R.id.iv_subtract);
//            holder.ivAdd=convertView.findViewById(R.id.iv_add);
//            holder.tvGoodsName= (TextView) convertView.findViewById(R.id.tv_goods_name);
//            holder.tvProperty= (TextView) convertView.findViewById(R.id.tv_property);
//            holder.tvPrice= (TextView) convertView.findViewById(R.id.tv_price);
//            holder.tvCount= (TextView) convertView.findViewById(R.id.tv_count);
//            holder.ivGoods= (ImageView) convertView.findViewById(R.id.iv_goods);


            convertView.setTag(holder);
        }else {
            holder= (GoodsHolder) convertView.getTag();
        }

        //是否已失效，0已失效
//        holder.rlLoseEfficacy.setVisibility(goodsBean.getShixiao()==0?View.VISIBLE:View.GONE);
//        //是否选中
//        if (goodsBean.getShixiao()==0){
//            //已失效的默认不选中
//            holder.checkTag.setSelected(false);
//            goodsBean.setSelected(0);
//        }else {
//            holder.checkTag.setSelected(goodsBean.getSelected()==1);
//        }
//
//        //商品名称
//        holder.tvGoodsName.setText(goodsBean.getGoods_name());
//        //已选属性
////        String spec_xuan_name = goodsBean.getSpec_xuan_name();
////        if (TextUtils.isEmpty(spec_xuan_name)){
////            holder.tvProperty.setVisibility(View.INVISIBLE);
////        }else {
////            holder.tvProperty.setVisibility(View.VISIBLE);
////            holder.tvProperty.setText(spec_xuan_name);
////        }
//        //价格
//        holder.tvPrice.setText(goodsBean.getShop_price());
//        //已选数量
//        holder.tvCount.setText(goodsBean.getGoods_num()+"");
//        GlideUtil.display(mContext,goodsBean.getIndex_pic(),holder.ivGoods);
//
//        setClick(holder.itemView,groupPosition,childPosition);
//        setClick(holder.checkTag,groupPosition,childPosition);
//        setClick(holder.tvProperty,groupPosition,childPosition);
//        setClick(holder.ivSubtract,groupPosition,childPosition);
//        setClick(holder.ivAdd,groupPosition,childPosition);
//
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//                if (onItemChildLongClickListener!=null){
//                    onItemChildLongClickListener.onItemLongChildClick(v,groupPosition,childPosition);
//                }
//
//                return true;
//            }
//        });
//        holder.rlLoseEfficacy.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//                if (onItemChildLongClickListener!=null){
//                    onItemChildLongClickListener.onItemLongChildClick(v,groupPosition,childPosition);
//                }
//
//                return true;
//            }
//        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class ShopHolder{
         View checkTag;
         TextView tvShopName;
    }

    class GoodsHolder{
         View itemView;
        View rlLoseEfficacy;
         View checkTag;
         TextView tvGoodsName;
         TextView tvProperty;
         TextView tvCount;
         TextView tvPrice;
         ImageView ivGoods;
         View ivSubtract;
         View ivAdd;

    }



    public List<ShoppingCartBean> getData() {
        return data;
    }

    public void setData(List<ShoppingCartBean> data) {
        if (data!=null){
            this.data.clear();
            this.data.addAll(data);
            notifyDataSetChanged();
        }else {
            this.data.clear();
            notifyDataSetChanged();
        }
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
