package com.ruirong.chefang.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseFragment;
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
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.EnteringActivity;
import com.ruirong.chefang.activity.GarageActivity;
import com.ruirong.chefang.activity.GarageDetailsActivity;
import com.ruirong.chefang.activity.LoginActivity;
import com.ruirong.chefang.adapter.CarAndRoomsLevelAdapter;
import com.ruirong.chefang.adapter.CarHouseAdapter;
import com.ruirong.chefang.bean.CarHouseBean;
import com.ruirong.chefang.bean.GetareaBean;
import com.ruirong.chefang.http.RemoteApi;
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
 * Created by chenlipeng on 2017/12/26 0026
 * describe:  车房
 */
public class GarageFragment extends BaseFragment implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener {

    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.banner_top)
    Banner bannerTop;
    @BindView(R.id.rv_level)
    RecyclerView rvLevel;
    @BindView(R.id.tv_businessarea)
    TextView tvBusinessarea;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.nslv_exposure)
    NoScrollListView nslvExposure;
    @BindView(R.id.can_content_view)
    NestedScrollView canContentView;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    private Unbinder unbinder;

    private String myDiqu = "";
    private CarAndRoomsLevelAdapter adapter;
    private CarHouseAdapter lightListHouseAdapter;
    private List<HotCity> hotCities = new ArrayList<>();

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_garage, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        tvTitle.setText("车房");
        if (TextUtils.isEmpty(new PreferencesHelper(getActivity()).getCityName())) {
            tvLeft.setText("安康");
        } else {
            tvLeft.setText(new PreferencesHelper(getActivity()).getCityName());
        }

        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotCities.clear();
                getarea();
            }
        });

        //       水平功能点
        adapter = new CarAndRoomsLevelAdapter(rvLevel);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvLevel.setLayoutManager(linearLayoutManager);
        rvLevel.setAdapter(adapter);

        adapter.setOnRVItemClickListener(this);

//        最下面的listview的
        lightListHouseAdapter = new CarHouseAdapter(nslvExposure);
        nslvExposure.setFocusable(false);
        nslvExposure.setAdapter(lightListHouseAdapter);
        nslvExposure.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("XXX", lightListHouseAdapter.getData().get(i).getId()+"  "+ lightListHouseAdapter.getData().get(i).getSp_name());
                GarageDetailsActivity.startActivityWithParmeter(getActivity(), lightListHouseAdapter.getData().get(i).getId(), lightListHouseAdapter.getData().get(i).getSp_name());
            }
        });
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
//        Eyes.setStatusBarColor(getActivity(), ContextCompat.getColor(getActivity(), R.color.stateColor));

        myDiqu = new PreferencesHelper(getActivity()).getCityName();

    }

    @Override
    public void getData() {

        getCarAndHouseData(new PreferencesHelper(getContext()).getLatitude(),
                new PreferencesHelper(getContext()).getLongTitude(), myDiqu);
    }

    /**
     * 轮播图
     *
     * @param stringList
     */
    public void getBannerData(List<String> stringList) {
        bannerTop.setImages(stringList)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        String urlImage = (String) path;
                        GlideUtil.display(getActivity(), Constants.IMG_HOST + urlImage, imageView);
                    }
                }).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        }).start();
    }

    /**
     * 获取页面所有数据
     */
    public void getCarAndHouseData(String lat, String lng, final String shiqu) {
        HttpHelp.getInstance().create(RemoteApi.class).getCarAndHouseData(lat, lng, shiqu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<CarHouseBean>>(getActivity(), null) {
                    @Override
                    public void onNext(BaseBean<CarHouseBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            CarHouseBean carHouseBean = baseBean.data;
                            // 给轮播图赋值
                            if (carHouseBean != null && carHouseBean.getLunbo().size() > 0) {
                                getBannerData(carHouseBean.getLunbo());
                            }
                            // 给分类赋值
                            adapter.setData(carHouseBean.getCates());
                            if (carHouseBean.getFangchan() != null && carHouseBean.getFangchan().size() > 0) {
                                rlEmpty.setVisibility(View.GONE);
                                nslvExposure.setVisibility(View.VISIBLE);
                                // 给下边的房产列表赋值
                                lightListHouseAdapter.setData(carHouseBean.getFangchan());
                            } else {
                                rlEmpty.setVisibility(View.VISIBLE);
                                nslvExposure.setVisibility(View.GONE);
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    @OnClick(R.id.tv_all)
    public void selectOrganization(View view) {
        Toast.makeText(mContext, "全部", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onLoadMore() {
        refresh.refreshComplete();
        refresh.loadMoreComplete();
    }

    @Override
    public void onRefresh() {
        refresh.refreshComplete();
        refresh.loadMoreComplete();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        GarageActivity.startActivityWithParmeter(getActivity(), adapter.getData().get(position).getName(), adapter.getData().get(position).getId());

    }

    @OnClick(R.id.iv_right)
    public void onViewClicked() {
        if (TextUtils.isEmpty(new PreferencesHelper(mContext).getToken())) {
            ToastUtil.showToast(mContext, getString(R.string.login_please));
            LoginActivity.startActivity(mContext, LoginActivity.class);
        } else {
            EnteringActivity.startActivityWithParmeter(mContext, 2);
        }
    }

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
                            tvLeft.setText(data.getName());
                            myDiqu = data.getName();
                            getCarAndHouseData(new PreferencesHelper(getContext()).getLatitude(),
                                    new PreferencesHelper(getContext()).getLongTitude(), data.getName());
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
