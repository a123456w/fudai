package com.ruirong.chefang;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.alibaba.security.rp.RPSDK;
import com.baidu.mapapi.SDKInitializer;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import cn.jpush.android.api.JPushInterface;
/*import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;*/

/**
 * Created by guo on 2017/3/21.
 */

public class MyApplication extends Application {
    private static final String APP_ID = "wx5d1eaf86165c26d4";
    //   private IWXAPI api ;
    private static Handler mainHandler;//全局的handler
    private static Context mContext;

    {

    }

    @Override
    public void onCreate() {
        super.onCreate();
       /* if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            LogUtil.d("isInAnalyzerProcess");
            return;
        }
       // LeakCanary.install(this);
*/
        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
        SophixManager.getInstance().queryAndLoadNewPatch();
        //分享初始化
        PlatformConfig.setWeixin("wx5d1eaf86165c26d4", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setQQZone("1106153309", "VPhdG6OAne52jxNP");
        //分享初始化
        UMShareAPI.get(this);

        mContext = this;
        //百度地图初始化
        SDKInitializer.initialize(this);

        //活体认证初始化
        RPSDK.initialize(this);

        //初始化mainHandler
        mainHandler = new Handler();

        LoadingLayout.getConfig()
                .setErrorText("出错啦~请稍后重试！")
                .setEmptyText("抱歉，暂无数据")
                .setNoNetworkText("无网络连接，请检查您的网络···")
                .setErrorImage(com.qlzx.mylibrary.R.drawable.net_error)
                .setEmptyImage(R.drawable.empty)
                .setNoNetworkImage(com.qlzx.mylibrary.R.drawable.no_wifi)
                .setAllTipTextColor(com.qlzx.mylibrary.R.color.tipTextColor)
                .setAllTipTextSize(12)
                .setAllEmptyTextSize(18)
                .setReloadButtonText("点我重试哦")
                .setReloadButtonTextSize(12)
                .setReloadButtonTextColor(com.qlzx.mylibrary.R.color.tipTextColor)
                //   .setReloadButtonWidthAndHeight(DimenUtils.dip2px(this,50),DimenUtils.dip2px(this,13))
                .setAllPageBackgroundColor(com.qlzx.mylibrary.R.color.loadingBackgroundColor)
                .setLoadingPageLayout(com.qlzx.mylibrary.R.layout.widget_loading_page);


        // 将应用的ID注册到应用中
        //    regToWx();


        //初始化极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);



    }

   /* private void regToWx(){
        api = WXAPIFactory.createWXAPI(this,APP_ID,true);
        api.registerApp(APP_ID);
    }*/

    public static Context getContext() {
        return mContext;
    }

    public static Handler getMainHandler() {
        return mainHandler;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        // initialize必须放在attachBaseContext最前面，初始化代码直接写在Application类里面，切勿封装到其他类。
        SophixManager.getInstance().setContext(this)
                .setAppVersion(String.valueOf(getAppVersion()))
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        Log.d("MyApplication",""+code);
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                        }
                    }
                }).initialize();
    }
    private int getAppVersion(){
        int versionCode = 19;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}