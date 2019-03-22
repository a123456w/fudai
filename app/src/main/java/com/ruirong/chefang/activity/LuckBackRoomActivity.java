package com.ruirong.chefang.activity;

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
 * Created by chenlipeng on 2017/12/28 0028
 * describe:  福返房产
 */
public class LuckBackRoomActivity
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


    private LuckBackRoomAdapter adapter;
    private List<HousePropertyBean.ListBean> baseList = new ArrayList<>();
    private int page = 1;
    private String shop_check_id;

    @Override
    public int getContentView() {
        return R.layout.activity_luck_back_room;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("福返房产");

        adapter = new LuckBackRoomAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(LuckBackRoomActivity.this));
        canContentView.addItemDecoration(new RecycleViewDivider(LuckBackRoomActivity.this));
        canContentView.setAdapter(adapter);
        adapter.setOnRVItemClickListener(this);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(LuckBackRoomActivity.this, 150));
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
        house(page, null);
    }

    private void house(final int page, String classify_id) {

        HttpHelp.getInstance().create(RemoteApi.class).houseProperty(page + "", classify_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<HousePropertyBean>>(LuckBackRoomActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<HousePropertyBean> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        Log.i("XXX", baseBean.code + "==" + baseBean.message);
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
                                    ToastUtil.showToast(LuckBackRoomActivity.this, getResources().getString(R.string.no_more));
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
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
        GarageDetailsActivity.startActivityWithParmeter(LuckBackRoomActivity.this, adapter.getData().get(position).getId(), adapter.getData().get(position).getSp_name());
    }
}
