package com.ruirong.chefang.personalcenter;

import android.os.Bundle;
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
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.RecomendListBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2018/1/8 0008
 * describe:  推荐记录
 */
public class RecommendRecordActivity
        extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener

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
    private int page=1;
    private RecommendRecordAdapter adapter;
    private List<RecomendListBean> beanList=new ArrayList<>();
    private BaseSubscriber<BaseBean<List<RecomendListBean>>> recommendSubscribe;
    @Override
    public int getContentView() {
        return R.layout.activity_recommend_record;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("推荐记录");


        adapter = new RecommendRecordAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(RecommendRecordActivity.this));
        canContentView.addItemDecoration(new RecycleViewDivider(RecommendRecordActivity.this));
        canContentView.setAdapter(adapter);
        adapter.setOnRVItemClickListener(this);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(RecommendRecordActivity.this, 150));
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
        getListData();
    }

    public void getListData() {
        recommendSubscribe=new BaseSubscriber<BaseBean<List<RecomendListBean>>>(this,null){
            @Override
            public void onNext(BaseBean<List<RecomendListBean>> listBaseBean) {
                super.onNext(listBaseBean);
                refresh.loadMoreComplete();
                refresh.refreshComplete();
                if (listBaseBean.code==0){
                    beanList=listBaseBean.data;
                    if (page == 1) {
                        if (beanList != null && beanList.size() > 0) {
                            adapter.setData(beanList);
                            rlEmpty.setVisibility(View.GONE);
                        }else {
                            rlEmpty.setVisibility(View.VISIBLE);
                        }
                    }else {
                        if (beanList.size() > 0) {
                            adapter.addMoreData(beanList);
                        } else {
                            ToastUtil.showToast(RecommendRecordActivity.this, getString(R.string.no_more));
                        }
                    }
                }else if (listBaseBean.code==4){
                    ToolUtil.loseToLogin(RecommendRecordActivity.this);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                refresh.loadMoreComplete();
                refresh.refreshComplete();
            }
        };

        HttpHelp.getRetrofit(this).create(RemoteApi.class).recomendList(String.valueOf(page),new PreferencesHelper(RecommendRecordActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recommendSubscribe);
    }

    @Override
    public void onLoadMore() {
        page++;
        refresh.loadMoreComplete();
        refresh.refreshComplete();
        getListData();
    }

    @Override
    public void onRefresh() {
        page=1;
        refresh.loadMoreComplete();
        refresh.refreshComplete();
        getListData();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {

    }


     class RecommendRecordAdapter extends RecyclerViewAdapter<RecomendListBean> {
        public RecommendRecordAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.item_recommend_record );
        }

        @Override
        protected void fillData(ViewHolderHelper viewHolderHelper, int position, RecomendListBean model) {
            viewHolderHelper.setText(R.id.tv_recommend_name,model.getTname());
            String  mobile=model.getTmobile();
            String q=model.getTmobile().substring(0,3);
            String h=model.getTmobile().substring(mobile.length()-4,mobile.length());
            viewHolderHelper.setText(R.id.tv_recommend_tel,q+"****"+h);
        }
    }



}
