package com.ruirong.chefang.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.ruirong.chefang.adapter.GoldAddAdapter;
import com.ruirong.chefang.bean.ProdeceBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.widget.MarqueTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 金豆增值列表
 * Created by dillon on 2017/9/8.
 */

public class GoldAddListActivity extends BaseActivity/* implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener*/ {


    @BindView(R.id.tv_table_name)
    MarqueTextView tvTableName;
    @BindView(R.id.swipe_target)
    RecyclerView canContentView;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;


    private GoldAddAdapter adapter;
    private BaseSubscriber<BaseBean<ProdeceBean>> getAddListSubscrisber;
    private List<ProdeceBean.DataBean> baseList = new ArrayList<>();


    @Override
    public int getContentView() {
        return R.layout.activity_goldaddlist;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("种金豆");

        showLoadingDialog("加载中...");

        adapter = new GoldAddAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(GoldAddListActivity.this));
        canContentView.addItemDecoration(new RecycleViewDivider(GoldAddListActivity.this));
        canContentView.setAdapter(adapter);

/*
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);*/
        refresh.setMaxFooterHeight(DensityUtil.dp2px(GoldAddListActivity.this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");

        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup parent, View childView, final int position) {

                if (TextUtils.isEmpty(new PreferencesHelper(GoldAddListActivity.this).getToken())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GoldAddListActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("您还未登录，请先登录");
                    builder.setNegativeButton("取消", null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LoginActivity.startActivity(GoldAddListActivity.this, LoginActivity.class);
                        }
                    });
                    builder.create().show();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(GoldAddListActivity.this, GoldThirtyBuyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("PRODECEBEAN", adapter.getItem(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public void getData() {
       HttpHelp.getInstance().create(RemoteApi.class).prodectList()
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new BaseSubscriber<BaseBean<ProdeceBean>>(this,null){
                   @Override
                   public void onNext(BaseBean<ProdeceBean> baseBean) {
                       super.onNext(baseBean);
                       hideLoadingDialog();
                       if (baseBean.code==0){
                           ProdeceBean prodeceBean = baseBean.data;
                           baseList = prodeceBean.getData();
                           tvTableName.setText(prodeceBean.getAdvertising());
                       }
                       if (baseList.size()>0){
                           adapter.setData(baseList);
                       }

                   }
                   @Override
                   public void onError(Throwable throwable) {
                       super.onError(throwable);
                       hideLoadingDialog();
                   }
               });

        }


  /*  @Override
    public void onLoadMore() {
        refresh.loadMoreComplete();
        refresh.refreshComplete();
    }

    @Override
    public void onRefresh() {
        refresh.loadMoreComplete();
        refresh.refreshComplete();
    }
*/

}
