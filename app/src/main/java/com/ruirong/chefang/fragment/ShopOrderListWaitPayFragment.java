package com.ruirong.chefang.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
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
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;

import com.ruirong.chefang.activity.ImmediatelyPaymentActivity;
import com.ruirong.chefang.activity.ShopOrderDetailsActivity;
import com.ruirong.chefang.activity.WebActivity;
import com.ruirong.chefang.adapter.ReasonAdapter;
import com.ruirong.chefang.adapter.ShopOrderWaitPayAdapter;
import com.ruirong.chefang.bean.SelectedStateBean;
import com.ruirong.chefang.bean.ShopOrderBean;
import com.ruirong.chefang.event.UpdateBeforeUIDateEvent;
import com.ruirong.chefang.event.UpdateInformationEvent;
import com.ruirong.chefang.event.UpdateShopOrderEvent;
import com.ruirong.chefang.http.RemoteApi;
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
 * Created by 16690 on 2018/3/28.
 * describe: 商城订单 待付款
 */

public class ShopOrderListWaitPayFragment extends BaseFragment implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener, OnItemChildClickListener {


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

    private int page = 1;
    private Unbinder unbinder;
    private List<ShopOrderBean> baseList = new ArrayList<>();
    private ShopOrderWaitPayAdapter adapter;

    private Dialog mDialog;
    private TextView no, yes;
    private ListView id_reason;
    private List<SelectedStateBean> reasonList = new ArrayList<>();
    private String reasonText;
    private ReasonAdapter reasonAdapter;

    private PreferencesHelper helper;

    private boolean isLazy = false;

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_shop_order_list, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);

        EventBusUtil.register(this);

        helper = new PreferencesHelper(getActivity());
        adapter = new ShopOrderWaitPayAdapter(canContentView);
        canContentView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter.setOnRVItemClickListener(this);
        adapter.setOnItemChildClickListener(this);
        canContentView.setAdapter(adapter);

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
        LogUtil.i("token: " + helper.getToken());
        isLazy = true;
        getListData(helper.getToken(), 2, page);
    }

    public void getListData(String token, int type, final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).getShopOrderList(token, type, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<ShopOrderBean>>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<List<ShopOrderBean>> baseBean) {
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
                                    adapter.clear();
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }

                            } else {
                                if (baseList != null && baseList.size() > 0) {
                                    adapter.addMoreData(baseList);
                                } else {
                                    ToastUtil.showToast(mContext, getResources().getString(R.string.no_more));
                                }
                            }
                        } else if (baseBean.code==4){
                            ToolUtil.loseToLogin(mContext);
                        }else {
                            ToastUtil.showToast(mContext, baseBean.message);
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
        page = 1;
        getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent = new Intent(mContext, ShopOrderDetailsActivity.class);
        intent.putExtra("number_bh", adapter.getItem(position).getOrder_id());
        mContext.startActivity(intent);
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        switch (childView.getId()) {
            case R.id.tv_contact_seller:
                WebActivity.startActivity(mContext, "客服热线", com.ruirong.chefang.common.Constants.SEARVICE);
                break;

            case R.id.goods_cancel:
                storeValueDialog(position);
                break;

            case R.id.order_all_tv_js:
                Intent intent = new Intent(mContext, ImmediatelyPaymentActivity.class);
                intent.putExtra("mode_type", "1");
                intent.putExtra("place_type", adapter.getItem(position).getGoods_type());
                intent.putExtra("shop_price", adapter.getItem(position).getShi_money() + "");
                intent.putExtra("order_sn", adapter.getItem(position).getOrder_id());
                startActivity(intent);
                break;

            case R.id.customer_del:
                delShopOrder(helper.getToken(), adapter.getItem(position).getOrder_id(), "订单过期");
                break;
        }
    }

    /**
     * 取消原因
     *
     * @param position
     */
    private void storeValueDialog(final int position) {
        mDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.cancel_reason, null);
        initDialogView(inflate);
        id_reason.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setFalse();
                reasonList.get(position).setTrue(true);
                reasonText = reasonList.get(position).getTitle();
                reasonAdapter.notifyDataSetChanged();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(reasonText)) {
                    ToastUtil.showToast(mContext, "请选择原因");
                } else {
                    mDialog.dismiss();
                    delShopOrder(new PreferencesHelper(getActivity()).getToken(), adapter.getItem(position).getOrder_id(), reasonText);
                }
            }
        });

        mDialog.setContentView(inflate);
        Window dialogwindow = mDialog.getWindow();
        dialogwindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogwindow.setAttributes(lp);
        mDialog.show();
    }

    private void initDialogView(View inflate) {
        no = (TextView) inflate.findViewById(R.id.no);
        yes = (TextView) inflate.findViewById(R.id.yes);
        id_reason = (ListView) inflate.findViewById(R.id.id_reason);
        reasonList.clear();
        for (int i = 0; i < 3; i++) {
            SelectedStateBean selectedStateBean = new SelectedStateBean();
            selectedStateBean.setTrue(false);
            switch (i) {
                case 0:
                    selectedStateBean.setTitle("不想要了");
                    break;
                case 1:
                    selectedStateBean.setTitle("信息填写错误,重新拍");
                    break;
                case 2:
                    selectedStateBean.setTitle("其他原因");
                    break;
            }
            reasonList.add(selectedStateBean);
        }

        reasonAdapter = new ReasonAdapter(id_reason);
        id_reason.setAdapter(reasonAdapter);
        reasonAdapter.setData(reasonList);
    }


    /**
     * 删除商城订单
     *
     * @param token
     * @param id
     * @param cancel
     */
    private void delShopOrder(String token, String id, String cancel) {
        HttpHelp.getInstance().create(RemoteApi.class).delShopOrder(token, id, cancel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(mContext, baseBean.message);
                            refresh.autoRefresh();
                        }else if (baseBean.code==4){
//                            ToolUtil.loseToLogin(mContext);
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

    private void setFalse() {
        for (int i = 0; i < reasonList.size(); i++) {
            reasonList.get(i).setTrue(false);
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

    /**
     * 付款事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateEvent(UpdateInformationEvent event) {
        page = 1;
        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

}
