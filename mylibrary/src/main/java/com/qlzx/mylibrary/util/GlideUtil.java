package com.qlzx.mylibrary.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qlzx.mylibrary.R;

import java.io.File;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by 87901 on 2016/10/29.
 */

public class GlideUtil {


    //加载圆形图片
    public static void displayAvatar(Context context,String url,ImageView view){
        Glide.with(context).load(url).bitmapTransform(new CropCircleTransformation(context)).placeholder(R.drawable.ic_df_avatar).into(view);
    }
    //加载圆形图片
    public static void displayAvatar(Context context,int url,ImageView view){
        Glide.with(context).load(url).bitmapTransform(new CropCircleTransformation(context)).placeholder(R.drawable.ic_df_avatar).into(view);
    }

    //加载圆形头像
    public static void displayHeadAvatar(Context context,String url,ImageView view){
        Glide.with(context).load(url).bitmapTransform(new CropCircleTransformation(context)).placeholder(R.drawable.ic_head_hold).into(view);
    }
    //加载本地圆形头像
    public static void displayHeadAvatar(Context context,int url,ImageView view){
        Glide.with(context).load(url).bitmapTransform(new CropCircleTransformation(context)).placeholder(R.drawable.ic_head_hold).into(view);
    }
    //加载网络图片
    public static void display(Context context,String url,ImageView view){
        //TODO 如果写成是这里的话注意地址。
       // Glide.with(context).load(Constants.IMG_HOST+url).asBitmap().dontAnimate().placeholder(R.drawable.ic_placeload).error(R.drawable.ic_load_error).into(view);
        Glide.with(context).load(url).asBitmap().dontAnimate().placeholder(R.drawable.ic_placeload).error(R.drawable.ic_load_error).into(view);
    }
    //从资源中加载
    public static void display(Context context,int resourceId,ImageView view){
        Glide.with(context).load(resourceId).into(view);
    }
    //从文件中加载
    public static void display(Context context, File file, ImageView view){
        Glide.with(context).load(file).into(view);
    }



}
