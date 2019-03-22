package com.ruirong.chefang.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.security.rp.RPSDK;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.TakePhotoUtils;
import com.qlzx.mylibrary.util.ToastUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.IdentyBean;
import com.ruirong.chefang.bean.UserInforBean;
import com.ruirong.chefang.common.Constants;
import com.ruirong.chefang.event.UpdateInformationEvent;
import com.ruirong.chefang.event.UpdateNickNameEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.view.ClipImageActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 个人资料
 * Created by dillon on 2017/3/31.
 */

public class PersionMessageActivity extends AppCompatActivity {


    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.circleview_headportrait)
    CircleImageView circleviewHeadportrait;
    @BindView(R.id.right_arrows)
    ImageView rightArrows;
    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    @BindView(R.id.icon_1)
    ImageView icon1;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.rl_nickname)
    RelativeLayout rlNickname;
    @BindView(R.id.icon_3)
    ImageView icon3;
    @BindView(R.id.tv_authentication)
    TextView tvAuthentication;
    @BindView(R.id.rl_identity)
    RelativeLayout rlIdentity;
    @BindView(R.id.swipe_target)
    LinearLayout swipeTarget;
    private ArrayList<String> headPhoto = new ArrayList<>();    // 身份证正面
    private String gendersSex;


    private BaseSubscriber<BaseBean<UserInforBean>> userInforSubscriber;

    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 200;


    private final int REQUEST_CODE_TAKE_PHOTO = 100;
    private final int REQUEST_CODE_CHOICE_PHOTO = 200;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    //调用照相机返回图片临时文件
    private File tempFile;

    UserInforBean userInforBean;

    private String idToken = "";
    private String isCard = "";
    private Dialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persionmessage);
        ButterKnife.bind(this);
        ActionBar supportActionBar = getSupportActionBar();
        if (null != supportActionBar) {
            supportActionBar.hide();  //隐藏掉标题栏
        }
        EventBusUtil.register(this);
        findViewById(R.id.iv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //创建拍照存储的临时文件
        createCameraTempFile(savedInstanceState);

        getUserInfor();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateNickName(UpdateNickNameEvent event) {
        getUserInfor();
    }


    /**
     * 获得用户信息
     */
    public void getUserInfor() {

        userInforSubscriber = new BaseSubscriber<BaseBean<UserInforBean>>(this, null) {
            @Override
            public void onNext(BaseBean<UserInforBean> userInforBeanBaseBean) {
                super.onNext(userInforBeanBaseBean);
                if (userInforBeanBaseBean.code == 0) {
                    userInforBean = userInforBeanBaseBean.data;

                    GlideUtil.display(PersionMessageActivity.this, com.qlzx.mylibrary.common.Constants.IMG_HOST + userInforBean.getPic(), circleviewHeadportrait);
                    tvNickname.setText(userInforBean.getName());

                    if ("1".equals(userInforBean.getIs_card())) {
                        tvAuthentication.setText("已认证");
                    } else {
                        tvAuthentication.setText("未认证");
                    }

                } else if (userInforBeanBaseBean.code == 4) {
                    ToolUtil.loseToLogin(PersionMessageActivity.this);
                }

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).userInfor(new PreferencesHelper(PersionMessageActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userInforSubscriber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userInforSubscriber != null && userInforSubscriber.isUnsubscribed()) {
            userInforSubscriber.unsubscribe();
        }
        EventBusUtil.unregister(this);
    }

    /**
     * 修改头像
     */
    @OnClick(R.id.rl_head)
    public void updateHeadPhoto() {
        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
        // File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");
        uploadHeadImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    List<String> listImg = BGAPhotoPickerActivity.getSelectedImages(intent);
                    if (listImg.size() > 0) {
                        gotoClipActivity(Uri.parse(listImg.get(0)));
                    }
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    circleviewHeadportrait.setImageBitmap(bitMap);

                    headPhoto.add(cropImagePath);

                    updateHeadPic();


                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......

                }
                break;
        }
    }

    /**
     * 外部存储权限申请返回
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoCarema();
            } else {
                // Permission Denied
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoPhoto();
            } else {
                // Permission Denied
            }
        }
    }

    /**
     * 修改头像
     */
    public void updateHeadPic() {
        File fileIDFront = new File(headPhoto.get(headPhoto.size() - 1));
        RequestBody IDFrontBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileIDFront);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("token", new PreferencesHelper(PersionMessageActivity.this).getToken())
                .addFormDataPart("pic[]", fileIDFront.getName(), IDFrontBody);

        BaseSubscriber<BaseBean<Object>> updateHeadPicSubscriber = new BaseSubscriber<BaseBean<Object>>(this, null) {
            @Override
            public void onNext(BaseBean<Object> objectBaseBean) {
                super.onNext(objectBaseBean);
                Log.i("XXX", objectBaseBean.code + "  " + objectBaseBean.message);
                if (objectBaseBean.code == 0) {
                    EventBusUtil.post(new UpdateInformationEvent());
                }
                ToastUtil.showToast(PersionMessageActivity.this, objectBaseBean.message);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).updateHeadPic(builder.build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updateHeadPicSubscriber);

    }


    @OnClick({R.id.rl_nickname, R.id.rl_identity})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_nickname:
                Intent intentnickname = new Intent(PersionMessageActivity.this, UpdateNikeNameActivity.class);
                startActivityForResult(intentnickname, Constants.UPDATENICKNAMEREQUESTCODE);
                break;
            case R.id.rl_identity://实名认证
                if (TextUtils.isEmpty(new PreferencesHelper(PersionMessageActivity.this).getToken())) {
                    ToolUtil.goToLogin(PersionMessageActivity.this);
                } else {
                    identity(new PreferencesHelper(PersionMessageActivity.this).getToken());
                }

                break;
        }
    }

    /**
     * 获取token
     *
     * @param token
     */
    public void identity(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).idention(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<IdentyBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<IdentyBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            IdentyBean identyBean = baseBean.data;
                            idToken = identyBean.getToken();
                            isCard = identyBean.getIs_card();

                            identytyAli(idToken);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 阿里实名认证
     *
     * @param token
     */
    public void identytyAli(String token) {

        RPSDK.start(token, PersionMessageActivity.this,
                new RPSDK.RPCompletedListener() {
                    @Override
                    public void onAuditResult(RPSDK.AUDIT audit) {
                        if (audit == RPSDK.AUDIT.AUDIT_PASS) { //认证通过
                            if ("0".equals(isCard)) {
                                updateIdentity(new PreferencesHelper(PersionMessageActivity.this).getToken());

                            }
                        } else if (audit == RPSDK.AUDIT.AUDIT_FAIL) { //认证不通过
                        } else if (audit == RPSDK.AUDIT.AUDIT_NOT) { //未认证，用户取消
                        } else if (audit == RPSDK.AUDIT.AUDIT_EXCEPTION) { //系统异常
                        }
                    }
                });


    }

    /**
     * 上传实名认证后数据
     *
     * @param token
     */
    public void updateIdentity(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).updateIdention(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this, null) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            getUserInfor();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });


    }


    /**
     * 上传头像弹框
     */
    private void uploadHeadImage() {

        mDialog = new Dialog(PersionMessageActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(PersionMessageActivity.this).inflate(R.layout.dialog_phone_picture, null);
        mDialog.setContentView(inflate);
        Window dialogwindow = mDialog.getWindow();
        dialogwindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.y = ScreenUtil.getBottomStatusHeight(PersionMessageActivity.this);
        dialogwindow.setAttributes(lp);
        mDialog.show();

        TextView tv_close, pat_pic, photo_select;
        tv_close = (TextView) inflate.findViewById(R.id.tv_close);
        photo_select = (TextView) inflate.findViewById(R.id.photo_select);
        pat_pic = (TextView) inflate.findViewById(R.id.pat_pic);

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
                //权限判断
                if (ContextCompat.checkSelfPermission(PersionMessageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(PersionMessageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统相机
                    gotoCarema();
                }


            }
        });
        photo_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                //权限判断
                if (ContextCompat.checkSelfPermission(PersionMessageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(PersionMessageActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统图库
                    gotoPhoto();
                }
            }
        });

    }


    /**
     * 跳转到相册
     */
    private void gotoPhoto() {

        startActivityForResult(BGAPhotoPickerActivity.newIntent(PersionMessageActivity.this, null, 1, null, false), REQUEST_PICK);

/*
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);*/
    }

    /**
     * 跳转到照相机
     */
    private void gotoCarema() {
        try {
            tempFile = TakePhotoUtils.takePhoto(PersionMessageActivity.this, "tempFile", REQUEST_CAPTURE);
        } catch (IOException e) {
            ToastUtil.showToast(PersionMessageActivity.this, "拍照失败");
            e.printStackTrace();
        }



/*

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, REQUEST_CAPTURE);*/
    }

    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }

}
