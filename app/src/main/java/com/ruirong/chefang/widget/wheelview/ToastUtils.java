package com.ruirong.chefang.widget.wheelview;

/**
 * Created by chenlipeng on 2017/6/27.
 */

import android.content.Context;
import android.widget.Toast;

public  class ToastUtils {

    public static void showTip(Context context, String data) {
        Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
    }
}
