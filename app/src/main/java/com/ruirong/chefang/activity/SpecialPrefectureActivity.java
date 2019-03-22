package com.ruirong.chefang.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.SpecialprefectureListviewAdapter;
import com.ruirong.chefang.bean.SpecialPrefectureListviewBean;
import com.ruirong.chefang.http.RemoteApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2017/12/28 0028
 * describe:  特产专区
 */
public class SpecialPrefectureActivity
        extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener {


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
    private SpecialprefectureListviewAdapter adapter;
    private List<SpecialPrefectureListviewBean> baseList = new ArrayList<>();


    @Override
    public int getContentView() {
        return R.layout.activity_special_prefecture;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("安康特产");

        adapter = new SpecialprefectureListviewAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(SpecialPrefectureActivity.this));
        canContentView.setAdapter(adapter);
        adapter.setOnRVItemClickListener(this);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(SpecialPrefectureActivity.this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");

        showLoadingDialog("加载中...");

    }

    @Override
    public void getData() {
        getListData();
    }

    public void getListData() {
        baseList.clear();
        HttpHelp.getInstance().create(RemoteApi.class).Commodityclassification()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<SpecialPrefectureListviewBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<SpecialPrefectureListviewBean>> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            baseList = baseBean.data;
                            if (baseList != null && baseList.size() > 0) {
                                rlEmpty.setVisibility(View.GONE);
                                adapter.setData(baseList);
                            } else {
                                rlEmpty.setVisibility(View.VISIBLE);
                            }
                        }
                    }


                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                    }
                });
    }

    @Override
    public void onLoadMore() {
        refresh.loadMoreComplete();
        refresh.refreshComplete();
    }

    @Override
    public void onRefresh() {
        getData();
    }


    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent1 = new Intent(getApplication(), SpecialtyListActivity.class);
        intent1.putExtra("id", baseList.get(position).getId());
        startActivity(intent1);
    }


}
