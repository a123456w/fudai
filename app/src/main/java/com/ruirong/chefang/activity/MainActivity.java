package com.ruirong.chefang.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.FileCallBack;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.http.OkHttpDownloadManager;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.MyApplication;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.UpdateBean;
import com.ruirong.chefang.event.MessageCountEvent;
import com.ruirong.chefang.fragment.GarageFragment;
import com.ruirong.chefang.fragment.HomepageFragment;
import com.ruirong.chefang.fragment.LifeFragment;
import com.ruirong.chefang.fragment.MineFragment;
import com.ruirong.chefang.fragment.ShopMallFragment;
import com.ruirong.chefang.http.RemoteApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.rg_bottom_nav)
    RadioGroup rgBottomNav;

    private FragmentManager fragmentManager;
    private Fragment f1, f2, f3, f4;
    private double exitTime;
    private String versionName;

    private BaseSubscriber<BaseBean<UpdateBean>> checkUpdateSubscriber;
    private PreferencesHelper preferencesHelper;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    private final static String authBaseArr[] =
            { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION };
    private final static String authComArr[] = { Manifest.permission.READ_PHONE_STATE };
    private final static int authBaseRequestCode = 1;
    private final static int authComRequestCode = 2;

    private boolean hasInitSuccess = false;
    private boolean hasRequestComAuth = false;


    private String mSDCardPath = "";
    private static final String APP_FOLDER_NAME = "YJIAFUDAI";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            fragmentManager = getSupportFragmentManager();
            f1 = (HomepageFragment) fragmentManager.findFragmentByTag("home");
            f2 = (ShopMallFragment) fragmentManager.findFragmentByTag("shop");
            f3 = (GarageFragment) fragmentManager.findFragmentByTag("life");
            f4 = (MineFragment) fragmentManager.findFragmentByTag("mine");
        }
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }


    /**
     * 初始化
     */
    private void initNavi() {

        BNOuterTTSPlayerCallback ttsCallback = null;

        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            if (!hasBasePhoneAuth()) {

                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;

            }
        }

        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {

            }

            public void initSuccess() {
                initSetting();

            }

            public void initStart() {

            }

            public void initFailed() {
                Log.e("mainactivity","百度导航引擎初始化失败");
            }

        }, null, ttsHandler, ttsPlayStateListener);

    }

    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    // showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    // showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };


    private void initSetting() {
        // BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager
                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        BNaviSettingManager.setIsAutoQuitWhenArrived(true);
        Bundle bundle = new Bundle();
        // 必须设置APPID，否则会静音
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, "11132108");
        BNaviSettingManager.setNaviSdkParam(bundle);
    }


    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
            // showToastMsg("TTSPlayStateListener : TTS play end");
        }

        @Override
        public void playStart() {
            // showToastMsg("TTSPlayStateListener : TTS play start");
        }
    };




    private boolean hasBasePhoneAuth() {
        // TODO Auto-generated method stub

        PackageManager pm = this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Loading);
        hideTitleBar();/***/


        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数

        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
//可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
//可选，设置返回经纬度坐标类型，默认gcj02
//gcj02：国测局坐标；
//bd09ll：百度经纬度坐标；
//bd09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        option.setScanSpan(0);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
//可选，7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位

        option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false


        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        mLocationClient.start();


        rgBottomNav.setOnCheckedChangeListener(this);

        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        if (f1 == null) {
            f1 = new HomepageFragment();//首页
            fragmentManager.beginTransaction().add(R.id.main_content, f1, "home").commit();
        } else {
            hideAllFragment(fragmentManager.beginTransaction());
            fragmentManager.beginTransaction().show(f1);
        }


        preferencesHelper = new PreferencesHelper(this);


        if (initDirs()) {
            initNavi();
        }

    }
    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }





    @Override
    public void getData() {
        /**
         * 获取版本信息，进行版本更新
         */
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(versionName)) {
            checkVersion();
        }
    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息

            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息

            PreferencesHelper preferencesHelper = new PreferencesHelper(MainActivity.this);

            preferencesHelper.saveCityName(city);
            preferencesHelper.saveLongTitude(String.valueOf(longitude));
            preferencesHelper.saveLatitude(String.valueOf(latitude));

            Log.e("lllllllll", String.valueOf(longitude) + "____" + String.valueOf(latitude));

            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明


            Log.e("ssssssssssss", city + "latitude" + latitude + "longitude" + longitude);

        }
    }


    /**
     * 检查更新
     */

    public void checkVersion() {

        checkUpdateSubscriber = new BaseSubscriber<BaseBean<UpdateBean>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<UpdateBean> updateBeanBaseBean) {
                super.onNext(updateBeanBaseBean);
                if (updateBeanBaseBean.code == 0) {
                    UpdateBean updateBean = updateBeanBaseBean.data;
                    if (updateBean.getStatus() == 0) {

                    } else if (updateBean.getStatus() == 1) {
                        //有新版本
                        LogUtil.d("有新版本---" + updateBean.getVersion());
                        if (!preferencesHelper.getIgnoreVersion().equals(updateBean.getVersion())) {
                            showNewVersion(updateBean);
                        }

                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).checkUpdate(1, versionName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(checkUpdateSubscriber);
    }


    /**
     * 新版本dialog
     *
     * @param updateBean
     */
    private void showNewVersion(final UpdateBean updateBean) {
        String content = String.format("最新版本：%1$s\n新版本大小：%2$s\n\n更新内容\n%3$s", updateBean.getVersion(), updateBean.getSize(), updateBean.getUpdate_log());

        final AlertDialog dialog = new AlertDialog.Builder(this).create();

        dialog.setTitle("应用更新");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        float density = getResources().getDisplayMetrics().density;
        TextView tv = new TextView(this);
        tv.setMovementMethod(new ScrollingMovementMethod());
        tv.setVerticalScrollBarEnabled(true);
        tv.setTextSize(14);
        tv.setMaxHeight((int) (250 * density));

        dialog.setView(tv, (int) (25 * density), (int) (15 * density), (int) (25 * density), 0);

        tv.setText(content);
        //是否强制更新
        boolean forceUpdate = updateBean.getIs_install().equals("1");
        if (forceUpdate) {
            tv.setText("您需要更新应用才能继续使用\n\n" + content);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    File mApkFile = new File(getExternalCacheDir(), "yjfd_" + updateBean.getVersion() + ".apk");
                    if (mApkFile.exists()) {
                        install(MainActivity.this, mApkFile);
                    } else {
                        getDownloadUrl(updateBean.getUrl(), mApkFile);
                    }
                }
            });
        } else {
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "立即更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    File mApkFile = new File(getExternalCacheDir(), "yjfd_" + updateBean.getVersion() + ".apk");
                    if (mApkFile.exists()) {
                        install(MainActivity.this, mApkFile);
                    } else {
                        getDownloadUrl(updateBean.getUrl(), mApkFile);
                    }
                }
            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "以后再说", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "忽略该版", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    preferencesHelper.saveIgnoreVersion(updateBean.getVersion());

                }
            });
        }


        dialog.show();
    }

    /**
     * 安装
     *
     * @param context
     * @param file
     */
    public void install(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        } else {
            Uri uri = FileProvider.getUriForFile(context, "com.ruirong.chefang.fileprovider", file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void getDownloadUrl(final String url, final File mApkFile) {
        final OkHttpClient okHttpClient = new OkHttpClient();
        final Request build = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(build);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showToast(MainActivity.this, "下载失败");
                if (mApkFile.exists()) {
                    mApkFile.delete();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    final String downloadUrl = jsonObject.getString("data");
                    MyApplication.getMainHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            dialog.setMessage("下载中...");
                            dialog.setIndeterminate(false);
                            dialog.setCancelable(false);
                            dialog.show();
                            download(dialog, downloadUrl, mApkFile);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void download(final ProgressDialog dialog, String downloadUrl, final File mApkFile) {
        FileCallBack fileCallBack = new FileCallBack(this, mApkFile) {
            @Override
            public void inProgress(long current, long total) {
                int progress = (int) (current * 100 / total);
                LogUtil.d("下载apk---" + "current" + current + "---" + "total " + total);
                dialog.setProgress(progress);
            }

            @Override
            public void onSuccess(File response) {
                install(MainActivity.this, response);
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showToast(MainActivity.this, "下载失败");
                if (mApkFile.exists()) {
                    mApkFile.delete();
                }
            }

            @Override
            public void onAfter() {
                dialog.dismiss();
            }
        };
        OkHttpDownloadManager.getInstance().download(downloadUrl, fileCallBack);
    }

    /**
     * 隐藏所有Fragment
     *
     * @param transaction
     */
    public void hideAllFragment(FragmentTransaction transaction) {
        if (f1 != null) {
            transaction.hide(f1);
        }
        if (f2 != null) {
            transaction.hide(f2);
        }
        if (f3 != null) {
            transaction.hide(f3);
        }
        if (f4 != null) {
            transaction.hide(f4);
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        switch (checkedId) {
            //首页
            case R.id.rb_home:
                fragmentTransaction.show(f1);
                EventBusUtil.post(new MessageCountEvent());
                break;
            //车忘了
            case R.id.rb_mall:
                if (f2 == null) {
                    f2 = new ShopMallFragment();
                    fragmentTransaction.add(R.id.main_content, f2, "shop");
                } else {
                    fragmentTransaction.show(f2);
                }

                break;
            //吃住玩
            case R.id.rb_life:
                if (f3 == null) {
                    f3 = new LifeFragment();
                    fragmentTransaction.add(R.id.main_content, f3, "life");
                } else {
                    fragmentTransaction.show(f3);
                }

                break;
            //我的
            case R.id.rb_me:
                if (f4 == null) {
                    f4 = new MineFragment();
                    fragmentTransaction.add(R.id.main_content, f4, "mine");
                } else {
                    fragmentTransaction.show(f4);
                }

                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (f1 == null && fragment instanceof HomepageFragment)
            f1 = fragment;
        if (f2 == null && fragment instanceof ShopMallFragment)
            f2 = fragment;
        if (f3 == null && fragment instanceof LifeFragment)
            f3 = fragment;
        if (f4 == null && fragment instanceof MineFragment)
            f4 = fragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (checkUpdateSubscriber != null && checkUpdateSubscriber.isUnsubscribed()) {
            checkUpdateSubscriber.unsubscribe();
        }
    }

    @Override
    public void onReload(View v) {
        getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {

            } else {
                // ScanResult 为获取到的字符串
                String ScanResult = intentResult.getContents();
                ToastUtil.showToast(MainActivity.this, ScanResult);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
