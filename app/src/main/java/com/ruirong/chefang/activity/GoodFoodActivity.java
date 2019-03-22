package com.ruirong.chefang.activity;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.CarAndRoomsLevelAdapter;
import com.ruirong.chefang.adapter.GoodTitleIetmAdapter;
import com.ruirong.chefang.adapter.LifeIetmAdapter;
import com.youth.banner.Banner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GoodFoodActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener {

    @BindView(R.id.banner_top)
    Banner bannerTop;
    @BindView(R.id.rv_level)
    RecyclerView rvLevel;
    @BindView(R.id.nslv_exposure)
    NoScrollListView nslvExposure;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.can_content_view)
    NestedScrollView canContentView;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    Unbinder unbinder;
    GoodTitleIetmAdapter itemAdapter;
    LifeIetmAdapter adapter;
    int page = 1;
    @Override
    public int getContentView() {
        return R.layout.activity_good_food;
    }

    @Override
    public void initView() {
        unbinder = ButterKnife.bind(this);
        itemAdapter = new GoodTitleIetmAdapter(rvLevel);
        //设置布局管理器
        rvLevel.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvLevel.setAdapter(itemAdapter);
        itemAdapter.setOnRVItemClickListener(this);
        nslvExposure.setFocusable(false);
        adapter = new LifeIetmAdapter(nslvExposure);
        nslvExposure.setAdapter(adapter);
        nslvExposure.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onLoadMore() {
        page++;
    }

    @Override
    public void onRefresh() {
        page = 1;
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {

    }
}
