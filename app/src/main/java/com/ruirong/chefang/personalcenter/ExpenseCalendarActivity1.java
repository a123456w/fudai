package com.ruirong.chefang.personalcenter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.telecom.PhoneAccountHandle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.ShopCartConfirmActivity;
import com.ruirong.chefang.adapter.ExpenseRecordListviewAdapter;
import com.ruirong.chefang.bean.GRecordBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2017/12/28 0028
 * describe:  消费记录
 * <p>
 * <p>
 */
public class ExpenseCalendarActivity1 extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, ExpenseRecordListviewAdapter.OnItemChildClickListener, ExpenseRecordListviewAdapter.OnItemChildLongClickListener {
    @BindView(R.id.can_content_view)
    ExpandableListView canContentView;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    private ExpenseRecordListviewAdapter shoppingCartAdapter;
    private int page = 1;

    private String time;
    List<GRecordBean.MonthBean> monthBeans = new ArrayList<>();
    List<GRecordBean> beanList = new ArrayList<>();
    List<Integer> headYears = new ArrayList<>();
    private final static int REQUESTCODE = 1;
    private AlertDialog dialog;

    @Override
    public int getContentView() {
        return R.layout.activity_expense_calendar;
    }

    @Override
    public void initView() {

        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("消费记录");
        showLoadingDialog("加载中...");
        shoppingCartAdapter = new ExpenseRecordListviewAdapter(ExpenseCalendarActivity1.this);
        canContentView.setAdapter(shoppingCartAdapter);
        shoppingCartAdapter.setOnItemChildClickListener(this);
        shoppingCartAdapter.setOnItemChildLongClickListener(this);
        canContentView.setGroupIndicator(null); // 去掉默认带的箭头
        canContentView.setSelection(0);// 设置默认选中项

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
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

        canContentView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //返回true则父item不可点击，否则点击父item字布局会折叠
                Intent intent = new Intent(ExpenseCalendarActivity1.this, ConsumeRecordChooseTimeActivity.class);
                startActivityForResult(intent, REQUESTCODE);
                return true;
            }
        });
        time = getIntent().getStringExtra("expense_time");
    }

    @Override
    public void getData() {
        HttpHelp.getRetrofit(this).create(RemoteApi.class).getGRecordHistory(time, page, new PreferencesHelper(this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<GRecordBean>>>(this, null) {
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                    }

                    @Override
                    public void onNext(BaseBean<List<GRecordBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (listBaseBean.code == 0) {
                            beanList = listBaseBean.data;
                            if (beanList != null && beanList.size() > 0) {
                                // 遍历所有group,将所有项设置成默认展开
                                for (int i = 0; i < beanList.size(); i++) {
                                    monthBeans = beanList.get(i).getMonth();
                                    for (int j = 0; j < monthBeans.size(); j++) {
                                        headYears.add(beanList.get(i).getYear());
                                    }
                                }
                            }
                            if (page == 1) {
                                if (beanList != null && beanList.size() > 0) {
                                    refresh.setVisibility(View.VISIBLE);
                                    rlEmpty.setVisibility(View.GONE);
                                    shoppingCartAdapter.setData(monthBeans, headYears);
                                    int groupCount = shoppingCartAdapter.getGroupCount();
                                    for (int i = 0; i < groupCount; i++) {
                                        canContentView.expandGroup(i);
                                    }
                                } else {
                                    refresh.setVisibility(View.GONE);
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (beanList != null && beanList.size() > 0) {
                                    shoppingCartAdapter.addData(monthBeans, headYears);
                                    int groupCount = shoppingCartAdapter.getGroupCount();
                                    for (int i = 0; i < groupCount; i++) {
                                        canContentView.expandGroup(i);
                                    }
                                } else {
                                    ToastUtil.showToast(ExpenseCalendarActivity1.this, getString(R.string.no_more));
                                }
                            }
                        } else if (listBaseBean.code == 4) {
                            ToolUtil.loseToLogin(ExpenseCalendarActivity1.this);
                        }
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
    public void onItemChildClick(View view, int groupPosition, int childPosition) {
        int id = Integer.valueOf(monthBeans.get(groupPosition).getInfo().get(childPosition).getId());
        Intent intent = new Intent(ExpenseCalendarActivity1.this, PurchaseHistoryDetailsActivity.class);
        intent.putExtra("purchase_id", id);
        startActivity(intent);
    }

    /**
     * 在一个主界面(主Activity)通过意图跳转至多个不同子Activity上去，
     * 当子模块的代码执行完毕后再次返回主页面，
     * 将子activity中得到的数据显示在主界面/完成的数据交给主Activity处理。
     * 这种带数据的意图跳转需要使用activity的onActivityResult()方法。
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE) {
            if (data != null) {
                time = data.getStringExtra("expense_time");
                page = 1;
                getData();

            }

        }

    }

    @Override
    public void onItemLongChildClick(View view, final int groupPosition, final int childPosition) {
        Log.i("XXX", shoppingCartAdapter.getData().get(groupPosition).getInfo().get(childPosition).getId());
        final AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseCalendarActivity1.this);
        builder.setTitle("温馨提示");
        builder.setMessage("您确定要删除此条记录");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                del_record(new PreferencesHelper(ExpenseCalendarActivity1.this).getToken(),
                        shoppingCartAdapter.getData().get(groupPosition).getInfo().get(childPosition).getId());
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void del_record(String token, String id) {

        HttpHelp.getRetrofit(this).create(RemoteApi.class).del_record(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(this, null) {
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }

                    @Override
                    public void onNext(BaseBean<Object> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            dialog.dismiss();
                            getData();
                            refresh.autoRefresh();
                        } else if (listBaseBean.code == 4) {
                            ToolUtil.loseToLogin(ExpenseCalendarActivity1.this);
                        }
                        ToastUtil.showToast(ExpenseCalendarActivity1.this, listBaseBean.message);
                    }
                });
    }
}
