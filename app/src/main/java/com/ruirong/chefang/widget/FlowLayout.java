//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.ruirong.chefang.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    private static final String TAG = "FlowLayout";
    private List<List<View>> mAllViews = new ArrayList();
    private List<Integer> mLineHeight = new ArrayList();

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(this.getContext(), attrs);
    }

    public LayoutParams generateDefaultLayoutParams() {
        MarginLayoutParams mp = new MarginLayoutParams(-2, -2);
        mp.leftMargin = 10;
        mp.rightMargin = 10;
        mp.topMargin = 10;
        mp.bottomMargin = 10;
        return mp;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - this.getPaddingRight() - this.getPaddingLeft();
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - this.getPaddingTop() - this.getPaddingBottom();
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        Log.e("FlowLayout", sizeWidth + "," + sizeHeight);
        int width = 0;
        int height = 0;
        int lineWidth = 0;
        int lineHeight = 0;
        int cCount = this.getChildCount();

        for (int i = 0; i < cCount; ++i) {
            View child = this.getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if (lineWidth + childWidth > sizeWidth) {
                width = Math.max(lineWidth, childWidth);
                lineWidth = childWidth;
                height += lineHeight;
                lineHeight = childHeight;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            if (i == cCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight + 5;
            }
        }

        this.setMeasuredDimension(modeWidth == 1073741824 ? sizeWidth : width, modeHeight == 1073741824 ? sizeHeight : height);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        this.mAllViews.clear();
        this.mLineHeight.clear();
        int width = this.getWidth();
        int lineWidth = 0;
        int lineHeight = 0;
        List<View> lineViews = new ArrayList();
        int cCount = this.getChildCount();

        int left;
        int i;
        int j;
        for (left = 0; left < cCount; ++left) {
            View child = this.getChildAt(left);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            i = child.getMeasuredWidth();
            j = child.getMeasuredHeight();
            if (i + lp.leftMargin + lp.rightMargin + lineWidth > width) {
                this.mLineHeight.add(Integer.valueOf(lineHeight));
                this.mAllViews.add(lineViews);
                lineWidth = 0;
                lineViews = new ArrayList();
            }

            lineWidth += i + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, j + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);
        }

        this.mLineHeight.add(Integer.valueOf(lineHeight));
        this.mAllViews.add(lineViews);
        left = 0;
        int top = 0;
        int lineNums = this.mAllViews.size();

        for (i = 0; i < lineNums; ++i) {
            List<View> lineViews1 = (List) this.mAllViews.get(i);
            lineHeight = ((Integer) this.mLineHeight.get(i)).intValue();
            Log.e("FlowLayout", "第" + i + "行 ：" + lineViews1.size() + " , " + lineViews1);
            Log.e("FlowLayout", "第" + i + "行， ：" + lineHeight);

            for (j = 0; j < lineViews1.size(); ++j) {
                View child = (View) lineViews1.get(j);
                if (child.getVisibility() != View.GONE) {
                    MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                    int lc = left + lp.leftMargin;
                    int tc = top + lp.topMargin;
                    int rc = lc + child.getMeasuredWidth();
                    int bc = tc + child.getMeasuredHeight();
                    Log.e("FlowLayout", child + " , l = " + lc + " , t = " + t + " , r =" + rc + " , b = " + bc);
                    child.layout(lc, tc, rc, bc);
                    left += child.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
                }
            }

            left = 0;
            top += lineHeight;
        }

    }
}
