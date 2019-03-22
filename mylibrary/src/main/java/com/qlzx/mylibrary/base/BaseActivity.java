package com.qlzx.mylibrary.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.qlzx.mylibrary.R;
import com.qlzx.mylibrary.util.DialogUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.TitleBar;
//import com.umeng.analytics.MobclickAgent;

import flyn.Eyes;


/**
 * Created by 87901 on 2016/9/20.
 */

public abstract class BaseActivity extends AppCompatActivity implements LoadingLayout.OnReloadListener {

    protected FrameLayout containerView;
    protected LoadingLayout loadingLayout;
    protected TitleBar titleBar;
    public DialogUtil mDialogUtil;
    public Dialog mBaseDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        loadingLayout = (LoadingLayout) findViewById(R.id.loading_layout);
        loadingLayout.setOnReloadListener(this);
        containerView = (FrameLayout) findViewById(R.id.container_view);
        View contentView = LayoutInflater.from(this).inflate(getContentView(), containerView, false);
        containerView.addView(contentView);
        mDialogUtil = new DialogUtil(this);
        initView();
        getData();

        ActionBar supportActionBar = getSupportActionBar();
        if (null != supportActionBar) {
            supportActionBar.hide();  //隐藏掉标题栏
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //  大于等于24即为7.0及以上执行内容
            Eyes.translucentStatusBar(BaseActivity.this, true);  //translucent 透明的
        } else {
            //  低于24即为7.0以下执行内容
            Eyes.translucentStatusBar(BaseActivity.this, false);  //translucent 透明的
        }

    }

    public abstract int getContentView();

    public abstract void initView();

    public abstract void getData();

    public void showTitleBar() {
        titleBar.setVisibility(View.VISIBLE);
    }

    public void hideTitleBar() {
        titleBar.setVisibility(View.GONE);
    }

    public static void startActivity(Context context, Class c) {
        String name = context.getClass().getName();
        Intent intent = new Intent(context, c);
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity activity, Class c, int requestCode) {
        Intent intent = new Intent(activity, c);
        activity.startActivityForResult(intent, requestCode);
    }

    public void showLoadingDialog(String msg) {
        if (msg != null) {
            if (mBaseDialog != null && mBaseDialog.isShowing()) {
                mBaseDialog.dismiss();
                mBaseDialog = null;
            }
            mBaseDialog = mDialogUtil.showLoading(msg);
            mBaseDialog.setCancelable(false);
        }
    }

    public void hideLoadingDialog() {
        if (mBaseDialog != null && mBaseDialog.isShowing()) {
            mBaseDialog.dismiss();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
      //  MobclickAgent.onResume(this);
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
       // MobclickAgent.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 隐藏输入法
     */
    public void hideSoftInput(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }


    @Override
    public void onReload(View v) {
        reload();
    }

    protected void reload() {

    }
}
