package com.ruirong.chefang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.LogisticsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlipeng on 2017/12/28 0028
   describe:  物流的适配器
 */
public class LogisticsListviewAdapter1 extends BaseAdapter {

    //4812
    private Context context;
    private LayoutInflater inflater;


    private List<LogisticsBean> listData = new ArrayList<>();

    public LogisticsListviewAdapter1(Context context, List<LogisticsBean> list) {

        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listData = list;

        //初始化参数

    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //每个convert view都会调用此方法，获得当前所需要的view样式
    public int getItemViewType(int position) {

        LogisticsBean logisticsBean = listData.get(position);

        int type = -1;

        if (position == 0) {
            type=0;
        } else if (position == getCount() - 1) {
            type=2;
        } else {
            type=1;
        }
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

//        mImageLoader.clearMemoryCache();
//        mImageLoader.clearDiskCache();

        viewHolder0 holder0 = null;
        viewHolder1 holder1 = null;
        viewHolder2 holder2 = null;


        //获得当前所需要的view样式
        int type = getItemViewType(i);

//无convertView，需要new出各个控件
        if (view == null) {
            switch (type) {
                case 0:
                    view = inflater.inflate(R.layout.listview_item_logistics_top_layout, null);
                    holder0 = new viewHolder0();

//                    holder0.name = (TextView) view.findViewById(tv_name);
//                    holder0.image1 = (RoundedImageView) view.findViewById(iv_item_image1);
//                    holder0.image2 = (RoundedImageView) view.findViewById(iv_item_image2);
//                    holder0.image3 = (RoundedImageView) view.findViewById(iv_item_image3);

                    view.setTag(holder0);

                    break;
                case 1:
                    view = inflater.inflate(R.layout.listview_item_logistics, null);
                    holder1 = new viewHolder1();
//                    holder1.tv_name = (TextView) view.findViewById(tv_name);
//                    holder1.iv_item_image1 = (RoundedImageView) view.findViewById(iv_item_image1);
//                    holder1.iv_item_image2 = (RoundedImageView) view.findViewById(iv_item_image2);
//                    holder1.iv_item_image3 = (RoundedImageView) view.findViewById(iv_item_image3);
//                    holder1.tv_join_number = (TextView) view.findViewById(R.id.tv_join_number);

                    view.setTag(holder1);
                    break;
                case 2:
                    view = inflater.inflate(R.layout.listview_item_logistics, null);
                    holder2 = new viewHolder2();

//                    holder2.tv_name = (TextView) view.findViewById(tv_name);
//                    holder2.tv_read_number = (TextView) view.findViewById(R.id.tv_read_number);
//                    holder2.iv_topic_image = (ImageView) view.findViewById(R.id.iv_topic_image);
                    view.setTag(holder2);
                    break;

            }
        } else {
            //有convertView，按样式，取得不用的布局
            switch (type) {
                case 0:
                    holder0 = (viewHolder0) view.getTag();
//                    Log.e("convertView !!!!!!= ", "0");
                    break;
                case 1:
                    holder1 = (viewHolder1) view.getTag();
                    break;
                case 2:
                    holder2 = (viewHolder2) view.getTag();
                    break;

            }

        }
        LogisticsBean logisticsBean = listData.get(i);

        //设置资源
        switch (type) {
            case 0: {


            }
            break;
            case 1: {


            }

            break;
            case 2: {//没有图片的


            }
        }


        return view;
    }


    class viewHolder0 {

        public TextView name;

    }

    class viewHolder1 {

        public TextView tv_name;

        public TextView tv_join_number;
        public TextView tv_picture_number;
        public TextView tv_at_once_join;
    }

    class viewHolder2 {

        public TextView tv_name;
        public TextView tv_content;
        public TextView tv_read_number;
        public TextView tv_time;
        public ImageView iv_topic_image;
    }

}
