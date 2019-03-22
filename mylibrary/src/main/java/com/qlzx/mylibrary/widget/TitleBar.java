package com.qlzx.mylibrary.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qlzx.mylibrary.R;

/**
 * Created by DreamSpark on 2016/11/7.
 */
public class TitleBar extends RelativeLayout {
    private ImageView iv_right;
    private ImageView iv_left;
    private TextView tv_title;
    private TextView tv_right;
    private TextView tv_left;
    private RelativeLayout rl_left;
    private RelativeLayout rl_right;
    private Context mContext;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        mContext = context;

    }

    private void initView(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.title_bar, this);
        iv_right = (ImageView) view.findViewById(R.id.iv_titlebar_right);
        iv_left = (ImageView) view.findViewById(R.id.iv_titlebar_left);
        tv_title = (TextView) view.findViewById(R.id.tv_titlebar_title);
        tv_right = (TextView) view.findViewById(R.id.tv_titlebar_right);
        tv_left = (TextView) view.findViewById(R.id.tv_titlebar_left);
        rl_left = (RelativeLayout) view.findViewById(R.id.rl_left);
        rl_right = (RelativeLayout) view.findViewById(R.id.rl_right);

        iv_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }
        });
    }

    public void setTitleText(String titleText) {
        tv_title.setText(titleText);
    }


    public void setRightImageRes(int resId) {
        iv_right.setVisibility(VISIBLE);
        iv_right.setImageResource(resId);
    }

    public String getRightTextRes(){
        String str = tv_right.getText().toString().trim();
        return str;
    }

    public void setRightTextRes(String text) {
        tv_right.setVisibility(VISIBLE);
        tv_right.setText(text);
    }

    public void changeRightTextRes(String text){
        tv_right.setText("");
        tv_right.setText(text);
    }

    public void setRightImageClick(OnClickListener listener) {
        iv_right.setOnClickListener(listener);
    }

    public void setRightTextClick(OnClickListener listener) {
        tv_right.setOnClickListener(listener);
    }


    public TextView getTvRight() {
        return tv_right;
    }


    public ImageView getIbRight(){
        return iv_right;
    }

    public void setLeftImageRes(int resId) {
        iv_left.setVisibility(VISIBLE);
        iv_left.setImageResource(resId);
    }

    public String getLeftTextRes(){
        String str = tv_left.getText().toString().trim();
        return str;
    }

    public void setLeftTextRes(String text) {
        tv_left.setVisibility(VISIBLE);
        tv_left.setText(text);
    }

    public void changeLeftTextRes(String text){
        tv_left.setText("");
        tv_left.setText(text);
    }

    public void setLeftImageClick(OnClickListener listener) {
        iv_left.setOnClickListener(listener);
    }

    public void setLeftTextClick(OnClickListener listener) {
        tv_left.setOnClickListener(listener);
    }


    public TextView getTvLeft() {
        return tv_left;
    }


    public ImageView getIbLeft(){
        return iv_left;
    }

    public void setLeftGone(){
        rl_left.setVisibility(GONE);
    }

    public void setRightGone(){
        rl_right.setVisibility(GONE);
    }

}
