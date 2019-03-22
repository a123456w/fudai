package com.ruirong.chefang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DateUtil;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.FoodsAdapter;
import com.ruirong.chefang.bean.ShopHomeBean;
import com.ruirong.chefang.bean.ShopItemBean;
import com.ruirong.chefang.fragment.HotelStayFragment;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.view.DateDialog;
import com.ruirong.chefang.view.OrderSpecDialog;
import com.ruirong.chefang.view.SelectDialog;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 酒店住宿
 * Created by 李  on 2018/11/27.
 */
public class HotelStayActivity extends BaseActivity implements CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener, CanRefreshLayout.OnRefreshListener {


    @BindView(R.id.banner_top)
    Banner bannerTop;
    @BindView(R.id.rlv_foods)
    RecyclerView rlvfoods;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

//    CanRefreshLayout refresh;
    @BindView(R.id.can_content_view)
    NestedScrollView canContentView;
    @BindView(R.id.tv_destination)
    TextView tvDestination;
    @BindView(R.id.ll_destination)
    LinearLayout llDestination;
    @BindView(R.id.tv_start_date)
    TextView tvStartDate;
    @BindView(R.id.tv_start_date_com)
    TextView tvStartDateCom;
    @BindView(R.id.tv_end_date)
    TextView tvEndDate;
    @BindView(R.id.tv_end_date_com)
    TextView tvEndDateCom;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.tv_nearby)
    TextView tvNearby;
    @BindView(R.id.tv_nearby_com)
    TextView tvNearbyCom;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.ll_price)
    LinearLayout llPrice;

    private String classify_id = "";
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;


    private FoodsAdapter mAdapter;
    boolean firstLoad = true;
    private String myDiqu = "";
    private List<String> carouselList = new ArrayList<>();
    private ShopHomeBean shopHomeBean = null;
    private List<ShopItemBean> baseList = new ArrayList<>();
    private LinearLayoutManager lm = new LinearLayoutManager(this);
    private String shopShiquname = "";
    private int page = 1;


    @Override
    public int getContentView() {
        return R.layout.activity_hotel_stay;
    }

    @Override
    public void initView() {

        ButterKnife.bind(this);
        classify_id = getIntent().getStringExtra("classify_id");
        titleBar.setTitleText(getIntent().getStringExtra("title"));
        loadingLayout.setStatus(LoadingLayout.Success);
        showLoadingDialog("加载中...");
        initRecyclerView();
        myDiqu = new PreferencesHelper(HotelStayActivity.this).getCityName();
        //initFragment(getSupportFragmentManager());
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText("所有");
        tabLayout.addTab(tab);
        TabLayout.Tab tab1 = tabLayout.newTab();
        tab1.setText("高端酒店");
        tabLayout.addTab(tab1);
        TabLayout.Tab tab2 = tabLayout.newTab();
        tab2.setText("经济");
        tabLayout.addTab(tab2);
        TabLayout.Tab tab3 = tabLayout.newTab();
        tab3.setText("商务");
        tabLayout.addTab(tab3);
        TabLayout.Tab tab4 = tabLayout.newTab();
        tab4.setText("钟点");
        tabLayout.addTab(tab4);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ToastUtil.showToast(HotelStayActivity.this,tab.getTag().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab Tab = tabLayout.getTabAt(i);
            if (Tab!=null){
                Tab.setTag(i);
            }
        }
    }

    private void initFragment(FragmentManager supportFragmentManager) {
//        supportFragmentManager.beginTransaction().replace(R.id.fl_layout,new HotelStayFragment()).commit();
    }

    private void initRecyclerView() {

        mAdapter = new FoodsAdapter(rlvfoods);
        rlvfoods.setAdapter(mAdapter);
        rlvfoods.setLayoutManager(lm);

//        refresh.setOnLoadMoreListener(this);
//        refresh.setOnRefreshListener(this);
        mAdapter.setOnRVItemClickListener(this);
//        refresh.setMaxFooterHeight(DensityUtil.dp2px(this, 150));
//        refresh.setStyle(0, 0);
//        canRefreshHeader.setPullStr("下拉刷新");
//        canRefreshHeader.setReleaseStr("松开刷新");
//        canRefreshHeader.setRefreshingStr("加载中");
//        canRefreshHeader.setCompleteStr("");
//
//        canRefreshFooter.setPullStr("上拉加载更多");
//        canRefreshFooter.setReleaseStr("松开刷新");
//        canRefreshFooter.setRefreshingStr("加载中");
//        canRefreshFooter.setCompleteStr("");


    }


    @Override
    public void getData() {


        getShopindexHome(myDiqu);

        getShopList(classify_id, new PreferencesHelper(HotelStayActivity.this).getLatitude(),
                new PreferencesHelper(HotelStayActivity.this).getLongTitude(), page, shopShiquname);


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
//                        refresh.loadMoreComplete();
//                        refresh.refreshComplete();
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
                                    ToastUtil.showToast(HotelStayActivity.this, getResources().getString(R.string.no_more));
                                }
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
//                        refresh.loadMoreComplete();
//                        refresh.refreshComplete();
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
                .subscribe(new BaseSubscriber<BaseBean<ShopHomeBean>>(HotelStayActivity.this, loadingLayout) {
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

                        GlideUtil.display(HotelStayActivity.this, imgUrl, imageView);

                    }
                }).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
//                startActivity(new Intent(HotelStayActivity.this, PackageDetailsActivity.class));
            }
        }).start();


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
        Intent intent1 = new Intent(this, NearbyShopDetailsActivity.class);
        intent1.putExtra("shop_id", mAdapter.getData().get(position).getId());
        startActivity(intent1);

    }


    SelectDialog mSelectDialog;
    DateDialog dateDialog;
    OrderSpecDialog orderSpecDialog;

    @OnClick({R.id.ll_destination, R.id.ll_time, R.id.ll_search, R.id.ll_price})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_destination:
                //地址选择dialog
                if (mSelectDialog == null) mSelectDialog = new SelectDialog(this);
                mSelectDialog.setOnClickConfirm(new SelectDialog.onClickConfirm() {
                    @Override
                    public void onConfirm(String city) {
                        tvDestination.setText(city);
                    }
                });
                mSelectDialog.show();
                break;
            case R.id.ll_time:
                //  日期dialog
                if (dateDialog == null) dateDialog = new DateDialog(this);
                dateDialog.setOnRangeDateListener(new DateDialog.onRangeDateListener() {
                    @Override
                    public void onRangeDate(Date startDate, Date endDate) {
                        String start = DateUtil.date2Str(startDate);
                        String end = DateUtil.date2Str(endDate);
                        String text = "";
                        switch (DateUtil.countDays(start)) {
                            case 0://今天
                                text = "今天";
                                break;
                            case 1://明天
                                text = "明天";
                                break;
                            case 2://后天
                                text = "后天";
                                break;
                            default:
                                text = String.valueOf(DateUtil.countDays(start) + 1) + "天后";
                                break;
                        }

                        tvStartDateCom.setText(text + "入住");

                        SimpleDateFormat format = new SimpleDateFormat(DateUtil.getDatePattern());
                        Calendar c = Calendar.getInstance();
                        try {
                            c.setTime(format.parse(end));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int day = 1;

                        int i = DateUtil.getDay(endDate);
                        int i1 = DateUtil.getDay(startDate);
                        day = i - i1;

                        tvEndDateCom.setText(DateUtil.getWeek(c) + "离店  " + "共" + day + "晚");
                        tvStartDate.setText(DateUtil.date2Str(startDate, DateUtil.FORMAT_MD_CN));
                        tvEndDate.setText(DateUtil.date2Str(endDate, DateUtil.FORMAT_MD_CN));
//                        ToastUtil.showToast(HotelStayActivity.this,"StartDate="+start+",endDate="+end);
                        if (dateDialog != null) dateDialog.dismiss();
                    }
                });
                dateDialog.show();
                break;
            case R.id.ll_search:
                //搜索页面
                Intent intent = new Intent(this, SearchActivitys.class);
                startActivityForResult(intent, 1001);
                break;
            case R.id.ll_price:
                //订单规格选择
                if (orderSpecDialog == null) orderSpecDialog = new OrderSpecDialog(this);
                orderSpecDialog.setOnSelectionListener(new OrderSpecDialog.OnSelectionListener() {
                    @Override
                    public void onSelection(String select) {
                        tvPrice.setText(select);
                    }
                });
                orderSpecDialog.show();
                break;
        }
    }
}