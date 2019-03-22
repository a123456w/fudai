package com.ruirong.chefang.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.EntertainmentAdapter;
//import com.ruirong.chefang.adapter.RightRecyler;
import com.ruirong.chefang.adapter.ShopMallClassifyAdapter;
//import com.ruirong.chefang.adapter.TableRecyler;
import com.ruirong.chefang.bean.EntertainmentBean;


import com.ruirong.chefang.bean.HomePageBean;
import com.ruirong.chefang.bean.ShopItemBean;
import com.ruirong.chefang.http.RemoteApi;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.yyydjk.library.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2017/12/26 0026
 * describe:  娱乐
 */
public class EntertainmentActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener {

    @BindView(R.id.can_content_view)
    RecyclerView canContentView;

    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.banner)
    Banner banner;
    private String titleName;//标题
    private String classify_id;
    private String shopShiquname = "" ;
    private int page = 1;
//    @BindView(R.id.dropDownMenu)
//    DropDownMenu mDropDownMenu;
    Context context;
    private ShopMallClassifyAdapter adapter;
//    private EntertainmentAdapter adapter;
    private List<EntertainmentBean> baseList = new ArrayList<>();
    private String[] strings = {"全部娱乐", "量贩式KTV", "附近", "智能排序"};
    private List<View> popupViews = new ArrayList<>();
    private HomePageBean homePageBean;
    //    private TableRecyler tableRecyler;
//    private RightRecyler rightRecyler;
//    private RecyclerView table;
//    private RecyclerView right;
//    private View childViews;
//    private View inflate;
//    private View inflates;

    @Override
    public int getContentView() {
        return R.layout.activity_entertainment;
    }

    @Override
    public void initView() {
        context=this;
//        //下拉框背景view
//        inflate = LayoutInflater.from(context).inflate(R.layout.log_item_drop, null);
//        canContentView= inflate.findViewById(R.id.can_content_view);
//        canRefreshHeader= inflate.findViewById(R.id.can_refresh_header);
//        canRefreshFooter= inflate.findViewById(R.id.can_refresh_footer);
//        //下拉框背景中的RecyclerView
//        refresh= inflate.findViewById(R.id.refresh);

        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("娱乐");

        canContentView.setLayoutManager(new LinearLayoutManager(EntertainmentActivity.this));
        canContentView.addItemDecoration(new RecycleViewDivider(EntertainmentActivity.this));

        adapter = new ShopMallClassifyAdapter(canContentView);
//        adapter = new EntertainmentAdapter(canContentView);
        //调用方法,传入一个接口回调
        adapter.setOnRVItemClickListener(this);
        canContentView.setAdapter(adapter);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(EntertainmentActivity.this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");
        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");

        refresh.setVisibility(View.VISIBLE);
        rlEmpty.setVisibility(View.GONE);
        getHomePageData(new PreferencesHelper(context).getLongTitude(),
                new PreferencesHelper(context).getLatitude());
//        for (String string : strings) {
//            onGetView();
//        }

//        mDropDownMenu.setDropDownMenu(Arrays.asList(strings), popupViews, inflate);
    }

//    private View onGetView() {
//        //下拉框对应的view 应需要放入循环中
//        inflates = LayoutInflater.from(context).inflate(R.layout.item_drop_layout, null);
//        //下拉框中主RecyclerView
//        table = inflates.findViewById(R.id.table);
//        //下拉框中筛选时展示的RecyclerView
//        right = inflates.findViewById(R.id.right);
//        table.setLayoutManager(new LinearLayoutManager(context));
//        right.setLayoutManager(new LinearLayoutManager(context));
//        tableRecyler = new TableRecyler(table, R.layout.table_item_recy);
//        rightRecyler = new RightRecyler(right, R.layout.right_item_recy);
//        table.setAdapter(tableRecyler);
//        right.setAdapter(rightRecyler);
//        tableRecyler.setOnItemChildClickListener(new OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(ViewGroup parent, View childView, int position) {
//                if (null!=childViews){
//                    //当选择时把上一个背景还原
//                    childViews.setBackgroundResource(R.color.grey);
//                }
//                //选择颜色进行改变
//                childView.setBackgroundResource(R.color.greys);
//
//            }
//        });
//
//        rightRecyler.setOnItemChildClickListener(new OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(ViewGroup parent, View childView, int position) {
//
//            }
//        });
//
//
//        return inflate;
//    }
    /**
     * 店铺列表
     *
     * @param classify_id
     * @param lat
     * @param lng
     * @param page
     */
    private List<ShopItemBean> baseLists = new ArrayList<>();
    public void getShopList(String classify_id, String lat, String lng, final int page,String shiqu) {
        HttpHelp.getInstance().create(RemoteApi.class).getShopList(classify_id, lat, lng, page,shiqu)
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
                            baseLists = baseBean.data;
                            if (page == 1) {
                                if (baseList != null && baseList.size() > 0) {
                                    rlEmpty.setVisibility(View.GONE);
                                    adapter.setData(baseLists);
                                } else {
                                    adapter.setData(baseLists);
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (baseList != null && baseList.size() > 0) {
                                    adapter.addMoreData(baseLists);
                                } else {
                                    ToastUtil.showToast(context, getResources().getString(R.string.no_more));
                                }
                            }
                            rlEmpty.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        refresh.loadMoreComplete();
                        hideLoadingDialog();
                        refresh.refreshComplete();
                    }
                });
    }


    @Override
    public void getData() {
        getListData();
    }

    public void getListData() {
        titleName = getIntent().getStringExtra("titleName");
        classify_id = getIntent().getStringExtra("classify_id");
        shopShiquname = getIntent().getStringExtra("shopcityname");
        getShopList(classify_id, new PreferencesHelper(context).getLatitude(),
                new PreferencesHelper(context).getLongTitude(), page,shopShiquname);
//        baseList.clear();

//        for (int i = 0; i < 12; i++) {
//            EntertainmentBean myOrderListviewBean = new EntertainmentBean();
//            baseList.add(myOrderListviewBean);
//        }
//        adapter.setData(baseList);

    }

    @Override
    public void onLoadMore() {
//        refresh.loadMoreComplete();
//        refresh.refreshComplete();
        page++;
        getData();
    }

    @Override
    public void onRefresh() {
//        refresh.loadMoreComplete();
//        refresh.refreshComplete();
        page=1;
        getData();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
//        Toast.makeText(this, "diand", Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(context, NearbyShopDetailsActivity.class);
        intent1.putExtra("shop_id", adapter.getData().get(position).getId());
        startActivity(intent1);
    }

    public static void startActivityWithParameter(Context context, String marppId) {
        Intent intent = new Intent(context, EntertainmentActivity.class);
        context.startActivity(intent);

    }
//    @Override
//    public void onBackPressed() {
//        //退出activity前关闭菜单
//        if (mDropDownMenu.isShowing()) {
//            mDropDownMenu.closeMenu();
//        } else {
//            super.onBackPressed();
//        }
//    }
    /**
     * 获取首页的数据
     *
     * @return
     */
    public void getHomePageData(String lng, String lat) {
        HttpHelp.getInstance().create(RemoteApi.class).getHomePageData(lng, lat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<HomePageBean>>(context, null) {
                    @Override
                    public void onNext(BaseBean<HomePageBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {

                            homePageBean = baseBean.data;

                            //获取banner数据
                            if (homePageBean != null) {
                                if (homePageBean.getThumb().size() > 0) {
                                    bindBannerData();
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
        banner.setImages(homePageBean.getThumb())
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        String imgUrl = (String) path;
                        Log.i("XXX", imgUrl);
                        GlideUtil.display(context, Constants.IMG_HOST + imgUrl, imageView);
                    }
                }).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                // startActivity((BGAPhotoPreviewActivity.newIntent(mContext, null, bannerList, position)));
            }
        }).start();
    }

}

