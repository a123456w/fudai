package com.ruirong.chefang.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.LuckBackRoomAdapter;
import com.ruirong.chefang.bean.HousePropertyBean;
import com.ruirong.chefang.http.RemoteApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 车房
 * Created by BX on 2017/12/26.
 */

public class GarageActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener {
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
    private int page = 1;
    private List<HousePropertyBean.ListBean> baseList = new ArrayList<>();
    private LuckBackRoomAdapter adapter;
    private static final String GARAGEID = "GARAGEID";
    private static final String GARAGENAME = "GARAGENAME";

    private String garid = "";
    private String garname = "";

    @Override
    public int getContentView() {
        return R.layout.activity_garage;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        ButterKnife.bind(this);
        garid = getIntent().getStringExtra(GARAGEID);
        garname = getIntent().getStringExtra(GARAGENAME);
        titleBar.setTitleText(garname);
        showLoadingDialog("加载中...");
        initData();
    }

    private void initData() {

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


        adapter = new LuckBackRoomAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(GarageActivity.this));
        canContentView.addItemDecoration(new RecycleViewDivider(GarageActivity.this));
        canContentView.setAdapter(adapter);
        adapter.setOnRVItemClickListener(this);

    }

    @Override
    public void getData() {
        house(page, garid);
    }

    private void house(final int page, String classify_id) {
        Log.i("XXX", classify_id);

        HttpHelp.getInstance().create(RemoteApi.class).houseProperty(page + "", classify_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<HousePropertyBean>>(GarageActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<HousePropertyBean> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {

                            baseList = baseBean.data.getList();
                            if (page == 1) {
                                if (baseList != null && baseList.size() > 0) {
                                    rlEmpty.setVisibility(View.GONE);
                                    adapter.setData(baseList);
                                } else {
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }

                            } else {
                                if (baseList != null && baseList.size() > 0) {
                                    adapter.addMoreData(baseList);
                                } else {
                                    ToastUtil.showToast(GarageActivity.this, getResources().getString(R.string.no_more));
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.i("XXX", throwable.getLocalizedMessage());
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
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

        GarageDetailsActivity.startActivityWithParmeter(GarageActivity.this, adapter.getData().get(position).getId(), adapter.getData().get(position).getSp_name());
    }

    public static void startActivityWithParmeter(Context context, String titleName, String titleId) {
        Intent intent = new Intent(context, GarageActivity.class);
        intent.putExtra(GARAGENAME, titleName);
        intent.putExtra(GARAGEID, titleId);
        context.startActivity(intent);
    }
}
