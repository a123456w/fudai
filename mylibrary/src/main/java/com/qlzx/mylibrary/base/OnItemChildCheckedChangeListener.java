package com.qlzx.mylibrary.base;

import android.view.ViewGroup;
import android.widget.CompoundButton;

/**
 * Created by 87901 on 2016/10/25.
 */

public interface OnItemChildCheckedChangeListener {
    void onItemChildCheckedChanged(ViewGroup parent, CompoundButton childView, int position, boolean isChecked);
}
