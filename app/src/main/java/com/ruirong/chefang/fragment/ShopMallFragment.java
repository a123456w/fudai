package com.ruirong.chefang.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
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
import com.ruirong.chefang.activity.AllBusinessActivity;
import com.ruirong.chefang.activity.EnteringActivity;
import com.ruirong.chefang.activity.EntertainmentActivity;
import com.ruirong.chefang.activity.HotelDetailActivity;
import com.ruirong.chefang.activity.NearbyShopDetailsActivity;
import com.ruirong.chefang.activity.SearchActivity;
import com.ruirong.chefang.activity.SearchActivitys;
import com.ruirong.chefang.activity.ShopMallClassifyActivity;
import com.ruirong.chefang.adapter.ShopMallLableAdapter;
import com.ruirong.chefang.adapter.ShopMallNearByShopAdapter;
import com.ruirong.chefang.bean.AllIndustriesBean;
import com.ruirong.chefang.bean.GetareaBean;
import com.ruirong.chefang.bean.ShopHomeBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.view.DateDialog;
import com.ruirong.chefang.view.NotesDialog;
import com.ruirong.chefang.view.OrderDetailDialog;
import com.ruirong.chefang.view.OrderSpecDialog;
import com.ruirong.chefang.view.ScreenDialog;
import com.ruirong.chefang.view.SelectDialog;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 商城
 * Created by dillon on 2017/12/25.
 */

public class ShopMallFragment extends BaseFragment implements CanRefreshLayout.OnLoadMoreListener, AdapterView.OnItemClickListener, CanRefreshLayout.OnRefreshListener {
    @BindView(R.id.banner_top)
    Banner bannerTop;
    @BindView(R.id.nsgv_shopmall_label)
    NoScrollGridView nsgvShopmallLabel;
    @BindView(R.id.nslv_shopmall_nearybyshop)
    NoScrollListView nslvShopmallNearybyshop;
    @BindView(R.id.can_content_view)
    NestedScrollView canContentView;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    Unbinder unbinder;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

    private ShopMallLableAdapter shopMallLableAdapter;
    private ShopMallNearByShopAdapter nearShopAdapter;
    private List<String> carouselList = new ArrayList<>();
    private List<ShopHomeBean.ShopBean> shopNearList = new ArrayList<>();
    private ShopHomeBean shopHomeBean = null;
    private int page = 1;
    boolean firstLoad = true;
    private String myDiqu = "";
    private List<HotCity> hotCities = new ArrayList<>();

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_shopmall, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        tvTitle.setText("商城");
        if (TextUtils.isEmpty(new PreferencesHelper(getActivity()).getCityName())) {
            tvLeft.setText("安康");
        } else {
            tvLeft.setText(new PreferencesHelper(getActivity()).getCityName());
        }
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, AfterSaleDetailsActivity.class);
//                startActivity(intent)
                hotCities.clear();
                getarea();


            }
        });

        myDiqu = new PreferencesHelper(getActivity()).getCityName();

        shopMallLableAdapter = new ShopMallLableAdapter(nsgvShopmallLabel);
        nsgvShopmallLabel.setFocusable(false);
        nsgvShopmallLabel.setAdapter(shopMallLableAdapter);
        nsgvShopmallLabel.setOnItemClickListener(this);

        nearShopAdapter = new ShopMallNearByShopAdapter(nslvShopmallNearybyshop);
        nslvShopmallNearybyshop.setFocusable(false);
        nslvShopmallNearybyshop.setAdapter(nearShopAdapter);
        nslvShopmallNearybyshop.setOnItemClickListener(this);

        initRefresh();


//        Eyes.setStatusBarColor(getActivity(), ContextCompat.getColor(getActivity(), R.color.stateColor));
    }

    /**
     * 初始化刷新控件，声明列表点击事件
     */
    public void initRefresh() {

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);

        refresh.setMaxFooterHeight(DensityUtil.dp2px(getActivity(), 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");

//        footView = View.inflate(mContext, R.layout.list_no_data, null);

    }

    @Override
    public void getData() {

        getShopindexHome(myDiqu);

    }

    private void getShopindexHome(String shiqu) {
        getShopindexHome(new PreferencesHelper(getContext()).getLatitude(),
                new PreferencesHelper(getContext()).getLongTitude(), shiqu, page);
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
                .subscribe(new BaseSubscriber<BaseBean<ShopHomeBean>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<ShopHomeBean> baseBean) {
                        super.onNext(baseBean);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            shopHomeBean = baseBean.data;
                            if (shopHomeBean != null) {
                                if (firstLoad) {
                                    if (shopHomeBean.getImages() != null) {
                                        for (int i = 0; i < shopHomeBean.getImages().size(); i++) {
                                            carouselList.add(Constants.IMG_HOST + shopHomeBean.getImages().get(i));
                                        }
                                        bindBannerData();
                                        shopMallLableAdapter.setData(shopHomeBean.getTrade());
                                        firstLoad = false;
                                    }
                                }

                                shopNearList = shopHomeBean.getShop();
                                if (page == 1) {
                                    if (shopNearList != null && shopNearList.size() > 0) {
                                        Log.i("XXX", shopNearList.size() + "");
//                                            nslvShopmallNearybyshop.removeFooterView(footView);
                                        nearShopAdapter.setData(shopNearList);
                                        nslvShopmallNearybyshop.setVisibility(View.VISIBLE);
                                        rlEmpty.setVisibility(View.GONE);
                                    } else {
//                                            nslvShopmallNearybyshop.addFooterView(footView);
                                        rlEmpty.setVisibility(View.VISIBLE);
                                        nslvShopmallNearybyshop.setVisibility(View.GONE);
                                    }

                                } else {
                                    if (shopNearList != null && shopNearList.size() > 0) {
                                        nearShopAdapter.addMoreData(shopNearList);
                                    } else {
                                        ToastUtil.showToast(getActivity(), getResources().getString(R.string.no_more));
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        Log.i("ShopMallFragment", "onError" + throwable.getLocalizedMessage());
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

                        GlideUtil.display(mContext, imgUrl, imageView);

                    }
                }).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
//                new NotesDialog(getActivity(),new ArrayList()).show();
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                // startActivity((BGAPhotoPreviewActivity.newIntent(mContext, null, bannerList, position)));
                //  日期dialog
//                new DateDialog(mContext).show();
                //订单dialog
//                new OrderDetailDialog(mContext).show();
                //地址选择dialog
//                new SelectDialog(mContext).show();
                //订单规格选择
                new OrderSpecDialog(mContext).show();
                //搜索页面
//                startActivity(new Intent(mContext, SearchActivitys.class));
                //酒店详情
//              R.layout.layout_hotel;
                //筛选dialog
                new ScreenDialog(mContext).show();


            }
        }).start();


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData();
    }

    @Override
    public void onLoadMore() {
        page++;
        getData();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == nsgvShopmallLabel) {
//            startActivity(new Intent(getActivity(), ShopMallClassifyActivity.class));
//            if (shopLableList.get(i).getName().equals("数码产品")) {
//                Intent intent1 = new Intent(getActivity(), NumericalCodeActivity.class);
//                intent1.putExtra("int_data", 100);
//                startActivity(intent1);
//
//                return;
//            }
            Log.e("onItemClick",shopHomeBean.getTrade().size() - 1+"");

            switch (i){
//                case 0:
//
//                    break;
//                case 1:
//                    break;
//                case 2:
//                    Intent intent = new Intent(getActivity(), EntertainmentActivity.class);
//                    startActivity(intent);
//                    break;
//                case 3:
//                    break;
//                case 4:
//                    break;
//                case 5:
//                    break;
//                case 6:
//                    break;
//                case 7:
//                    break;
//                case 8:
//                    break;
                case 9:
                    Intent intents = new Intent(getActivity(), AllBusinessActivity.class);
                    startActivity(intents);
                    break;
                    default:
                        onStatar(i);
                        break;

            }
//            if (i != shopHomeBean.getTrade().size() - 1) {
//                Intent intent1 = new Intent(getActivity(), ShopMallClassifyActivity.class);
//                Log.i("XXX", shopHomeBean.getTrade().get(i).getId());
//                intent1.putExtra("titleName", shopHomeBean.getTrade().get(i).getName());
//                intent1.putExtra("classify_id", shopHomeBean.getTrade().get(i).getId());
//                intent1.putExtra("shopcityname",myDiqu);
//                startActivity(intent1);
//            } else if (i!=2){//全部
//
//            }


        } else if (adapterView == nslvShopmallNearybyshop) {
            if ("酒店住宿".equals(nearShopAdapter.getData().get(i).getClassify_id())) {
                HotelDetailActivity.startActivityWithParmeter(getActivity(), nearShopAdapter.getData().get(i).getId());
            } else {
                Intent intent1 = new Intent(getActivity(), NearbyShopDetailsActivity.class);
                intent1.putExtra("shop_id", nearShopAdapter.getData().get(i).getId());
                intent1.putExtra("shop_name", nearShopAdapter.getData().get(i).getSp_name());
                startActivity(intent1);
            }
        }
    }

    private void onStatar(final int i) {
        HttpHelp.getInstance().create(RemoteApi.class).allcate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<AllIndustriesBean>>>(getContext(), loadingLayout) {
                    @Override
                    public void onNext(BaseBean<List<AllIndustriesBean>> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        List<AllIndustriesBean> data = baseBean.data;
                        if (baseBean.code == 0) {
//                            if (i==2){
//                                Intent intent = new Intent(getActivity(), EntertainmentActivity.class);
//                                intent.putExtra("titleName", data.get(i).getName());
//                                intent.putExtra("classify_id", data.get(i).getId());
//                                startActivity(intent);
//                            }else {
                                Intent intent1 = new Intent(getContext(), ShopMallClassifyActivity.class);
                                intent1.putExtra("titleName", data.get(i).getName());
                                intent1.putExtra("classify_id", data.get(i).getId());
                                startActivity(intent1);
//                            }

                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                    }
                });
    }


    @OnClick({R.id.iv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_right:
                if (TextUtils.isEmpty(new PreferencesHelper(mContext).getToken())) {
                    ToolUtil.goToLogin(getActivity());
                } else {
                    EnteringActivity.startActivityWithParmeter(mContext, 1);
                }
                break;

        }
    }

    /**
     * 热门城市
     */
    public void getarea() {
        HttpHelp.getInstance().create(RemoteApi.class).getarea()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<GetareaBean>>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<List<GetareaBean>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (baseBean.data != null && baseBean.data.size() > 0) {
                                for (int i = 0; i < baseBean.data.size(); i++) {
                                    HotCity city = new HotCity(baseBean.data.get(i).getName(), baseBean.data.get(i).getName(), i + "");
                                    hotCities.add(city);
                                }
                            }

                            showCity(hotCities);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    private void showCity(List<HotCity> hot) {

        CityPicker.getInstance()
                .setFragmentManager(getActivity().getSupportFragmentManager())
                .enableAnimation(true)
                .setAnimationStyle(R.style.DefaultCityPickerAnimation)
                .setLocatedCity(new LocatedCity(tvLeft.getText().toString().trim(), tvLeft.getText().toString().trim(), "1"))
                .setHotCities(hot)
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
//                                currentTV.setText(data == null ? "杭州" : String.format("当前城市：%s，%s", data.getName(), data.getCode()));
                        if (data != null) {
//                            Toast.makeText(
//                                    getActivity().getApplicationContext(),
//                                    String.format("点击的数据：%s，%s", data.getName(), data.getCode()),
//                                    Toast.LENGTH_SHORT)
//                                    .show();
                            page = 1;
                            myDiqu = data.getName();
                            new PreferencesHelper(mContext).saveCityName(myDiqu);
                            tvLeft.setText(data.getName());
                            getShopindexHome(data.getName());
                        }
                    }

                    @Override
                    public void onLocate() {
                        //开始定位，这里模拟一下定位 写死的
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (TextUtils.isEmpty(new PreferencesHelper(getActivity()).getCityName())) {
                                    CityPicker.getInstance()
                                            .locateComplete(new LocatedCity("安康", "安康", "1"),
                                                    LocateState.SUCCESS);
                                    tvLeft.setText("安康");
                                } else {
                                    tvLeft.setText(new PreferencesHelper(getActivity()).getCityName());
                                    CityPicker.getInstance()
                                            .locateComplete(new LocatedCity(new PreferencesHelper(getActivity()).getCityName(), new PreferencesHelper(getActivity()).getCityName(), "1"),
                                                    LocateState.SUCCESS);
                                }

                            }
                        }, 2000);
                    }
                })
                .show();
    }
}
