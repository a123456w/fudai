package com.ruirong.chefang.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
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
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.payDialog.PayPwdView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.ExpressDeliveryDetailsActivity;
import com.ruirong.chefang.activity.ImmediatelyPaymentActivity;
import com.ruirong.chefang.activity.SpecialProductReview;
import com.ruirong.chefang.activity.SpecialtyOrderDetailsActivity;
import com.ruirong.chefang.activity.WebActivity;
import com.ruirong.chefang.adapter.MyOrderAllListviewAdapter;
import com.ruirong.chefang.adapter.ReasonAdapter;
import com.ruirong.chefang.bean.SelectedStateBean;
import com.ruirong.chefang.bean.SpecialtyOrderBean;
import com.ruirong.chefang.event.PendingPaymentEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import org.greenrobot.eventbus.EventBus;
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
 * 全部订单
 */

public class OrderListAllFragment extends BaseFragment implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener, OnItemChildClickListener, PayPwdView.InputCallBack {

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
    private List<SpecialtyOrderBean> baseList = new ArrayList<>();
    private MyOrderAllListviewAdapter adapter;
    private Dialog mDialog;
    private Dialog mDialogh;
    private Dialog mDialoghh;
    private int positionF;

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_wait_pay_money, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        showLoadingDialog("加载中...");
        initData();
//        Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
    }

    /**
     * 初始化数据
     */
    public void initData() {
        adapter = new MyOrderAllListviewAdapter(canContentView);
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

//        canContentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
//                startActivity(intent);
//
//            }
//        });
    }


    @Override
    public void getData() {
        getListData(new PreferencesHelper(getActivity()).getToken(), "1", page);
        Log.i("XXX", new PreferencesHelper(getActivity()).getToken());
    }


    public void getListData(String token, String type, final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).orderList(token, type, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<SpecialtyOrderBean>>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<List<SpecialtyOrderBean>> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            baseList = baseBean.data;
                            if (page == 1) {
                                if (baseList != null && baseList.size() > 0) {
                                    refresh.setVisibility(View.VISIBLE);
                                    rlEmpty.setVisibility(View.GONE);
                                    adapter.setData(baseList);
                                } else {
                                    refresh.setVisibility(View.GONE);
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
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.i("XXX", throwable.getLocalizedMessage());
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


    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void PendingPaymentEventBus(PendingPaymentEvent msg) {
        page = 1;
        getData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {

    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        Intent intentx = new Intent(mContext, SpecialtyOrderDetailsActivity.class);
        intentx.putExtra("number_bh", adapter.getData().get(position).getNumber_bh());
        Intent intentw = new Intent(mContext, ExpressDeliveryDetailsActivity.class);
        intentw.putExtra("number_bh", adapter.getData().get(position).getNumber_bh());
        switch (childView.getId()) {
            case R.id.order_all_tv_payment:
//                ToastUtil.showToast(mContext, "付款");
                Intent intent = new Intent(mContext, ImmediatelyPaymentActivity.class);
                intent.putExtra("mode_type", "1");
                intent.putExtra("place_type", "4");
                intent.putExtra("shop_price", adapter.getData().get(position).getShi_money() + "");
                intent.putExtra("order_sn", adapter.getData().get(position).getNumber_bh());
                startActivity(intent);
                break;
            case R.id.customer_del:
//                ToastUtil.showToast(mContext, "删除");
                delspecialty(new PreferencesHelper(mContext).getToken(), adapter.getData().get(position).getNumber_bh(), null);
                break;
            case R.id.order_all_tv_cancel_order:
//                ToastUtil.showToast(mContext, "取消订单");
                storeValueDialog(position);
                break;
            case R.id.tv_contact_seller:
//                ToastUtil.showToast(mContext, "联系卖家");
                WebActivity.startActivity(mContext, "客服热线", com.ruirong.chefang.common.Constants.SEARVICE);
                break;
            case R.id.order_all_tv_deliver:
                remind(new PreferencesHelper(mContext).getToken(), adapter.getData().get(position).getNumber_bh(), "1");
                break;
            case R.id.order_all_tv_confirm:
//                ToastUtil.showToast(mContext, "确认收货");
                positionF = position;
                showPop();
                break;
            case R.id.tv_look_logistics:
//                ToastUtil.showToast(mContext, "待收货-查看物流");
                startActivity(intentw);
                break;
            case R.id.order_all_tv_evaluate:
//                ToastUtil.showToast(mContext, "评价");
                Intent intentp = new Intent(mContext, SpecialProductReview.class);
                intentp.putExtra("order_id", adapter.getData().get(position).getNumber_bh());
                mContext.startActivity(intentp);
                break;
            case R.id.tv_look_logistics_collect:
//                ToastUtil.showToast(mContext, "待评价 查看物流");
                startActivity(intentw);
                break;
            case R.id.order_all_tv_details:
//                ToastUtil.showToast(mContext, "查看详情");
                startActivity(intentx);
                break;
            case R.id.tv_contact_seller_details:
//                ToastUtil.showToast(mContext, "待付款 查看详情");
                startActivity(intentx);
                break;
            case R.id.customer_del_details:
//                ToastUtil.showToast(mContext, "待付款 已过期 查看详情");
                startActivity(intentx);
                break;
            case R.id.order_all_tv_deliver_details:
//                ToastUtil.showToast(mContext, "待发货 查看详情");
                startActivity(intentx);
                break;
            case R.id.customer_complete:
//                ToastUtil.showToast(mContext, "已完成 查看详情");
                startActivity(intentx);
                break;
            case R.id.customer_complete_collect:
//                ToastUtil.showToast(mContext, "已完成 查看物流");
                startActivity(intentw);
                break;
            case R.id.tv_look_logistics_details:
//                ToastUtil.showToast(mContext, "待收货 查看详情");
                startActivity(intentx);
                break;
            case R.id.tv_look_logistics_collect_details:
//                ToastUtil.showToast(mContext, "待评价 查看详情");
                startActivity(intentx);
                break;
        }
    }

    /**************************   待收货   ***********************************/


    private void showPop() {
//
//        View mContentView = null;
//        if(mContentView == null){
//            mContentView = LayoutInflater.from(mContext).inflate(R.layout.pop_confirm, null);
//        }
//
//        if(mPopUpWindow == null){
//            mPopUpWindow = new PopupWindow(mContentView, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//            mPopUpWindow.setBackgroundDrawable(new BitmapDrawable());
//            mPopUpWindow.setOutsideTouchable(true);
//            mPopUpWindow.setFocusable(true);
//        }
//        mPopUpWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);


        mDialogh = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.pop_confirm, null);

        TextView quxiao, queren;
        quxiao = (TextView) inflate.findViewById(R.id.quxiao);
        queren = (TextView) inflate.findViewById(R.id.queren);

        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogh.dismiss();
            }
        });

        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogh.dismiss();
//                showMima(position);
                showPayDialog();
            }
        });


        mDialogh.setContentView(inflate);
        Window dialogwindow = mDialogh.getWindow();

        dialogwindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
//        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        dialogwindow.setAttributes(lp);
        mDialogh.show();

    }

//    private void showMima(final int position) {
//
//        mDialoghh = new Dialog(mContext, R.style.ActionSheetDialogStyle);
//        View inflate = LayoutInflater.from(mContext).inflate(R.layout.pop_mima, null);
//
//        final PasswordInputView pass_pwd;
//        pass_pwd = (PasswordInputView) inflate.findViewById(R.id.pass_pwd);
//
//        pass_pwd.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (pass_pwd.getText().toString().length() == 6 && pass_pwd.getText().toString().length() == 6) {
//                    ToastUtil.showToast(mContext, "输入密码");
//                    goodsreceipt(new PreferencesHelper(mContext).getToken(), adapter.getData().get(position).getNumber_bh(), pass_pwd.getText().toString().trim());
//                }
//            }
//        });
//
//
//        mDialoghh.setContentView(inflate);
//        Window dialogwindow = mDialoghh.getWindow();
//
//        dialogwindow.setGravity(Gravity.CENTER);
//        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
////        lp.windowAnimations = R.style.PopUpBottomAnimation;
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//
//        dialogwindow.setAttributes(lp);
//        mDialoghh.show();
//    }

    private void goodsreceipt(String token, String id, String password) {
        HttpHelp.getInstance().create(RemoteApi.class).goodsreceipt(token, id, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            page = 1;
                            getData();
                            ToastUtil.showToast(mContext, baseBean.message);
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(mContext);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }


    /*************************  待付款  *********************************/

    private TextView no, yes;
    private ListView id_reason;
    private List<SelectedStateBean> reasonList = new ArrayList<>();
    private ReasonAdapter reasonAdapter;
    private String reasonText;

    /**
     * 取消的弹框
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
                    delspecialty(new PreferencesHelper(mContext).getToken(), adapter.getData().get(position).getNumber_bh(), reasonText);
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

    /**
     * 取消订单
     *
     * @param token
     * @param id
     * @param cancel
     */
    private void delspecialty(String token, String id, String cancel) {
        HttpHelp.getInstance().create(RemoteApi.class).delspecialty(token, id, cancel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            page = 1;
                            getData();
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(mContext);
                        }
                        ToastUtil.showToast(mContext, baseBean.message);
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


    private void showPayDialog() {
        //弹出支付密码输入框
        DialogUtil.showPayDialog(this, "请输入支付密码", "", "", getActivity().getSupportFragmentManager());
    }

    @Override
    public void onInputFinish(String result) {
        goodsreceipt(new PreferencesHelper(mContext).getToken(), adapter.getData().get(positionF).getNumber_bh(), result);
        DialogUtil.dismissPayDialog();
    }
}
