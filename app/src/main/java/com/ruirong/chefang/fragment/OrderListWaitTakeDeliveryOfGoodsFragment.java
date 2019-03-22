package com.ruirong.chefang.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
import com.ruirong.chefang.activity.SpecialtyOrderDetailsActivity;
import com.ruirong.chefang.adapter.MyOrderListviewWaitTakeGoodAdapter;
import com.ruirong.chefang.bean.SpecialtyOrderBean;
import com.ruirong.chefang.event.PendingPaymentEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.widget.PasswordInputView;
import com.umeng.socialize.utils.Log;

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
 * describe:待收货
 * Created by chenlipeng on 2017/12/21 13:43
 */


public class OrderListWaitTakeDeliveryOfGoodsFragment extends BaseFragment implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener, OnItemChildClickListener, PayPwdView.InputCallBack {

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
    int page = 1;

    private List<SpecialtyOrderBean> baseList = new ArrayList<>();
    private MyOrderListviewWaitTakeGoodAdapter adapter;
    private PopupWindow mPopUpWindow;
    private Dialog mDialog;
    private Dialog mDialogs;
    private int positionF = -1;

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
        initData();
//        Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
    }

    /**
     * 初始化数据
     */
    public void initData() {
        adapter = new MyOrderListviewWaitTakeGoodAdapter(canContentView);
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
//                intent.putExtra("type", "待收货");
//
//                startActivity(intent);
//
//            }
//        });
    }


    @Override
    public void getData() {
        getListData(new PreferencesHelper(getActivity()).getToken(), "4", page);
    }


    public void getListData(String token, String type, final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).orderList(token, type, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<SpecialtyOrderBean>>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<List<SpecialtyOrderBean>> baseBean) {
                        super.onNext(baseBean);
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
        switch (childView.getId()) {
            case R.id.tv_look_logistics:
                Intent intent = new Intent(mContext, ExpressDeliveryDetailsActivity.class);
                intent.putExtra("number_bh", adapter.getData().get(position).getNumber_bh());
                startActivity(intent);
                break;
            case R.id.order_all_tv_js:
                positionF = position;
                showPop();

                break;
            case R.id.tv_look_details:
//                ToastUtil.showToast(mContext, "查看详情");
                Intent intentx = new Intent(mContext, SpecialtyOrderDetailsActivity.class);
                intentx.putExtra("number_bh", adapter.getData().get(position).getNumber_bh());
                startActivity(intentx);
                break;

        }
    }

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


        mDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.pop_confirm, null);

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
                mDialog.dismiss();
//                showMima(position);
                showPayDialog();
            }
        });


        mDialog.setContentView(inflate);
        Window dialogwindow = mDialog.getWindow();

        dialogwindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
//        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        dialogwindow.setAttributes(lp);
        mDialog.show();

    }

    private void showPayDialog() {
        //弹出支付密码输入框
        DialogUtil.showPayDialog(this, "请输入支付密码", "", "", getActivity().getSupportFragmentManager());
    }

    @Override
    public void onInputFinish(String result) {
        Log.i("XXX", result);
        goodsreceipt(new PreferencesHelper(mContext).getToken(), adapter.getData().get(positionF).getNumber_bh(), result);
        DialogUtil.dismissPayDialog();
    }

    private void showMima(final int position) {

        mDialogs = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.pop_mima, null);

        final PasswordInputView pass_pwd;
        pass_pwd = (PasswordInputView) inflate.findViewById(R.id.pass_pwd);
        ImageView del = (ImageView) inflate.findViewById(R.id.del_pic);

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogs.dismiss();
            }
        });

        pass_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (pass_pwd.getText().toString().length() == 6 && pass_pwd.getText().toString().length() == 6) {
//                    ToastUtil.showToast(mContext, "输入密码");
                    goodsreceipt(new PreferencesHelper(mContext).getToken(), adapter.getData().get(position).getNumber_bh(), pass_pwd.getText().toString().trim());
                }
            }
        });


        mDialogs.setContentView(inflate);
        Window dialogwindow = mDialogs.getWindow();

        dialogwindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
//        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        dialogwindow.setAttributes(lp);
        mDialogs.show();

    }

    private void goodsreceipt(String token, String id, String password) {
        HttpHelp.getInstance().create(RemoteApi.class).goodsreceipt(token, id, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
//                        mDialogs.dismiss();
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
//                        mDialogs.dismiss();
                    }
                });

    }


}
