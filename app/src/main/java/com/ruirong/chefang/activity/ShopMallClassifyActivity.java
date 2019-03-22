package com.ruirong.chefang.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.StringUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.ShopMallClassifyAdapter;
import com.ruirong.chefang.bean.ShopItemBean;
import com.ruirong.chefang.http.RemoteApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 商城分类通用一级页面
 * Created by dillon on 2017/12/27.
 */

public class ShopMallClassifyActivity extends BaseActivity implements CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener, CanRefreshLayout.OnRefreshListener {
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
    private ShopMallClassifyAdapter adapter;
    private List<ShopItemBean> baseList = new ArrayList<>();
    private String titleName;//标题
    private int page = 1;
    private String classify_id;
    private String shopShiquname = "" ;


    @Override
    public int getContentView() {
        return R.layout.activity_commen_list;
    }

    @Override
    public void initView() {
        titleName = getIntent().getStringExtra("titleName");
        classify_id = getIntent().getStringExtra("classify_id");
        shopShiquname = getIntent().getStringExtra("shopcityname");
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        showLoadingDialog("加载中...");

        if (classify_id == null) {
            finish();
        }

        if (!StringUtil.isEmpty(titleName)) {
            titleBar.setTitleText(titleName);
        }

        initRefresh();
    }

    /**
     * 初始化刷新控件，声明列表点击事件
     */
    public void initRefresh() {
        adapter = new ShopMallClassifyAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(this));
        canContentView.addItemDecoration(new RecycleViewDivider(this));
        canContentView.setAdapter(adapter);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        adapter.setOnRVItemClickListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(this, 150));
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

        getShopList(classify_id, new PreferencesHelper(ShopMallClassifyActivity.this).getLatitude(),
                new PreferencesHelper(ShopMallClassifyActivity.this).getLongTitude(), page,shopShiquname);

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

    /**
     * 店铺列表
     *
     * @param classify_id
     * @param lat
     * @param lng
     * @param page
     */
    public void getShopList(String classify_id, String lat, String lng, final int page,String shiqu) {
        HttpHelp.getInstance().create(RemoteApi.class).getShopList(classify_id, lat, lng, page,shiqu)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<ShopItemBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<ShopItemBean>> baseBean) {
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
                                    adapter.setData(baseList);
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (baseList != null && baseList.size() > 0) {
                                    adapter.addMoreData(baseList);
                                } else {
                                    ToastUtil.showToast(ShopMallClassifyActivity.this, getResources().getString(R.string.no_more));
                                }
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        refresh.loadMoreComplete();
                        hideLoadingDialog();
                        refresh.refreshComplete();
                    }
                });
    }


    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
//        startActivity(new Intent(this, HotelDetailActivity.class));


        if (titleName.equals("酒店住宿")) {

            HotelDetailActivity.startActivityWithParmeter(ShopMallClassifyActivity.this, adapter.getData().get(position).getId());

        }/*else if (titleName.equals("美食")){

            Intent intent1 = new Intent(ShopMallClassifyActivity.this, CookingDetailActivity.class);
            intent1.putExtra("shop_id", adapter.getData().get(position).getId());
            startActivity(intent1);
        }
        */
        else {
            Intent intent1 = new Intent(ShopMallClassifyActivity.this, NearbyShopDetailsActivity.class);
            intent1.putExtra("shop_id", adapter.getData().get(position).getId());
            startActivity(intent1);
        }

//        if (!StringUtil.isEmpty(titleName)) {
//            if (titleName.equals("美食")) {
//                Intent intent1 = new Intent(getApplicationContext(), NearbyShopDetailsActivity.class);
//                ;//这个的详情和附近店铺的一样
//                intent1.putExtra("int_data", 100);
//                startActivity(intent1);
//            } else if (titleName.equals("娱乐")) {
//                Intent intent1 = new Intent(getApplicationContext(), NearbyShopDetailsActivity.class);
//                ;//这个的详情和附近店铺的一样
//                intent1.putExtra("int_data", 100);
//                startActivity(intent1);
//            } else if (titleName.equals("旅游")) {
//                Intent intent1 = new Intent(getApplicationContext(), BuildingHomeDetailsActivity.class);//这个的详情和建材家居的一样
//                intent1.putExtra("titleName", "旅游详情");
//                startActivity(intent1);
//            } else if (titleName.equals("健身")) {
//                Intent intent1 = new Intent(getApplicationContext(), BuildingHomeDetailsActivity.class);//这个的详情和建材家居的一样
//                intent1.putExtra("int_data", 100);
//                startActivity(intent1);
//            } else if (titleName.equals("数码产品")) {
//                Intent intent1 = new Intent(getApplicationContext(), BuildingHomeDetailsActivity.class);//这个的详情和建材家居的一样
//                intent1.putExtra("int_data", 100);
//                startActivity(intent1);
//            } else if (titleName.equals("酒店住宿")) {
//                Intent intent1 = new Intent(getApplicationContext(), HotelDetailActivity.class);
//                intent1.putExtra("int_data", 100);
//                startActivity(intent1);
//            } else if (titleName.equals("教育培训")) {
//                Intent intent1 = new Intent(getApplicationContext(), BuildingHomeDetailsActivity.class);
//                intent1.putExtra("int_data", 100);
//                startActivity(intent1);
//            } else if (titleName.equals("建材家居")) {
//                Intent intent1 = new Intent(getApplicationContext(), BuildingHomeDetailsActivity.class);
//                intent1.putExtra("int_data", 100);
//                startActivity(intent1);
//            } else if (titleName.equals("生活服务")) {
//                Intent intent1 = new Intent(getApplicationContext(), LifeServiceActivity.class);
//                intent1.putExtra("int_data", 100);
//                startActivity(intent1);
//
//            } else {
//
//            }
//        }


    }


}
