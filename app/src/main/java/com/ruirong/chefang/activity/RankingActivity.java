package com.ruirong.chefang.activity;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.RankingAdapter;
import com.ruirong.chefang.bean.RankingBean;
import com.ruirong.chefang.http.RemoteApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 排行榜
 * Created by BX on 2017/12/28.
 */

public class RankingActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener {
    @BindView(R.id.rb_newcar)
    RadioButton rbNewcar;
    @BindView(R.id.rb_olddcar)
    RadioButton rbOlddcar;
    @BindView(R.id.rb_newhouse)
    RadioButton rbNewhouse;
    @BindView(R.id.rb_oldhouse)
    RadioButton rbOldhouse;
    @BindView(R.id.rb_commercial_property)
    RadioButton rbCommercialProperty;
    @BindView(R.id.rg_garade)
    RadioGroup rgGarade;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.view_placeholder)
    View viewPlaceholder;
    @BindView(R.id.can_content_view)
    ListView canContentView;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    private RankingAdapter adapter;
    private List<RankingBean.ListBean> lists = new ArrayList<>();
    private int type = 1;
    private int page = 1;

    @Override
    public int getContentView() {
        return R.layout.activity_ranking;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("排行榜");

        adapter = new RankingAdapter(canContentView);
        canContentView.setAdapter(adapter);

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

        rgGarade.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_newcar:
                        type = 1;
                        page = 1;
                        getData();
                        break;
                    case R.id.rb_olddcar:
                        type = 2;
                        page = 1;
                        getData();
                        break;
                    case R.id.rb_newhouse:
                        type = 3;
                        page = 1;
                        getData();
                        break;
                    case R.id.rb_oldhouse:
                        type = 4;
                        page = 1;
                        getData();
                        break;
                    case R.id.rb_commercial_property:
                        type = 5;
                        page = 1;
                        getData();
                        break;
                }
                refresh.autoRefresh();
            }

        });

    }

    @Override
    public void getData() {
        sales(type + "", page);
    }

    private void sales(String type, final int page) {
        Log.i("XXX", type + " " + page);
        HttpHelp.getInstance().create(RemoteApi.class).sales(type, page + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<RankingBean>>(RankingActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<RankingBean> baseBean) {
                        super.onNext(baseBean);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {

                            lists = baseBean.data.getList();
                            if (page == 1) {
                                if (lists != null && lists.size() > 0) {
                                    refresh.setVisibility(View.VISIBLE);
                                    rlEmpty.setVisibility(View.GONE);
                                    adapter.setData(lists);
                                } else {
                                    refresh.setVisibility(View.GONE);
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }

                            } else {
                                if (lists != null && lists.size() > 0) {
                                    adapter.addMoreData(lists);
                                } else {
                                    ToastUtil.showToast(RankingActivity.this, getResources().getString(R.string.no_more));
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
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
}
