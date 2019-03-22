package com.qlzx.mylibrary.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.text.Html;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guo on 2017/3/6.
 * ListView的adapter的封装类
 */

public abstract class BaseListAdapter<M> extends BaseAdapter {

    public final Context mContext;
    private final int mItemLayoutId;
    private final List<M> mData;

    public BaseListAdapter(ListView listView, int itemLayoutId) {
        mContext = listView.getContext();
        mItemLayoutId = itemLayoutId;
        mData = new ArrayList<>();
    }

    public BaseListAdapter(GridView gridView, int itemLayoutId) {
        mContext = gridView.getContext();
        mItemLayoutId = itemLayoutId;
        mData = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mItemLayoutId, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        fillData(holder, position, mData.get(position));
        return convertView;
    }

    public abstract void fillData(ViewHolder holder, int position, M model);

    public class ViewHolder {
        private SparseArray<View> mViews;
        private View mConvertView;

        ViewHolder(View itemView) {
            mConvertView = itemView;
            mViews = new SparseArray<>();
        }

        /**
         * 通过控件的Id获取对应的控件，如果没有则加入mViews，则从item根控件中查找并保存到mViews中
         *
         * @param viewId
         * @return
         */
        public <T extends View> T getView(@IdRes int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        /**
         * 通过ImageView的Id获取ImageView
         *
         * @param viewId
         * @return
         */
        public ImageView getImageView(@IdRes int viewId) {
            return getView(viewId);
        }

        /**
         * 通过TextView的Id获取TextView
         *
         * @param viewId
         * @return
         */
        public TextView getTextView(@IdRes int viewId) {
            return getView(viewId);
        }

        /**
         * 获取一个LinearLayout
         * @param viewId
         * @return
         */
        public LinearLayout getLinearLayout(@IdRes int viewId){
            return getView(viewId);
        }

        /**
         * 获取item的根控件
         *
         * @return
         */
        public View getConvertView() {
            return mConvertView;
        }


        /**
         * 设置对应id的控件的文本内容
         *
         * @param viewId
         * @param text
         * @return
         */
        public ViewHolder setText(@IdRes int viewId, CharSequence text) {
            TextView view = getView(viewId);
            view.setText(text);
            return this;
        }

        /**
         * 设置对应id的控件的文本内容
         *
         * @param viewId
         * @param stringResId 字符串资源id
         * @return
         */
        public ViewHolder setText(@IdRes int viewId, @StringRes int stringResId) {
            TextView view = getView(viewId);
            view.setText(stringResId);
            return this;
        }

        /**
         * 设置对应id的控件的html文本内容
         *
         * @param viewId
         * @param source html文本
         * @return
         */
        public ViewHolder setHtml(@IdRes int viewId, String source) {
            TextView view = getView(viewId);
            view.setText(Html.fromHtml(source));
            return this;
        }

        /**
         * 设置对应id的控件是否选中
         *
         * @param viewId
         * @param checked
         * @return
         */
        public ViewHolder setChecked(@IdRes int viewId, boolean checked) {
            Checkable view = getView(viewId);
            view.setChecked(checked);
            return this;
        }

        public ViewHolder setTag(@IdRes int viewId, Object tag) {
            View view = getView(viewId);
            view.setTag(tag);
            return this;
        }

        public ViewHolder setTag(@IdRes int viewId, int key, Object tag) {
            View view = getView(viewId);
            view.setTag(key, tag);
            return this;
        }

        public ViewHolder setVisibility(@IdRes int viewId, int visibility) {
            View view = getView(viewId);
            view.setVisibility(visibility);
            return this;
        }

        public ViewHolder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
            ImageView view = getView(viewId);
            view.setImageBitmap(bitmap);
            return this;
        }

        public ViewHolder setImageDrawable(@IdRes int viewId, Drawable drawable) {
            ImageView view = getView(viewId);
            view.setImageDrawable(drawable);
            return this;
        }

        /**
         * @param viewId
         * @param textColorResId 颜色资源id
         * @return
         */
        public ViewHolder setTextColorRes(@IdRes int viewId, @ColorRes int textColorResId) {
            TextView view = getView(viewId);
            view.setTextColor(mContext.getResources().getColor(textColorResId));
            return this;
        }

        /**
         * @param viewId
         * @param textColor 颜色值
         * @return
         */
        public ViewHolder setTextColor(@IdRes int viewId, int textColor) {
            TextView view = getView(viewId);
            view.setTextColor(textColor);
            return this;
        }

        /**
         * @param viewId
         * @param backgroundResId 背景资源id
         * @return
         */
        public ViewHolder setBackgroundRes(@IdRes int viewId, int backgroundResId) {
            View view = getView(viewId);
            view.setBackgroundResource(backgroundResId);
            return this;
        }

        /**
         * @param viewId
         * @param color  颜色值
         * @return
         */
        public ViewHolder setBackgroundColor(@IdRes int viewId, int color) {
            View view = getView(viewId);
            view.setBackgroundColor(color);
            return this;
        }

        /**
         * @param viewId
         * @param colorResId 颜色值资源id
         * @return
         */
        public ViewHolder setBackgroundColorRes(@IdRes int viewId, @ColorRes int colorResId) {
            View view = getView(viewId);
            view.setBackgroundColor(mContext.getResources().getColor(colorResId));
            return this;
        }

        /**
         * @param viewId
         * @param imageResId 图像资源id
         * @return
         */
        public ViewHolder setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
            ImageView view = getView(viewId);
            view.setImageResource(imageResId);
            return this;
        }

    }

    /**
     * 在集合头部添加新的数据集合（下拉从服务器获取最新的数据集合，例如新浪微博加载最新的几条微博数据）
     *
     * @param data
     */
    public void addNewData(List<M> data) {
        if (data != null) {
            mData.addAll(0, data);
            notifyDataSetChanged();
        }
    }

    /**
     * 在集合尾部添加更多数据集合（上拉从服务器获取更多的数据集合，例如新浪微博列表上拉加载更晚时间发布的微博数据）
     *
     * @param data
     */
    public void addMoreData(List<M> data) {
        if (data != null) {
            mData.addAll(mData.size(), data);
            notifyDataSetChanged();
        }
    }

    /**
     * 设置全新的数据集合，如果传入null，则清空数据列表（第一次从服务器加载数据，或者下拉刷新当前界面数据表）
     *
     * @param data
     */
    public void setData(List<M> data) {
        if (data != null) {
            mData.clear();
            mData.addAll(data);
        } else {
            mData.clear();

        }
        notifyDataSetChanged();
    }

    public List<M> getData(){
        return mData;
    }

    /**
     * 清空数据列表
     */
    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    /**
     * 删除指定索引数据条目
     *
     * @param position
     */
    public void removeItem(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 删除指定数据条目
     *
     * @param model
     */
    public void removeItem(M model) {
        removeItem(mData.indexOf(model));
    }

    /**
     * 在指定位置添加数据条目
     *
     * @param position
     * @param model
     */
    public void addItem(int position, M model) {
        mData.add(position, model);
        notifyDataSetChanged();
    }

    /**
     * 在集合头部添加数据条目
     *
     * @param model
     */
    public void addFirstItem(M model) {
        addItem(0, model);
    }

    /**
     * 在集合末尾添加数据条目
     *
     * @param model
     */
    public void addLastItem(M model) {
        addItem(mData.size(), model);
    }

    /**
     * 替换指定索引的数据条目
     *
     * @param location
     * @param newModel
     */
    public void setItem(int location, M newModel) {
        mData.set(location, newModel);
        notifyDataSetChanged();
    }

    /**
     * 替换指定数据条目
     *
     * @param oldModel
     * @param newModel
     */
    public void setItem(M oldModel, M newModel) {
        setItem(mData.indexOf(oldModel), newModel);
    }

    /**
     * 移动数据条目的位置
     *
     * @param fromPosition
     * @param toPosition
     */
    public void moveItem(int fromPosition, int toPosition) {
        mData.add(toPosition, mData.remove(fromPosition));
        notifyDataSetChanged();
    }
}
