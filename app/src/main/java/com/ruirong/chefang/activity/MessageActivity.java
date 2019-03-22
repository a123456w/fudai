package com.ruirong.chefang.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.MessageAdapter;
import com.qlzx.mylibrary.bean.BaseBean;
import com.ruirong.chefang.bean.MessageItemBean;
import com.ruirong.chefang.event.MessageCountEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by guo on 2017/4/1.
 * 消息页
 */

public class MessageActivity extends BaseActivity implements OnRVItemClickListener, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.swipe_target)
    RecyclerView rvMessage;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.ll_edit)
    LinearLayout llEdit;


    private TextView tvRight;
    private MessageAdapter messageAdapter;
    private int page = 1;
    private BaseSubscriber<BaseBean<List<MessageItemBean>>> messageListSubscriber;
    private PreferencesHelper preferencesHelper;
    private BaseSubscriber<BaseBean<Object>> deleteSubscriber;
    private final int REQUEST_CODE_DETAIL = 100;

    @Override
    public int getContentView() {
        return R.layout.activity_messsage;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Loading);
        loadingLayout.setEmptyText("您还没有任务消息");
        titleBar.setTitleText("消息列表");

        titleBar.setRightTextRes("编辑");
        tvRight = titleBar.getTvRight();

        messageAdapter = new MessageAdapter(rvMessage);
        rvMessage.setLayoutManager(new LinearLayoutManager(this));
        rvMessage.setAdapter(messageAdapter);
        messageAdapter.setOnRVItemClickListener(this);

        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);

        preferencesHelper = new PreferencesHelper(this);
    }

    @Override
    public void getData() {
        messageListSubscriber = new BaseSubscriber<BaseBean<List<MessageItemBean>>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<List<MessageItemBean>> listBaseBean) {
                super.onNext(listBaseBean);
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setLoadingMore(false);
                List<MessageItemBean> data = listBaseBean.data;
                if (listBaseBean.code == 4) {
                    ToolUtil.loseToLogin(MessageActivity.this);
                }
                if (page == 1) {
                    if (data != null && data.size() > 0) {
                        tvRight.setVisibility(View.VISIBLE);
                        messageAdapter.setData(listBaseBean.data);
                    } else {
                        loadingLayout.setStatus(LoadingLayout.Empty);
                        tvRight.setVisibility(View.GONE);
                    }

                } else {

                    if (data != null && data.size() > 0) {
                        messageAdapter.addMoreData(listBaseBean.data);
                    } else {

                        ToastUtil.showToast(MessageActivity.this, getString(R.string.no_more));

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
        HttpHelp.getInstance().create(RemoteApi.class).getMessageList(preferencesHelper.getToken(), page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageListSubscriber);

    }

    /**
     * 完成
     */
    @OnClick(R.id.tv_titlebar_right)
    public void edit() {
        if ("编辑".equals(tvRight.getText().toString())) {
            tvRight.setText("完成");
            //进入编辑状态
            llEdit.setVisibility(View.VISIBLE);
            messageAdapter.selectList.clear();
            messageAdapter.edit = true;
            messageAdapter.notifyDataSetChanged();
            tvAll.setText("全选");
        } else {
            tvRight.setText("编辑");
            llEdit.setVisibility(View.GONE);
            messageAdapter.edit = false;
            messageAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 全选
     */
    @OnClick(R.id.tv_all)
    public void selectAll() {
        if (tvAll.getText().toString().equals("全选")) {
            tvAll.setText("取消全选");
            //全选
            int itemCount = messageAdapter.getItemCount();
            for (int i = 0; i < itemCount; i++) {
                messageAdapter.selectList.add(messageAdapter.getItem(i).getId());
            }
            messageAdapter.notifyDataSetChanged();
        } else {
            tvAll.setText("全选");
            //取消全选
            messageAdapter.selectList.clear();
            messageAdapter.notifyDataSetChanged();
        }


    }

    /**
     * 删除
     */
    @OnClick(R.id.tv_delete)
    public void delete() {

        if (messageAdapter.selectList.size() == 0) {
            ToastUtil.showToast(this, "请先选择");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("真的要删除吗");
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteCommit();
                }
            });
            builder.create().show();
        }

    }

    private void deleteCommit() {
        StringBuffer ids = new StringBuffer();
        for (String id : messageAdapter.selectList) {
            ids.append(id + ",");
        }
        ids.deleteCharAt(ids.length() - 1);
        LogUtil.d("要删除的messageIds---" + ids);
        deleteSubscriber = new BaseSubscriber<BaseBean<Object>>(this, null) {
            @Override
            public void onNext(BaseBean<Object> objectBaseBean) {
                if (objectBaseBean.code == 0) {
                    tvRight.setText("编辑");
                    llEdit.setVisibility(View.GONE);
                    messageAdapter.edit = false;

                    page = 1;
                    getData();

                }

                ToastUtil.showToast(MessageActivity.this, objectBaseBean.message);
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast(MessageActivity.this, getString(R.string.net_error));
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).deleteMessage(preferencesHelper.getToken(), ids.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deleteSubscriber);
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        String id = messageAdapter.getItem(position).getId();
        if (messageAdapter.edit) {
            if (messageAdapter.selectList.contains(id)) {
                messageAdapter.selectList.remove(id);
            } else {
                messageAdapter.selectList.add(id);
            }
            messageAdapter.notifyDataSetChanged();
        } else {
            MessageDetailActivity.startActivityForResult(MessageActivity.this, id, position, REQUEST_CODE_DETAIL);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (messageListSubscriber != null && messageListSubscriber.isUnsubscribed()) {
            messageListSubscriber.unsubscribe();
        }
        if (deleteSubscriber != null && deleteSubscriber.isUnsubscribed()) {
            deleteSubscriber.unsubscribe();
        }
    }

    @Override
    public void onReload(View v) {
        super.onReload(v);
        page = 1;
        getData();
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

    /**
     * 返回
     */
    @OnClick(R.id.iv_titlebar_left)
    public void back() {
        EventBusUtil.post(new MessageCountEvent());
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            EventBusUtil.post(new MessageCountEvent());
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DETAIL && resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", -1);
            boolean isReaded = data.getBooleanExtra("isReaded", false);
            if (isReaded && position != -1) {
                MessageItemBean item = messageAdapter.getItem(position);
                item.setIs_read("1");
                messageAdapter.notifyDataSetChanged();
            }
        }
    }
}
