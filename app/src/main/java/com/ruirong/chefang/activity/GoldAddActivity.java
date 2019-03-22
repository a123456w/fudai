package com.ruirong.chefang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;
import com.qlzx.mylibrary.widget.pullToRefresh.GoogleCircleProgressView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.GoldAddHisAdapter;
import com.ruirong.chefang.bean.GoldAddDetailBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 金豆增值
 * Created by dillon on 2017/9/8.
 */

public class GoldAddActivity extends BaseActivity implements OnRefreshListener {
    @BindView(R.id.tv_cash)
    TextView tvCash;
    @BindView(R.id.cash)
    TextView cash;
    @BindView(R.id.tv_allprofit)
    TextView tvAllprofit;
    @BindView(R.id.tv_investgold)
    TextView tvInvestgold;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.googleProgress)
    GoogleCircleProgressView googleProgress;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

    private List<GoldAddDetailBean.DetailBean> goldAddHisBeanList = new ArrayList<>();
    private GoldAddHisAdapter goldAddHisAdapter;


    private BaseSubscriber<BaseBean<GoldAddDetailBean>> goldAddDetialSubscriber;
    private String ordierids;

    @Override
    public int getContentView() {
        return R.layout.activity_goldadd;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("金豆种植");
//        showLoadingDialog("加载中...");
        ordierids = getIntent().getStringExtra("ORDERID");
        goldAddHisAdapter = new GoldAddHisAdapter(swipeTarget);
        swipeTarget.setLayoutManager(new LinearLayoutManager(this));
        swipeTarget.setAdapter(goldAddHisAdapter);
        swipeTarget.addItemDecoration(new RecycleViewDivider(this));
        swipeToLoadLayout.setOnRefreshListener(this);

    }

    @Override
    public void getData() {

        initData(ordierids, new PreferencesHelper(GoldAddActivity.this).getToken());

    }

    public void initData(String orderid, String token) {
        Log.i("XXX", orderid + "  " + token);
        goldAddDetialSubscriber = new BaseSubscriber<BaseBean<GoldAddDetailBean>>(this, null) {
            @Override
            public void onNext(BaseBean<GoldAddDetailBean> goldAddDetailBeanBaseBean) {
                super.onNext(goldAddDetailBeanBaseBean);
                hideLoadingDialog();
                swipeToLoadLayout.setRefreshing(false);
                if (goldAddDetailBeanBaseBean.code == 0) {
                    swipeToLoadLayout.setVisibility(View.VISIBLE);
                    rlEmpty.setVisibility(View.GONE);
                    GoldAddDetailBean goldAddDetailBean = goldAddDetailBeanBaseBean.data;
                    goldAddHisBeanList = goldAddDetailBean.getDetail();
                    tvAllprofit.setText(goldAddDetailBean.getSum());
                    cash.setText(goldAddDetailBean.getDateEarnings());
                    tvInvestgold.setText(goldAddDetailBean.getSelfMoney());

                    if (goldAddHisBeanList.size() > 0) {
                        goldAddHisAdapter.setData(goldAddHisBeanList);
                    }
                } else if (goldAddDetailBeanBaseBean.code == 4) {
                    ToolUtil.loseToLogin(GoldAddActivity.this);
                }else if (goldAddDetailBeanBaseBean.message.contains("暂无详情")) {
                    swipeToLoadLayout.setVisibility(View.GONE);
                    rlEmpty.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                swipeToLoadLayout.setRefreshing(false);
                hideLoadingDialog();
            }
        };

        HttpHelp.getInstance().create(RemoteApi.class).goldAddDetail(orderid, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(goldAddDetialSubscriber);
    }

    public static void startActivityWithParameter(Context context, String orderid) {
        Intent intent = new Intent(context, GoldAddActivity.class);
        intent.putExtra("ORDERID", orderid);
        context.startActivity(intent);
    }


    @Override
    public void onRefresh() {
        getData();
    }

}
