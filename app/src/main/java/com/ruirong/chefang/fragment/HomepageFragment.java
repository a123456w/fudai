package com.ruirong.chefang.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollGridView;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.AllProductActivity;
import com.ruirong.chefang.activity.ClassifiedGoodsActivity;
import com.ruirong.chefang.activity.DeliciousFoodActivity;
import com.ruirong.chefang.activity.GarageDetailsActivity;
import com.ruirong.chefang.activity.GoToBuyABillActivity;
import com.ruirong.chefang.activity.GongGaoActivity;
import com.ruirong.chefang.activity.HotelDetailActivity;
import com.ruirong.chefang.activity.LuckBackRoomActivity;
import com.ruirong.chefang.activity.LuckbackHotelActivity;
import com.ruirong.chefang.activity.LuckyBagPrefectureActivity;
import com.ruirong.chefang.activity.NearbyShopDetailsActivity;
import com.ruirong.chefang.activity.RankingActivity;
import com.ruirong.chefang.activity.RechargeActivity;
import com.ruirong.chefang.activity.ScanAndPaymentActivity;
import com.ruirong.chefang.activity.SearchActivity;
import com.ruirong.chefang.activity.SpecialPrefectureActivity;
import com.ruirong.chefang.activity.SpecialtyDetailsActivity;
import com.ruirong.chefang.adapter.BagzoneAdapter;
import com.ruirong.chefang.adapter.HomeLableAdapter;
import com.ruirong.chefang.adapter.HotSaleAdapter;
import com.ruirong.chefang.adapter.ShopAndHouseAdapter;
import com.ruirong.chefang.bean.CookingSwipBean;
import com.ruirong.chefang.bean.GoodsBuyConfirmBean;
import com.ruirong.chefang.bean.HomeHeadBean;
import com.ruirong.chefang.bean.HomeLableBean;
import com.ruirong.chefang.bean.HomePageBean;
import com.ruirong.chefang.bean.KtvSwipBean;
import com.ruirong.chefang.bean.ShopAndHouseBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 首页
 * Created by dillon on 2017/12/25.
 */

public class HomepageFragment extends BaseFragment {
    @BindView(R.id.banner_top)
    Banner bannerTop;
    /*    @BindView(R.id.iv_pic)
        ImageView ivPic;*/
    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;
    /*    @BindView(R.id.tv_title)
        TextView tvTitle;*/
/*    @BindView(R.id.tv_people)
    TextView tvPeople;*/
    @BindView(R.id.nsgv_home_label)
    NoScrollGridView nsgvHomeLabel;
    @BindView(R.id.nsgv_home_bagzone)
    NoScrollGridView nsgvHomeBagzone;
    @BindView(R.id.nsgv_home_hotsale)
    NoScrollListView nsgvHomeHotsale;
    @BindView(R.id.nsgv_home_recomonendshop)
    NoScrollGridView nsgvHomeRecomonendshop;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.rl_top_search)
    LinearLayout rlTopSearch;
    Unbinder unbinder;
    /*    @BindView(R.id.ll_shop_headline)
        LinearLayout llShopHeadline;*/
    Unbinder unbinder1;
    Unbinder unbinder2;
    @BindView(R.id.vf_shop_headline)
    ViewFlipper vfShopHeadline;
    @BindView(R.id.img_home_1)
    ImageView imgHome1;
    @BindView(R.id.img_home_2)
    ImageView imgHome2;
    @BindView(R.id.img_home_3)
    ImageView imgHome3;

    @BindView(R.id.rl_home1)
    RelativeLayout rlHome1;
    @BindView(R.id.rl_home2)
    RelativeLayout rlHome2;
    @BindView(R.id.rl_home3)
    RelativeLayout rlHome3;
    //标签
    private HomeLableAdapter homeLableAdapter;
    //福袋专区
    private BagzoneAdapter bagzoneAdapter;
    //热销产品
    private HotSaleAdapter hotSaleAdapter;

    //推荐房产和店铺
    private ShopAndHouseAdapter shopAndHouseAdapter;

    private List<HomeLableBean> homeLableBeanList = new ArrayList<>();

    private List<HomeHeadBean> homeHeadBeansList = new ArrayList<>();

    //  房产和店铺的集合。
    private List<ShopAndHouseBean> shopAndHouseBeanList = new ArrayList<>();

    HomePageBean homePageBean;
    private static final String GARAGEDETAILHOUSEID = "GARAGEDETAILHOUSEID";
    private static final String GARAGEDETALTITLENAME = "GARAGEDETALTITLENAME";
    private final static String HOTELDETAILIDOFYIJIA = "HOTELDETAILIDOFYIJIA";
    private BaseSubscriber<BaseBean<GoodsBuyConfirmBean>> sweepOrderSubscriber;

    private boolean firstInto = true;

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_homepage, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        Log.d("TTTT", "" + new PreferencesHelper(mContext).getToken());
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        //TODO 标题颜色渐变。
        //标题栏颜色渐变
        final int imageHeight = DensityUtil.dp2px(mContext, 200 - 45 - 40);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY <= 0) {
                    rlTopSearch.setBackgroundColor(Color.argb((int) 0, 50, 157, 253));//AGB由相关工具获得，或者美工提供
                } else if (scrollY > 0 && scrollY <= imageHeight) {
                    float scale = (float) scrollY / imageHeight;
                    float alpha = (255 * scale);
                    // 只是layout背景透明(仿知乎滑动效果)
                    rlTopSearch.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    // tvSearch.setBackgroundColor(Color.argb((int) alpha, 144, 144, 144));

                } else {
                    rlTopSearch.setBackgroundColor(Color.argb((int) 255, 143, 196, 46));
                    // tvSearch.setBackgroundColor(Color.argb((int)255, 144, 144, 144));
                }
            }
        });


        homeLableAdapter = new HomeLableAdapter(nsgvHomeLabel);
        nsgvHomeLabel.setFocusable(false);
        nsgvHomeLabel.setAdapter(homeLableAdapter);
        //福袋专区
        bagzoneAdapter = new BagzoneAdapter(nsgvHomeBagzone);
        nsgvHomeBagzone.setFocusable(false);
        nsgvHomeBagzone.setAdapter(bagzoneAdapter);
        //热销产品
        hotSaleAdapter = new HotSaleAdapter(nsgvHomeHotsale);
        nsgvHomeHotsale.setFocusable(false);
        nsgvHomeHotsale.setAdapter(hotSaleAdapter);
        //推荐店铺和房产
        shopAndHouseAdapter = new ShopAndHouseAdapter(nsgvHomeRecomonendshop);
        nsgvHomeRecomonendshop.setFocusable(false);
        nsgvHomeRecomonendshop.setAdapter(shopAndHouseAdapter);


        //6个功能点
        nsgvHomeLabel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    Intent intent1 = new Intent(getActivity(), LuckbackHotelActivity.class);
                    intent1.putExtra("int_data", 100);
                    startActivity(intent1);
                } else if (i == 1) {
//                    Intent intent1 = new Intent(getActivity(), LuckBackRoomActivity.class);
//                    intent1.putExtra("int_data", 100);
//                    startActivity(intent1);
                    Intent intent = new Intent(getActivity(), DeliciousFoodActivity.class);
                    intent.putExtra("title","精品美食");
                    intent.putExtra("classify_id","4");
                    startActivity(intent);
                } else if (i == 2) {
                    Intent intent1 = new Intent(getActivity(), SpecialPrefectureActivity.class);
                    intent1.putExtra("int_data", 100);
                    startActivity(intent1);
                } else if (i == 3) {
                    if (TextUtils.isEmpty(new PreferencesHelper(getActivity()).getToken())) {
                        ToolUtil.goToLogin(getActivity());
                    } else {
                        goScan();
                    }
                } else if (i == 4) {
                    if (TextUtils.isEmpty(new PreferencesHelper(getContext()).getToken())) {
                        ToolUtil.goToLogin(getActivity());
                    } else {
                        Intent intent1 = new Intent(getActivity(), RechargeActivity.class);
                        intent1.putExtra("int_data", 100);
                        startActivity(intent1);
                    }
                } else if (i == 5) {
                    Intent intent5 = new Intent(getActivity(), RankingActivity.class);
                    startActivity(intent5);
                } else {

                    Intent intent1 = new Intent(getActivity(), SpecialPrefectureActivity.class);
                    intent1.putExtra("int_data", 100);
                    startActivity(intent1);
                }
            }
        });


        nsgvHomeBagzone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), LuckyBagPrefectureActivity.class);
                intent.putExtra("blessing_id", bagzoneAdapter.getData().get(i).getId());
                startActivity(intent);
            }
        });
        nsgvHomeHotsale.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, ClassifiedGoodsActivity.class);
                intent.putExtra("goods_id", hotSaleAdapter.getData().get(i).getId());
                intent.putExtra("shop_id", hotSaleAdapter.getData().get(i).getShop_id());
                startActivity(intent);

//                Intent intent1 = new Intent(getActivity(), HotSellProductDetailsActivity.class);
//                intent1.putExtra("int_data", 100);
//                startActivity(intent1);
            }
        });

        nsgvHomeRecomonendshop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShopAndHouseBean shopAndHouseBean = shopAndHouseAdapter.getData().get(i);

                if ((!TextUtils.isEmpty(shopAndHouseBean.getAddress1())) || (!TextUtils.isEmpty(shopAndHouseBean.getAddress2())) || (!TextUtils.isEmpty(shopAndHouseBean.getTitle()))) {
                    GarageDetailsActivity.startActivityWithParmeter(getActivity(), shopAndHouseBean.getId(), shopAndHouseBean.getSp_name());
                } else if ("酒店住宿".equals(shopAndHouseBean.getCate())) {
                    HotelDetailActivity.startActivityWithParmeter(getActivity(), shopAndHouseBean.getId());
                } else {
                    Intent intent1 = new Intent(getActivity(), NearbyShopDetailsActivity.class);
                    intent1.putExtra("shop_id", shopAndHouseBean.getId());
                    startActivity(intent1);
                }
            }
        });


//        Eyes.translucentStatusBar(getActivity(), false);
    }


    /**
     * 搜索
     */
    @OnClick(R.id.rl_search)
    public void register() {
        Intent intent1 = new Intent(getActivity(), SearchActivity.class);
        intent1.putExtra("int_data", 100);
        startActivity(intent1);
    }

    @Override
    public void getData() {
        //获取首页数据
        getHomePageData(new PreferencesHelper(getContext()).getLongTitude(),
                new PreferencesHelper(getContext()).getLatitude());

    }


    @Override
    public void onResume() {
        super.onResume();
        if (!firstInto) {
            getHomePageVerticalData(new PreferencesHelper(getContext()).getLongTitude(),
                    new PreferencesHelper(getContext()).getLatitude());
        }
        //获取首页数据
//        getHomePageData(new PreferencesHelper(getContext()).getLongTitude(),
//                new PreferencesHelper(getContext()).getLatitude());
    }


    /**
     * 添加商家浏览量
     * @param id   商家id
     */
    public void addBrowse(String id){
        HttpHelp.getInstance().create(RemoteApi.class).addBrowse(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(getActivity(),null){
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code==0){

                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });


    }



    /**
     * 进入扫描页
     */
    public void goScan() {

//        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
//        intentIntegrator
//                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
//                .setOrientationLocked(false)//扫描方向固定
//                .setPrompt("将条形码置于取景框内")
//                .setCaptureActivity(ScanAndPaymentActivity.class) // 设置自定义的activity是CustomActivity
//                .initiateScan(); // 初始化扫描
        IntentIntegrator.forSupportFragment(this)
                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                .setOrientationLocked(false)//扫描方向固定
                .setPrompt("将条形码/二维码置于取景框内")
                .setCaptureActivity(ScanAndPaymentActivity.class) // 设置自定义的activity是CustomActivity
                .initiateScan(); // 初始化扫描
//
//        IntentIntegrator.forSupportFragment(this).setCaptureActivity(ScanActivity.class).initiateScan();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
//                ToastUtil.showToast(mContext, "Cancelled");
            } else {
//                ToastUtil.showToast(mContext, "Scanned: " + result.getContents());
                //商家二维码会返回类似index.php?s=/Admin/Store/code/marking/6A75B12FFBB68B4BD8EAB4A72132BA7F9312.html
                //截取6A75B12FFBB68B4BD8EAB4A72132BA7F9312部分
                String content = result.getContents();

                if (content.contains("index.php?s=/Admin/Store/code/marking/")) {
                    int start = content.lastIndexOf("/");
                    int end = content.lastIndexOf(".");
                    String shopMark = content.substring(start + 1, end);
                    Log.i("XXX", shopMark);
                    Log.i("XXX", content);

                    showLoadingDialog("");
                    getSweepOrderInfo(new PreferencesHelper(mContext).getToken(), shopMark);

                } else if (content.contains("diancanid")&&content.contains("diancannum")){
                    Gson gson = new Gson();
                    Log.e("choseorder",content);
                    CookingSwipBean cookingSwipBean = gson.fromJson(content,CookingSwipBean.class);
                    if (cookingSwipBean!=null&&!TextUtils.isEmpty(cookingSwipBean.getDiancanid())){
                        AllProductActivity.startActivityWith(getActivity(),cookingSwipBean.getDiancanid(),cookingSwipBean.getDiancannum(),"1");
                    }
                } else if (content.contains("ktvid")&&content.contains("ktvnum")){
                    Gson gson = new Gson();
                    KtvSwipBean ktvSwipBean = gson.fromJson(content,KtvSwipBean.class);
                    if (ktvSwipBean!=null&&!TextUtils.isEmpty(ktvSwipBean.getKtvid())){
                        AllProductActivity.startActivityWith(getActivity(),ktvSwipBean.getKtvid(),ktvSwipBean.getKtvnum(),"2");
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    /**
     * 扫码后获取商家信息
     */
    private void getSweepOrderInfo(String token, String mark) {
        sweepOrderSubscriber = new BaseSubscriber<BaseBean<GoodsBuyConfirmBean>>(mContext, null) {
            @Override
            public void onNext(BaseBean<GoodsBuyConfirmBean> sweepOrderBeanBaseBean) {
                super.onNext(sweepOrderBeanBaseBean);
                hideLoadingDialog();
                if (sweepOrderBeanBaseBean.code == 0) {
//                    SweepOrderActivity.startActivity(mContext, sweepOrderBeanBaseBean.data);
                    Intent intent = new Intent(mContext, GoToBuyABillActivity.class);
                    intent.putExtra("shop_id", sweepOrderBeanBaseBean.data.getShop_id());
                    intent.putExtra("mode_name", sweepOrderBeanBaseBean.data.getName());
                    startActivity(intent);
                } else if (sweepOrderBeanBaseBean.code == 4) {
                    ToolUtil.loseToLogin(mContext);
                } else {
                    ToastUtil.showToast(mContext, sweepOrderBeanBaseBean.message);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                hideLoadingDialog();
                ToastUtil.showToast(mContext, getString(R.string.net_error));
                Log.i("XXX", throwable.getLocalizedMessage());
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).goodsBuyConfirm(token, mark)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sweepOrderSubscriber);
    }


    /**
     * 获取首页的数据
     *
     * @return
     */
    public void getHomePageData(String lng, String lat) {
        HttpHelp.getInstance().create(RemoteApi.class).getHomePageData(lng, lat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<HomePageBean>>(getActivity(), null) {
                    @Override
                    public void onNext(BaseBean<HomePageBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {

                            firstInto = false;
                            homePageBean = baseBean.data;

                            //获取banner数据
                            if (homePageBean != null) {
                                if (homePageBean.getThumb().size() > 0) {
                                    bindBannerData();
                                }
                            }


                            //获取首页的标签
                            getHomeLable();

                            //获取福袋专区的数据
                            if (homePageBean != null) {
                                if (homePageBean.getFudai().size() > 0) {
                                    rlHome1.setVisibility(View.VISIBLE);
                                    imgHome1.setVisibility(View.VISIBLE);
                                    bagzoneAdapter.setData(homePageBean.getFudai());
                                } else {
                                    rlHome1.setVisibility(View.GONE);
                                    imgHome1.setVisibility(View.GONE);
                                }
                            }
                            //获取热销产品的列表
                            if (homePageBean != null) {
                                if (homePageBean.getRexiao().size() > 0) {
                                    rlHome2.setVisibility(View.VISIBLE);
                                    imgHome2.setVisibility(View.VISIBLE);
                                    hotSaleAdapter.setData(homePageBean.getRexiao());
                                } else {
                                    rlHome2.setVisibility(View.GONE);
                                    imgHome2.setVisibility(View.GONE);
                                }
                            }

                            if (homePageBean.getTuishop().size() > 0 || homePageBean.getFangchan().size() > 0) {
                                rlHome3.setVisibility(View.VISIBLE);
                                imgHome3.setVisibility(View.VISIBLE);

                                if (homePageBean.getTuishop().size() > 0) {
                                    for (HomePageBean.TuishopBean tuishopBean :
                                            homePageBean.getTuishop()) {
                                        ShopAndHouseBean shopAndHouseBean = new ShopAndHouseBean();
                                        shopAndHouseBean.setId(tuishopBean.getId());
                                        shopAndHouseBean.setCover(tuishopBean.getCover());
                                        shopAndHouseBean.setSp_name(tuishopBean.getSp_name());
                                        shopAndHouseBean.setDp_grade(tuishopBean.getDp_grade());
                                        shopAndHouseBean.setCate(tuishopBean.getCate());
                                        shopAndHouseBean.setArea(tuishopBean.getArea());
                                        shopAndHouseBean.setDistance(tuishopBean.getDistance());
                                        shopAndHouseBean.setTitle("");
                                        shopAndHouseBean.setAddress1("");
                                        shopAndHouseBean.setAddress2("");
                                        shopAndHouseBeanList.add(shopAndHouseBean);
                                    }
                                }
                                if (homePageBean.getFangchan().size() > 0) {
                                    for (HomePageBean.FangchanBean fangchanBean :
                                            homePageBean.getFangchan()) {
                                        ShopAndHouseBean shopAndHouseBean = new ShopAndHouseBean();
                                        shopAndHouseBean.setId(fangchanBean.getId());
                                        shopAndHouseBean.setCover(fangchanBean.getCover());
                                        shopAndHouseBean.setSp_name(fangchanBean.getSp_name());
                                        shopAndHouseBean.setTitle(fangchanBean.getTitle());
                                        shopAndHouseBean.setAddress2(fangchanBean.getAddress2());
                                        shopAndHouseBean.setAddress1(fangchanBean.getAddress1());

                                        shopAndHouseBean.setDp_grade("");
                                        shopAndHouseBean.setDistance("");
                                        shopAndHouseBean.setCate("");
                                        shopAndHouseBean.setArea("");

                                        shopAndHouseBeanList.add(shopAndHouseBean);
                                    }
                                }

                                shopAndHouseAdapter.setData(shopAndHouseBeanList);

                            } else if (homePageBean.getTuishop().size() == 0 && homePageBean.getFangchan().size() == 0) {
                                rlHome3.setVisibility(View.GONE);
                                imgHome3.setVisibility(View.GONE);
                            }

                            homeHeadBeansList.clear();
                            //商圈头条
                            if (homePageBean != null) {
                                if (homePageBean.getGglunbo().size() > 0) {
                                    vfShopHeadline.setVisibility(View.VISIBLE);
                                    homeHeadBeansList.addAll(homePageBean.getGglunbo());
                                    headlinesa();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 获取首页垂直滚动数据
     * @return
     */
    public void getHomePageVerticalData(String lng, String lat) {
        HttpHelp.getInstance().create(RemoteApi.class).getHomePageData(lng, lat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<HomePageBean>>(getActivity(), null) {
                    @Override
                    public void onNext(BaseBean<HomePageBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {

                            firstInto = false;
                            homePageBean = baseBean.data;

                            homeHeadBeansList.clear();
                            //商圈头条
                            if (homePageBean != null) {
                                if (homePageBean.getGglunbo().size() > 0) {
                                    vfShopHeadline.setVisibility(View.VISIBLE);
                                    homeHeadBeansList.addAll(homePageBean.getGglunbo());
                                    headlinesa();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 为顶部轮播添加图片
     */
    public void bindBannerData() {
        bannerTop.setImages(homePageBean.getThumb())
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        String imgUrl = (String) path;
                        Log.i("XXX", imgUrl);
                        GlideUtil.display(mContext, Constants.IMG_HOST + imgUrl, imageView);
                    }
                }).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                // startActivity((BGAPhotoPreviewActivity.newIntent(mContext, null, bannerList, position)));
            }
        }).start();
    }

    List<View> views = new ArrayList<>();

    /**
     * 商圈头条
     */
    private void headlinesa() {
        setView();

        vfShopHeadline.removeAllViews();
        for (int i = 0; i < views.size(); i++) {
            vfShopHeadline.addView(views.get(i));
        }
        vfShopHeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToastUtil.showToast(mContext, "dianji" + vfShopHeadline.getDisplayedChild());
                int i = vfShopHeadline.getDisplayedChild();

                HomeHeadBean headBean = homeHeadBeansList.get(i);

                /**
                 * 添加浏览量
                 */
                addBrowse(headBean.getId());

                String type = headBean.getType();
                if ("1".equals(type)) {
                    Intent intent1 = new Intent(getActivity(), HotelDetailActivity.class);
                    intent1.putExtra(HOTELDETAILIDOFYIJIA, headBean.getType_id());
                    startActivity(intent1);
                } else if ("2".equals(type)) {
                    Intent intent1 = new Intent(getActivity(), GarageDetailsActivity.class);
                    intent1.putExtra(GARAGEDETAILHOUSEID, headBean.getType_id());
                    intent1.putExtra(GARAGEDETALTITLENAME, "房产");
                    startActivity(intent1);
                } else if ("3".equals(type)) {
                    Log.e("Specialty",""+headBean.getType_id());
                    Intent intent3 = new Intent(getActivity(), SpecialtyDetailsActivity.class);
                    intent3.putExtra("Specialty_id", headBean.getType_id());
                    startActivity(intent3);
                } else if ("4".equals(type)) {
                    Intent intent4 = new Intent(getActivity(), LuckyBagPrefectureActivity.class);
                    intent4.putExtra("blessing_id", headBean.getType_id());
                    startActivity(intent4);
                } else if ("5".equals(type)) {
                    Intent intent5 = new Intent(getActivity(), NearbyShopDetailsActivity.class);
                    intent5.putExtra("shop_id", headBean.getType_id());
                    startActivity(intent5);
                } else if ("6".equals(type)) {
                    Intent intent6 = new Intent(getActivity(), GongGaoActivity.class);
                    intent6.putExtra("url", headBean.getUrl());
                    intent6.putExtra("title", headBean.getTitle());
                    intent6.putExtra("content", headBean.getContent());
                    startActivity(intent6);
                }
//                Intent intent1 = new Intent(getActivity(), ShopHeadlineActivity.class);
//                intent1.putExtra("int_data", 100);
//                startActivity(intent1);
            }
        });

    }

    /**
     * 商圈头条
     */
    private void setView() {
        views.clear();
        for (int i = 0; i < homeHeadBeansList.size(); i++) {
            //设置滚动的单个布局
            //初始化布局的控件
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_home_headline, null);
            ImageView ivPic = (ImageView) moreView.findViewById(R.id.iv_pic);
            TextView tvTitle = (TextView) moreView.findViewById(R.id.tv_title);
            TextView tvPeople = (TextView) moreView.findViewById(R.id.tv_people);
            tvTitle.setText(homeHeadBeansList.get(i).getContent());
            tvPeople.setText(homeHeadBeansList.get(i).getLiulan() + "人浏览");
            GlideUtil.display(getContext(), Constants.IMG_HOST + homeHeadBeansList.get(i).getPic(), ivPic);
            //添加到循环滚动数组里面去
            views.add(moreView);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            vfShopHeadline.stopFlipping();
        } else {
            vfShopHeadline.startFlipping();
        }
    }

    /**
     * 获取首页的标签
     */
    private void getHomeLable() {
        HomeLableBean homeLableBean = new HomeLableBean();
        homeLableBean.setContent("超值服务");
        homeLableBean.setName("精品酒店");
        homeLableBean.setPic(R.drawable.ic_returnhappy);
        homeLableBeanList.add(homeLableBean);
        HomeLableBean homeLableBean1 = new HomeLableBean();
        homeLableBean1.setContent("得天独厚");
        homeLableBean1.setName("精品美食");
        homeLableBean1.setPic(R.drawable.good_foods);
        homeLableBeanList.add(homeLableBean1);
        HomeLableBean homeLableBean2 = new HomeLableBean();
        homeLableBean2.setContent("畅享优惠");
        homeLableBean2.setName("安康特产");
        homeLableBean2.setPic(R.drawable.image_special_area);
        homeLableBeanList.add(homeLableBean2);
        HomeLableBean homeLableBean3 = new HomeLableBean();
        homeLableBean3.setContent("方便支付");
        homeLableBean3.setName("扫码付款");
        homeLableBean3.setPic(R.drawable.image_scan_code);
        homeLableBeanList.add(homeLableBean3);
        HomeLableBean homeLableBean4 = new HomeLableBean();
        homeLableBean4.setContent("快捷安全");
        homeLableBean4.setName("充值");
        homeLableBean4.setPic(R.drawable.image_recharg);
        homeLableBeanList.add(homeLableBean4);
        HomeLableBean homeLableBean5 = new HomeLableBean();
        homeLableBean5.setContent("火爆榜单");
        homeLableBean5.setName("排行榜");
        homeLableBean5.setPic(R.drawable.image_ranking);
        homeLableBeanList.add(homeLableBean5);

        homeLableAdapter.setData(homeLableBeanList);
    }


  /*  @OnClick({R.id.ll_shop_headline})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_shop_headline:

                if ("酒店住宿".equals(homePageBean.getClassify())) {
                    HotelDetailActivity.startActivityWithParmeter(getActivity(), homePageBean.getShop_id());
                } else {
                    Intent intent1 = new Intent(getActivity(), NearbyShopDetailsActivity.class);
                    intent1.putExtra("shop_id", homePageBean.getShop_id());
                    startActivity(intent1);
                }
//                Intent intent1 = new Intent(getActivity(), ShopHeadlineActivity.class);
//                intent1.putExtra("int_data", 100);
//                startActivity(intent1);
                break;

        }
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
