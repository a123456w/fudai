package com.ruirong.chefang.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import com.google.gson.Gson;
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
import com.qlzx.mylibrary.widget.payDialog.PayPwdView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.ExpressDeliveryDetailsActivity;
import com.ruirong.chefang.activity.ReserveOrderCommentActivity;
import com.ruirong.chefang.activity.SettingPaymentPasswordActivity;
import com.ruirong.chefang.activity.WebActivity;
import com.ruirong.chefang.adapter.LuckyBagListAdapter;
import com.ruirong.chefang.adapter.ReasonAdapter;
import com.ruirong.chefang.bean.LuckyBagBean;
import com.ruirong.chefang.bean.SelectedStateBean;
import com.ruirong.chefang.bean.UserInforBean;
import com.ruirong.chefang.event.LuckyOrderCommnetEvent;
import com.ruirong.chefang.event.UpdateBeforeUIDateEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.personalcenter.LuckyBagOrderDetailActivity;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.widget.PasswordInputView;

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
 * Created by 16690 on 2018/4/3.
 * describe: 福袋订单 全部
 */

public class LuckyBagListAllFragment extends BaseFragment implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnItemChildClickListener, OnRVItemClickListener, PayPwdView.InputCallBack {

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

    public static final int PAYCODE = 1;
    public static final int CONFIG = 2;

    private Dialog mDialog;
    private TextView no, yes;
    private ListView id_reason;
    private List<SelectedStateBean> reasonList = new ArrayList<>();
    private String reasonText;
    private ReasonAdapter reasonAdapter;

    private Dialog qrDialog;//确认收货

    private boolean isLazy = false;

    private int position;
    private int type;

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

        showLoadingDialog("加载中...");
    }

    @Override
    public void getData() {
        isLazy = true;
        getListData(new PreferencesHelper(mContext).getToken(), "1", page);
    }



    private void getListData(String token, String type, final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).getLuckyBagList(token, type, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<LuckyBagBean>>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<List<LuckyBagBean>> baseBean) {
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
        Intent intent = new Intent(mContext, LuckyBagOrderDetailActivity.class);
        intent.putExtra("number_bh", adapter.getItem(position).getNumber_bh());
        startActivity(intent);
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        switch (childView.getId()) {
            case R.id.tv_pay:
                showPayDialog(position);
                break;
            case R.id.goods_cancel:
                storeValueDialog(position);
                break;
            case R.id.tv_contact_seller:
//                ToastUtil.showToast(mContext, "联系卖家");
                WebActivity.startActivity(mContext, "客服热线", com.ruirong.chefang.common.Constants.SEARVICE);
                break;
            case R.id.tv_del:
                TextView tv = (TextView) childView;
                String str = tv.getText().toString();
                if ("删除订单".equals(str)) {
                    delLuckyBagOrder(new PreferencesHelper(getActivity()).getToken(), adapter.getItem(position).getNumber_bh(), "");
                } else if ("确认收货".equals(str)) {
                    showPop(position);
                } else if ("评价".equals(str)) {
                    Intent intent = new Intent(getActivity(), ReserveOrderCommentActivity.class);
                    intent.putExtra("order_id", adapter.getItem(position).getNumber_bh());
                    intent.putExtra("type", 2);
                    startActivity(intent);
                }
                break;
            case R.id.goods_hint:
                TextView tv1 = (TextView) childView;
                String str1 = tv1.getText().toString();
                if ("提醒发货".equals(str1)) {
                    remind(new PreferencesHelper(mContext).getToken(), adapter.getItem(position).getNumber_bh(), "2");
                } else if ("查看详情".equals(str1)) {
                    Intent intent = new Intent(mContext, LuckyBagOrderDetailActivity.class);
                    intent.putExtra("number_bh", adapter.getItem(position).getNumber_bh());
                    startActivity(intent);
                }
                break;
            case R.id.goods_address://查看物流
                Intent intent = new Intent(mContext, ExpressDeliveryDetailsActivity.class);
                intent.putExtra("number_bh", adapter.getItem(position).getNumber_bh());
                startActivity(intent);
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
                    delLuckyBagOrder(new PreferencesHelper(getActivity()).getToken(), adapter.getItem(position).getNumber_bh(), reasonText);
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
     * 删除订单
     *
     * @param token
     * @param id
     * @param cancel
     */
    private void delLuckyBagOrder(String token, String id, String cancel) {
        HttpHelp.getInstance().create(RemoteApi.class).delLuckyBagOrder(token, id, cancel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(mContext, baseBean.message);
                            page = 1;
                            getData();
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
     * 支付弹窗
     *
     * @param position
     */
    private void showPayDialog(int position) {
        String userInfo = new PreferencesHelper(mContext).getUserInfo();
        Gson gson = new Gson();
        UserInforBean userInforBean = gson.fromJson(userInfo, UserInforBean.class);

        if (userInforBean.getIs_has_pay_pass() == 0) {
            //没有pay_pass
            final Intent intent = new Intent(getActivity(), SettingPaymentPasswordActivity.class);
            //ToastUtil.showToast(MyRoomTimeActivity.this, "没有设置key_pass");
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("提示");
            builder.setMessage("您还设置支付密码，前去设置");
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(intent);
                }
            });
            builder.create().show();

        } else {
            //弹出密码输入框
            showmima(position, PAYCODE);
        }
    }

    private void showmima(int position, int type) {
        this.position = position;
        this.type = type;
        if (type == PAYCODE) {
            DialogUtil.showPayDialog(this, "请输入支付密码", adapter.getItem(position).getOrder_amount(), "", getActivity().getSupportFragmentManager());
        } else if (type == CONFIG) {
            DialogUtil.showPayDialog(this, "请输入支付密码", "", "", getActivity().getSupportFragmentManager());
        }
    }


    /**
     * 付款
     *
     * @param token
     * @param orderId
     * @param password
     */
    private void pay(String token, String orderId, String password) {
        HttpHelp.getInstance().create(RemoteApi.class).paychild(token, orderId, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(mContext, baseBean.message);
                            DialogUtil.dismissPayDialog();
                            page = 1;
                            getData();
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
     * 确认收货弹窗
     *
     * @param position
     */
    private void showPop(final int position) {
        qrDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.pop_confirm, null);
        TextView quxiao, queren;
        quxiao = (TextView) inflate.findViewById(R.id.quxiao);
        queren = (TextView) inflate.findViewById(R.id.queren);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrDialog.dismiss();
            }
        });
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrDialog.dismiss();
                showmima(position, CONFIG);
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
     * 确认收货
     *
     * @param token
     * @param orderId
     * @param password
     */
    private void config(String token, String orderId, String password) {
        HttpHelp.getInstance().create(RemoteApi.class).LukyBagsreceipt(token, orderId, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(mContext, baseBean.message);
                            DialogUtil.dismissPayDialog();
                            page = 1;
                            getData();
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
     * 刷新数据
     *
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

    @Override
    public void onInputFinish(String result) {
        if (type == PAYCODE) {
            pay(new PreferencesHelper(getActivity()).getToken(), adapter.getItem(position).getNumber_bh(), result);
        } else if (type == CONFIG) {
            config(new PreferencesHelper(getActivity()).getToken(), adapter.getItem(position).getNumber_bh(), result);
        }
    }

}
