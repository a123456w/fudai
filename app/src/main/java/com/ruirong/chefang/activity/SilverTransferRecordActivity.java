package com.ruirong.chefang.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseActivity;
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
import com.ruirong.chefang.adapter.SilverTransferRecodeAdapter;
import com.ruirong.chefang.bean.SilverTransferBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 金豆转账记录
 * Created by dillon on 2017/5/5.
 */
//  TODO 没有把分页加上。没有加到接口上
public class SilverTransferRecordActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener {


    @BindView(R.id.rbn_silverout)
    RadioButton rbnSilverout;
    @BindView(R.id.rbn_silverincome)
    RadioButton rbnSilverincome;
    @BindView(R.id.rgp_silvertransfer)
    RadioGroup rgpSilvertransfer;
    @BindView(R.id.recyclerview_silvertranfer)
    RecyclerView recyclerviewSilvertranfer;
    @BindView(R.id.can_content_view)
    ScrollView canContentView;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

    private int type = 1;
    private int page = 1;

    private List<SilverTransferBean> beanList = new ArrayList<>();
    private SilverTransferRecodeAdapter adapter;


    @Override
    public int getContentView() {
        return R.layout.activity_silvertransferrecode;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("转账记录");

        adapter = new SilverTransferRecodeAdapter(recyclerviewSilvertranfer);
        recyclerviewSilvertranfer.setLayoutManager(new LinearLayoutManager(SilverTransferRecordActivity.this));
        recyclerviewSilvertranfer.addItemDecoration(new RecycleViewDivider(SilverTransferRecordActivity.this));
        recyclerviewSilvertranfer.setAdapter(adapter);
        adapter.setOnRVItemClickListener(this);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(SilverTransferRecordActivity.this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");
        rgpSilvertransfer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbn_silverout:
                        type = 1;
                        getData();
                        break;
                    case R.id.rbn_silverincome:
                        type = 2;
                        getData();
                        break;
                }
            }
        });
    }


    @Override
    public void getData() {
        showLoadingDialog("加载中...");
        HttpHelp.getInstance().create(RemoteApi.class).silverTransfer(type, new PreferencesHelper(SilverTransferRecordActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<SilverTransferBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<SilverTransferBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (listBaseBean.code == 0) {
                            beanList = listBaseBean.data;
                            if (page == 1) {
                                if (beanList != null && beanList.size() > 0) {
                                    rlEmpty.setVisibility(View.GONE);
                                    recyclerviewSilvertranfer.setVisibility(View.VISIBLE);
                                    adapter.setData(beanList);
                                } else {
                                    rlEmpty.setVisibility(View.VISIBLE);
                                    recyclerviewSilvertranfer.setVisibility(View.GONE);
                                }

                            } else {
                                if (beanList != null && beanList.size() > 0) {
                                    adapter.addMoreData(beanList);
                                } else {
                                    ToastUtil.showToast(SilverTransferRecordActivity.this, getString(R.string.no_more));
                                }
                            }
                        }else if (listBaseBean.code==4){
                            ToolUtil.loseToLogin(SilverTransferRecordActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
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
        SilverTransferDetialActivity.startActivityResult(SilverTransferRecordActivity.this, ((SilverTransferBean) (adapter.getItem(position))).getType(), ((SilverTransferBean) (adapter.getItem(position))).getId());

    }
}
