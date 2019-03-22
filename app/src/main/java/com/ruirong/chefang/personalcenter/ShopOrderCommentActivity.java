package com.ruirong.chefang.personalcenter;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.TakePhotoUtils;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.SpecialProductReview;
import com.ruirong.chefang.adapter.ShopOrderCommentAdapter;
import com.ruirong.chefang.bean.ShopOrderCommentBean;
import com.ruirong.chefang.bean.TeCommentBean;
import com.ruirong.chefang.event.CommentPicDeleteEvent;
import com.ruirong.chefang.event.PendingPaymentEvent;
import com.ruirong.chefang.event.ShopOrderCommentEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.view.XLHRatingBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by wu on 2018/4/1.
 * describe: 商城订单评论
 */

public class ShopOrderCommentActivity extends BaseActivity {


    @BindView(R.id.comment_goods_list)
    NoScrollListView commentGoodsList;
    @BindView(R.id.ratingbar_match)
    XLHRatingBar ratingbarMatch;
    @BindView(R.id.ratingbar_match_tv)
    TextView ratingbarMatchTv;
    @BindView(R.id.ratingbar_logistics)
    XLHRatingBar ratingbarLogistics;
    @BindView(R.id.ratingbar_logistics_tv)
    TextView ratingbarLogisticsTv;
    @BindView(R.id.ratingbar_service)
    XLHRatingBar ratingbarService;
    @BindView(R.id.ratingbar_service_tv)
    TextView ratingbarServiceTv;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    private String order_id;
    private String shop_id;
    private List<ShopOrderCommentBean> commentList = new ArrayList<>();
    private ShopOrderCommentAdapter adapter;
    private Map<String, List<String>> listMap = new LinkedHashMap<>();
    private Map<String, List<File>> listFile = new LinkedHashMap<>();


    private final int REQUEST_CODE_TAKE_PHOTO = 100;
    private final int REQUEST_CODE_CHOICE_PHOTO = 200;
    private final int REQUEST_CODE_MODIFY_NICKNAME = 300;
    private static final int PAGE_INTO_LIVENESS = 400;
    private final int REQUEST_CODE_UPLOAD_ID = 500;
    private File takePhotoFile;
    private Dialog mDialog;
    private String goods_id;

    private int num = 0;
    private int shop_score = -1;//描述
    private int logistics_score = -1;//物流
    private int service_score = -1;//服务

    @Override
    public int getContentView() {
        return R.layout.activity_shop_order_comment;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("订单评论");

        order_id = getIntent().getStringExtra("order_id");
        shop_id = getIntent().getStringExtra("shop_id");
        if (order_id == null) {
            finish();
        }

        adapter = new ShopOrderCommentAdapter(commentGoodsList);
        commentGoodsList.setAdapter(adapter);

        initRatingBar();
    }

    @Override
    public void getData() {
        getComment(new PreferencesHelper(ShopOrderCommentActivity.this).getToken(), order_id);
    }

    public void getComment(String token, String orderId) {
        HttpHelp.getInstance().create(RemoteApi.class).shopComment(token, orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<ShopOrderCommentBean>>>(ShopOrderCommentActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<List<ShopOrderCommentBean>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            commentList = baseBean.data;
                            if (commentList != null && commentList.size() > 0) {
                                adapter.setData(commentList);
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(ShopOrderCommentActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }


    private void release(MultipartBody.Builder builder) {
        HttpHelp.getInstance().create(RemoteApi.class).shopOrderRelease(builder.build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(ShopOrderCommentActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        Log.i("XXX", baseBean.code + "  " + baseBean.message);
                        if (baseBean.code == 0) {
                            num++;
                            if (num == adapter.getData().size()) {
                                hideLoadingDialog();
                                EventBus.getDefault().post(new ShopOrderCommentEvent());
                                finish();
                            }
                        } else {
                            hideLoadingDialog();
                            ToastUtil.showToast(ShopOrderCommentActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                        Log.i("XXX", throwable.getLocalizedMessage());
                        ToastUtil.showToast(ShopOrderCommentActivity.this, "提交失败");
                    }
                });
    }


    @OnClick({R.id.tv_submit})
    public void Onclick(View view) {
        for (int i = 0; i < adapter.getData().size(); i++) {
            if (TextUtils.isEmpty(adapter.getData().get(i).getContent())) {
                ToastUtil.showToast(ShopOrderCommentActivity.this, "请输入全部商品的评价！");
                return;
            }
        }

        if (shop_score == -1 || logistics_score == -1 || service_score == -1) {
            ToastUtil.showToast(ShopOrderCommentActivity.this, "请输选择评价分数！");
            return;
        }

        int average = (shop_score + logistics_score + service_score) / 3;

        showLoadingDialog("提交中");
        for (int i = 0; i < adapter.getData().size(); i++) {
            String niming;
            if (adapter.getData().get(i).isTrue()) {
                niming = "1";//匿名
            } else {
                niming = "2";//不匿名
            }
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("token", new PreferencesHelper(ShopOrderCommentActivity.this).getToken())
                    .addFormDataPart("content", adapter.getData().get(i).getContent())
                    .addFormDataPart("goods_id", adapter.getData().get(i).getGoods_id())
                    .addFormDataPart("shop_id", shop_id)
                    .addFormDataPart("order_id", order_id)
                    .addFormDataPart("anonymous", niming)
                    .addFormDataPart("score", average + "");
//            Log.i("XXX", "adapter.getData().get(i).getGoods_id()" + adapter.getData().get(i).getGoods_id());
//            Log.i("XXX", "order_id" + order_id);


            if (listFile.get(adapter.getData().get(i).getGoods_id()) != null && listFile.get(adapter.getData().get(i).getGoods_id()).size() > 0) {
                List<File> fileList = listFile.get(adapter.getData().get(i).getGoods_id());
                for (int p = 0; p < fileList.size(); p++) {
                    RequestBody IDFrontBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileList.get(p));
                    builder.addFormDataPart(String.valueOf(p), fileList.get(p).getName(), IDFrontBody);
                    Log.i("XXX", String.valueOf(p) + "  " + fileList.get(p).getName() + "");
                }
                release(builder);
            } else {
                release(builder);
            }

        }

    }


    //设置ratingBar
    private void initRatingBar() {
        ratingbarMatch.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                shop_score = countSelected;
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

        ratingbarLogistics.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                logistics_score = countSelected;
                switch (countSelected) {
                    case 0:
                        ratingbarLogisticsTv.setVisibility(View.GONE);
                        break;
                    case 1:
                        ratingbarLogisticsTv.setText("非常差");
                        break;
                    case 2:
                        ratingbarLogisticsTv.setText("差");
                        break;
                    case 3:
                        ratingbarLogisticsTv.setText("一般");
                        break;
                    case 4:
                        ratingbarLogisticsTv.setText("好");
                        break;
                    case 5:
                        ratingbarLogisticsTv.setText("非常好");
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

    /**
     * 弹框
     */
    public void DialogPic(String goods_id, final int count) {
        if (goods_id == null) {
            return;
        }
        this.goods_id = goods_id;
        mDialog = new Dialog(ShopOrderCommentActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(ShopOrderCommentActivity.this).inflate(R.layout.dialog_phone_picture, null);

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
                    takePhotoFile = TakePhotoUtils.takePhoto(ShopOrderCommentActivity.this, "nwbPhotos", REQUEST_CODE_TAKE_PHOTO);
                } catch (IOException e) {
                    ToastUtil.showToast(ShopOrderCommentActivity.this, "拍照失败");
                    e.printStackTrace();
                }
            }
        });
        photo_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//相册
                mDialog.dismiss();
                //从相册选择
                startActivityForResult(BGAPhotoPickerActivity.newIntent(ShopOrderCommentActivity.this, null, count, null, false), REQUEST_CODE_CHOICE_PHOTO);
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
                List<String> list = new ArrayList<>();
                if (listMap.get(goods_id) != null) {
                    list.addAll(listMap.get(goods_id));
                }
                path = takePhotoFile.getAbsolutePath();
                list.add(path);
                listMap.put(goods_id, list);
                modifyAvatar(listMap.get(goods_id), goods_id);
                adapter.setListMap(listMap);

            }
        }

        //选择图片回调
        if (requestCode == REQUEST_CODE_CHOICE_PHOTO && resultCode == RESULT_OK) {
            try {
                List<String> lists = new ArrayList<>();
                List<String> listImg = BGAPhotoPickerActivity.getSelectedImages(data);
                if (listMap.get(goods_id) != null) {
                    lists.addAll(listMap.get(goods_id));
                }
                lists.addAll(listImg);
                listMap.put(goods_id, lists);
                modifyAvatar(listMap.get(goods_id), goods_id);
                adapter.setListMap(listMap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩图片
     */
    private void modifyAvatar(List<String> list, String goods_id) {
        if (list != null && list.size() > 0 && goods_id != null) {
            final List<File> fileList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Luban.with(ShopOrderCommentActivity.this)
                        .load(new File(list.get(i)))//传人要压缩的图片
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                                //  压缩开始前调用，可以在方法内启动 loading UI
                            }

                            @Override
                            public void onSuccess(File file) {
                                //  压缩成功后调用，返回压缩后的图片文件
                                fileList.add(file);
                            }

                            @Override
                            public void onError(Throwable e) {
                                //  当压缩过程出现问题时调用
                            }
                        }).launch();    //启动压缩
            }
            listFile.put(goods_id, fileList);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBusUtil.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBusUtil.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void delPic(CommentPicDeleteEvent event) {
        if (!TextUtils.isEmpty(event.getGoodsId())) {
            if (event.getPosition() < listMap.get(event.getGoodsId()).size()) {
                listFile.get(event.getGoodsId()).remove(event.getPosition());
//                ToastUtil.showToast(ShopOrderCommentActivity.this, "删除成功"+listMap.get(event.getGoodsId()).size()+"  "+listFile.get(event.getGoodsId()).size());
            }
        } else {
//            ToastUtil.showToast(ShopOrderCommentActivity.this, "删除失败");
        }
    }

}
