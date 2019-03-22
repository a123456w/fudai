package com.ruirong.chefang.personalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.ExpenseRecordListviewAdapter;
import com.ruirong.chefang.bean.GRecordBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.widget.recycleviewsuspension.AnimalAdapter;
import com.ruirong.chefang.widget.recycleviewsuspension.RecyclerItemClickListener;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2017/12/28 0028
 * describe:  消费记录
 */
public class ExpenseCalendarActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, ExpenseRecordListviewAdapter.OnItemChildClickListener {
    @BindView(R.id.can_content_view)
    RecyclerView rv;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

    private int page = 1;
    private boolean isScroll = true;
    private AnimalsHeadersAdapter adapter;
    private StickyRecyclerHeadersDecoration headersDecor;
    private List<GRecordBean> beanList = new ArrayList<>();
    private BaseSubscriber<BaseBean<List<GRecordBean>>> baseSubscriber;

    @Override
    public int getContentView() {
        return R.layout.activity_expense_calendar;
    }

    @Override
    public void initView() {

        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("消费记录");
        titleBar.setRightTextRes("时间");
        titleBar.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(ExpenseCalendarActivity.this, ConsumeRecordChooseTimeActivity.class);
                startActivity(intent1);
            }
        });


        // 为RecyclerView设置适配器
        adapter = new AnimalsHeadersAdapter();
        adapter.addAll(getDummyDataSet());
        rv.setAdapter(adapter);
        // 为RecyclerView添加LayoutManager
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(layoutManager);
        // 为RecyclerView添加Decorator装饰器
        // 为RecyclerView中的Item添加Header头部（自动获取头部ID，将相邻的ID相同的聚合到一起形成一个Header）
        headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        rv.addItemDecoration(headersDecor);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(ExpenseCalendarActivity.this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");
        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(ExpenseCalendarActivity.this, adapter.getItem(position) + " Clicked", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public void getData() {
        baseSubscriber = new BaseSubscriber<BaseBean<List<GRecordBean>>>(this, null) {
            @Override
            public void onNext(BaseBean<List<GRecordBean>> listBaseBean) {
                super.onNext(listBaseBean);
                refresh.loadMoreComplete();
                refresh.refreshComplete();
                if (listBaseBean.code == 0) {
                    beanList = listBaseBean.data;
                    if (page == 1) {
                        if (beanList != null && beanList.size() > 0) {
                            //adapter.setData(beanList);
                        }
                    } else {
                        if (beanList.size() > 0) {
                            //adapter.addMoreData(beanList);
                        } else {
                            ToastUtil.showToast(ExpenseCalendarActivity.this, getString(R.string.no_more));
                        }
                    }
                } else if (listBaseBean.code == 4) {
                    ToolUtil.loseToLogin(ExpenseCalendarActivity.this);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                refresh.loadMoreComplete();
                refresh.refreshComplete();
            }
        };
        String time = "";
        HttpHelp.getRetrofit(this).create(RemoteApi.class).getGRecordHistory(time, page, new PreferencesHelper(this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseSubscriber);
    }

    @Override
    public void onItemChildClick(View view, int groupPosition, int childPosition) {

    }

    private void initEvent() {
        // 为RecyclerView添加普通Item的点击事件（点击Header无效）

        // 为RecyclerView添加Header的点击事件
//        StickyRecyclerHeadersTouchListener touchListener = new StickyRecyclerHeadersTouchListener(rv, headersDecor);
//        touchListener.setOnHeaderClickListener(new StickyRecyclerHeadersTouchListener.OnHeaderClickListener() {
//            @Override
//            public void onHeaderClick(View header, int position, final long headerId) {
//                Toast.makeText(ExpenseCalendarActivity.this, "Header position: " + position + ", id: " + headerId, Toast.LENGTH_SHORT).show();
//            }
//        });
//        rv.addOnItemTouchListener(touchListener);
    }

    // 获取RecyclerView中展示的数据源
    private String[] getDummyDataSet() {
        return getResources().getStringArray(R.array.animals);
    }

    // StickyHeadersRecyclerView的适配器类
    private class AnimalsHeadersAdapter extends AnimalAdapter<RecyclerView.ViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expenserecord_child_layout, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            //TextView textView = (TextView) holder.itemView;
            //textView.setText(getItem(position));
        }

        // 获取当前Item的首字母，按照首字母将相邻的Item聚集起来并添加统一的头部
        @Override
        public long getHeaderId(int position) {
            return getItem(position).charAt(0);
        }

        // 获取头部布局
        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expenserecord_group_layout, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        // 为头部布局中的控件绑定数据
        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
//            final TextView textView = (TextView) holder.itemView;
//            textView.setText(String.valueOf(getItem(position).charAt(0)));
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }
}
