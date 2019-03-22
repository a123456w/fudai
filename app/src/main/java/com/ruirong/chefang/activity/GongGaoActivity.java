package com.ruirong.chefang.activity;

import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.util.SharedUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/12.
 */

public class GongGaoActivity extends BaseActivity {

    @BindView(R.id.web)
    WebView web;
    private String url;
    private String title;
    private String content;

    @Override
    public int getContentView() {
        return R.layout.activity_gong_gao;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);

        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");

        if (url == null || title == null || content == null) {
            finish();
        }
        Log.i("XXX", url);
        titleBar.setTitleText(title);

        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setRightImageRes(R.drawable.ic_share);
        titleBar.setRightImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedUtil sharedUtil = new SharedUtil(GongGaoActivity.this);
                sharedUtil.initSharedG(GongGaoActivity.this, Constants.HOST + url, title, content);
            }
        });
    }

    @Override
    public void getData() {

        web.loadUrl(Constants.IMG_HOST + url);

        //声明WebSettings子类
        WebSettings webSettings = web.getSettings();

////如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
//        webSettings.setJavaScriptEnabled(true);
////支持插件
//        webSettings.setPluginsEnabled(true);

//设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//缩放操作
//        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
//        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(true); //隐藏原生的缩放控件

//其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
//        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

    }

}
