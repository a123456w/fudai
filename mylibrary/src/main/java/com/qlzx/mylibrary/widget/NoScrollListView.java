package com.qlzx.mylibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 失去滑动的listview
 * 用于嵌套在scrollview中使用
 * @author limm
 *
 */
public class NoScrollListView extends ListView {
	  
    public NoScrollListView(Context context, AttributeSet attrs){
         super(context, attrs);  
    }  
 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){  
         int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
         super.onMeasure(widthMeasureSpec, mExpandSpec);  
    }  
}
