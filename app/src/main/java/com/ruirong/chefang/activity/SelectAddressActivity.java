package com.ruirong.chefang.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.RecycleViewDivider;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.SelectAddressAdapter;
import com.ruirong.chefang.bean.AddressItemBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by guo on 2017/5/5.
 * 选择收货地址
 * <p>
 * AddressActivity
 */

public class SelectAddressActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, OnRVItemClickListener, OnItemChildClickListener {
    @BindView(R.id.swipe_target)
    RecyclerView rvAddress;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;

    private int page = 1;
    private String addressId;
    private PreferencesHelper preferencesHelper;
    private SelectAddressAdapter selectAddressAdapter;
    private final int REQUEST_CODE_ADD_ADDRESS = 0;
    private final int REQUEST_CODE_MODIFY = 1;

    @Override
    public int getContentView() {
        return R.layout.activity_select_address;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleBar.setTitleText("选择地址");

        addressId = getIntent().getStringExtra("addressId");
        preferencesHelper = new PreferencesHelper(this);

        selectAddressAdapter = new SelectAddressAdapter(rvAddress);
        selectAddressAdapter.selectedAddressId = addressId;
        rvAddress.setLayoutManager(new LinearLayoutManager(this));
        rvAddress.addItemDecoration(new RecycleViewDivider(this));
        rvAddress.setAdapter(selectAddressAdapter);
        selectAddressAdapter.setOnRVItemClickListener(this);
        selectAddressAdapter.setOnItemChildClickListener(this);

        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
    }

    @Override
    public void getData() {
        BaseSubscriber<BaseBean<List<AddressItemBean>>> addressListSubscriber = new BaseSubscriber<BaseBean<List<AddressItemBean>>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<List<AddressItemBean>> listBaseBean) {
                super.onNext(listBaseBean);

                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setLoadingMore(false);
                List<AddressItemBean> data = listBaseBean.data;
                if (listBaseBean.code==4){
                    ToolUtil.loseToLogin(SelectAddressActivity.this);
                }
                if (page == 1) {
                    selectAddressAdapter.setData(data);
                } else {
                    if (data.size() == 0) {
                        ToastUtil.showToast(SelectAddressActivity.this, getString(R.string.no_more));
                    } else {
                        selectAddressAdapter.addMoreData(data);
                    }

                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setLoadingMore(false);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).addressList(page, preferencesHelper.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addressListSubscriber);
    }

    /**
     * 新增地址
     */
    @OnClick(R.id.tv_add)
    public void addAddress() {
        AddreceiveAddressActivity.startActivityForResult(this, AddreceiveAddressActivity.class, REQUEST_CODE_ADD_ADDRESS);
    }

    public static void startActivityForResult(Activity context, String addressId, int requestCode) {
        Intent intent = new Intent(context, SelectAddressActivity.class);
        intent.putExtra("addressId", addressId);
        context.startActivityForResult(intent, requestCode);
    }

    @OnClick(R.id.iv_titlebar_left)
    public void back() {
        if (!TextUtils.isEmpty(addressId)) {

            AddressItemBean item = selectAddressAdapter.getItem(selectAddressAdapter.selectedPosition);
            Intent intent = new Intent();
            intent.putExtra("consignee", item);
            setResult(RESULT_OK, intent);
        }

        finish();
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData();
    }

    @Override
    public void onLoadMore() {
        page++;
        getData();
    }

    @Override
    protected void reload() {
        super.reload();
        page = 1;
        getData();
    }

    /**
     * 选中某个地址
     *
     * @param parent
     * @param itemView
     * @param position
     */
    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        AddressItemBean item = selectAddressAdapter.getItem(position);
        Intent intent = new Intent();
        intent.putExtra("consignee", item);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 修改某个地址
     *
     * @param parent
     * @param childView
     * @param position
     */
    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        UpdateReceiveAddressActivity.startActivityForResult(SelectAddressActivity.this, selectAddressAdapter.getItem(position), true, REQUEST_CODE_MODIFY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_ADD_ADDRESS || requestCode == REQUEST_CODE_MODIFY) && resultCode == RESULT_OK) {
            page = 1;
            getData();
        }
    }
}
