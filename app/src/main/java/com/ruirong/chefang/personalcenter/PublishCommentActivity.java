package com.ruirong.chefang.personalcenter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.ScreenUtil;
import com.qlzx.mylibrary.util.TakePhotoUtils;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by chenlipeng on 2018/1/8 0008
 * describe:  发表评论
 */
public class PublishCommentActivity extends BaseActivity {
    @BindView(R.id.business_img)
    ImageView businessImg;
    @BindView(R.id.content_edit_text)
    EditText contentEditText;
    @BindView(R.id.iv_one_pic)
    ImageView ivOnePic;
    @BindView(R.id.iv_one_delete)
    ImageView ivOneDelete;
    @BindView(R.id.rl_one_pic)
    RelativeLayout rlOnePic;
    @BindView(R.id.iv_two_pic)
    ImageView ivTwoPic;
    @BindView(R.id.iv_two_delete)
    ImageView ivTwoDelete;
    @BindView(R.id.rl_two_pic)
    RelativeLayout rlTwoPic;
    @BindView(R.id.iv_three_pic)
    ImageView ivThreePic;
    @BindView(R.id.iv_three_delete)
    ImageView ivThreeDelete;
    @BindView(R.id.rl_three_pic)
    RelativeLayout rlThreePic;
    @BindView(R.id.add_pic)
    ImageView addPic;
    @BindView(R.id.imgview1)
    ImageView imgview1;
    @BindView(R.id.photo_layout)
    RelativeLayout photoLayout;
    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.rb_grade)
    RatingBar rbGrade;
    @BindView(R.id.rb_grade1)
    RatingBar rbGrade1;
    @BindView(R.id.rb_grade2)
    RatingBar rbGrade2;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    private final int REQUEST_CODE_TAKE_PHOTO = 100;
    private final int REQUEST_CODE_CHOICE_PHOTO = 200;
    private final int REQUEST_CODE_MODIFY_NICKNAME = 300;
    private static final int PAGE_INTO_LIVENESS = 400;
    private final int REQUEST_CODE_UPLOAD_ID = 500;
    private File takePhotoFile;
    private String photoPath;

    @Override
    public int getContentView() {
        return R.layout.activity_publish_comment;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("发表评论");

    }

    @Override
    public void getData() {

    }

    @OnClick({R.id.iv_one_delete, R.id.iv_two_delete, R.id.iv_three_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.add_pic:  //
//
//
//
//                break;
            // TODO: 2018/1/10 0010   这个地方删除，还是有问题的 
            case R.id.iv_one_delete:
                currentPosition=currentPosition-1;
                addPic.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_two_delete:
                currentPosition=currentPosition-1;
                addPic.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_three_delete:
                currentPosition=currentPosition-1;
                addPic.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    /**
     * 换头像
     */
    @OnClick(R.id.add_pic)
    public void choiceAvatar() {
//        viewShade.setVisibility(View.VISIBLE);
        final Dialog mDialog = new Dialog(this, R.style.dialog_in_bottom);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_choice_avatar, null);
        mDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = ScreenUtil.getScreenWidth(this);
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        contentView.setLayoutParams(layoutParams);
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        mDialog.show();
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
//                viewShade.setVisibility(View.GONE);
            }
        });
        contentView.findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                //拍照
                try {
                    takePhotoFile = TakePhotoUtils.takePhoto(PublishCommentActivity.this, "zdhtPhotos", REQUEST_CODE_TAKE_PHOTO);
                } catch (IOException e) {
                    ToastUtil.showToast(PublishCommentActivity.this, "拍照失败");
                    e.printStackTrace();
                }
            }
        });

        contentView.findViewById(R.id.tv_choice_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                //从相册选择
                startActivityForResult(BGAPhotoPickerActivity.newIntent(
                        PublishCommentActivity.this, null, 1, null,
                        false), REQUEST_CODE_CHOICE_PHOTO);

            }
        });

        contentView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍照回调
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            if (takePhotoFile != null) {

                photoPath = takePhotoFile.getAbsolutePath();
                LogUtil.d("拍照后的图片路径---" + photoPath);
                modifyAvatar();
            }
        }

        //选择图片回调
        if (requestCode == REQUEST_CODE_CHOICE_PHOTO && resultCode == RESULT_OK) {
            try {
                photoPath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);
                LogUtil.d("选择图片后的图片路径---" + photoPath);
                modifyAvatar();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int currentPosition = 0;

    /**
     * 修改头像
     */
    private void modifyAvatar() {
//        builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                .addFormDataPart("token", preferencesHelper.getToken());

//        showLoadingDialog("上传中...");
//        mBaseDialog.setCancelable(false);

        Luban.with(this)
                .load(new File(photoPath))                     //传人要压缩的图片
                .setCompressListener(new OnCompressListener() {

                    @Override
                    public void onStart() {
                        //  压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        //  压缩成功后调用，返回压缩后的图片文件

//                        upload(file);
                        ImageView  currentImage=null;

                        if (currentPosition == 0) {
                            currentPosition=1;
                            currentImage=ivOnePic;
                            rlOnePic.setVisibility(View.VISIBLE);

//                            ivOnePic.setVisibility(View.VISIBLE);
                        } else if (currentPosition == 1) {
                            currentPosition=2;
                            currentImage=ivTwoPic;
                            rlTwoPic.setVisibility(View.VISIBLE);

                        } else if (currentPosition == 2) {
                            currentPosition=3;

                            currentImage=ivThreePic;
                            rlThreePic.setVisibility(View.VISIBLE);

                            addPic.setVisibility(View.GONE);

                        }



                        Glide.with(PublishCommentActivity.this).load(photoPath)
                                .asBitmap().dontAnimate().fitCenter().placeholder(com.qlzx.mylibrary.R.drawable.ic_placeload)
                                .error(com.qlzx.mylibrary.R.drawable.ic_load_error).into(currentImage);


                    }

                    @Override
                    public void onError(Throwable e) {
                        //  当压缩过程出现问题时调用
                    }
                }).launch();    //启动压缩
    }
}
