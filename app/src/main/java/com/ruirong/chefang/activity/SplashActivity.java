package com.ruirong.chefang.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.StringUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.ruirong.chefang.MyApplication;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.UserInforBean;
import com.ruirong.chefang.http.RemoteApi;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by guo on 2017/4/25.
 * 欢迎页
 */

public class SplashActivity extends AppCompatActivity{

    @BindView(R.id.iv_splansh)
    ImageView ivSplansh;
    @BindView(R.id.btn_skip)
    TextView btnSkip;
    @BindView(R.id.jump)
    TextView jump ;
    private BaseSubscriber<BaseBean<UserInforBean>> userInforSubscriber;
    private PreferencesHelper preferencesHelper;
    private final int REQUIRES_PERMISSION = 0;
    private static final int REQUEST_CODE_SETTING = 300;
    private String token;
    Timer timer = new Timer();
    private int time=3;



    private MyLocationListener myListener = new MyLocationListener();
    public LocationClient mLocationClient = null;
    /**
     * Rationale支持，这里自定义对话框。
     */
    private RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            AlertDialog.newBuilder(SplashActivity.this)
                    .setTitle("友好提醒")
                    .setMessage("为保证APP的正常运行，需要以下权限:\n1.访问SD卡（选择图片等功能）\n2.调用摄像头（扫码下单等功能）\n3.打电话功能\n4.获取位置权限")
                    .setPositiveButton("好，给你", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            rationale.resume();
                        }
                    })
                    .setNegativeButton("依然拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            rationale.cancel();
                        }
                    }).show();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        ActionBar supportActionBar = getSupportActionBar();
        if (null != supportActionBar) {
            supportActionBar.hide();  //隐藏掉标题栏
        }
        preferencesHelper = new PreferencesHelper(this);
        token = preferencesHelper.getToken();

        LogUtil.d("SplashActivity token--" + token);
        requestPermission();
        initLocation();
        timer.schedule(task,1000,1000);
    }

    TimerTask task=new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                        btnSkip.setText(String.valueOf(time));
                        time--;
                        if (time<0){
                            timer.cancel();

                    }

                }
            });
        }
    };

    /**
     * 初始化定位
     */
    public void initLocation(){

        mLocationClient = new LocationClient(SplashActivity.this);
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

            PreferencesHelper preferencesHelper = new PreferencesHelper(SplashActivity.this);

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




    private void requestPermission() {
        // 申请多个权限。
        AndPermission.with(this)
                .requestCode(REQUIRES_PERMISSION)
                .permission(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE,Manifest.permission.ACCESS_COARSE_LOCATION)
                .callback(permissionListener)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(rationaleListener)
                .start();
    }

    /**
     * 回调监听。
     */
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantPermissions) {
            LogUtil.d("权限申请成功  onSucceed");
            switch (requestCode) {

                case REQUIRES_PERMISSION: {

                    if (preferencesHelper.isFirstTime()) {
                        gotoGuide();
                    } else {
                        if (TextUtils.isEmpty(token)) {
                            gotoMain();
                        } else {
                            getUserInfor(token);
                        }
                    }
                    break;
                }
            }
        }


        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            LogUtil.d("权限申请成功  onFailed");
            switch (requestCode) {

                case REQUIRES_PERMISSION: {
                    // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
                    if (AndPermission.hasAlwaysDeniedPermission(SplashActivity.this, deniedPermissions)) {
                        // 第一种：用默认的提示语。
//                AndPermission.defaultSettingDialog(ListenerActivity.this, REQUEST_CODE_SETTING).show();

                        // 第二种：用自定义的提示语。
                        AndPermission.defaultSettingDialog(SplashActivity.this, REQUEST_CODE_SETTING)
                                .setTitle("权限申请失败")
                                .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                                .setPositiveButton("好，去设置")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ToastUtil.showToast(SplashActivity.this, "权限申请失败，无法进入app");
                                        finish();
                                    }
                                })
                                .show();

//            第三种：自定义dialog样式。
//            SettingService settingService = AndPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
//            你的dialog点击了确定调用：
//            settingService.execute();
//            你的dialog点击了取消调用：
//            settingService.cancel();
                    } else {
                        ToastUtil.showToast(SplashActivity.this, "权限申请失败，无法进入app");
                        finish();
                    }

                    break;
                }
            }


        }
    };

    /**
     * 获取用户信息。
     *
     * @param token
     */
    public void getUserInfor(String token) {
        userInforSubscriber = new BaseSubscriber<BaseBean<UserInforBean>>(this, null) {
            @Override
            public void onNext(BaseBean<UserInforBean> userInforBeanBaseBean) {
                super.onNext(userInforBeanBaseBean);
                if (userInforBeanBaseBean.code == 0) {
                    UserInforBean userInforBean = userInforBeanBaseBean.data;
                    preferencesHelper.saveUserInfo(new Gson().toJson(userInforBean));
                    gotoMain();
                } else if (userInforBeanBaseBean.code == 4 || userInforBeanBaseBean.code == 5) {

                    preferencesHelper.clear();
                    MyApplication.getMainHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(SplashActivity.this, "账号已失效，请重新登录");
                            LoginActivity.startActivity(SplashActivity.this, true);
                            finish();
                        }
                    }, 2000);
                } else {
                    preferencesHelper.clear();
                    ToastUtil.showToast(SplashActivity.this, userInforBeanBaseBean.message);
                    gotoMain();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                gotoMain();
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).userInfor(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userInforSubscriber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userInforSubscriber != null && userInforSubscriber.isUnsubscribed()) {
            userInforSubscriber.unsubscribe();
        }

    }

    private void gotoMain() {
        MyApplication.getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    private void gotoGuide() {
        MyApplication.getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_SETTING: {
                LogUtil.d("从设置回来");
                requestPermission();
                break;
            }
        }
    }

    /**
     * 跳转到主页面
     */
    @OnClick(R.id.rl_jump)
    public void jump(){
        if (preferencesHelper.isFirstTime()){
            return;
        }else {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

}
