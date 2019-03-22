package com.ruirong.chefang.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollGridView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.SpecialtyListAdapter;
import com.ruirong.chefang.bean.SpecialListListviewBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.shoppingcart.ShoppingCartActivity;
import com.ruirong.chefang.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2017/12/28 0028
 * describe:  特产列表
 */
public class SpecialtyListActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener {

    @BindView(R.id.tv_cancle)
    ImageView tvCancle;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_left_arrows)
    ImageView ivLeftArrows;
    @BindView(R.id.nsgv_home_bagzone)
    NoScrollGridView nsgvHomeBagzone;
    @BindView(R.id.can_content_view)
    NestedScrollView canContentView;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private SpecialtyListAdapter adapter;
    private List<SpecialListListviewBean.ListBean> baseList = new ArrayList<>();

    private String id;
    private int page = 1;


    @Override
    public int getContentView() {
        return R.layout.activity_specialty_list;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();/***/
        adapter = new SpecialtyListAdapter(nsgvHomeBagzone);
        nsgvHomeBagzone.setAdapter(adapter);
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(SpecialtyListActivity.this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");

        id = getIntent().getStringExtra("id");

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 当按了搜索之后关闭软键盘
                    ((InputMethodManager) etSearch.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            SpecialtyListActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    getListData(etSearch.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(new PreferencesHelper(SpecialtyListActivity.this).getToken())) {
                    ToolUtil.goToLogin(SpecialtyListActivity.this);
                } else {
                    startActivity(new Intent(SpecialtyListActivity.this, ShoppingCartActivity.class));
                }
            }
        });

        showLoadingDialog("加载中...");
    }

    @Override
    public void getData() {
        getListData(etSearch.getText().toString().trim());
    }

    public void getListData(String title) {
        baseList.clear();

        HttpHelp.getInstance().create(RemoteApi.class).commodityList(id, title, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<SpecialListListviewBean>>(this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<SpecialListListviewBean> listBaseBean) {
                        hideLoadingDialog();
                        super.onNext(listBaseBean);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (listBaseBean.code == 0) {
                            baseList = listBaseBean.data.getList();
                            if (page == 1) {
                                if (baseList != null && baseList.size() > 0) {
                                    rlEmpty.setVisibility(View.GONE);
                                    adapter.setData(baseList);
                                } else {
                                    adapter.setData(baseList);
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (baseList != null && baseList.size() > 0) {
                                    adapter.addMoreData(baseList);
                                } else {
                                    ToastUtil.showToast(SpecialtyListActivity.this, getResources().getString(R.string.no_more));
                                }
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                    }
                });


        nsgvHomeBagzone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplication(), SpecialtyDetailsActivity.class);
                intent.putExtra("Specialty_id", adapter.getData().get(i).getId());
                startActivity(intent);
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


    @OnClick({R.id.iv_back, R.id.iv_left_arrows})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:  //获取图片码
                finish();
                break;
            case R.id.iv_left_arrows:
                getData();
                break;
        }
    }


    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {

    }


}
