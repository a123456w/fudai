package com.ruirong.chefang.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/*
 *  Created by 李  on 2018/10/19.
 */
public class DialogUtil {
    private static float mHeight=0.83f;

    /**
     *  屏幕下边的dialog
     * @param dialog 弹窗
     * @param mContext 上下文
     */
    public static void initDialog(Dialog dialog, Context mContext){
        initDialog(dialog,mContext,mHeight);
    }

    /**
     *
     * @param dialog
     * @param mContext
     * @param f  屏幕占比
     */
    public static void initDialog(Dialog dialog, Context mContext,float f){
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        // 获取Window的LayoutParams
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;
        attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        // 一定要重新设置, 才能生效
        window.setAttributes(attributes);

        //设置弹框的高为屏幕的一半宽是屏幕的宽
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int)(metrics.widthPixels); //设置宽度
        lp.height = (int)(metrics.heightPixels*f); //设置宽度
        lp.dimAmount = 0.5f;

        //取消最外层View的背景色
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setAttributes(lp);
    }
}
