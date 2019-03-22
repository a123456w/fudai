package com.ruirong.chefang.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviCommonModule;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenlipeng on 2018/1/3 0003
 * describe:  商城地图页面
 */
public class MapActivity extends BaseActivity {
    @BindView(R.id.mv_mapview)
    MapView mMapView;
    @BindView(R.id.iv_myback)
    ImageView ivMyback;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_shopname)
    TextView tvShopname;
    @BindView(R.id.tv_distanse)
    TextView tvDistanse;
    @BindView(R.id.tv_detailaddress)
    TextView tvDetailaddress;
    @BindView(R.id.tv_togo)
    TextView tvTogo;
    private BaiduMap mBaiduMap;
    private String longTitude;
    private String latiTude;
    private String shopname;
    private String distanse;
    private String address;
    private double mystartLongtitu ;
    private double mystartLatitu;

    private final static String MAPLONGTITUDE = "MAPLONGTITUDE";
    private final static String MAPLATITUDE = "MAPLATITUDE";
    private final static String MAPSHOPNAME = "MAPSHOPNAME";
    private final static String MAPDISTANSE = "MAPDISTANSE";
    private final static String MAPADDRESS = "MAPADDRESS";


    /**
     * 导航初始化相关
     */
    private String mSDCardPath = "";
    private static final String APP_FOLDER_NAME = "YJIAFUDAI";
    private final static String authBaseArr[] =
            {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
    private final static int authBaseRequestCode = 1;
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    String authinfo = null;
    private boolean hasInitSuccess = false;


    private final String TAG = BNDemoGuideActivity.class.getName();
    private BNRoutePlanNode mBNRoutePlanNode = null;
    private BaiduNaviCommonModule mBaiduNaviCommonModule = null;


    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();


    /*
     * 对于导航模块有两种方式来实现发起导航。 1：使用通用接口来实现 2：使用传统接口来实现
     *
     */
    // 是否使用通用接口
    private boolean useCommonInterface = true;




    @Override
    public int getContentView() {
        return R.layout.activity_map;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();/***/
        tvTitle.setText("商家地图");
        longTitude = getIntent().getStringExtra(MAPLONGTITUDE);
        latiTude = getIntent().getStringExtra(MAPLATITUDE);
        shopname = getIntent().getStringExtra(MAPSHOPNAME);
        distanse = getIntent().getStringExtra(MAPDISTANSE);
        address = getIntent().getStringExtra(MAPADDRESS);

/**
 * 地图
 */
        tvShopname.setText(shopname);
        tvDistanse.setText(distanse+"km");
        tvDetailaddress.setText(address);
        //地图
        //获取地图控件引用
        mBaiduMap = mMapView.getMap();
        //定义Maker坐标点

//        40.1511790000,116.4052570000
        LatLng point = new LatLng(Double.parseDouble(latiTude), Double.parseDouble(longTitude));
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(point)  //标注居中显示!!!!!!!!
                .zoom(15)//缩放级别(3-21)21是放大到最大
                .build();

        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        mBaiduMap.addOverlay(option);

/**
 * 定位
 */
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数

        LocationClientOption options = new LocationClientOption();

        options.setIsNeedAddress(true);
//可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true
        options.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        options.setCoorType("bd09ll");
//可选，设置返回经纬度坐标类型，默认gcj02
//gcj02：国测局坐标；
//bd09ll：百度经纬度坐标；
//bd09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        options.setScanSpan(0);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        options.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        options.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        options.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        options.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        options.setWifiCacheTimeOut(5 * 60 * 1000);
//可选，7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位

        options.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false


        mLocationClient.setLocOption(options);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        mLocationClient.start();



    }

    /**
     * 定位
     */
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

            mystartLatitu = location.getLatitude();    //获取纬度信息
            mystartLongtitu = location.getLongitude();    //获取经度信息


            Log.e("经纬度", String.valueOf(mystartLatitu) + "____" + String.valueOf(mystartLongtitu));
            Log.e("经纬度存储", new PreferencesHelper(MapActivity.this).getLatitude() + "____" + new PreferencesHelper(MapActivity.this).getLongTitude());

            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明



        }
    }

    @Override
    public void getData() {

    }

    @OnClick({R.id.iv_myback, R.id.tv_togo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_myback:
                finish();
                break;
            case R.id.tv_togo:
                Log.e("mainactivity",longTitude);
                Log.e("mainactivity",latiTude);
                Log.e("mainactivity","起点经度"+mystartLongtitu);
                Log.e("mainactivity", "起点维度"+mystartLatitu);
                Log.e("mainactivity",BaiduNaviManager.isNaviInited()+"");
                Log.e("mainactivity","点击导航");
                if (BaiduNaviManager.isNaviInited()){

                    if ((!TextUtils.isEmpty(longTitude))&&(!TextUtils.isEmpty(latiTude))&&(!TextUtils.isEmpty(String.valueOf(mystartLatitu))&&(!TextUtils.isEmpty(String.valueOf(mystartLongtitu)))))
                    {
                        showLoadingDialog("正在规划路线，请等待...");
                        routeplanToNavi(BNRoutePlanNode.CoordinateType.BD09LL);
                    }

                }

                break;
        }
    }

    /**
     * @param context
     * @param longtitu 经度
     * @param latitude 维度
     * @param shopName 店名
     * @param distanse 距离
     * @param address  地址
     */
    public static void startActivityWithParmeter(Context context, String longtitu, String latitude, String shopName, String distanse, String address) {
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra(MAPLONGTITUDE, longtitu);
        intent.putExtra(MAPLATITUDE, latitude);
        intent.putExtra(MAPSHOPNAME, shopName);
        intent.putExtra(MAPDISTANSE, distanse);
        intent.putExtra(MAPADDRESS, address);
        context.startActivity(intent);
    }



    /****************发起算路*****************/

    private void routeplanToNavi(BNRoutePlanNode.CoordinateType coType) {
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        switch(coType) {

            case BD09LL: {

                sNode = new BNRoutePlanNode(mystartLongtitu,
                        mystartLatitu, "", null, coType);
                eNode = new BNRoutePlanNode(Double.parseDouble(longTitude),
                        Double.parseDouble(latiTude), "", null, coType);

                Log.e("mainactivity","获取经纬度");
                break;
            }
            default : ;
        }
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            Log.e("mainactivity","开始算路");
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode),eventListerner);
        }
    }
    BaiduNaviManager.NavEventListener eventListerner = new BaiduNaviManager.NavEventListener() {

        @Override
        public void onCommonEventCall(int what, int arg1, int arg2, Bundle bundle) {

        }
    };


    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNodes = null;
        public DemoRoutePlanListener(BNRoutePlanNode node){
            mBNRoutePlanNodes = node;
        }

        @Override
        public void onJumpToNavigator() {
            Log.e("mainactivity","进入回调");

            Log.e("mainactivity","即将跳转");

            Intent intent = new Intent(MapActivity.this, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);

            hideLoadingDialog();




        }
        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub

        }
    }



}
