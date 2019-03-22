package com.ruirong.chefang.personalcenter;

import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DateUtil;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.CashBackBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2017/12/28 0028
 * describe:  我的福袋
 */
public class MyLuckyBagActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener {

    @BindView(R.id.iv_titlebar_left)
    ImageView ivTitlebarLeft;
    @BindView(R.id.tv_title1)
    TextView tv_title1;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.nslv_exposure)
    NoScrollListView nslvExposure;
    @BindView(R.id.can_content_view)
    NestedScrollView canContentView;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.iv_titlebar_left1)
    ImageView ivTitlebarLeft1;
    @BindView(R.id.tv_lucky_amount)
    TextView tvLuckyAmount;
    private myAdapter adapter;
    private int page = 1;
    private List<CashBackBean.ListBean> baseList = new ArrayList<>();


    @Override
    public int getContentView() {
        return R.layout.activity_my_lucky_bag;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("待返金");
        showLoadingDialog("加载中...");
        adapter = new myAdapter(nslvExposure);
        nslvExposure.setAdapter(adapter);
        nslvExposure.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(MyLuckyBagActivity.this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");


        ivTitlebarLeft1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_title1.setFocusable(true);
        tv_title1.setFocusableInTouchMode(true);
        tv_title1.requestFocus();


        //View inflate = MyLuckyBagActivity.this.getLayoutInflater().inflate(R.layout.item_my_luckybag_top_layout, null);
        View inflate_bottom = MyLuckyBagActivity.this.getLayoutInflater().inflate(R.layout.item_my_luckybag_bottom_layout, null);

       // nslvExposure.addHeaderView(inflate);
        nslvExposure.addFooterView(inflate_bottom);
    }

    @Override
    public void getData() {
        getListData(new PreferencesHelper(MyLuckyBagActivity.this).getToken());
    }

    public void getListData(String token) {
        HttpHelp.getRetrofit(this).create(RemoteApi.class).cashBackList(page, token)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<CashBackBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<CashBackBean> cashBackBeanBaseBean) {
                        super.onNext(cashBackBeanBaseBean);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (cashBackBeanBaseBean.code==0){
                            baseList=cashBackBeanBaseBean.data.getList();
                            tvLuckyAmount.setText(cashBackBeanBaseBean.data.getResidue_money());
                            if (page==1){
                                if (baseList!=null&&baseList.size()>0){adapter.setData(baseList);}
                            }else {
                                if (baseList.size()>0){
                                    adapter.addMoreData(baseList);
                                }else {
                                    ToastUtil.showToast(MyLuckyBagActivity.this,getString(R.string.no_more));
                                }
                            }
                        }else if (cashBackBeanBaseBean.code==4){
                            ToolUtil.loseToLogin(MyLuckyBagActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        refresh.loadMoreComplete();
                        hideLoadingDialog();
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
        page=1;
        getData();
    }


    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {

    }


    class myAdapter extends BaseListAdapter<CashBackBean.ListBean> {
        public myAdapter(ListView listView) {
            super(listView, R.layout.item_my_luckybag);
        }

        @Override
        public void fillData(ViewHolder holder, int position, CashBackBean.ListBean model) {
            holder.setText(R.id.tv_harvest_number, model.getToday_cash());
            holder.setText(R.id.tv_harvest_time, DateUtil.toDate(model.getCreate_time(),DateUtil.FORMAT_YMD_CN));
        }
    }

}
