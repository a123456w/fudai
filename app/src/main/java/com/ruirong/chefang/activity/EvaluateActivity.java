package com.ruirong.chefang.activity;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.ruirong.chefang.adapter.SpecialtyEvaluateAdapter;
import com.ruirong.chefang.bean.SpecialtyCommodityEvaluateBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.view.MyRatingBar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 评价
 * Created by BX on 2017/12/29.
 */

public class EvaluateActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener {

    @BindView(R.id.can_content_view)
    ListView nslvList;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    private SpecialtyEvaluateAdapter adapter;
    private List<SpecialtyCommodityEvaluateBean.ListBean> lists = new ArrayList<>();
    private View headView;
    private int page = 1;
    private String specialty_id;
    private MyRatingBar head_rb_grade;
    private TextView head_branch;

    @Override
    public int getContentView() {
        return R.layout.activity_evaluate;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("评价");
        showLoadingDialog("加载中...");
        initData();
        headView = getLayoutInflater().inflate(R.layout.list_hand, null);
        nslvList.addHeaderView(headView);
        head_rb_grade = (MyRatingBar) headView.findViewById(R.id.head_rb_grade);
        head_branch = (TextView) headView.findViewById(R.id.head_branch);
    }

    private void initData() {

        specialty_id = getIntent().getStringExtra("specialty_id");
        if (specialty_id == null) {
            finish();
        }
        Log.i("XXX", specialty_id);
        adapter = new SpecialtyEvaluateAdapter(nslvList);
        nslvList.setAdapter(adapter);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(EvaluateActivity.this, 150));
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
        pinglun(specialty_id, page);
    }

    private void pinglun(String specialty_id, final int page) {

        HttpHelp.getInstance().create(RemoteApi.class).pinglun(specialty_id, page + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<SpecialtyCommodityEvaluateBean>>(EvaluateActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<SpecialtyCommodityEvaluateBean> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {

                            if (baseBean.data.getScore() > 0.00) {
                                head_rb_grade.setRating((float) baseBean.data.getScore());
                                NumberFormat nf = NumberFormat.getNumberInstance();
                                nf.setMaximumFractionDigits(2);
                                head_branch.setText(nf.format(baseBean.data.getScore()) + "分");
                                Log.i("XXX", baseBean.data.getScore() + "");
                            }


                            lists = baseBean.data.getList();
                            if (page == 1) {
                                if (lists != null && lists.size() > 0) {
//                                    rlEmpty.setVisibility(View.GONE);
                                    adapter.setData(lists);
                                } else {
                                    loadingLayout.setStatus(LoadingLayout.Empty);
//                                    rlEmpty.setVisibility(View.VISIBLE);
                                }

                            } else {
                                if (lists != null && lists.size() > 0) {
                                    adapter.addMoreData(lists);
                                } else {
                                    ToastUtil.showToast(EvaluateActivity.this, getResources().getString(R.string.no_more));
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

}
