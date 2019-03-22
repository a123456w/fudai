package com.ruirong.chefang.activity;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.event.LoginOutEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.GlideCacheUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**设置
 * Created by dillon on 2017/3/31.
 */

public class SetActivity extends BaseActivity {

    @BindView(R.id.tv_clearcache)
    TextView tvClearcache;
    @BindView(R.id.tv_updatepwd)
    TextView tvUpdatepwd;
    @BindView(R.id.tv_aboutus)
    TextView tvAboutus;
    @BindView(R.id.btn_logout)
    Button btnLogout;


    private BaseSubscriber<BaseBean<String>> logOutSubscriber;

    public AlertDialog alertDialog;

    @Override
    public int getContentView() {
        return R.layout.activity_set;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitleText("设置");
        tvClearcache.setText(GlideCacheUtil.getInstance().getCacheSize(this));

        PreferencesHelper preferencesHelper = new PreferencesHelper(this);
        if (TextUtils.isEmpty(preferencesHelper.getToken())) {
            tvUpdatepwd.setVisibility(View.GONE);
            btnLogout.setVisibility(View.GONE);
        } else {
            tvUpdatepwd.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getData() {

    }


    @OnClick({R.id.ll_catch, R.id.tv_updatepwd, R.id.tv_aboutus, R.id.btn_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_catch:
                showDialog(SetActivity.this, 1, "确定清除缓存么？", "取消");
                break;
            case R.id.tv_updatepwd:
                ModifyPasswordActivity.startActivityWithParmeter(SetActivity.this,"修改密码");
                //startActivity(SetActivity.this, ModifyPasswordActivity.class);
                break;
            case R.id.tv_aboutus:
                AboutOusActivity.startActivity(SetActivity.this, AboutOusActivity.class);
                break;
            case R.id.btn_logout:
                showDialog(SetActivity.this, 2, "确认退出登录么?", "取消");
                break;
        }
    }

    /**
     * 退出登录，清除pushid
     * @param token
     */
    public void logOut(String token){
        logOutSubscriber = new BaseSubscriber<BaseBean<String>>(this,null){
            @Override
            public void onNext(BaseBean<String> stringBaseBean) {
                super.onNext(stringBaseBean);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getRetrofit(this).create(RemoteApi.class).logOut(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(logOutSubscriber);
    }

    public void showDialog(final Context context, final int icon, String msg, String btncontinue) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_dialogd, null, false);
        ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        TextView tvHint = (TextView) view.findViewById(R.id.tv_hint);
        TextView tvContinue = (TextView) view.findViewById(R.id.tv_continue);

        GlideUtil.display(SetActivity.this, R.mipmap.ic_launcher, ivIcon);

        tvHint.setText(msg);
        tvContinue.setText(btncontinue);

        view.findViewById(R.id.tv_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        view.findViewById(R.id.tv_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (icon) {
                    case 1:
                        GlideCacheUtil.getInstance().clearImageAllCache(SetActivity.this);
                        tvClearcache.setText(GlideCacheUtil.getInstance().getCacheSize(SetActivity.this));
                        alertDialog.dismiss();
                        break;
                    case 2:
                        //百度导航un初始化
                        BaiduNaviManager.getInstance().uninit();

                        new PreferencesHelper(SetActivity.this).clear();
                        EventBusUtil.post(new LoginOutEvent());

                        logOut(new PreferencesHelper(SetActivity.this).getToken());

                        finish();
                        break;
                }
            }
        });
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (logOutSubscriber!=null&&logOutSubscriber.isUnsubscribed()){
            logOutSubscriber.unsubscribe();
        }
    }
}
