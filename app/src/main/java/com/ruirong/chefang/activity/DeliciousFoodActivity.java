package com.ruirong.chefang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.FoodAdapter;
import com.ruirong.chefang.adapter.FoodsAdapter;
import com.ruirong.chefang.bean.FoodBean;
import com.ruirong.chefang.bean.HomePageBean;
import com.ruirong.chefang.bean.ShopHomeBean;
import com.ruirong.chefang.bean.ShopItemBean;
import com.ruirong.chefang.event.MyItemClickListener;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.view.DateDialog;
import com.ruirong.chefang.view.GoodFoodView;
import com.ruirong.chefang.view.NearbyView;
import com.ruirong.chefang.view.ScreenView;
import com.ruirong.chefang.view.SortView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.yyydjk.library.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/*
 * 美食推荐页
 *   create by xuxx data:2018/10/29
 * */
public class DeliciousFoodActivity extends BaseActivity implements CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener, CanRefreshLayout.OnRefreshListener , MyItemClickListener {


    @BindView(R.id.banner_top)
    Banner bannerTop;
    @BindView(R.id.rlv_foods)
    RecyclerView rlvfoods;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.can_content_view)
    NestedScrollView canContentView;
    private String classify_id="";
    @BindView(R.id.ry_recy)
    RecyclerView ryRecy;
    @BindView(R.id.dropDownMenu)
    DropDownMenu mDropDownMenu;
//    @BindView(R.id.refresh)
//    CanRefreshLayout refresh;

    //    private FoodsAdapter mAdapter;
    private FoodsAdapter mAdapter;
    private FoodAdapter foodAdapter;
    HomePageBean homePageBean;
    boolean firstLoad = true;
    private String myDiqu = "";
    private List<String> carouselList = new ArrayList<>();
    private ShopHomeBean shopHomeBean = null;
    private List<ShopItemBean> baseList = new ArrayList<>();
    private List<FoodBean> foodList=new ArrayList<>();


    private String[] strings = {"全部美食", "附近", "智能排序", "筛选"};
    private String[] citys={"附近","热门商圈","昌平","东城区","东城区","东城区","东城区","东城区"};
    private String[] citysMessage={"附近","1km","3km","5km","7km","10km"};
    private String[] sorts={"智能排序","人气优先","离我最近","人均从高到低","人均从低到高"};
    private String[] foods={"全部分类","美食","娱乐","酒店住宿"};
    private String[] screen={"只看免预约","节假日可用","只看新店"};
    private RecyclerView rlvFoods;
    private List<View> popupViews = new ArrayList<>();
    private LinearLayoutManager lm = new LinearLayoutManager(this);
    private LinearLayoutManager lm2 = new LinearLayoutManager(this);
    private String shopShiquname = "";
    private int page = 1;
    private NearbyView nearbyView;
    private SortView sortView;
    private GoodFoodView foodsView;
    private ScreenView screenView;


    @Override
    public int getContentView() {
        return R.layout.activity_delicious_food;
    }


    @Override
    public void initView() {

        ButterKnife.bind(this);
        classify_id=getIntent().getStringExtra("classify_id");
        titleBar.setTitleText(getIntent().getStringExtra("title"));
        loadingLayout.setStatus(LoadingLayout.Success);
        showLoadingDialog("加载中...");
        initRecyclerView();
        myDiqu = new PreferencesHelper(DeliciousFoodActivity.this).getCityName();

//        //美食
        foodsView = new GoodFoodView(DeliciousFoodActivity.this,null);
        View inflate = getLayoutInflater().inflate(R.layout.classification_fragment_layout, null);
        RecyclerView viewById = inflate.findViewById(R.id.ry_classification);
        popupViews.add(foodsView.getView());
//        //附近
        nearbyView = new NearbyView(this, null);
        nearbyView.setListener(this);
        popupViews.add(nearbyView.getView());
//        //排序
        sortView = new SortView(this, null);
        sortView.setListener(this);
        popupViews.add(sortView.getView());
//
//        //筛选
        screenView = new ScreenView(this, null);
        screenView.setListener(this);
        popupViews.add(screenView.getView());

//        /**
//         * Dropdownmenu下面的主体部分
//         * */
        View fifthView = LayoutInflater.from(DeliciousFoodActivity.this).inflate(R.layout.activity_delicious_foods_view, null);
        rlvFoods = (RecyclerView) fifthView.findViewById(R.id.rlv_foods);

            mDropDownMenu.setDropDownMenu(Arrays.asList(strings), popupViews, fifthView);


    }

    private void initRecyclerView() {
        lm2.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置 recyclerview 布局方式为横向布局
        foodAdapter=new FoodAdapter(ryRecy);
        ryRecy.setAdapter(foodAdapter);
        ryRecy.setLayoutManager(lm2);




        mAdapter = new FoodsAdapter(rlvfoods);
        rlvfoods.setAdapter(mAdapter);
        rlvfoods.setLayoutManager(lm);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        mAdapter.setOnRVItemClickListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");


    }


    @Override
    public void getData() {

        ArrayList<Object> objects1 = new ArrayList<>();
        for (int i = 0; i < screen.length; i++) {
            objects1.add(screen[i]);
        }
        screenView.setData(objects1);

        ArrayList<Object> objects = new ArrayList<>();
        for (int i = 0; i < citys.length; i++) {
            objects.add(citys[i]);
        }


        ArrayList<Object> objects2 = new ArrayList<>();
//        for (int i = 0; i < citysMessage.length; i++) {
//            objects2.add(citysMessage[i]);
//        }
        objects2.add(citysMessage);
        objects2.add(citysMessage);
        objects2.add(citysMessage);
        objects2.add(citysMessage);
        objects2.add(citysMessage);
        objects2.add(citysMessage);
        objects2.add(citysMessage);
        objects2.add(citys);
        nearbyView.setData(objects,objects2);
        ArrayList<Object> objects3 = new ArrayList<>();
        for (int i = 0; i < sorts.length; i++) {
            objects3.add(sorts[i]);
        }
        sortView.setData(objects3);

        ArrayList<Object> objects4=new ArrayList<>();
        for (int i = 0; i < foods.length; i++) {
            objects4.add(foods[i]);
        }
        foodsView.setData(objects4);







        getShopindexHome(myDiqu);

        getShopList(classify_id, new PreferencesHelper(DeliciousFoodActivity.this).getLatitude(),
                new PreferencesHelper(DeliciousFoodActivity.this).getLongTitude(), page, shopShiquname);


    }
    private void getShopindexHome(String shiqu) {
        getShopindexHome(new PreferencesHelper(this).getLatitude(),
                new PreferencesHelper(this).getLongTitude(), shiqu, page);
    }

    /**
     * 店铺列表
     *
     * @param classify_id
     * @param lat
     * @param lng
     * @param page
     */
    public void getShopList(String classify_id, String lat, String lng, final int page, String shiqu) {
        HttpHelp.getInstance().create(RemoteApi.class).getShopList(classify_id, lat, lng, page, shiqu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<ShopItemBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<ShopItemBean>> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            baseList = baseBean.data;
                            if (page == 1) {
                                if (baseList != null && baseList.size() > 0) {
                                    rlEmpty.setVisibility(View.GONE);
                                    mAdapter.setData(baseList);
                                } else {
                                    mAdapter.setData(baseList);
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (baseList != null && baseList.size() > 0) {
                                    mAdapter.addMoreData(baseList);
                                } else {
                                    ToastUtil.showToast(DeliciousFoodActivity.this, getResources().getString(R.string.no_more));
                                }
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        hideLoadingDialog();


                    }
                });
    }
    /**
     * 商城首页
     *
     * @param lat
     * @param lng
     * @param page
     */
    private void getShopindexHome(String lat, String lng, String shiqu, final int page) {
        Log.i("XXX", lat + "  " + lng + " " + shiqu);
        HttpHelp.getInstance().create(RemoteApi.class).shopindexHome(lat, lng, shiqu, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ShopHomeBean>>(DeliciousFoodActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<ShopHomeBean> baseBean) {
                        super.onNext(baseBean);

                        if (baseBean.code == 0) {
                            shopHomeBean = baseBean.data;
                            if (shopHomeBean != null) {
                                if (firstLoad) {
                                    if (shopHomeBean.getImages() != null) {
                                        for (int i = 0; i < shopHomeBean.getImages().size(); i++) {
                                            carouselList.add(Constants.IMG_HOST + shopHomeBean.getImages().get(i));
                                        }
                                        bindBannerData();

                                        firstLoad = false;
                                    }
                                }

                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                        Log.i("DeliciousFoodActivity", "onError" + throwable.getLocalizedMessage());
                    }
                });


    }
    /**
     * 为顶部轮播添加图片
     */
    public void bindBannerData() {
        bannerTop.setImages(carouselList)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        String imgUrl = path.toString();

                        GlideUtil.display(DeliciousFoodActivity.this, imgUrl, imageView);

                    }
                }).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //new NotesDialog(getActivity(),new ArrayList()).show();
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                // startActivity((BGAPhotoPreviewActivity.newIntent(mContext, null, bannerList, position)));
                startActivity(new Intent(DeliciousFoodActivity.this,PackageDetailsActivity.class));

            }
        }).start();


    }


    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onLoadMore() {
        page++;
        getData();
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData();

    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent1 = new Intent(DeliciousFoodActivity.this, NearbyShopDetailsActivity.class);
        intent1.putExtra("shop_id", mAdapter.getData().get(position).getId());
        startActivity(intent1);

    }




    @Override
    public void onItemClick(View view, int postion, String string) {
        switch (postion){
            case 1:
                mDropDownMenu.setTabText(string);
                mDropDownMenu.closeMenu();

                break;
            case 2:
                mDropDownMenu.setTabText(string);
                mDropDownMenu.closeMenu();

                break;
            case 3:
                mDropDownMenu.setTabText(string);
                mDropDownMenu.closeMenu();

                break;

            default:
                break;
        }

    }


}
