package com.ruirong.chefang.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.AddressAdapter;
import com.ruirong.chefang.bean.AddressItemBean;
import com.ruirong.chefang.event.JiesuanEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的地址
 * Created by BX on 2017/12/29.
 */

public class AddressActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener, OnItemChildClickListener {
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

    private AddressAdapter adapter;
    private List<AddressItemBean> lists = new ArrayList<>();
    private int page = 1;

    private int type_choice; //1是店铺的确认订单  2是特产专区的确认订单  3是福袋专区
    private int type;//1是修改地址跳转过来  2是新增地址跳转过来

    @Override
    public int getContentView() {
        return R.layout.activity_myaddress;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("我的地址");

        titleBar.setRightTextRes("新增");
        titleBar.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(AddressActivity.this, AddAddressActivity.class);
                intent1.putExtra("type", 1);
                startActivity(intent1);

            }
        });

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(AddressActivity.this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");

        type_choice = getIntent().getIntExtra("type_choice", -1);
        type = getIntent().getIntExtra("type", -1);

        initData();
    }

    private void initData() {
        adapter = new AddressAdapter(canContentView);
        canContentView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter.setOnItemChildClickListener(this);
        adapter.setOnRVItemClickListener(this);
        canContentView.setAdapter(adapter);
    }

    @Override
    public void getData() {
        addressList(page, new PreferencesHelper(AddressActivity.this).getToken());
    }

    /**
     * 地址列表
     *
     * @param page
     * @param token
     */
    private void addressList(final int page, String token) {
        HttpHelp.getInstance().create(RemoteApi.class).addressList(page, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<AddressItemBean>>>(AddressActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<List<AddressItemBean>> baseBean) {
                        super.onNext(baseBean);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            lists = baseBean.data;
                            if (page == 1) {
                                if (lists != null && lists.size() > 0) {
                                    rlEmpty.setVisibility(View.GONE);
                                    refresh.setVisibility(View.VISIBLE);
                                    adapter.setData(lists);

                                    if (type == 2) {
                                        showLoadingDialog("请稍等...");

                                        Timer timer = new Timer();
                                        timer.schedule(new TimerTask() {

                                            @Override
                                            public void run() {

                                                hideLoadingDialog();

                                                if (type_choice == 1) {
                                                    Intent intent = new Intent(AddressActivity.this, ShopCartConfirmActivity.class);
                                                    String address = new Gson().toJson(adapter.getData().get(lists.size() - 1));
                                                    intent.putExtra("address", address);
                                                    startActivity(intent);
                                                    finish();
                                                } else if (type_choice == 2) {
                                                    Intent intent = new Intent(AddressActivity.this, LuckybagPrefectureConfirmOrderActivity.class);
                                                    String address = new Gson().toJson(adapter.getData().get(lists.size() - 1));
                                                    intent.putExtra("address", address);
                                                    startActivity(intent);
                                                    finish();
                                                } else if (type_choice == 3) {
                                                    Intent intent = new Intent(AddressActivity.this, EachChildConfirmationOfOrderActivity.class);
                                                    String address = new Gson().toJson(adapter.getData().get(lists.size() - 1));
                                                    intent.putExtra("address", address);
                                                    startActivity(intent);
                                                    finish();
                                                }


                                            }
                                        }, 1000);
                                    }

                                } else {
                                    adapter.clear();
                                    refresh.setVisibility(View.GONE);
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }

                            } else {
                                if (lists != null && lists.size() > 0) {
                                    rlEmpty.setVisibility(View.GONE);
                                    adapter.addMoreData(lists);
                                } else {
                                    ToastUtil.showToast(AddressActivity.this, getResources().getString(R.string.no_more));
                                }
                            }
                        }else if (baseBean.code==4){
                            ToolUtil.loseToLogin(AddressActivity.this);
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
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        if (type_choice == 1) {
            Intent intent = new Intent(AddressActivity.this, ShopCartConfirmActivity.class);
            String address = new Gson().toJson(adapter.getData().get(position));
            intent.putExtra("address", address);
            startActivity(intent);
            finish();
        } else if (type_choice == 2) {
            Intent intent = new Intent(AddressActivity.this, LuckybagPrefectureConfirmOrderActivity.class);
            String address = new Gson().toJson(adapter.getData().get(position));
            intent.putExtra("address", address);
            startActivity(intent);
            finish();
        } else if (type_choice == 3) {
            Intent intent = new Intent(AddressActivity.this, EachChildConfirmationOfOrderActivity.class);
            String address = new Gson().toJson(adapter.getData().get(position));
            intent.putExtra("address", address);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        refresh.autoRefresh();
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        switch (childView.getId()) {
            case R.id.tv_edit:
                Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                intent.putExtra("type", 2);
                String address = new Gson().toJson(adapter.getData().get(position));
                intent.putExtra("address", address);
                startActivity(intent);
                break;
            case R.id.tv_address_default:
                setFalse();
                adapter.getData().get(position).setIs_defa("1");
                adapter.notifyDataSetChanged();
                setdefaultAddress(adapter.getData().get(position).getId(), new PreferencesHelper(AddressActivity.this).getToken());
                Log.i("XXX", "--" + adapter.getData().get(position).getId());
                break;
            case R.id.tv_delete:

                delete(adapter.getData().get(position).getId(), position);
                break;
        }
    }

    private void setFalse() {
        for (int i = 0; i < adapter.getData().size(); i++) {

            adapter.getData().get(i).setIs_defa("0");

        }
    }

    /**
     * 设置默认地址
     *
     * @param addressid 地址的ID
     */
    public void setdefaultAddress(String addressid, String token) {
        BaseSubscriber<BaseBean<String>> setdefaultAddressSubscriber = new BaseSubscriber<BaseBean<String>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<String> stringBaseBean) {
                super.onNext(stringBaseBean);
                ToastUtil.showToast(AddressActivity.this, stringBaseBean.message);
                if (stringBaseBean.code == 0) {
//                    setResult(RESULT_OK);
//                    finish();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).setdefaultAddress(addressid, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(setdefaultAddressSubscriber);
    }


    /**
     * 删除址
     */
    public void delete(final String addressid, final int position) {
        if (addressid != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("真的要删除么?");
            builder.setNegativeButton("取消", null);

            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.getData().remove(position);
                    adapter.notifyDataSetChanged();
                    deleteAddress(addressid, new PreferencesHelper(AddressActivity.this).getToken());
                }
            });
            builder.create().show();
        } else {
            ToastUtil.showToast(AddressActivity.this, "地址选择错误！");
        }
    }

    /**
     * 删除一条地址
     *
     * @param addressid 地址的ID
     */
    public void deleteAddress(String addressid, String token) {
        BaseSubscriber<BaseBean<String>> deleteAddressSubscriber = new BaseSubscriber<BaseBean<String>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<String> stringBaseBean) {
                super.onNext(stringBaseBean);
                ToastUtil.showToast(AddressActivity.this, stringBaseBean.message);
                if (stringBaseBean.code == 0) {
                    EventBus.getDefault().post(new JiesuanEvent());
                    addressList(page, new PreferencesHelper(AddressActivity.this).getToken());
                }else if (stringBaseBean.code==4){
                    ToolUtil.loseToLogin(AddressActivity.this);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).deleteAddress(addressid, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deleteAddressSubscriber);
    }

}
