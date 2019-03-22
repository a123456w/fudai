package com.ruirong.chefang.personalcenter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.GarageDetailsActivity;
import com.ruirong.chefang.bean.LookRoomHistoryBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by chenlipeng on 2017/12/28 0028
 * describe:  看房记录
 */
public class LookRoomHistoryActivity
        extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener

{


    @BindView(R.id.rb_bottom1)
    RadioButton rbBottom1;
    @BindView(R.id.rb_bottom2)
    RadioButton rbBottom2;
    @BindView(R.id.rg_bottom)
    RadioGroup rgBottom;
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
    @BindView(R.id.tv_all_check)
    CheckBox tvAllCheck;
    @BindView(R.id.tv_all_delete)
    TextView tvAllDelete;
    @BindView(R.id.rl_all_product)
    RelativeLayout rlAllProduct;
    private int page = 1;
    private int type = 1;
    private LookRoomHistoryAlreadyLookAdapter roomTimeListAdapter;
    private LookRoomHistoryAlreadyLookAdapter alreadyExchangeListAdapter;

    private List<LookRoomHistoryBean.ListBean> baseList = new ArrayList<>();
    private Subscriber<BaseBean<LookRoomHistoryBean>> subscriber;
    private Subscriber<BaseBean<LookRoomHistoryBean>> mSubscriber;

    //所有要删除的id集合
    public Map<Integer, Boolean> checkBoxStates = new HashMap<>();
    public Boolean isLook = true;

    @Override
    public int getContentView() {
        return R.layout.activity_look_room_history;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("预约记录");
        titleBar.setRightTextRes("编辑");
        titleBar.setRightTextClick(onClickListener);


        roomTimeListAdapter = new LookRoomHistoryAlreadyLookAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(LookRoomHistoryActivity.this));
        canContentView.addItemDecoration(new RecycleViewDivider(LookRoomHistoryActivity.this));
        canContentView.setAdapter(roomTimeListAdapter);
        roomTimeListAdapter.setOnRVItemClickListener(this);

        alreadyExchangeListAdapter = new LookRoomHistoryAlreadyLookAdapter(canContentView);
        alreadyExchangeListAdapter.setOnRVItemClickListener(this);


        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(LookRoomHistoryActivity.this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");
        //设置是否有已看三角图片
        roomTimeListAdapter.setType(1);
        alreadyExchangeListAdapter.setType(2);

        tvAllCheck.setOnClickListener(onCheckedChangeListener);
        rgBottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_bottom1:
                        canContentView.setAdapter(roomTimeListAdapter);
                        type = 1;
                        titleBar.setRightTextRes("编辑");
                        rlAllProduct.setVisibility(View.GONE);
                        rlEmpty.setVisibility(GONE);
                        roomTimeListAdapter.displayChecked(false);
                        roomTimeListAdapter.notifyDataSetChanged();
                        getData();
                        break;
                    case R.id.rb_bottom2:
                        canContentView.setAdapter(alreadyExchangeListAdapter);
                        type = 2;
                        titleBar.setRightTextRes("编辑");
                        rlAllProduct.setVisibility(View.GONE);
                        rlEmpty.setVisibility(GONE);
                        alreadyExchangeListAdapter.displayChecked(false);
                        alreadyExchangeListAdapter.notifyDataSetChanged();
                        getData();
                        break;
                }

            }
        });

    }

    View.OnClickListener onCheckedChangeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Boolean isChecked = tvAllCheck.isChecked();
            if (isChecked) {
                for (int i = 0; i < baseList.size(); i++) {
                    checkBoxStates.put(i, true);
                }
            } else {
                for (int i = 0; i < baseList.size(); i++) {
                    checkBoxStates.put(i, false);
                }
            }
            roomTimeListAdapter.notifyDataSetChanged();
            alreadyExchangeListAdapter.notifyDataSetChanged();
        }
    };
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (true) {
                if (titleBar.getRightTextRes().toString().equals("编辑")) {
                    titleBar.setRightTextRes("完成");
                    rlAllProduct.setVisibility(View.VISIBLE);
                    roomTimeListAdapter.displayChecked(true);
                    alreadyExchangeListAdapter.displayChecked(true);
                    roomTimeListAdapter.notifyDataSetChanged();
                    alreadyExchangeListAdapter.notifyDataSetChanged();
                    tvAllDelete.setOnClickListener(onClickListenerDelete);
                } else if (titleBar.getRightTextRes().toString().equals("完成")) {
                    titleBar.setRightTextRes("编辑");
                    rlAllProduct.setVisibility(View.GONE);
                    roomTimeListAdapter.displayChecked(false);
                    alreadyExchangeListAdapter.displayChecked(false);
                    roomTimeListAdapter.notifyDataSetChanged();
                    alreadyExchangeListAdapter.notifyDataSetChanged();
                }
            }
        }
    };
    View.OnClickListener onClickListenerDelete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (checkBoxStates.size() > 0 && checkBoxStates != null) {

                for (int i = 0; i < checkBoxStates.size(); i++) {
                    if (checkBoxStates.get(i)) {
                        deleteHistory(baseList.get(i).getId());
                        checkBoxStates.put(i, false);
                    }
                }
            }
            roomTimeListAdapter.notifyDataSetChanged();
            alreadyExchangeListAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void getData() {
        getListData();
    }

    public void getListData() {
        showLoadingDialog("加载中...");
        if (type == 1) {
            subscriber = new BaseSubscriber<BaseBean<LookRoomHistoryBean>>(this, null) {
                @Override
                public void onError(Throwable throwable) {
                    super.onError(throwable);
                    refresh.loadMoreComplete();
                    refresh.refreshComplete();
                    hideLoadingDialog();
                }

                @Override
                public void onNext(BaseBean<LookRoomHistoryBean> lookRoomHistoryBeanBaseBean) {
                    super.onNext(lookRoomHistoryBeanBaseBean);
                    hideLoadingDialog();
                    refresh.loadMoreComplete();
                    refresh.refreshComplete();
                    if (lookRoomHistoryBeanBaseBean.code == 0) {
                        baseList = lookRoomHistoryBeanBaseBean.data.getList();
                        initCheckedData();
                        if (lookRoomHistoryBeanBaseBean.data.getTotal() == 0) {
                            rlEmpty.setVisibility(View.VISIBLE);
                            refresh.setVisibility(View.GONE);
                            return;
                        }
                        if (page == 1) {
                            refresh.setVisibility(View.VISIBLE);
                            rlEmpty.setVisibility(View.GONE);
                            roomTimeListAdapter.setData(baseList);
                        } else {
                            if (baseList != null && baseList.size() > 0) {
                                roomTimeListAdapter.addMoreData(baseList);
                            } else {
                                ToastUtil.showToast(LookRoomHistoryActivity.this, getString(R.string.no_more));
                            }
                        }
                    } else if (lookRoomHistoryBeanBaseBean.code == 4) {
                        ToolUtil.loseToLogin(LookRoomHistoryActivity.this);
                    }
                }
            };
        } else if (type == 2) {

            subscriber = new BaseSubscriber<BaseBean<LookRoomHistoryBean>>(this, null) {
                @Override
                public void onError(Throwable e) {
                    hideLoadingDialog();
                    refresh.loadMoreComplete();
                    refresh.refreshComplete();
                }

                @Override
                public void onNext(BaseBean<LookRoomHistoryBean> lookRoomHistoryBeanBaseBean) {
                    hideLoadingDialog();
                    refresh.loadMoreComplete();
                    refresh.refreshComplete();
                    if (lookRoomHistoryBeanBaseBean.code == 0) {
                        baseList = lookRoomHistoryBeanBaseBean.data.getList();
                        initCheckedData();
                        if (page == 1) {
                            if (baseList != null && baseList.size() > 0) {
                                refresh.setVisibility(View.VISIBLE);
                                rlEmpty.setVisibility(View.GONE);
                                alreadyExchangeListAdapter.setData(baseList);
                            } else {
                                refresh.setVisibility(View.GONE);
                                rlEmpty.setVisibility(View.VISIBLE);
                            }

                        } else {
                            if (baseList != null && baseList.size() > 0) {
                                alreadyExchangeListAdapter.addMoreData(baseList);
                            } else {
                                ToastUtil.showToast(LookRoomHistoryActivity.this, getString(R.string.no_more));
                            }
                        }
                    } else if (lookRoomHistoryBeanBaseBean.code == 4) {
                        ToolUtil.loseToLogin(LookRoomHistoryActivity.this);
                    }
                }
            };
        }
        HttpHelp.getRetrofit(this).create(RemoteApi.class).getLookRoomHistory(type, page, new PreferencesHelper(LookRoomHistoryActivity.this).getToken())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        if (baseList.size() < 0 || baseList == null) {
            rlEmpty.setVisibility(VISIBLE);
        }
    }

    private void initCheckedData() {
        for (int i = 0; i < baseList.size(); i++) {
            checkBoxStates.put(i, false);
        }
    }

    public void deleteHistory(String id) {
        HttpHelp.getRetrofit(this).create(RemoteApi.class).deleteLookRoomHistory(Integer.valueOf(id), new PreferencesHelper(LookRoomHistoryActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this, null) {
                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(BaseBean<String> deleteData) {
                        if (deleteData.code == 0) {
                            ToastUtil.showToast(LookRoomHistoryActivity.this, deleteData.message);
                        } else if (deleteData.code == 1) {
                            ToastUtil.showToast(LookRoomHistoryActivity.this, deleteData.message);
                        } else if (deleteData.code == 4) {
                            ToolUtil.loseToLogin(LookRoomHistoryActivity.this);
                        } else {
                            ToastUtil.showToast(LookRoomHistoryActivity.this, "操作失败");
                        }
                        getData();
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
        if (type == 1) {
            GarageDetailsActivity.startActivityWithParmeter(LookRoomHistoryActivity.this, roomTimeListAdapter.getData().get(position).getPid(), roomTimeListAdapter.getData().get(position).getSp_name());
        } else if (type == 2) {
            GarageDetailsActivity.startActivityWithParmeter(LookRoomHistoryActivity.this, alreadyExchangeListAdapter.getData().get(position).getPid(), alreadyExchangeListAdapter.getData().get(position).getSp_name());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public class LookRoomHistoryAlreadyLookAdapter extends RecyclerViewAdapter<LookRoomHistoryBean.ListBean> {
        private int type = 1;
        private Boolean isDisplay = false;

        public void setType(int type) {
            this.type = type;
        }

        public void displayChecked(Boolean isDisplay) {
            this.isDisplay = isDisplay;
        }

        public LookRoomHistoryAlreadyLookAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.item_yet_look);
        }

        @Override
        protected void fillData(ViewHolderHelper viewHolderHelper, final int position, LookRoomHistoryBean.ListBean model) {
            if (isDisplay) {
                viewHolderHelper.setVisibility(R.id.tv_is_check, VISIBLE);
                CheckBox checkBox = viewHolderHelper.getView(R.id.tv_is_check);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        checkBoxStates.put(position, isChecked);
                        if (!isChecked && tvAllCheck.isChecked()) {
                            tvAllCheck.setChecked(false);
                        }
                    }
                });

            } else {
                viewHolderHelper.setVisibility(R.id.tv_is_check, GONE);
            }
            if (type == 1) {
                viewHolderHelper.setImageResource(R.id.iv_pic, R.drawable.yet_look);
            } else if (type == 2) {
                viewHolderHelper.setVisibility(R.id.iv_pic, GONE);
            }

            viewHolderHelper.setChecked(R.id.tv_is_check, checkBoxStates.get(position));

            GlideUtil.display(mContext, Constants.IMG_HOST + model.getCover(), viewHolderHelper.getImageView(R.id.item_lookroom_img));
            //Glide.with(mContext).load(Constants.IMG_HOST+ model.getCover()).into((ImageView) viewHolderHelper.getView(R.id.item_lookroom_img));
            Log.d("TAG", Constants.IMG_HOST + model.getCover());
            viewHolderHelper.setText(R.id.item_lookroom_name, model.getSp_name());
            viewHolderHelper.setText(R.id.item_lookroom_time, TimeStamp2Date(model.getLook_time(),""));
            // viewHolderHelper.setText(R.id.item_lookroom_address,model.getSp_address());
            viewHolderHelper.setText(R.id.item_lookroom_address, model.getSp_address());

        }

        public String getDateToString(String mil) {
            String pattern = "yyyy-MM-dd HH:mm";
            long milSecond = Long.valueOf(mil);
            Date date = new Date(milSecond);
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.format(date);
        }

        /*
 * 将时间戳转换为时间
 */
        public String stampToDate(String s) {//2018-04-14 17:57:05
            String res;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long lt = new Long(s);
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
            return res;
        }
    }

    public  String TimeStamp2Date(String timestampString, String formats) {
        if (TextUtils.isEmpty(formats))
            formats = "yyyy-MM-dd HH:mm:ss";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }
}
