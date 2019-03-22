package com.ruirong.chefang.personalcenter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.DialogUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;
import com.qlzx.mylibrary.widget.payDialog.PayPwdView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.GoToBuyABillActivity;
import com.ruirong.chefang.activity.SettingPaymentPasswordActivity;
import com.ruirong.chefang.activity.WebActivity;
import com.ruirong.chefang.adapter.AlreadyExchangeListAdapter;
import com.ruirong.chefang.adapter.RoomTimeListAdapter;
import com.ruirong.chefang.bean.AlreadyExchangeBean;
import com.ruirong.chefang.bean.RoomTimeBean;
import com.ruirong.chefang.bean.UserInforBean;
import com.ruirong.chefang.common.Constants;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.widget.PasswordInputView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2018/1/4 0004
 * describe:  我的房时
 */
public class MyRoomTimeActivity
        extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener, OnItemChildClickListener, PayPwdView.InputCallBack

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
    @BindView(R.id.rb_bottom1)
    RadioButton rbBottom1;
    @BindView(R.id.rb_bottom2)
    RadioButton rbBottom2;
    @BindView(R.id.rg_bottom)
    RadioGroup rgBottom;


    private RoomTimeListAdapter roomTimeListAdapter;
    private AlreadyExchangeListAdapter alreadyExchangeListAdapter;

    private List<RoomTimeBean> roomTimeList = new ArrayList<>();
    private List<AlreadyExchangeBean> exchangedList = new ArrayList<>();

    private PreferencesHelper helper;
    private UserInforBean userInforBean;
    private int type = 1;
    private int page = 1;

    private Dialog mimaDialog;//输入密码
    private int currentPosition = -1;

    @Override
    public int getContentView() {
        return R.layout.activity_my_room_time;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("我的房时");
        titleBar.setRightTextRes("规则");
        titleBar.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebActivity.startActivity(MyRoomTimeActivity.this, "房时规则", Constants.HOUSERULE);
            }
        });

        helper = new PreferencesHelper(this);
        showLoadingDialog("加载中...");
        String userInfo = new PreferencesHelper(MyRoomTimeActivity.this).getUserInfo();
        Gson gson = new Gson();
        userInforBean = gson.fromJson(userInfo, UserInforBean.class);


        roomTimeListAdapter = new RoomTimeListAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(MyRoomTimeActivity.this));
        canContentView.addItemDecoration(new RecycleViewDivider(MyRoomTimeActivity.this));
        canContentView.setAdapter(roomTimeListAdapter);
        roomTimeListAdapter.setOnRVItemClickListener(this);
        roomTimeListAdapter.setOnItemChildClickListener(this);

        alreadyExchangeListAdapter = new AlreadyExchangeListAdapter(canContentView);
        alreadyExchangeListAdapter.setOnRVItemClickListener(this);


        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(MyRoomTimeActivity.this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");


        rgBottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_bottom1:
                        type = 1;
                        if (roomTimeListAdapter.getItemCount() > 0) {
                            rlEmpty.setVisibility(View.GONE);
                        } else {
                            rlEmpty.setVisibility(View.VISIBLE);
                        }
                        canContentView.setAdapter(roomTimeListAdapter);

                        break;
                    case R.id.rb_bottom2:
                        type = 2;
                        if (alreadyExchangeListAdapter.getItemCount() > 0) {
                            rlEmpty.setVisibility(View.GONE);
                        } else {
                            rlEmpty.setVisibility(View.VISIBLE);
                        }
                        canContentView.setAdapter(alreadyExchangeListAdapter);
                        break;
                }

            }
        });

    }

    @Override
    public void getData() {
        getAlreadyData(helper.getToken(), true);
        getRoomTimeData(helper.getToken(), page);
    }

    /**
     * 获取已兑换数据
     *
     * @param token
     */
    public void getAlreadyData(String token, final boolean isFirst) {
        HttpHelp.getInstance().create(RemoteApi.class).getAlreadyExchange(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<AlreadyExchangeBean>>>(MyRoomTimeActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<List<AlreadyExchangeBean>> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            exchangedList = baseBean.data;
                            if (page == 1) {
                                if (exchangedList != null && exchangedList.size() > 0) {
                                    alreadyExchangeListAdapter.setData(exchangedList);
                                } else if (!isFirst) {
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }

                            } else {
                                if (exchangedList != null && exchangedList.size() > 0) {
                                    alreadyExchangeListAdapter.addMoreData(exchangedList);
                                } else {
                                    ToastUtil.showToast(MyRoomTimeActivity.this, getResources().getString(R.string.no_more));
                                }
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(MyRoomTimeActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                    }
                });
    }

    /**
     * 获取房时数据
     *
     * @param token
     * @param page
     */
    public void getRoomTimeData(String token, final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).getRoomTime(token, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<RoomTimeBean>>>(MyRoomTimeActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<List<RoomTimeBean>> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            roomTimeList = baseBean.data;
                            if (page == 1) {
                                if (roomTimeList != null && roomTimeList.size() > 0) {
                                    roomTimeListAdapter.setData(roomTimeList);
                                } else {
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }

                            } else {
                                if (roomTimeList != null && roomTimeList.size() > 0) {
                                    roomTimeListAdapter.addMoreData(roomTimeList);
                                } else {
                                    ToastUtil.showToast(MyRoomTimeActivity.this, getResources().getString(R.string.no_more));
                                }
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(MyRoomTimeActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                    }
                });
    }


    /**
     * 兑换房时
     *
     * @param token
     * @param shop_id
     * @param pay_pase
     * @param position item的位置
     */
    public void exchangTime(String token, String shop_id, String pay_pase, final int position) {
        LogUtil.i("token: " + token + "  shop_id: " + shop_id + "   pay_pase" + pay_pase);
        HttpHelp.getInstance().create(RemoteApi.class).exchangeRoomTime(token, shop_id, pay_pase)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(MyRoomTimeActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            RoomTimeBean bean = roomTimeListAdapter.getItem(position);
                            ToastUtil.showToast(MyRoomTimeActivity.this, baseBean.message);
                            int curtime = Integer.parseInt(bean.getSumtimes()) - Integer.parseInt(bean.getDui_time());
                            if (curtime >= 0) {
                                bean.setSumtimes(String.valueOf(curtime));
                                roomTimeListAdapter.notifyItemChanged(position);
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(MyRoomTimeActivity.this);
                        } else {
                            ToastUtil.showToast(MyRoomTimeActivity.this, baseBean.message);
                        }
//                        mimaDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
//                        mimaDialog.dismiss();
                    }
                });
    }


    @Override
    public void onLoadMore() {
        if (type == 1) {
            page++;
            getRoomTimeData(helper.getToken(), page);
        } else {
            refresh.loadMoreComplete();
            refresh.refreshComplete();

        }
    }

    @Override
    public void onRefresh() {
        if (type == 1) {
            page = 1;
            getRoomTimeData(helper.getToken(), page);
        } else {
            refresh.loadMoreComplete();
            refresh.refreshComplete();
        }

    }


    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {

    }


    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        switch (childView.getId()) {
            case R.id.tv_exchange:
                if (userInforBean.getIs_has_pay_pass() == 0) {
                    // TODO  跳转到设置密码
                    //没有pay_pass
                    final Intent intent = new Intent(MyRoomTimeActivity.this, SettingPaymentPasswordActivity.class);
                    //ToastUtil.showToast(MyRoomTimeActivity.this, "没有设置key_pass");
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyRoomTimeActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("您还未登录，请先登录");
                    builder.setNegativeButton("取消", null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(intent);
                        }
                    });
                    builder.create().show();


                } else {
                    //弹出密码输入框    兑换接口  Personal/exchange
//                    showMima(position);
                    currentPosition = position;
                    showPayDialog();
                }
                break;
        }
    }

    private void showPayDialog() {
        //弹出支付密码输入框
        DialogUtil.showPayDialog(MyRoomTimeActivity.this, "请输入支付密码", "", "", getSupportFragmentManager());
    }

    @Override
    public void onInputFinish(String result) {

        if (currentPosition != -1) {
            exchangTime(helper.getToken(), roomTimeListAdapter.getItem(currentPosition).getShop_id(), result, currentPosition);
        }

        DialogUtil.dismissPayDialog();
    }


    private void showMima(final int position) {
        mimaDialog = new Dialog(MyRoomTimeActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(MyRoomTimeActivity.this).inflate(R.layout.pop_mima, null);
        ImageView del = (ImageView) inflate.findViewById(R.id.del_pic);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mimaDialog.dismiss();
            }
        });
        final PasswordInputView pass_pwd;
        pass_pwd = (PasswordInputView) inflate.findViewById(R.id.pass_pwd);
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
                    exchangTime(helper.getToken(), roomTimeListAdapter.getItem(position).getShop_id(), pass_pwd.getText().toString(), position);
                }
            }
        });


        mimaDialog.setContentView(inflate);
        Window dialogwindow = mimaDialog.getWindow();
        dialogwindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogwindow.setAttributes(lp);
        mimaDialog.show();
    }


}
