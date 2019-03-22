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
import com.qlzx.mylibrary.util.DialogUtil;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.payDialog.PayPwdView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.ImmediatelyPaymentActivity;
import com.ruirong.chefang.activity.ShopOrderDetailsActivity;
import com.ruirong.chefang.activity.WebActivity;
import com.ruirong.chefang.adapter.ReasonAdapter;
import com.ruirong.chefang.adapter.ShopOrderWaitReceiveAdapter;
import com.ruirong.chefang.bean.SelectedStateBean;
import com.ruirong.chefang.bean.ShopOrderBean;
import com.ruirong.chefang.event.LoginSuccessEvent;
import com.ruirong.chefang.event.ShopOrderCommentEvent;
import com.ruirong.chefang.event.UpdateBeforeUIDateEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.personalcenter.ShopOrderCommentActivity;
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
 * Created by wu on 2018/3/28.
 * describe: 商城订单  全部
 */

public class ShopOrderListAllFragment extends BaseFragment implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener, OnItemChildClickListener, PayPwdView.InputCallBack {

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
    private ShopOrderWaitReceiveAdapter adapter;

    private PreferencesHelper helper;

    private Dialog mDialog;
    private TextView no, yes;
    private ListView id_reason;
    private List<SelectedStateBean> reasonList = new ArrayList<>();
    private String reasonText;
    private ReasonAdapter reasonAdapter;

    private Dialog qrDialog;//确认收货

    private boolean isLazy = false;
    private int positionF;

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_shop_order_list, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        EventBusUtil.register(this);
        loadingLayout.setStatus(LoadingLayout.Success);

        helper = new PreferencesHelper(getActivity());

        adapter = new ShopOrderWaitReceiveAdapter(canContentView);
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

        showLoadingDialog("加载中...");
    }

    @Override
    public void getData() {
        isLazy = true;
        getListData(helper.getToken(), 1, page);
    }

    public void getListData(String token, int type, final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).getShopOrderList(token, type, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<ShopOrderBean>>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<List<ShopOrderBean>> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
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
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(mContext);
                        } else {
                            ToastUtil.showToast(mContext, baseBean.message);
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
            case R.id.tv_one:
                WebActivity.startActivity(mContext, "客服热线", com.ruirong.chefang.common.Constants.SEARVICE);
                break;
            case R.id.tv_two:
                storeValueDialog(position);
                break;
            case R.id.tv_three:
                String msg = ((TextView) childView).getText().toString();
                switch (msg) {
                    case "付款"://待付款  未过期
                        Intent intent2 = new Intent(mContext, ImmediatelyPaymentActivity.class);
                        intent2.putExtra("mode_type", "1");
                        intent2.putExtra("place_type", adapter.getItem(position).getGoods_type());
                        intent2.putExtra("shop_price", adapter.getData().get(position).getShi_money() + "");
                        intent2.putExtra("order_sn", adapter.getData().get(position).getOrder_id());
                        startActivity(intent2);
                        break;
                    case "删除订单"://待付款 过期
                        delShopOrder(helper.getToken(), adapter.getItem(position).getOrder_id(), "订单过期");
                        break;
                    case "确认订单"://使用中
                        positionF = position;
                        showPop();
                        break;
                    case "评价"://待评价
                        Intent intent = new Intent(getActivity(), ShopOrderCommentActivity.class);
                        intent.putExtra("order_id", baseList.get(position).getOrder_id());
                        intent.putExtra("shop_id", baseList.get(position).getGoodslist().get(0).getShop_id());
                        startActivity(intent);
                        break;
                    case "查看详情"://售后
                        Intent intent1 = new Intent(mContext, ShopOrderDetailsActivity.class);
                        intent1.putExtra("number_bh", adapter.getItem(position).getOrder_id());
                        startActivity(intent1);
                        break;
                    case "提醒配送":
                        remind(new PreferencesHelper(mContext).getToken(), adapter.getItem(position).getOrder_id(), "3");
                        break;
                    case "去核销":
                        Intent intenthexiao = new Intent(mContext, ShopOrderDetailsActivity.class);
                        intenthexiao.putExtra("number_bh", adapter.getItem(position).getOrder_id());
                        startActivity(intenthexiao);
                        break;
                }
                break;

        }

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
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(mContext);
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

    private void setFalse() {
        for (int i = 0; i < reasonList.size(); i++) {
            reasonList.get(i).setTrue(false);
        }
    }


    /**
     * 确认收货弹窗
     */
    private void showPop() {
        qrDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.pop_shop_confirm, null);
        TextView quxiao, queren;
        quxiao = (TextView) inflate.findViewById(R.id.quxiao);
        queren = (TextView) inflate.findViewById(R.id.queren);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrDialog.dismiss();
                showPayDialog();

            }
        });
        qrDialog.setContentView(inflate);
        Window dialogwindow = qrDialog.getWindow();
        dialogwindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogwindow.setAttributes(lp);
        qrDialog.show();

    }

    /**
     * 提醒发货
     *
     * @param token
     * @param order_id
     * @param type
     */
    private void remind(String token, String order_id, String type) {
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


    /**
     * 确认收货
     *
     * @param token
     * @param id
     * @param password
     */
    private void goodsreceipt(String token, String id, String password) {
        HttpHelp.getInstance().create(RemoteApi.class).shopGoodsreceipt(token, id, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            refresh.autoRefresh();
                            ToastUtil.showToast(mContext, baseBean.message);
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(mContext);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isLazy) {
            page = 1;
            getData();
        }
    }

    /**
     * 刷新数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void commentCallback(UpdateBeforeUIDateEvent event) {
        page = 1;
        getData();
    }

    //直接评论时 刷新数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void commentCallback(ShopOrderCommentEvent event) {
        page = 1;
        getData();
    }

    /**
     * 付款事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateEvent(LoginSuccessEvent event) {
        page = 1;
        getData();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }


    private void showPayDialog() {
        //弹出支付密码输入框
        DialogUtil.showPayDialog(this, "请输入支付密码", "", "", getActivity().getSupportFragmentManager());
    }

    @Override
    public void onInputFinish(String result) {
        goodsreceipt(new PreferencesHelper(mContext).getToken(), adapter.getData().get(positionF).getOrder_id(), result);
        DialogUtil.dismissPayDialog();
    }
}
