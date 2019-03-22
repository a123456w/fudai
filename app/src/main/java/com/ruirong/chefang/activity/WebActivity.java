    package com.ruirong.chefang.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.util.DialogUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.util.AndroidBug5497Workaround;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by guo on 2017/11/15.
 */

public class WebActivity extends BaseActivity {
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private String url;
    public DialogUtil mDialogUtil;
    public Dialog mBaseDialog;
 /*   @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

    }*/

    @Override
    public int getContentView() {
        return R.layout.activity_web;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        ButterKnife.bind(this);
        ActionBar supportActionBar = getSupportActionBar();
        if (null != supportActionBar) {
            supportActionBar.hide();  //隐藏掉标题栏
        }
        mDialogUtil = new DialogUtil(this);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        LogUtil.e("url",url);
        String title = intent.getStringExtra("title");
        tvTitle.setText(title);
        titleBar.setTitleText(title);
        AndroidBug5497Workaround.assistActivity(this);
    }

    @Override
    public void getData() {
        //设置webview的屏幕适配
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.getJavaScriptCanOpenWindowsAutomatically();

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoadingDialog("");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoadingDialog();
            }
        });
        webView.loadUrl(url);
    }

    public void showLoadingDialog(String msg) {
        if (msg!=null) {
            if (mBaseDialog != null && mBaseDialog.isShowing()) {
                mBaseDialog.dismiss();
                mBaseDialog = null;
            }
            mBaseDialog = mDialogUtil.showLoading(msg);
        }
    }
    public void hideLoadingDialog() {
        if (mBaseDialog != null && mBaseDialog.isShowing()) {
            mBaseDialog.dismiss();
        }
    }

    @OnClick(R.id.tv_myback)
    public void back(){
        finish();
    }
    public static void startActivity(Context context, String title, String url){
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }
}
