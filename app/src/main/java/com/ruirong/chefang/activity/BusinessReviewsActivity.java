package com.ruirong.chefang.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.TakePhotoUtils;
import com.qlzx.mylibrary.util.TimeUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollGridView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.CommentPicListAdapter;
import com.ruirong.chefang.adapter.PinglunEvent;
import com.ruirong.chefang.bean.BusinessBean;
import com.ruirong.chefang.event.CommentPicDeleteEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.view.XLHRatingBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**商家评论
 * Created by Administrator on 2018/4/17.
 */

public class BusinessReviewsActivity extends BaseActivity {

    private final int REQUEST_CODE_TAKE_PHOTO = 100;
    private final int REQUEST_CODE_CHOICE_PHOTO = 200;
    private final int REQUEST_CODE_MODIFY_NICKNAME = 300;
    private static final int PAGE_INTO_LIVENESS = 400;
    private final int REQUEST_CODE_UPLOAD_ID = 500;
    @BindView(R.id.business_pic)
    ImageView businessPic;
    @BindView(R.id.pic_list)
    NoScrollGridView picList;
    @BindView(R.id.evaluate_goods_type_pic)
    CheckBox evaluateGoodsTypePic;
    @BindView(R.id.evaluate_goods_type_pic_ll)
    LinearLayout evaluateGoodsTypePicLl;
    @BindView(R.id.evaluate_goods_type_text)
    TextView evaluateGoodsTypeText;
    @BindView(R.id.business_name)
    TextView businessName;
    @BindView(R.id.evaluate_goods_type_ll)
    LinearLayout evaluateGoodsTypeLl;
    @BindView(R.id.ratingbar_match)
    XLHRatingBar ratingbarMatch;
    @BindView(R.id.ratingbar_match_tv)
    TextView ratingbarMatchTv;
    @BindView(R.id.ratingbar_service)
    XLHRatingBar ratingbarService;
    @BindView(R.id.ratingbar_service_tv)
    TextView ratingbarServiceTv;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.can_content_view)
    NestedScrollView canContentView;
    @BindView(R.id.business_content)
    EditText businessContent;
    private File takePhotoFile;
    private Dialog mDialog;
    private String shop_id;
    private String type;//7支付订单 ,8储值订单
    private boolean isTrue = true;
    private int shop_scor = -1;
    private int service_score = -1;
    private List<String> listPath = new ArrayList<>();
    private List<File> listFile = new ArrayList<>();
    private CommentPicListAdapter commentPicListAdapter;
    private MultipartBody.Builder builder;
    private int num = 0;


    @Override
    public int getContentView() {
        return R.layout.activity_business_reviews;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("商家评论");
        EventBusUtil.register(this);

        shop_id = getIntent().getStringExtra("shop_id");
        type = getIntent().getStringExtra("type");

        commentPicListAdapter = new CommentPicListAdapter(BusinessReviewsActivity.this);
        picList.setAdapter(commentPicListAdapter);

        initRatingBar();

        picList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listPath != null) {
                    if (listPath.size() <= position) {
                        DialogPic(3 - listPath.size());
                    } else {
                        startActivity(BGAPhotoPreviewActivity.newIntent(BusinessReviewsActivity.this,
                                null, listPath.get(position)));
                    }
                } else {
                    DialogPic(3);
                }
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (shop_scor == -1 || service_score == -1) {
                    ToastUtil.showToast(BusinessReviewsActivity.this, "请选择星级评分");
                    return;
                }
                showLoadingDialog("上传中...");
                int average = (shop_scor + service_score) / 2;

                String niming;
                if (evaluateGoodsTypePic.isChecked()) {
                    niming = "1";//匿名
                } else {
                    niming = "2";//不匿名
                }
                builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                builder.addFormDataPart("token", new PreferencesHelper(BusinessReviewsActivity.this).getToken())
                        .addFormDataPart("content", businessContent.getText().toString().trim())
                        .addFormDataPart("score", average + "")
                        .addFormDataPart("rand_time", TimeUtil.getRandomTime())
                        .addFormDataPart("shop_id", shop_id)
                        .addFormDataPart("type", type)
                        .addFormDataPart("anonymous", niming);

                Log.i("XXX", listPath.size() + "");
                if (listPath != null && listPath.size() > 0) {
                    modifyAvatar(listPath);
                } else {
                    businesMent(listFile);
                }


            }
        });


    }

    /**
     * 发表评论
     */
    private void businesMent(List<File> listFile) {
        Log.i("XXX", num + "-" + listFile.size());
        if (listFile.size() == num) {
            for (int i = 0; i < listFile.size(); i++) {
                RequestBody IDFrontBody = RequestBody.create(MediaType.parse("multipart/form-data"), listFile.get(i));
                builder.addFormDataPart(i + "", listFile.get(i).getName(), IDFrontBody);
            }
        }


        HttpHelp.getInstance().create(RemoteApi.class).businesMent(builder.build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(BusinessReviewsActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        Log.i("XXX", baseBean.code + "  " + baseBean.message);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {

                            EventBusUtil.post(new PinglunEvent());

                            finish();
                        }
                        ToastUtil.showToast(BusinessReviewsActivity.this, baseBean.message);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                        Log.i("XXX", throwable.getLocalizedMessage());
                        ToastUtil.showToast(BusinessReviewsActivity.this, "提交失败");
                    }
                });
    }


    @Override
    public void getData() {
        business(new PreferencesHelper(BusinessReviewsActivity.this).getToken(), shop_id);
    }

    /**
     * 店铺信息
     *
     * @param token
     * @param shop_id
     */
    private void business(String token, String shop_id) {
        HttpHelp.getInstance().create(RemoteApi.class).business(token, shop_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<BusinessBean>>(BusinessReviewsActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<BusinessBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (baseBean.data != null) {
                                GlideUtil.display(BusinessReviewsActivity.this, Constants.IMG_HOST + baseBean.data.getCover(), businessPic);
                                businessName.setText(baseBean.data.getSp_name());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }


    /**
     * 弹框
     */
    public void DialogPic(final int count) {
        mDialog = new Dialog(BusinessReviewsActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(BusinessReviewsActivity.this).inflate(R.layout.dialog_phone_picture, null);

        mDialog.setContentView(inflate);
        Window dialogwindow = mDialog.getWindow();

        dialogwindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

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
            public void onClick(View view) {//照片
                mDialog.dismiss();
                try {
                    takePhotoFile = TakePhotoUtils.takePhoto(BusinessReviewsActivity.this, "nwbPhotos", REQUEST_CODE_TAKE_PHOTO);
                } catch (IOException e) {
                    ToastUtil.showToast(BusinessReviewsActivity.this, "拍照失败");
                    e.printStackTrace();
                }
            }
        });
        photo_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//相册
                mDialog.dismiss();
                //从相册选择
                startActivityForResult(BGAPhotoPickerActivity.newIntent(BusinessReviewsActivity.this, null, count, null, false), REQUEST_CODE_CHOICE_PHOTO);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String path;
//        mBaseDialog.dismiss();

        //拍照回调
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            if (takePhotoFile != null) {
//                List<String> list = new ArrayList<>();
//                if (listPath != null) {
//                    listPath.addAll(list);
//                }
                path = takePhotoFile.getAbsolutePath();
                listPath.add(path);
//                listMap.put(goods_id, list);
//                modifyAvatar(listMap.get(goods_id), goods_id);
                commentPicListAdapter.setList(listPath);

            }
        }

        //选择图片回调
        if (requestCode == REQUEST_CODE_CHOICE_PHOTO && resultCode == RESULT_OK) {
            try {
//                List<String> lists = new ArrayList<>();
                List<String> listImg = BGAPhotoPickerActivity.getSelectedImages(data);
//                if (listPath != null) {
//                    lists.addAll(listPath);
//                }
                listPath.addAll(listImg);
//                listMap.put(goods_id, lists);
//                modifyAvatar(listMap.get(goods_id), goods_id);
//                adapter.setListMap(listMap);
                commentPicListAdapter.setList(listPath);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩图片
     *
     * @param list
     */
    private void modifyAvatar(final List<String> list) {
        Log.i("XXX", list.size() + "xx");
        for (int i = 0; i < list.size(); i++) {
            Luban.with(BusinessReviewsActivity.this)
                    .load(new File(list.get(i)))                     //传人要压缩的图片
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            //  压缩开始前调用，可以在方法内启动 loading UI
                        }

                        @Override
                        public void onSuccess(File file) {
                            //  压缩成功后调用，返回压缩后的图片文件
                            listFile.add(file);
                            num++;
                            if (list.size() == listFile.size()) {
                                businesMent(listFile);
                                Log.i("XXX", num + "-" + listFile.size()+"-"+list.size());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            //  当压缩过程出现问题时调用
                        }
                    }).launch();    //启动压缩
        }
    }

    //设置ratingBar
    private void initRatingBar() {
        ratingbarMatch.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                shop_scor = countSelected;
                switch (countSelected) {
                    case 0:
                        ratingbarMatchTv.setVisibility(View.GONE);
                        break;
                    case 1:
                        ratingbarMatchTv.setText("非常差");
                        break;
                    case 2:
                        ratingbarMatchTv.setText("差");
                        break;
                    case 3:
                        ratingbarMatchTv.setText("一般");
                        break;
                    case 4:
                        ratingbarMatchTv.setText("好");
                        break;
                    case 5:
                        ratingbarMatchTv.setText("非常好");
                        break;
                }
            }
        });

        ratingbarService.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                service_score = countSelected;
                switch (countSelected) {
                    case 0:
                        ratingbarServiceTv.setVisibility(View.GONE);
                        break;
                    case 1:
                        ratingbarServiceTv.setText("非常差");
                        break;
                    case 2:
                        ratingbarServiceTv.setText("差");
                        break;
                    case 3:
                        ratingbarServiceTv.setText("一般");
                        break;
                    case 4:
                        ratingbarServiceTv.setText("好");
                        break;
                    case 5:
                        ratingbarServiceTv.setText("非常好");
                        break;
                }
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void delPic(CommentPicDeleteEvent event) {
        if (!TextUtils.isEmpty(event.getGoodsId())) {
            if (event.getPosition() < listPath.size()) {
                Log.i("XXX", event.getPosition() + "");
                listPath.remove(event.getPosition());
//                ToastUtil.showToast(ShopOrderCommentActivity.this, "删除成功"+listMap.get(event.getGoodsId()).size()+"  "+listFile.get(event.getGoodsId()).size());
            }
        } else {
//            ToastUtil.showToast(ShopOrderCommentActivity.this, "删除失败");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

}
