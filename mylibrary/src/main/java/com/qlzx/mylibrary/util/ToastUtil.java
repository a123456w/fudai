package com.qlzx.mylibrary.util;

import android.content.Context;
import android.widget.Toast;


/**
 * Created by admin on 2015/12/3.
 */
public class ToastUtil {
    private static Toast toast;

    public static void showToast(Context context,String text){
        if (toast==null){
            toast=Toast.makeText(context,text,Toast.LENGTH_SHORT);
        }
        toast.setText(text);
        toast.show();
    }
}
