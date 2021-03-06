package com.ruirong.chefang.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.ruirong.chefang.activity.ReserveOrderCommentActivity;
import com.ruirong.chefang.adapter.ReasonAdapter;
import com.ruirong.chefang.adapter.ReserveOrderListAllAdapter;
import com.ruirong.chefang.adapter.ReserveOrderListviewUseAdapter;
import com.ruirong.chefang.bean.ReserveOrderBean;
import com.ruirong.chefang.bean.SelectedStateBean;
import com.ruirong.chefang.event.ReservationOrderEvent;
import com.ruirong.chefang.event.UpdateBeforeUIDateEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.personalcenter.ReserveOrderDetailsActivity;
import com.ruirong.chefang.personalcenter.ReserveOrderRefundActicity;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.widget.PasswordInputView;

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
 * Created by 16690 on 2018/4/9.
 * describe:  预定订单全部
 */

public class ReserveOrderListAllFragment extends BaseFragment implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener, OnItemChildClickListener, PayPwdView.InputCallBack {
    Unbinder unbinder;
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

    private PreferencesHelper helper;
    private int type = 1;
    private int page = 1;
    private int num = 10;
    private boolean isLazy = false;

    private Dialog mDialogs;//输入密码

    private Dialog mDialog;
    private TextView no, yes;
    private ListView id_reason;
    private List<SelectedStateBean> reasonList = new ArrayList<>();
    private String reasonText;
    private ReasonAdapter reasonAdapter;

    private ReserveOrderListAllAdapter adapter;
    private List<ReserveOrderBean.ListBean> baseList = new ArrayList<>();
    private int positionF;

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_reserve_order_all, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        EventBusUtil.register(this);
        showLoadingDialog("加载中...");
        initData();
    }

    /**
     * 初始化数据
     */
    public void initData() {
        adapter = new ReserveOrderListAllAdapter(canContentView);
        canContentView.setAdapter(adapter);
        canContentView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter.setOnItemChildClickListener(this);
        adapter.setOnRVItemClickListener(this);

        helper = new PreferencesHelper(getActivity());

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
        getListData(helper.getToken(), type, page, num);
    }

    public void getListData(String token, int type, final int page, int num) {

        HttpHelp.getInstance().create(RemoteApi.class).getReserveOrder(token, type, page, num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ReserveOrderBean>>(getActivity(), loadingLayout) {
                    @Override
                    public void onNext(BaseBean<ReserveOrderBean> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            baseList = baseBean.data.getList();
                            if (page == 1) {
                                if (baseList != null && baseList.size() > 0) {
                                    adapter.setGqTime(Integer.parseInt(baseBean.data.getGqTime()));
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
                                    ToastUtil.showToast(getActivity(), "暂无更多");
                                }
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(mContext);
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
        Intent intent = new Intent(getActivity(), ReserveOrderDetailsActivity.class);
        intent.putExtra("order_id", ((ReserveOrderBean.ListBean) adapter.getItem(position)).getOrder_id());
        startActivity(intent);
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        switch (childView.getId()) {
            case R.id.tv_pay://付款
                Intent intent = new Intent(mContext, ImmediatelyPaymentActivity.class);
                intent.putExtra("place_type", "3");
                intent.putExtra("shop_price", adapter.getItem(position).getMoney());
                intent.putExtra("order_sn", adapter.getItem(position).getOrder_id());
                startActivity(intent);
                break;
            case R.id.tv_refund://退款
                Intent intent2 = new Intent(mContext, ReserveOrderRefundActicity.class);
                intent2.putExtra("data", adapter.getItem(position));
                startActivity(intent2);
                break;
            case R.id.tv_cancel://取消订单
                storeValueDialog(position);
                break;
            case R.id.customer_del://删除订单
                delReserveOrder(helper.getToken(), adapter.getItem(position).getOrder_id(), "订单过期");
                break;
            case R.id.tv_confirm:// 使用中 确认订单
                if ("4".equals(adapter.getItem(position).getStatus())) {
                    positionF = position;
//                    showMima(position);
                    showPayDialog();
                } else {
                    ToastUtil.showToast(mContext, "离店后，才可确认订单");
                }
                break;

            case R.id.tv_comment://评价
                Intent intent1 = new Intent(getActivity(), ReserveOrderCommentActivity.class);
                intent1.putExtra("order_id", adapter.getItem(position).getOrder_id());
                intent1.putExtra("type", 1);
                startActivity(intent1);
                break;

            case R.id.tv_go_detail://查看详情
                Intent intent3 = new Intent(getActivity(), ReserveOrderDetailsActivity.class);
                intent3.putExtra("order_id", ((ReserveOrderBean.ListBean) adapter.getItem(position)).getOrder_id());
                startActivity(intent3);
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isLazy) {
            page = 1;
            getData();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    /**
     * 确认订单
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
//                            mDialogs.dismiss();
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
     * 删除订单
     *
     * @param token
     * @param id
     * @param cancel
     */
    private void delReserveOrder(String token, String id, String cancel) {
        HttpHelp.getInstance().create(RemoteApi.class).delReserveOrder(token, id, cancel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(mContext, "删除成功");
                            if (mDialog != null && mDialog.isShowing()) {
                                mDialog.dismiss();
                            }
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
                    delReserveOrder(helper.getToken(), adapter.getItem(position).getOrder_id(), "reasonText");
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
        for (int i = 0; i < 6; i++) {
            SelectedStateBean selectedStateBean = new SelectedStateBean();
            selectedStateBean.setTrue(false);
            switch (i) {
                case 0:
                    selectedStateBean.setTitle("不想买了");
                    break;
                case 1:
                    selectedStateBean.setTitle("信息填写错误,重新拍");
                    break;
                case 2:
                    selectedStateBean.setTitle("卖家缺货");
                    break;
                case 3:
                    selectedStateBean.setTitle("同城见面交易");
                    break;
                case 4:
                    selectedStateBean.setTitle("卖家服务态度不好");
                    break;
                case 5:
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
     * 评价完成事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void commentCallback(ReservationOrderEvent event) {
        page = 1;
        getData();
    }

    /**
     * 支付更新数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void payUpdate(ReservationOrderEvent event) {
        page = 1;
        getData();
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
