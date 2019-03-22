package com.ruirong.chefang.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.LruCache;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.FileCallBack;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.http.OkHttpDownloadManager;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.MyApplication;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.ContactOusBean;
import com.ruirong.chefang.bean.UpdateBean;
import com.ruirong.chefang.http.RemoteApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ruirong.chefang.common.Constants.ABOUT_OUS_URL;

/**
 * Created by guo on 2017/6/1.
 * 关于我们
 */

public class AboutOusActivity extends BaseActivity {

    @BindView(R.id.tv_version_code)
    TextView tvVersionCode;
    @BindView(R.id.tv_copyright)
    TextView tvCopyright;

    private String icp;
    private String tel;
    private BaseSubscriber<BaseBean<ContactOusBean>> getContactOusInfoSubscriber;
    private String versionName;
    private BaseSubscriber<BaseBean<UpdateBean>> checkUpdateSubscriber;

    @Override
    public int getContentView() {
        return R.layout.activity_about_ous;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("关于驴优客");

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(versionName)) {

            tvVersionCode.setText("V " + versionName);
        }


    }

    @Override
    public void getData() {

        getContactOusInfo(false);

    }


    private void getContactOusInfo(final boolean needCall) {
        getContactOusInfoSubscriber = new BaseSubscriber<BaseBean<ContactOusBean>>(this, null) {
            @Override
            public void onNext(BaseBean<ContactOusBean> contactOusBeanBaseBean) {
                if (contactOusBeanBaseBean.code == 0) {
                    tvCopyright.setText(contactOusBeanBaseBean.data.getIcp());
                    tel = contactOusBeanBaseBean.data.getTel();
                    if (needCall) {
                        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));//跳转到拨号界面，同时传递电话号码
                        startActivity(dialIntent);
                    }
                }
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).getContactOusInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getContactOusInfoSubscriber);
    }


    /**
     * 关于我们
     */
    @OnClick(R.id.rl_about_ous)
    public void gotoAboutOus() {
        WebActivity.startActivity(this, "关于我们", ABOUT_OUS_URL);
    }
    /**
     * 申请合伙人
     */
    @OnClick(R.id.rl_applay)
    public void applayPartener() {
        if (TextUtils.isEmpty(new PreferencesHelper(AboutOusActivity.this).getToken())){
            ToastUtil.showToast(AboutOusActivity.this, "请先登录");
            LoginActivity.startActivity(AboutOusActivity.this, LoginActivity.class);
        }else {
            startActivity(new Intent(AboutOusActivity.this, ApplayPartenerActivity.class));
        }
    }
    /**
     * 联系我们
     */
    @OnClick(R.id.rl_contact_ous)
    public void contactOus() {
        if (TextUtils.isEmpty(tel)) {

            getContactOusInfo(true);

        } else {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));//跳转到拨号界面，同时传递电话号码
            startActivity(dialIntent);
        }
    }

    /**
     * 检查更新
     */
    @OnClick(R.id.rl_check_version)
    public void checkVersion() {
        if (TextUtils.isEmpty(versionName)) {
            ToastUtil.showToast(this, "当前版本号获取失败");
        } else {
            checkUpdateSubscriber = new BaseSubscriber<BaseBean<UpdateBean>>(this, null) {
                @Override
                public void onNext(BaseBean<UpdateBean> updateBeanBaseBean) {
                    if (updateBeanBaseBean.code == 0) {
                        UpdateBean updateBean = updateBeanBaseBean.data;
                        if (updateBean.getStatus() == 0) {
                            //无需更新
                            ToastUtil.showToast(AboutOusActivity.this, "当前已是最新版本");
                        } else if (updateBean.getStatus() == 1) {
                            //有新版本
                            LogUtil.d("有新版本---"+updateBean.getVersion());
                            showNewVersion(updateBean);
                        }
                    }else if (updateBeanBaseBean.code==1){
                        //无需更新
                        ToastUtil.showToast(AboutOusActivity.this, "当前已是最新版本");
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    ToastUtil.showToast(AboutOusActivity.this, getString(R.string.net_error));
                }
            };
            HttpHelp.getInstance().create(RemoteApi.class).checkUpdate(1, versionName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(checkUpdateSubscriber);
        }

    }

    /**
     * 新版本dialog
     * @param updateBean
     */
    private void showNewVersion(final UpdateBean updateBean) {
        String content = String.format("最新版本：%1$s\n新版本大小：%2$s\n\n更新内容\n%3$s", updateBean.getVersion(), updateBean.getSize(), updateBean.getUpdate_log());

        final AlertDialog dialog = new AlertDialog.Builder(this).create();

        dialog.setTitle("应用更新");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        float density = getResources().getDisplayMetrics().density;
        TextView tv = new TextView(this);
        tv.setMovementMethod(new ScrollingMovementMethod());
        tv.setVerticalScrollBarEnabled(true);
        tv.setTextSize(14);
        tv.setMaxHeight((int) (250 * density));

        dialog.setView(tv, (int) (25 * density), (int) (15 * density), (int) (25 * density), 0);


        tv.setText(content);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                File mApkFile = new File(getExternalCacheDir(), "yjfd_"+updateBean.getVersion()+ ".apk");
                if (mApkFile.exists()){
                    install(AboutOusActivity.this,mApkFile);
                }else {
                    getDownloadUrl(updateBean.getUrl(),mApkFile);
                }
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void getDownloadUrl(final String url, final File mApkFile) {
        final OkHttpClient okHttpClient = new OkHttpClient();
        final Request build = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(build);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showToast(AboutOusActivity.this,"下载失败");
                if (mApkFile.exists()){
                    mApkFile.delete();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    final String downloadUrl = jsonObject.getString("data");
                    MyApplication.getMainHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            final ProgressDialog dialog = new ProgressDialog(AboutOusActivity.this);
                            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            dialog.setMessage("下载中...");
                            dialog.setIndeterminate(false);
                            dialog.setCancelable(false);
                            dialog.show();
                            download(dialog,downloadUrl,mApkFile);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void download(final ProgressDialog dialog, String downloadUrl, final File mApkFile) {



        FileCallBack fileCallBack = new FileCallBack(this,mApkFile) {
            @Override
            public void inProgress(long current, long total) {
                int progress = (int) (current * 100 / total);
                LogUtil.d("下载apk---"+"current"+current+"---"+"total "+total);
                dialog.setProgress(progress);
            }

            @Override
            public void onSuccess(File response) {
                install(AboutOusActivity.this,response);
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showToast(AboutOusActivity.this,"下载失败");
                if (mApkFile.exists()){
                    mApkFile.delete();
                }
            }

            @Override
            public void onAfter() {
                dialog.dismiss();
            }
        };
        OkHttpDownloadManager.getInstance().download(downloadUrl,fileCallBack);
    }


    /**
     * 安装
     * @param context
     * @param file
     */
    public void install(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        } else {
            Uri uri = FileProvider.getUriForFile(context, "com.ruirong.chefang.fileprovider", file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (checkUpdateSubscriber != null && checkUpdateSubscriber.isUnsubscribed()) {
            checkUpdateSubscriber.unsubscribe();
        }
    }
}
