package com.ruirong.chefang.personalcenter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
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
import com.qlzx.mylibrary.widget.RecycleViewDivider;
import com.ruirong.chefang.MyApplication;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.GarageDetailsActivity;
import com.ruirong.chefang.activity.SpecialtyDetailsActivity;
import com.ruirong.chefang.adapter.MyCollectionAdapter;
import com.ruirong.chefang.adapter.MyRoomCollectionAdapter;
import com.ruirong.chefang.bean.MyCollectionBean;
import com.ruirong.chefang.bean.MyRoomCollectionBean;
import com.ruirong.chefang.http.RemoteApi;
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
 * describe:  我的收藏
 */
public class MyCollectionActivity extends BaseActivity implements CanRefreshLayout.OnLoadMoreListener, CanRefreshLayout.OnRefreshListener, OnItemChildClickListener {
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
    @BindView(R.id.tv_shop)
    TextView tvShop;
    @BindView(R.id.tv_carhouse)
    TextView tvCarhouse;
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.rl_edit)
    RelativeLayout rlEdit;
    private MyCollectionAdapter myCollectionAdapter;
    private List<MyCollectionBean.ListBean> baseList = new ArrayList<>();

    private MyRoomCollectionAdapter myRoomCollectionAdapter;
    private List<MyRoomCollectionBean.ListBean> roomList = new ArrayList<>();

    private int showItem = 0;
    private int page = 1;

    private PreferencesHelper helper;

    private String longitude = "";
    private String latitude = "";
    private int type = 0;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_collection);
//    }

    @Override
    public int getContentView() {
        return R.layout.activity_my_collection;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ("编辑".equals(titleBar.getRightTextRes())) {
                myCollectionAdapter.setCanEdit(true);
                myRoomCollectionAdapter.setCanEdit(true);
                titleBar.setRightTextRes("完成");
                myCollectionAdapter.notifyDataSetChanged();
                myRoomCollectionAdapter.notifyDataSetChanged();
                rlEdit.setVisibility(View.VISIBLE);
                refresh.setRefreshEnabled(false);
                refresh.setLoadMoreEnabled(false);
            } else {
                myCollectionAdapter.setCanEdit(false);
                myRoomCollectionAdapter.setCanEdit(false);
                titleBar.setRightTextRes("编辑");
                myCollectionAdapter.notifyDataSetChanged();
                myRoomCollectionAdapter.notifyDataSetChanged();
                ivPic.setSelected(false);
                rlEdit.setVisibility(View.GONE);
                refresh.setRefreshEnabled(true);
                refresh.setLoadMoreEnabled(true);
            }
        }
    };

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("我的收藏");

        helper = new PreferencesHelper(this);
        titleBar.setRightTextRes("编辑");
        titleBar.setRightTextClick(onClickListener);


        myCollectionAdapter = new MyCollectionAdapter(canContentView);
        myRoomCollectionAdapter = new MyRoomCollectionAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(MyCollectionActivity.this));
        canContentView.addItemDecoration(new RecycleViewDivider(MyCollectionActivity.this));

        canContentView.setAdapter(myCollectionAdapter);
        myCollectionAdapter.setOnRVItemClickListener(new OnRVItemClickListener() {//商品
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
//                GarageDetailsActivity.startActivityWithParmeter(MyCollectionActivity.this, myCollectionAdapter.getData().get(position).getPid(), myCollectionAdapter.getData().get(position).getName());
                Intent intent = new Intent(getApplication(), SpecialtyDetailsActivity.class);
                intent.putExtra("Specialty_id", myCollectionAdapter.getData().get(position).getPid());
                startActivity(intent);

            }
        });
        myCollectionAdapter.setOnItemChildClickListener(this);
        myRoomCollectionAdapter.setOnRVItemClickListener(new OnRVItemClickListener() {//房产
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                GarageDetailsActivity.startActivityWithParmeter(MyCollectionActivity.this, myRoomCollectionAdapter.getData().get(position).getShop_check_id(), myRoomCollectionAdapter.getData().get(position).getSp_name());
            }
        });
        myRoomCollectionAdapter.setOnItemChildClickListener(this);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(MyCollectionActivity.this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");
        longitude = new PreferencesHelper(this).getLongTitude();
        latitude = new PreferencesHelper(this).getLatitude();

    }

    @Override
    public void getData() {
        showLoadingDialog(getString(R.string.loding));
        if (showItem == 0) {//商城
            getMyCollection(helper.getToken(), page);
        } else if (showItem == 1) {//车房
            getMyRoomCollection(helper.getToken(), longitude, latitude, page);
        }
    }


    /**
     * 获取商品的收藏
     *
     * @param myToken
     * @param page
     */
    public void getMyCollection(String myToken, final int page) {
        Log.i("XXX", myToken + " " + page);
        HttpHelp.getInstance().create(RemoteApi.class).getMyCollection(myToken, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<MyCollectionBean>>(MyCollectionActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<MyCollectionBean> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            baseList = baseBean.data.getList();
                            Log.i("XXX", baseList.size() + "");
                            if (page == 1) {
                                if (baseList != null && baseList.size() > 0) {
                                    rlEmpty.setVisibility(View.GONE);
                                    refresh.setVisibility(View.VISIBLE);
                                    myCollectionAdapter.setData(baseList);
                                    titleBar.setRightTextRes("编辑");
                                    titleBar.setRightTextClick(onClickListener);
                                } else {
                                    myCollectionAdapter.clear();
                                    rlEmpty.setVisibility(View.VISIBLE);
                                    refresh.setVisibility(View.GONE);
                                    titleBar.setRightTextClick(null);
                                }
                            } else {
                                if (baseList != null && baseList.size() > 0) {
                                    titleBar.setRightTextRes("编辑");
                                    titleBar.setRightTextClick(onClickListener);
                                    myCollectionAdapter.addMoreData(baseList);
                                } else {
                                    ToastUtil.showToast(MyCollectionActivity.this, "暂无更多");
                                }
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(MyCollectionActivity.this);
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
     * 获取房车收藏
     *
     * @param myToken
     * @param longitude
     * @param latitude
     * @param page
     */
    public void getMyRoomCollection(String myToken, String longitude, String latitude, final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).getMyRoomCollection(myToken, longitude, latitude, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<MyRoomCollectionBean>>(MyCollectionActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<MyRoomCollectionBean> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            roomList = baseBean.data.getList();
                             if (page == 1) {
                                if (roomList != null && roomList.size() > 0) {
                                    rlEmpty.setVisibility(View.GONE);
                                    refresh.setVisibility(View.VISIBLE);
                                    titleBar.setRightTextRes("编辑");
                                    myRoomCollectionAdapter.setData(roomList);
                                    titleBar.setRightTextClick(onClickListener);
                                } else {
                                    myRoomCollectionAdapter.clear();
                                    rlEmpty.setVisibility(View.VISIBLE);
                                    refresh.setVisibility(View.GONE);
                                    titleBar.setRightTextClick(null);
                                }
                            } else {
                                if (roomList != null && roomList.size() > 0) {
                                    titleBar.setRightTextRes("编辑");
                                    titleBar.setRightTextClick(onClickListener);
                                    myRoomCollectionAdapter.addMoreData(roomList);
                                } else {
                                    ToastUtil.showToast(MyCollectionActivity.this, "暂无更多");
                                }
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(MyCollectionActivity.this);
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
     * 删除特产
     *
     * @param myToken
     * @param id
     */
    public void deleteMyCollection(String myToken, String id) {
        HttpHelp.getInstance().create(RemoteApi.class).deleteCollection(myToken, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(MyCollectionActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            page = 1;
                            getData();
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(MyCollectionActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
        if ("完成".equals(titleBar.getRightTextRes())) {
            titleBar.setRightTextRes("编辑");
            rlEdit.setVisibility(View.GONE);
        }
    }

    /**
     * 删除我的车房
     *
     * @param myToken
     * @param id
     */
    public void deleteMyRoomCollection(String myToken, String id) {
        HttpHelp.getInstance().create(RemoteApi.class).deleteRoomCollection(myToken, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(MyCollectionActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            page = 1;
                            getData();
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(MyCollectionActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
        if ("完成".equals(titleBar.getRightTextRes())) {
            titleBar.setRightTextRes("编辑");
            rlEdit.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_shop, R.id.tv_carhouse, R.id.iv_pic, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_shop:
                if ("完成".equals(titleBar.getRightTextRes())) {
                    myCollectionAdapter.setCanEdit(false);
                    myRoomCollectionAdapter.setCanEdit(false);
                    titleBar.setRightTextRes("编辑");
                    myCollectionAdapter.notifyDataSetChanged();
                    myRoomCollectionAdapter.notifyDataSetChanged();
                    ivPic.setSelected(false);
                    rlEdit.setVisibility(View.GONE);
                    refresh.setRefreshEnabled(true);
                    refresh.setLoadMoreEnabled(true);
                }
                tvShop.setBackgroundResource(R.drawable.bg_collectiona);
                tvShop.setTextColor(Color.parseColor("#ffffff"));
                tvCarhouse.setTextColor(Color.parseColor("#333333"));
                tvCarhouse.setBackgroundResource(R.drawable.bg_collectionb);
                showItem = 0;
                page = 1;
                getData();
                setAllSelected(false, showItem);
                ivPic.setSelected(false);
                canContentView.setAdapter(myCollectionAdapter);
                canContentView.setLayoutManager(new LinearLayoutManager(MyCollectionActivity.this));
                canContentView.addItemDecoration(new RecycleViewDivider(MyCollectionActivity.this));
                titleBar.setRightTextRes("编辑");
                if (myCollectionAdapter.getItemCount() > 0) {
//                    rlEmpty.setVisibility(View.GONE);
                    titleBar.setRightTextClick(onClickListener);
                } else {
//                    rlEmpty.setVisibility(View.VISIBLE);
                    titleBar.setRightTextClick(null);
                }
                break;
            case R.id.tv_carhouse:
                if ("完成".equals(titleBar.getRightTextRes())) {
                    myCollectionAdapter.setCanEdit(false);
                    myRoomCollectionAdapter.setCanEdit(false);
                    titleBar.setRightTextRes("编辑");
                    myCollectionAdapter.notifyDataSetChanged();
                    myRoomCollectionAdapter.notifyDataSetChanged();
                    ivPic.setSelected(false);
                    rlEdit.setVisibility(View.GONE);
                    refresh.setRefreshEnabled(true);
                    refresh.setLoadMoreEnabled(true);
                }
                tvCarhouse.setBackgroundResource(R.drawable.bg_collectiona);
                tvCarhouse.setTextColor(Color.parseColor("#ffffff"));
                tvShop.setTextColor(Color.parseColor("#333333"));
                tvShop.setBackgroundResource(R.drawable.bg_collectionb);
                showItem = 1;
                page = 1;
                getData();
                setAllSelected(false, showItem);
                ivPic.setSelected(false);
                canContentView.setAdapter(myRoomCollectionAdapter);
                canContentView.setLayoutManager(new LinearLayoutManager(MyCollectionActivity.this));
                canContentView.addItemDecoration(new RecycleViewDivider(MyCollectionActivity.this));
                if (myRoomCollectionAdapter.getItemCount() > 0) {
//                    rlEmpty.setVisibility(View.GONE);
                    titleBar.setRightTextClick(onClickListener);
                } else {
//                    rlEmpty.setVisibility(View.VISIBLE);
                    titleBar.setRightTextClick(null);
                }
                break;
            case R.id.iv_pic:
                view.setSelected(!view.isSelected());
                setAllSelected(view.isSelected(), showItem);
                break;

            case R.id.tv_delete:
                deleteCollection(showItem);
                break;
        }
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
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        switch (childView.getId()) {
            case R.id.iv_check:
                if (showItem == 0) {
                    if (childView.isSelected()) {
                        myCollectionAdapter.getItem(position).setChecked(false);
                        myCollectionAdapter.notifyItemChanged(position);
                        ivPic.setSelected(false);
                    } else {
                        myCollectionAdapter.getItem(position).setChecked(true);
                        myCollectionAdapter.notifyItemChanged(position);
                        for (MyCollectionBean.ListBean bean : myCollectionAdapter.getData()) {
                            if (!bean.isChecked()) {
                                return;
                            }
                        }
                        ivPic.setSelected(true);
                    }
                } else {
                    if (childView.isSelected()) {
                        myRoomCollectionAdapter.getItem(position).setChecked(false);
                        myRoomCollectionAdapter.notifyItemChanged(position);
                        ivPic.setSelected(false);
                    } else {
                        myRoomCollectionAdapter.getItem(position).setChecked(true);
                        myRoomCollectionAdapter.notifyItemChanged(position);
                        for (MyRoomCollectionBean.ListBean bean : myRoomCollectionAdapter.getData()) {
                            if (!bean.isChecked()) {
                                return;
                            }
                        }
                        ivPic.setSelected(true);
                    }
                }

                break;
        }
    }

    /**
     * 设置全选和全不选
     *
     * @param selected
     * @param item
     */
    public void setAllSelected(boolean selected, int item) {
        if (item == 0) {
            for (int i = 0; i < myCollectionAdapter.getItemCount(); i++) {
                myCollectionAdapter.getItem(i).setChecked(selected);
            }
            myCollectionAdapter.notifyDataSetChanged();
        } else {
            for (int i = 0; i < myRoomCollectionAdapter.getItemCount(); i++) {
                myRoomCollectionAdapter.getItem(i).setChecked(selected);
            }
            myRoomCollectionAdapter.notifyDataSetChanged();
        }
    }

    public void deleteCollection(int item) {
        if (item == 0) {
            for (MyCollectionBean.ListBean bean : myCollectionAdapter.getData()) {
                if (bean.isChecked()) {
                    showLoadingDialog("删除中...");
                    deleteMyCollection(helper.getToken(), bean.getId());
                }
            }
        } else {

            for (MyRoomCollectionBean.ListBean bean : myRoomCollectionAdapter.getData()) {
                if (bean.isChecked()) {
                    showLoadingDialog("删除中...");
                    deleteMyRoomCollection(helper.getToken(), bean.getId());
                }
            }
        }
        MyApplication.getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                titleBar.getTvRight().performClick();
                page = 1;
//                getData();

                if (0 == showItem) {
                    getMyCollection(helper.getToken(), page);
                } else if (1 == showItem) {
                    getMyRoomCollection(helper.getToken(), longitude, latitude, page);
                }

                hideLoadingDialog();
            }
        }, 1000);

    }
}
