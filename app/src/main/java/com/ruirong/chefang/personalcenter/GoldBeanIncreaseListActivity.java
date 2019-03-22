package com.ruirong.chefang.personalcenter;

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
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.GoldAddActivity;
import com.ruirong.chefang.adapter.GoldProfitAdapter;
import com.ruirong.chefang.bean.GoldProfitBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2018/1/9 0009
 * describe:  金豆增长列表
 */
public class GoldBeanIncreaseListActivity
        extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener

{
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

    private List<GoldProfitBean> baseList = new ArrayList<>();
    private GoldProfitAdapter adapter;


    @Override
    public int getContentView() {
        return R.layout.activity_gold_bean_increase_list;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("金豆增长列表");
        showLoadingDialog("加载中...");

        adapter = new GoldProfitAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(GoldBeanIncreaseListActivity.this));
        canContentView.addItemDecoration(new RecycleViewDivider(GoldBeanIncreaseListActivity.this));
        canContentView.setAdapter(adapter);


        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup parent, View childView, int position) {
                Log.i("XXX", adapter.getItem(position).getOrder_id());
                GoldAddActivity.startActivityWithParameter(GoldBeanIncreaseListActivity.this, adapter.getItem(position).getOrder_id());
            }
        });

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(GoldBeanIncreaseListActivity.this, 150));
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
        HttpHelp.getInstance().create(RemoteApi.class).goldProfitList(new PreferencesHelper(GoldBeanIncreaseListActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<GoldProfitBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<GoldProfitBean>> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            baseList = baseBean.data;
                            adapter.setData(baseList);
                            if (baseList == null || baseList.size() <= 0) {
                                rlEmpty.setVisibility(View.VISIBLE);
                            } else {
                                rlEmpty.setVisibility(View.GONE);
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(GoldBeanIncreaseListActivity.this);
                            rlEmpty.setVisibility(View.VISIBLE);
                        } else {
                            rlEmpty.setVisibility(View.VISIBLE);
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
        getData();
    }

    @Override
    public void onRefresh() {
        getData();
    }

}
