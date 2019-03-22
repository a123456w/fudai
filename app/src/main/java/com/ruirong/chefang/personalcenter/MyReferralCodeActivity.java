package com.ruirong.chefang.personalcenter;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.FileCallBack;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.http.OkHttpDownloadManager;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.MyTowCodeBean;
import com.ruirong.chefang.common.Constants;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.SharedUtil;
import com.ruirong.chefang.util.ToolUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2017/12/28 0028
 * describe:  我的推荐码
 */
public class MyReferralCodeActivity extends BaseActivity {
    @BindView(R.id.iv_twocodeImg)
    ImageView ivTwocodeImg;


    private BaseSubscriber<BaseBean<MyTowCodeBean>> usePurchaseSubscriber;
    private String qrcodeUrl;
    private Dialog mDialog;


    private static String url;
    /**
     * 对保存的图片命名
     */
    private String imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "我的推荐码.gif";


    @Override
    public int getContentView() {
        return R.layout.activity_my_referral_code;
    }


    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("我的推荐码");
        titleBar.setRightImageRes(R.drawable.ic_share);
        titleBar.setRightImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(new PreferencesHelper(MyReferralCodeActivity.this).getToken())) {
                    ToolUtil.goToLogin(MyReferralCodeActivity.this);
                } else {
                    SharedUtil sharedUtil = new SharedUtil(MyReferralCodeActivity.this);
                    sharedUtil.initShared(MyReferralCodeActivity.this);
                }
            }
        });

        ivTwocodeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPic();
            }
        });


    }

    @Override
    public void getData() {
        usePurchaseSubscriber = new BaseSubscriber<BaseBean<MyTowCodeBean>>(this, loadingLayout) {
            @Override
            public void onNext(BaseBean<MyTowCodeBean> stringBaseBean) {
                super.onNext(stringBaseBean);
                if (stringBaseBean.code == 0) {
                    qrcodeUrl = stringBaseBean.data.getQrcode();
                    url = com.qlzx.mylibrary.common.Constants.IMG_HOST + qrcodeUrl;
//                    GlideUtil.display(MyReferralCodeActivity.this, com.qlzx.mylibrary.common.Constants.IMG_HOST + qrcodeUrl, ivTwocodeImg);
                    Glide.with(MyReferralCodeActivity.this).load(com.qlzx.mylibrary.common.Constants.IMG_HOST + qrcodeUrl).asBitmap().dontAnimate().placeholder(com.qlzx.mylibrary.R.drawable.ic_placeload).error(com.qlzx.mylibrary.R.drawable.ic_load_error).into(ivTwocodeImg);
                } else if (stringBaseBean.code == 4) {
                    ToolUtil.loseToLogin(MyReferralCodeActivity.this);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).userPurchase(new PreferencesHelper(MyReferralCodeActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(usePurchaseSubscriber);
    }

    /**
     * 保存图片dialog
     */
    @OnClick(R.id.ll_root)
    public void showSaveImgDialog() {
        if (!TextUtils.isEmpty(qrcodeUrl)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("保存二维码到相册？");
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showLoadingDialog("");
                    File dir = null;
                    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                        dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Constants.YJFD_DOWNLOAD);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                    } else {
                        dir = getExternalCacheDir();
                    }

                    final File file = new File(dir, "yjfd_qrcode.jpg");


                    OkHttpDownloadManager.getInstance().download(com.qlzx.mylibrary.common.Constants.IMG_HOST + qrcodeUrl, new FileCallBack(MyReferralCodeActivity.this, file) {
                        @Override
                        public void inProgress(long current, long total) {

                        }

                        @Override
                        public void onSuccess(File response) {
                            ToastUtil.showToast(MyReferralCodeActivity.this, "已保存到" + file.getAbsolutePath());
                            // 通知图库更新
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
                        }

                        @Override
                        public void onError(Exception e) {
                            ToastUtil.showToast(MyReferralCodeActivity.this, "保存失败");
                        }

                        @Override
                        public void onAfter() {
                            hideLoadingDialog();
                        }
                    });

                }
            });

            builder.create().show();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 弹框
     */
    public void DialogPic() {

        mDialog = new Dialog(MyReferralCodeActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(MyReferralCodeActivity.this).inflate(R.layout.dialog_phone_picture, null);
        mDialog.setContentView(inflate);
        Window dialogwindow = mDialog.getWindow();
        dialogwindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.y = ScreenUtil.getBottomStatusHeight(MyReferralCodeActivity.this);
        dialogwindow.setAttributes(lp);

//        mDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//
        mDialog.show();
//        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                mDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//            }
//        });


        TextView tv_close, pat_pic, photo_select;
        tv_close = (TextView) inflate.findViewById(R.id.tv_close);
        photo_select = (TextView) inflate.findViewById(R.id.photo_select);
        pat_pic = (TextView) inflate.findViewById(R.id.pat_pic);

        photo_select.setText("推荐记录");
        pat_pic.setText("保存图片");

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        pat_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();

                if (TextUtils.isEmpty(url)) {
                    return;
                }

                if (ActivityCompat.checkSelfPermission(MyReferralCodeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MyReferralCodeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }

                //String path =   getImagePath(url);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String path = getImagePath(url);
                        /**
                         * 拷贝到指定路径
                         */
                        copyFile(path, imagePath);
                        Intent intentBroadcast = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        File file = new File(imagePath);
                        intentBroadcast.setData(Uri.fromFile(file));
                        sendBroadcast(intentBroadcast);

                    }
                }).start();


            }
        });
        photo_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                Intent intent1 = new Intent(MyReferralCodeActivity.this, RecommendRecordActivity.class);
                startActivity(intent1);
            }
        });

    }


    /**********************************************************/


    /**
     * Glide 获得图片缓存路径
     */
    private String getImagePath(String imgUrl) {
        String path = null;
        FutureTarget<File> future = Glide.with(MyReferralCodeActivity.this)
                .load(imgUrl)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        try {
            File cacheFile = future.get();
            path = cacheFile.getAbsolutePath();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return path;
    }

    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                fs.close();
                inStream.close();
                ToastUtil.showToast(MyReferralCodeActivity.this, "下载完成");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}
