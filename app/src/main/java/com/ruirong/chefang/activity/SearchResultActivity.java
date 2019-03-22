package com.ruirong.chefang.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.SearchResultAdapter;
import com.ruirong.chefang.bean.HomeSearchBean;
import com.ruirong.chefang.common.Constants;
import com.ruirong.chefang.event.MySearchEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.SharedPrefsStrListUtil;
import com.ruirong.chefang.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * desceribe 搜索结果界面
 */
public class SearchResultActivity extends BaseActivity implements CanRefreshLayout.OnLoadMoreListener, CanRefreshLayout.OnRefreshListener {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_left_arrows)
    ImageView ivLeftArrows;
    @BindView(R.id.tv_search)
    TextView tvSearch;
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

    private String title;
    private int page = 1;

    private List<HomeSearchBean.ListBean> searchList;
    private SearchResultAdapter adapter;
    public static final String SEARCHRESULTTITLE = "SEARCHRESULTTITLE";

    @Override
    public int getContentView() {
        return R.layout.activity_search_result;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();/***/

        title = getIntent().getStringExtra(SEARCHRESULTTITLE);
        etSearch.setText(title);


        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(SearchResultActivity.this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");

        canContentView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter = new SearchResultAdapter(this, null);
        canContentView.setAdapter(adapter);

        adapter.setRvItemClickListener(new SearchResultAdapter.RvItemClickListener() {
            @Override
            public void onClick(View view, int position, int itemType) {
                if (itemType == 0) {
                    String shopId = adapter.getItem(position).getId();
                    if (!TextUtils.isEmpty(shopId)) {
                        Intent shopIntent = new Intent(SearchResultActivity.this, NearbyShopDetailsActivity.class);
                        shopIntent.putExtra("shop_id", shopId);
                            startActivity(shopIntent);
                    }

                } else {
                    GarageDetailsActivity.startActivityWithParmeter(SearchResultActivity.this,adapter.getItem(position).getId(),adapter.getItem(position).getSp_name());
                }
            }
        });
    }
    /**
     * 返回
     */
    @OnClick(R.id.iv_back)
    public void goBack(){
        finish();
    }


    @Override
    public void getData() {
        search(etSearch.getText().toString().trim(), new PreferencesHelper(SearchResultActivity.this).getLatitude(), new PreferencesHelper(SearchResultActivity.this).getLongTitude(), page);
    }

    private void search(final String title, String lat, String lng, final int page) {

        HttpHelp.getInstance().create(RemoteApi.class).search(title, lat, lng, ToolUtil.getMacAddr(), page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<HomeSearchBean>>(SearchResultActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<HomeSearchBean> baseBean) {
                        refresh.refreshComplete();
                        refresh.loadMoreComplete();
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            //存储搜索记录到本地
                            List<String> stringList = new ArrayList<>();
                            stringList = SharedPrefsStrListUtil.getStrListValue(SearchResultActivity.this, Constants.SEARCHLISTKEY);
                            stringList.add(title);
                            SharedPrefsStrListUtil.putStrListValue(SearchResultActivity.this, Constants.SEARCHLISTKEY,stringList);

                            EventBusUtil.post(new MySearchEvent());
                            searchList = baseBean.data.getList();
                            if (page == 1) {
                                if (searchList != null && searchList.size() > 0) {
                                    rlEmpty.setVisibility(View.GONE);
                                    adapter.setData((ArrayList<HomeSearchBean.ListBean>) searchList);
                                } else {
                                    rlEmpty.setVisibility(View.VISIBLE);
                                    adapter.setData((ArrayList<HomeSearchBean.ListBean>) searchList);
                                }

                            } else {
                                if (searchList != null && searchList.size() > 0) {
                                    adapter.addMoreData((ArrayList<HomeSearchBean.ListBean>) searchList);
                                } else {
                                    ToastUtil.showToast(SearchResultActivity.this, getResources().getString(R.string.no_more));
                                }
                            }
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

    @OnClick({R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                if (TextUtils.isEmpty(etSearch.getText().toString().trim())) {
                    ToastUtil.showToast(SearchResultActivity.this, "搜索内容不能为空");
                    return;
                } else {
                    search(etSearch.getText().toString().trim(), new PreferencesHelper(SearchResultActivity.this).getLatitude(), new PreferencesHelper(SearchResultActivity.this).getLongTitude(), page);
                }

                break;

        }
    }

    /**
     *
     * @param context
     * @param title   传入过来的搜索内容
     */
    public static void startActivityWithResult(Context context, String title){
        Intent intent = new Intent(context,SearchResultActivity.class);
        intent.putExtra(SEARCHRESULTTITLE,title);
        context.startActivity(intent);

    }
}
