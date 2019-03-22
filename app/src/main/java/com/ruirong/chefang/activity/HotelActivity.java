package com.ruirong.chefang.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.HotelAdapter;
import com.ruirong.chefang.bean.HotelBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商城通用分类一级页面
 * Created by dillon on 2017/12/26.
 */

public class HotelActivity extends BaseActivity implements CanRefreshLayout.OnLoadMoreListener,OnRVItemClickListener,CanRefreshLayout.OnRefreshListener{
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_content_view)
    RecyclerView canContentView;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    private HotelAdapter adapter ;
    private List<HotelBean> baseList = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_commen_list;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("酒店住宿");
        initRefresh();
    }

       /**
            * 初始化刷新控件，声明列表点击事件
            */
          public void  initRefresh() {
              adapter = new HotelAdapter(canContentView);
              canContentView.setLayoutManager(new LinearLayoutManager(this));
              canContentView.addItemDecoration(new RecycleViewDivider(this));
              canContentView.setAdapter(adapter);

              refresh.setOnLoadMoreListener(this);
              refresh.setOnRefreshListener(this);
              adapter.setOnRVItemClickListener(this);
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
        for (int i = 0; i < 11; i++) {
            HotelBean hotelBean = new HotelBean();
            baseList.add(hotelBean);
        }
        adapter.setData(baseList);
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
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
    }
}
