package com.ruirong.chefang.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.HotelDetailActivity;
import com.ruirong.chefang.adapter.LuckBackHotelAdapter;
import com.ruirong.chefang.bean.FuBackHotelBean;
import com.ruirong.chefang.http.RemoteApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2017/12/27 0027
 * describe: 福返酒店 附近
 */
public class LuckbackHotelNearbyFragment extends BaseFragment implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener {

    Unbinder unbinder;
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

    private List<FuBackHotelBean.ListBean> baseList = new ArrayList<>();
    private LuckBackHotelAdapter adapter;
    private int page = 1;

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_luckback_hotel, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        showLoadingDialog("加载中...");
        initData();
    }

    /**
     * 初始化数据
     */
    public void initData() {

        adapter = new LuckBackHotelAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(getContext()));
        canContentView.addItemDecoration(new RecycleViewDivider(getContext()));
        canContentView.setAdapter(adapter);
        adapter.setOnRVItemClickListener(this);

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


    }


    @Override
    public void getData() {
        getListData(new PreferencesHelper(getContext()).getLatitude(),
                new PreferencesHelper(getContext()).getLongTitude(),
                 "1", page);
    }

    public void getListData(String lat, String lng, String type, final int page) {

        HttpHelp.getInstance().create(RemoteApi.class).hoteList(lat, lng, type, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<FuBackHotelBean>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<FuBackHotelBean> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            baseList = baseBean.data.getList();
                            if (page == 1) {
                                if (baseList != null && baseList.size() > 0) {
                                    rlEmpty.setVisibility(View.GONE);
                                    refresh.setVisibility(View.VISIBLE);
                                    adapter.setData(baseList);
                                } else {
                                    refresh.setVisibility(View.GONE);
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }

                            } else {
                                if (baseList != null && baseList.size() > 0) {
                                    adapter.addMoreData(baseList);
                                } else {
                                    LuckbackHotelNearbyFragment.this.page--;
                                    ToastUtil.showToast(getActivity(), getResources().getString(R.string.no_more));
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.i("LuckbackHotel", "onError" + throwable.getLocalizedMessage());
                        hideLoadingDialog();
                    }
                });


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
        HotelDetailActivity.startActivityWithParmeter(getActivity(), adapter.getData().get(position).getId());
    }
    }

