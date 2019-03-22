package com.ruirong.chefang.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.TakePhotoUtils;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.CommentPicListAdapter;
import com.ruirong.chefang.bean.LuckyBagCommentBean;
import com.ruirong.chefang.bean.ReserveOrderCommentBean;
import com.ruirong.chefang.event.CommentPicDeleteEvent;
import com.ruirong.chefang.event.LuckyOrderCommnetEvent;
import com.ruirong.chefang.event.ReservationOrderEvent;
import com.ruirong.chefang.event.ReserveOrderCommentEvent;
import com.ruirong.chefang.event.ShopOrderCommentEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.personalcenter.ShopOrderCommentActivity;
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
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * describe: 预定订单的评价
 */

public class ReserveOrderCommentActivity extends BaseActivity {

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
    @BindView(R.id.goods_pic)
    ImageView goodsPic;
    @BindView(R.id.goods_title)
    TextView goodsTitle;
    @BindView(R.id.user_content)
    EditText userContent;
    @BindView(R.id.goods_pid_list)
    GridView goodsPidList;
    @BindView(R.id.evaluate_goods_type_pic)
    ImageView evaluateGoodsTypePic;
    @BindView(R.id.evaluate_goods_type_text)
    TextView evaluateGoodsTypeText;


    private final int REQUEST_CODE_TAKE_PHOTO = 100;
    private final int REQUEST_CODE_CHOICE_PHOTO = 200;
    private Dialog mDialog;

    private String order_id;
    private int type;//  1 为预定订单评论    2为福袋订单评论
    private ReserveOrderCommentBean bean;
    private LuckyBagCommentBean luckyBean;

    private int shop_score = -1;//描述
    private int logistics_score = -1;//物流
    private int service_score = -1;//服务

    private CommentPicListAdapter commentPicListAdapter;
    private List<String> picList = new ArrayList<>();
    private File takePhotoFile;
    private List<File> fileList;


    @Override
    public int getContentView() {
        return R.layout.activity_reserve_order_comment;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("订单评论");

        order_id = getIntent().getStringExtra("order_id");
        type = getIntent().getIntExtra("type", 0);

        if (order_id == null || type == 0) {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBusUtil.register(this);
    }

    @Override
    public void getData() {
        if (type == 1){
            getComment(new PreferencesHelper(this).getToken(), order_id);
        }else if (type == 2){
            getLuclyBagComment(new PreferencesHelper(this).getToken(), order_id);
        }

    }

    /**
     * 获取预定订单评论
     *
     * @param token
     * @param order_id
     */
    public void getComment(String token, String order_id) {
        HttpHelp.getInstance().create(RemoteApi.class).getReserveComment(token, order_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ReserveOrderCommentBean>>(ReserveOrderCommentActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<ReserveOrderCommentBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            bean = baseBean.data;
                            updateUI(bean, null);
                        }else if (baseBean.code==4){
                           ToolUtil.loseToLogin(ReserveOrderCommentActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 获取福袋订单评论
     *
     * @param token
     * @param order_id
     */
    public void getLuclyBagComment(String token, String order_id) {

        HttpHelp.getInstance().create(RemoteApi.class).getLuckyBagComment(token, order_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<LuckyBagCommentBean>>(ReserveOrderCommentActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<LuckyBagCommentBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            luckyBean = baseBean.data;
                            updateUI(null, luckyBean);
                        }else if (baseBean.code==4){
                            ToolUtil.loseToLogin(ReserveOrderCommentActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    private void updateUI(ReserveOrderCommentBean comment, LuckyBagCommentBean luckyComment) {
        if (type == 1 && comment != null) {
            GlideUtil.display(ReserveOrderCommentActivity.this, Constants.IMG_HOST + comment.getPinglun().getCover(), goodsPic);
            goodsTitle.setText(comment.getPinglun().getName());
            if (bean.isTrue()) {
                evaluateGoodsTypePic.setImageResource(R.drawable.orange_choosed_icon);
                evaluateGoodsTypeText.setText("你的评论会以匿名的形式展现");
            } else {
                evaluateGoodsTypePic.setImageResource(R.drawable.no_choosed);
                evaluateGoodsTypeText.setText("你的评论能帮助其他小伙伴呦");
            }
        } else if (type == 2 && luckyComment != null) {
            GlideUtil.display(ReserveOrderCommentActivity.this, Constants.IMG_HOST + luckyComment.getPic(), goodsPic);
            goodsTitle.setText(luckyComment.getGoods_name());
            if (luckyComment.isTrue()) {
                evaluateGoodsTypePic.setImageResource(R.drawable.orange_choosed_icon);
                evaluateGoodsTypeText.setText("你的评论会以匿名的形式展现");
            } else {
                evaluateGoodsTypePic.setImageResource(R.drawable.no_choosed);
                evaluateGoodsTypeText.setText("你的评论能帮助其他小伙伴呦");
            }
        }


        initRatingBar();
        initPicPicker();
    }

    private void initPicPicker() {
        //照片选择
        commentPicListAdapter = new CommentPicListAdapter(ReserveOrderCommentActivity.this);
        goodsPidList.setAdapter(commentPicListAdapter);
        commentPicListAdapter.setList(picList);
        if (type ==1){
            commentPicListAdapter.setGoodsId(bean.getShop_id());
        }else if (type == 2){
            commentPicListAdapter.setGoodsId(luckyBean.getGoods_id());
        }

        goodsPidList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (picList != null) {
                    if (picList.size() <= position) {
                        DialogPic(3 - picList.size());
                    } else {
                        startActivity(BGAPhotoPreviewActivity.newIntent(ReserveOrderCommentActivity.this,
                                null, picList.get(position)));
                    }
                } else {
                    DialogPic(3);
                }

            }
        });

    }

    @OnClick({R.id.evaluate_goods_type_pic, R.id.tv_submit})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.evaluate_goods_type_pic:
                //是否设置匿名
                if (type == 1){
                    if (bean.isTrue()) {
                        bean.setTrue(false);
                        evaluateGoodsTypePic.setImageResource(R.drawable.no_choosed);
                        evaluateGoodsTypeText.setText("你的评论能帮助其他小伙伴呦");
                    } else {
                        bean.setTrue(true);
                        evaluateGoodsTypePic.setImageResource(R.drawable.orange_choosed_icon);
                        evaluateGoodsTypeText.setText("你的评论会以匿名的形式展现");
                    }
                }else if (type == 2){
                    if (luckyBean.isTrue()) {
                        luckyBean.setTrue(false);
                        evaluateGoodsTypePic.setImageResource(R.drawable.no_choosed);
                        evaluateGoodsTypeText.setText("你的评论能帮助其他小伙伴呦");
                    } else {
                        luckyBean.setTrue(true);
                        evaluateGoodsTypePic.setImageResource(R.drawable.orange_choosed_icon);
                        evaluateGoodsTypeText.setText("你的评论会以匿名的形式展现");
                    }
                }

                break;
            case R.id.tv_submit:
                //发表评论
                if (TextUtils.isEmpty(userContent.getText().toString().trim())) {
                    ToastUtil.showToast(ReserveOrderCommentActivity.this, "请输入商品的评价！");
                    return;
                }

                if (shop_score == -1 || logistics_score == -1 || service_score == -1) {
                    ToastUtil.showToast(ReserveOrderCommentActivity.this, "请输选择评价分数！");
                    return;
                }

                int average = (shop_score + logistics_score + service_score) / 3;

                showLoadingDialog("提交中");
                String niming;

                if (type == 1){
                    if (bean.isTrue()) {
                        niming = "1";//匿名
                    } else {
                        niming = "2";//不匿名
                    }
                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    builder.addFormDataPart("token", new PreferencesHelper(ReserveOrderCommentActivity.this).getToken())
                            .addFormDataPart("content", userContent.getText().toString().trim())
                            .addFormDataPart("score", average + "")
                            .addFormDataPart("order_id", order_id)
                            .addFormDataPart("hotel_id", bean.getHotel_id())
                            .addFormDataPart("shop_id", bean.getShop_id())
                            .addFormDataPart("anonymous", niming);


                    if (fileList != null && fileList.size() > 0) {
                        for (int p = 0; p < fileList.size(); p++) {
                            LogUtil.i("pic_path:" + fileList.get(p).getName());
                            RequestBody IDFrontBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileList.get(p));
                            builder.addFormDataPart(String.valueOf(p), fileList.get(p).getName(), IDFrontBody);
                        }
                        release(builder);
                    } else {
                        release(builder);
                    }
                }else if (type == 2){
                    if (luckyBean.isTrue()) {
                        niming = "1";//匿名
                    } else {
                        niming = "2";//不匿名
                    }
                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    builder.addFormDataPart("token", new PreferencesHelper(ReserveOrderCommentActivity.this).getToken())
                            .addFormDataPart("content", userContent.getText().toString().trim())
                            .addFormDataPart("score", average + "")
                            .addFormDataPart("order_id", order_id)
                            .addFormDataPart("goods_id", luckyBean.getGoods_id())
                            .addFormDataPart("anonymous", niming);


                    if (fileList != null && fileList.size() > 0) {
                        for (int p = 0; p < fileList.size(); p++) {
                            LogUtil.i("pic_path:" + fileList.get(p).getName());
                            RequestBody IDFrontBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileList.get(p));
                            builder.addFormDataPart(String.valueOf(p), fileList.get(p).getName(), IDFrontBody);
                        }
                        releaseLuckyBag(builder);
                    } else {
                        releaseLuckyBag(builder);
                    }
                }


                break;
        }
    }

    /**
     * 发布预定订单评论
     *
     * @param builder
     */
    private void release(MultipartBody.Builder builder) {
        HttpHelp.getInstance().create(RemoteApi.class).reserveOrderComment(builder.build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(ReserveOrderCommentActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        Log.i("XXX", baseBean.code + "  " + baseBean.message);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {
                            EventBus.getDefault().post(new ReservationOrderEvent());
                            finish();
                        } else {
                            ToastUtil.showToast(ReserveOrderCommentActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                        Log.i("XXX", throwable.getLocalizedMessage());
                        ToastUtil.showToast(ReserveOrderCommentActivity.this, "提交失败");
                    }
                });
    }


    /**
     * 福袋订单发表评论
     * @param builder
     */
    private void releaseLuckyBag(MultipartBody.Builder builder){
        HttpHelp.getInstance().create(RemoteApi.class).luckyBagOrderComment(builder.build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(ReserveOrderCommentActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        Log.i("XXX", baseBean.code + "  " + baseBean.message);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {
                            EventBus.getDefault().post(new LuckyOrderCommnetEvent());
                            finish();
                        } else {
                            ToastUtil.showToast(ReserveOrderCommentActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                        Log.i("XXX", throwable.getLocalizedMessage());
                        ToastUtil.showToast(ReserveOrderCommentActivity.this, "提交失败");
                    }
                });
    }


    /**
     * 弹框
     */
    public void DialogPic(final int count) {

        mDialog = new Dialog(ReserveOrderCommentActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(ReserveOrderCommentActivity.this).inflate(R.layout.dialog_phone_picture, null);
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
                    takePhotoFile = TakePhotoUtils.takePhoto(ReserveOrderCommentActivity.this, "nwbPhotos", REQUEST_CODE_TAKE_PHOTO);
                } catch (IOException e) {
                    ToastUtil.showToast(ReserveOrderCommentActivity.this, "拍照失败");
                    e.printStackTrace();
                }
            }
        });
        photo_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//相册
                mDialog.dismiss();
                //从相册选择
                startActivityForResult(BGAPhotoPickerActivity.newIntent(ReserveOrderCommentActivity.this, null, count, null, false), REQUEST_CODE_CHOICE_PHOTO);
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
                path = takePhotoFile.getAbsolutePath();
                picList.add(path);
                modifyAvatar(picList);
                commentPicListAdapter.setList(picList);
            }
        }

        //选择图片回调
        if (requestCode == REQUEST_CODE_CHOICE_PHOTO && resultCode == RESULT_OK) {
            try {
                List<String> listImg = BGAPhotoPickerActivity.getSelectedImages(data);
                picList.addAll(listImg);
                modifyAvatar(picList);
                commentPicListAdapter.setList(picList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩图片
     */
    private void modifyAvatar(List<String> list) {
        if (list != null && list.size() > 0) {
            fileList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Luban.with(ReserveOrderCommentActivity.this)
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
     * 删除选中图片时 收到消息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void delPic(CommentPicDeleteEvent event) {
        if (!TextUtils.isEmpty(event.getGoodsId())) {
            if (event.getPosition() < fileList.size()) {
                fileList.remove(event.getPosition());
                ToastUtil.showToast(ReserveOrderCommentActivity.this, "删除成功");
            }
        } else {
            ToastUtil.showToast(ReserveOrderCommentActivity.this, "删除失败");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBusUtil.unregister(this);
    }
}
