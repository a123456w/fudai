package com.ruirong.chefang.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.EnteringStatusBean;
import com.ruirong.chefang.event.EnteringEvent;
import com.ruirong.chefang.fragment.EnteringFragment1;
import com.ruirong.chefang.fragment.EnteringFragment2;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by guo on 2017/4/1.
 * 入驻
 */

public class EnteringActivity extends BaseActivity {
    @BindView(R.id.ll_examine)
    View llExamine;
    @BindView(R.id.content)
    View content;
    @BindView(R.id.ll_examine_fail)
    View llExamineFail;
    @BindView(R.id.tv_examine)
    TextView tvExamine;
    private PreferencesHelper preferencesHelper;
    private BaseSubscriber<BaseBean<EnteringStatusBean>> getStatusSubscriber;


    public static final String ENTERINGTYPE = "ENTERINGTYPE";
    private int type = 0;

    @Override
    public int getContentView() {
        return R.layout.activity_entering;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleBar.setTitleText("商家入驻");
        type = getIntent().getIntExtra(ENTERINGTYPE, 1);
        loadingLayout.setStatus(LoadingLayout.Loading);
        preferencesHelper = new PreferencesHelper(this);
        EventBusUtil.register(this);
    }

    public int getType() {
        return type;
    }

    @Override
    public void getData() {
        getStatusSubscriber = new BaseSubscriber<BaseBean<EnteringStatusBean>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<EnteringStatusBean> enteringStatusBeanBaseBean) {
                super.onNext(enteringStatusBeanBaseBean);
                if (enteringStatusBeanBaseBean.code == 0) {
                    switch (enteringStatusBeanBaseBean.data.status) {
                        //未入驻
                        case 0:
                            content.setVisibility(View.VISIBLE);
                            llExamine.setVisibility(View.GONE);
                            llExamineFail.setVisibility(View.GONE);
                            getSupportFragmentManager().beginTransaction().replace(R.id.content, new EnteringFragment1()).commit();
                            break;
                        //待审核
                        case 1:
                            content.setVisibility(View.GONE);
                            llExamine.setVisibility(View.VISIBLE);
                            llExamineFail.setVisibility(View.GONE);
                            tvExamine.setText("提交成功，等待审核");
                            break;
                        //审核拒绝
                        case 2:
                            content.setVisibility(View.GONE);
                            llExamine.setVisibility(View.GONE);
                            llExamineFail.setVisibility(View.VISIBLE);
                            break;
                        //审核通过调用商家详情接口
                        case 3:
                            content.setVisibility(View.VISIBLE);
                            llExamine.setVisibility(View.GONE);
                            llExamineFail.setVisibility(View.GONE);
                            getSupportFragmentManager().beginTransaction().replace(R.id.content, new EnteringFragment2()).commit();
                            break;
                            //其他情况都是审核拒绝
                        default:
                            content.setVisibility(View.GONE);
                            llExamine.setVisibility(View.VISIBLE);
                            llExamineFail.setVisibility(View.GONE);
                            tvExamine.setText("提交成功，等待审核");
                            break;

                    }
                } else if (enteringStatusBeanBaseBean.code==4){
                    ToolUtil.loseToLogin(EnteringActivity.this);
                }else {
                    ToastUtil.showToast(EnteringActivity.this, enteringStatusBeanBaseBean.message);
                }
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).getEnteringStatus(preferencesHelper.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getStatusSubscriber);
    }

    /**
     * 重新申请
     */
    @OnClick(R.id.tv_retry)
    public void retry() {
        llExamineFail.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new EnteringFragment1()).commit();
    }

    /**
     * 申请成功后重新走接口
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void applySuccess(EnteringEvent event) {
        getData();
    }

    @Override
    public void onReload(View v) {
        super.onReload(v);
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
        if (getStatusSubscriber != null && getStatusSubscriber.isUnsubscribed()) {
            getStatusSubscriber.unsubscribe();
        }
    }

    /**
     * 显示非法商家页
     *
     * @param message
     */
    public void showIllegalShop(String message) {
        //商家被下架等等
        content.setVisibility(View.GONE);
        llExamine.setVisibility(View.VISIBLE);
        llExamineFail.setVisibility(View.GONE);
        tvExamine.setText(message);
    }

    public static void startActivityWithParmeter(Context context, int type) {
        Intent intent = new Intent(context, EnteringActivity.class);
        intent.putExtra(ENTERINGTYPE, type);
        context.startActivity(intent);
    }
}
