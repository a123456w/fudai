package com.ruirong.chefang.personalcenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.AddressBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenlipeng on 2018/1/9 0009
 * describe:  金豆种植
 */
public class GoldBeanGrowActivity extends BaseActivity implements CanRefreshLayout.OnLoadMoreListener, CanRefreshLayout.OnRefreshListener, OnRVItemClickListener {


    @BindView(R.id.goldbean_num)
    TextView goldbeanNum;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
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
    @BindView(R.id.tv_goldsum)
    TextView tvGoldsum;


    private ListAdapter adapter;
    private List<AddressBean> lists = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_gold_bean_grow;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("金豆种植");

//        adapter = new ListAdapter(nslvExposure);
//        nslvExposure.setAdapter(adapter);


        adapter = new ListAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(GoldBeanGrowActivity.this));
//        canContentView.addItemDecoration(new RecycleViewDivider(GoldBeanGrowActivity.this));
        canContentView.setAdapter(adapter);
        adapter.setOnRVItemClickListener(this);


        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(GoldBeanGrowActivity.this, 150));
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
        for (int i = 0; i < 3; i++) {
            AddressBean bean = new AddressBean();
            lists.add(bean);
        }
        adapter.setData(lists);
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


    class ListAdapter extends RecyclerViewAdapter<AddressBean> {
        public ListAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.item_goldbean_grow);
        }

        @Override
        protected void fillData(ViewHolderHelper viewHolderHelper, int position, AddressBean model) {
//            viewHolderHelper.setText(R.id.tv_moneyo,model.getMoney());
//            viewHolderHelper.setText(R.id.tv_profit,model.getProfit());
        }
    }

    @Override
    public void onLoadMore() {
        refresh.loadMoreComplete();
        refresh.refreshComplete();
    }

    @Override
    public void onRefresh() {
        refresh.loadMoreComplete();
        refresh.refreshComplete();
    }
    public static void startActivityWithParameter(Context context, String orderid){
        Intent intent = new Intent(context,GoldBeanGrowActivity.class);
        intent.putExtra("ORDERID",orderid);
        context.startActivity(intent);
    }


}
