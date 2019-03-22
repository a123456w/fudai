package com.qlzx.mylibrary.base;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 87901 on 2016/10/25.
 */

public interface OnItemChildLongClickListener {

    boolean onItemChildLongClick(ViewGroup parent, View childView, int position);
}
