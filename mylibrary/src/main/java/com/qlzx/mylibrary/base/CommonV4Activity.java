package com.qlzx.mylibrary.base;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import flyn.Eyes;

/**
 * 直接嵌套业务Fragment
 * Created by Xuxx on 2017/6/24.
 */
public class CommonV4Activity extends FragmentActivity {
    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar supportActionBar = getActionBar();
        if (null != supportActionBar) {
            supportActionBar.hide();  //隐藏掉标题栏
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //  大于等于24即为7.0及以上执行内容
            Eyes.translucentStatusBar(this, true);  //translucent 透明的
        } else {
            //  低于24即为7.0以下执行内容
            Eyes.translucentStatusBar(this, false);  //translucent 透明的
        }
        String frag = getIntent().getStringExtra("fragment");
        if(frag != null){
            try {
                Class<?> cls = Class.forName(frag);
                if(cls != null){
                    fragment = (Fragment) cls.newInstance();
                    if(fragment != null){
                        fragment.setArguments(getIntent().getExtras());
                        getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(fragment != null){
            fragment.onActivityResult(requestCode,resultCode,data);
        }
    }
}
