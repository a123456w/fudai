package com.ruirong.chefang.personalcenter;

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
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DateUtil;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.GoldBuyRecordBean;
import com.ruirong.chefang.common.Constants;
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
 * describe:  金豆购买记录
 */
public class GoldBeanBuyActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener

{

    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.can_content_view)
    RecyclerView canContentView;
    private int page = 1;
    private ListAdapter adapter;
    private int type = 3;
    private List<GoldBuyRecordBean> baseList = new ArrayList<>();

    private BaseSubscriber<BaseBean<List<GoldBuyRecordBean>>> exchangeRecordSubscriber;

    @Override
    public int getContentView() {
        return R.layout.activity_gold_bean;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("充值记录");
        showLoadingDialog("加载中...");
        adapter = new ListAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(GoldBeanBuyActivity.this));
        canContentView.addItemDecoration(new RecycleViewDivider(GoldBeanBuyActivity.this));
        canContentView.setAdapter(adapter);
        adapter.setOnRVItemClickListener(this);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(GoldBeanBuyActivity.this, 150));
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

        exchangeRecordSubscriber = new BaseSubscriber<BaseBean<List<GoldBuyRecordBean>>>(this, null) {
            @Override
            public void onNext(BaseBean<List<GoldBuyRecordBean>> listBaseBean) {
                super.onNext(listBaseBean);
                hideLoadingDialog();
                refresh.loadMoreComplete();
                refresh.refreshComplete();
                if (listBaseBean.code == 0) {
                    baseList = listBaseBean.data;
                    if (page == 1) {
                        if (baseList != null && baseList.size() > 0) {
                            refresh.setVisibility(View.VISIBLE);
                            rlEmpty.setVisibility(View.GONE);
                            adapter.setData(baseList);
                        } else {
                            rlEmpty.setVisibility(View.VISIBLE);
                            refresh.setVisibility(View.GONE);
                        }

                    } else {

                        if (baseList != null && baseList.size() > 0) {
                            adapter.addMoreData(baseList);
                        } else {
                            ToastUtil.showToast(GoldBeanBuyActivity.this, getString(R.string.no_more));
                        }
                    }

                } else if (listBaseBean.code == 4) {
                    ToolUtil.loseToLogin(GoldBeanBuyActivity.this);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                hideLoadingDialog();
                refresh.loadMoreComplete();
                refresh.refreshComplete();
            }
        };
        HttpHelp.getRetrofit(this).create(RemoteApi.class).orderList(type, page, new PreferencesHelper(this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(exchangeRecordSubscriber);
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
        page = 1;
        refresh.loadMoreComplete();
        refresh.refreshComplete();
        getListData();
    }


    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent1 = new Intent(this, RechargeDetailActivity.class);
        intent1.putExtra(Constants.ORDERID, baseList.get(position).getType_id());
        intent1.putExtra("type", String.valueOf(type));
        startActivity(intent1);
    }


    class ListAdapter extends RecyclerViewAdapter<GoldBuyRecordBean> {
        public ListAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.item_exchange_record_listview);
        }

        @Override
        protected void fillData(ViewHolderHelper viewHolderHelper, int position, GoldBuyRecordBean model) {
            viewHolderHelper.setText(R.id.tv_exchange_record_num, model.getMoney());
            viewHolderHelper.setText(R.id.tv_exchange_record_cash, model.getTypeName());
            viewHolderHelper.setText(R.id.tv_exchange_record_time, DateUtil.toDate(model.getCreate_time(), DateUtil.FORMAT_YMD));
        }
    }

}