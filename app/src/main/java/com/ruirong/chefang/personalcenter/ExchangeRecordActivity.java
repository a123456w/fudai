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
import com.ruirong.chefang.activity.UpdateReceiveAddressActivity;
import com.ruirong.chefang.bean.ForRecordBean;
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
 * describe:  兑现记录
 */
public class ExchangeRecordActivity
        extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener

{

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


    private ListAdapter adapter;
    private List<ForRecordBean> baseList = new ArrayList<>();
    private String type = "4";
    private int page = 1;
    private String  token="";

    @Override
    public int getContentView() {
        return R.layout.activity_xchange_record;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("提现记录");
        showLoadingDialog("加载中...");
        adapter = new ListAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(ExchangeRecordActivity.this));
        canContentView.addItemDecoration(new RecycleViewDivider(ExchangeRecordActivity.this));
        canContentView.setAdapter(adapter);
        adapter.setOnRVItemClickListener(this);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(ExchangeRecordActivity.this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");

       token=new PreferencesHelper(this).getToken();

    }

    @Override
    public void getData() {
        getListData(type, page, token);
    }

    public void getListData(String type, final int page, String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getForRecord(type, page, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<ForRecordBean>>>(ExchangeRecordActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<List<ForRecordBean>> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            baseList = baseBean.data;
                            if (page == 1) {
                                if (baseList != null && baseList.size() > 0) {
                                    adapter.setData(baseList);
                                } else {
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (baseList != null && baseList.size() > 0) {
                                    adapter.addMoreData(baseList);
                                } else {
                                    ToastUtil.showToast(ExchangeRecordActivity.this, "暂无更多");
                                }
                            }
                        }else if (baseBean.code==4){
                            ToolUtil.loseToLogin(ExchangeRecordActivity.this);
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
        Intent intent1 = new Intent(this, GoldbeanBusinessDetailsActivity.class);
        intent1.putExtra("type",type);
        intent1.putExtra(Constants.ORDERID,baseList.get(position).getType_id());
        startActivity(intent1);
    }


    class ListAdapter extends RecyclerViewAdapter<ForRecordBean> {
        public ListAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.item_exchange_record_listview);
        }

        @Override
        protected void fillData(ViewHolderHelper viewHolderHelper, int position, ForRecordBean model) {
            viewHolderHelper.setText(R.id.tv_exchange_record_num, model.getMoney());
            viewHolderHelper.setText(R.id.tv_exchange_record_cash, model.getTypeName());
            viewHolderHelper.setText(R.id.tv_exchange_record_time, DateUtil.toDate(model.getCreate_time(),DateUtil.FORMAT_YMD));
        }
    }
}
