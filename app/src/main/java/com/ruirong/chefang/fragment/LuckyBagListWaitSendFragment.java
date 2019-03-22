package com.ruirong.chefang.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.ExpressDeliveryDetailsActivity;
import com.ruirong.chefang.adapter.LuckyBagListAdapter;
import com.ruirong.chefang.bean.LuckyBagBean;
import com.ruirong.chefang.event.UpdateBeforeUIDateEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.personalcenter.LuckyBagOrderDetailActivity;
import com.ruirong.chefang.util.ToolUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 16690 on 2018/4/3.
 * describe: 福袋订单 待发货
 */

public class LuckyBagListWaitSendFragment extends BaseFragment implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener,OnItemChildClickListener,OnRVItemClickListener {

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

    private Unbinder unbinder;
    private int page = 1;
    private List<LuckyBagBean> baseList = new ArrayList<>();
    private LuckyBagListAdapter adapter;

    private boolean isLazy = false;

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_wait_pay_money, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        EventBusUtil.register(this);
        adapter = new LuckyBagListAdapter(canContentView);
        canContentView.setAdapter(adapter);
        canContentView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter.setOnItemChildClickListener(this);
        adapter.setOnRVItemClickListener(this);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(getActivity(), 150));
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
        isLazy = true;
        getListData(new PreferencesHelper(mContext).getToken(),"2",page);
    }

    private void getListData(String token, String type, final int page){
        HttpHelp.getInstance().create(RemoteApi.class).getLuckyBagList(token, type, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<LuckyBagBean>>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<List<LuckyBagBean>> baseBean) {
                        super.onNext(baseBean);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            baseList = baseBean.data;
                            if (page == 1) {
                                if (baseList != null && baseList.size() > 0) {
                                    rlEmpty.setVisibility(View.GONE);
                                    adapter.setData(baseList);
                                } else {
                                    rlEmpty.setVisibility(View.VISIBLE);
                                    adapter.clear();
                                }

                            } else {
                                if (baseList != null && baseList.size() > 0) {
//                                    adapter.addMoreData(baseList);
                                } else {
                                    ToastUtil.showToast(mContext, getResources().getString(R.string.no_more));
                                }
                            }
                        }else if (baseBean.code==4){
//                            ToolUtil.loseToLogin(mContext);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

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
        page =1;
        getData();
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent = new Intent(mContext, LuckyBagOrderDetailActivity.class);
        intent.putExtra("number_bh",adapter.getItem(position).getNumber_bh());
        startActivity(intent);
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        switch (childView.getId()){
            case R.id.tv_pay:
                ToastUtil.showToast(mContext,"付款");
                break;
            case R.id.goods_cancel:
                ToastUtil.showToast(mContext,"取消订单");
                break;
            case R.id.tv_contact_seller:
                ToastUtil.showToast(mContext,"联系卖家");
                break;
            case R.id.tv_del:
                TextView tv = (TextView) childView;
                String str = tv.getText().toString();
                if ("删除订单".equals(str)){
                    ToastUtil.showToast(mContext,"删除订单");
                }else if ("确认收货".equals(str)){
                    ToastUtil.showToast(mContext,"确认收货");
                }else if ("评价".equals(str)){
                    ToastUtil.showToast(mContext,"评价");
                }
                break;
            case R.id.goods_hint:
                TextView tv1 = (TextView) childView;
                String str1 = tv1.getText().toString();
                if ("提醒发货".equals(str1)){
                    remind(new PreferencesHelper(mContext).getToken(),adapter.getItem(position).getNumber_bh(),"2");
                }else if ("查看详情".equals(str1)){
                    ToastUtil.showToast(mContext,"查看详情");
                }
                break;
            case R.id.goods_address://查看物流
                ToastUtil.showToast(mContext,"查看物流");
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isLazy){
            page = 1;
            getData();
        }
    }

    /**
     * 刷新数据
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void commentCallback(UpdateBeforeUIDateEvent event) {
        page = 1;
        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    /**
     * 提醒发货
     * @param token
     * @param order_id
     * @param type
     */
    private void remind(String token,String order_id,String type){
        HttpHelp.getInstance().create(RemoteApi.class).remind(token, order_id, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(mContext, baseBean.message);
                        } else {
                            ToastUtil.showToast(mContext, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }


}
