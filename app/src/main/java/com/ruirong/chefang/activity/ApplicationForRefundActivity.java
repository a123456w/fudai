package com.ruirong.chefang.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.TakePhotoUtils;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollGridView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.CommentPicListAdapter;
import com.ruirong.chefang.adapter.ReasonAdapter;
import com.ruirong.chefang.bean.SelectedStateBean;
import com.ruirong.chefang.bean.SpecialtyOrderDetailBean;
import com.ruirong.chefang.event.PendingPaymentEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
 * Created by Administrator on 2018/3/31.
 */

public class ApplicationForRefundActivity extends BaseActivity {
    @BindView(R.id.goods_name)
    TextView goodsName;
    @BindView(R.id.goods_type)
    TextView goodsType;
    @BindView(R.id.goods_num)
    TextView goodsNum;
    @BindView(R.id.tv_goods_state_pic)
    ImageView tvGoodsStatePic;
    @BindView(R.id.tv_goods_state)
    RelativeLayout tvGoodsState;
    @BindView(R.id.reasons_for_refunds_pic)
    ImageView reasonsForRefundsPic;
    @BindView(R.id.tv_reasons_for_refunds)
    RelativeLayout tvReasonsForRefunds;
    @BindView(R.id.goods_price)
    TextView goodsPrice;
    @BindView(R.id.et_refund_amount)
    EditText etRefundAmount;
    @BindView(R.id.tv_explain)
    EditText tvExplain;
    @BindView(R.id.goods_pic_list)
    NoScrollGridView goodsPicList;
    @BindView(R.id.tv_tijioa)
    TextView tvTijioa;
    @BindView(R.id.views)
    View views;
    @BindView(R.id.tv_goods_state_content)
    TextView tvGoodsStateContent;
    @BindView(R.id.tv_reasons_for_content)
    TextView tvReasonsForContent;
    @BindView(R.id.goods_pic)
    ImageView goodsPic;
    private int type;//1是仅退款 2退货退款
    private Dialog mDialogT;
    private TextView no, yes;
    private ListView id_reason;
    private List<SelectedStateBean> reasonList = new ArrayList<>();
    private String goods_reasonText;
    private String Reasons_reasonText;
    private ReasonAdapter reasonAdapter;

    private final int REQUEST_CODE_TAKE_PHOTO = 100;
    private final int REQUEST_CODE_CHOICE_PHOTO = 200;
    private final int REQUEST_CODE_MODIFY_NICKNAME = 300;
    private static final int PAGE_INTO_LIVENESS = 400;
    private final int REQUEST_CODE_UPLOAD_ID = 500;
    private File takePhotoFile;
    private Dialog mDialog;
    private CommentPicListAdapter commentPicListAdapter;
    private List<String> listPic = new ArrayList<>();
    private String goodsContent;
    private SpecialtyOrderDetailBean.GoodsInfoBean goodsInfoBean;
    private List<File> fileList = new ArrayList<>();
    private MultipartBody.Builder builder;
    private String frontText;
    private float goodsM;
    private int digits = 2;
    private boolean isMX = false;
    private String number_bh;
    private int numC = 0;

    @Override
    public int getContentView() {
        return R.layout.activity_application_for_refund;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("退款详情");

        type = getIntent().getIntExtra("type", -1);
        goodsContent = getIntent().getStringExtra("goodsContent");
        number_bh = getIntent().getStringExtra("number_bh");
        if (goodsContent != null && number_bh != null) {
            goodsInfoBean = new Gson().fromJson(goodsContent, SpecialtyOrderDetailBean.GoodsInfoBean.class);
            if (goodsInfoBean == null) {
                finish();
            } else {

                GlideUtil.display(ApplicationForRefundActivity.this,
                        Constants.IMG_HOST + goodsInfoBean.getPic(), goodsPic);

                if (goodsInfoBean.getAttr() != null && goodsInfoBean.getAttr().size() > 0) {
                    goodsType.setText(goodsInfoBean.getAttr().get(0));
                }

                goodsNum.setText("￥" + goodsInfoBean.getNow_price() + "  x" + goodsInfoBean.getNum());
                if (goodsInfoBean.getNow_price() != null && goodsInfoBean.getNum() != null) {
                    goodsM = Float.parseFloat(goodsInfoBean.getNow_price()) * Float.parseFloat(goodsInfoBean.getNum());
                }
                goodsPrice.setText("￥" + goodsM);
                SpannableString s = new SpannableString("最多￥" + goodsM + "，含发货邮费￥0.00");//这里输入自己想要的提示文字
                etRefundAmount.setHint(s);
                etRefundAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


                etRefundAmount.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //删除“.”后面超过2位后的数据
                        if (s.toString().contains(".")) {
                            if (s.length() - 1 - s.toString().indexOf(".") > digits) {
                                s = s.toString().subSequence(0,
                                        s.toString().indexOf(".") + digits + 1);
                                etRefundAmount.setText(s);
                                etRefundAmount.setSelection(s.length()); //光标移到最后
                            }
                        }
                        //如果"."在起始位置,则起始位置自动补0
                        if (s.toString().trim().substring(0).equals(".")) {
                            s = "0" + s;
                            etRefundAmount.setText(s);
                            etRefundAmount.setSelection(2);
                        }

                        //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                        if (s.toString().startsWith("0")
                                && s.toString().trim().length() > 1) {
                            if (!s.toString().substring(1, 2).equals(".")) {
                                etRefundAmount.setText(s.subSequence(0, 1));
                                etRefundAmount.setSelection(1);
                                return;
                            }
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!TextUtils.isEmpty(s.toString())) {
                            if (goodsM >= Float.parseFloat(s.toString().trim())) {
                                isMX = false;
                            } else {
                                isMX = true;
                                ToastUtil.showToast(ApplicationForRefundActivity.this, "最多可退款项" + goodsM);
                            }
                        }
                    }
                });

            }


        } else {
            finish();
        }
        if (type != -1) {
            if (1 == type) {
                tvGoodsState.setVisibility(View.GONE);
                views.setVisibility(View.GONE);
            }
        } else {
            finish();
        }

        commentPicListAdapter = new CommentPicListAdapter(ApplicationForRefundActivity.this);
        goodsPicList.setAdapter(commentPicListAdapter);

        goodsPicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listPic.size() <= position) {
                    DialogPic();
                } else {
                    startActivity(BGAPhotoPreviewActivity.newIntent(ApplicationForRefundActivity.this,
                            null, listPic.get(position)));
                }
            }
        });

    }

    @Override
    public void getData() {

    }


    @OnClick({R.id.tv_goods_state, R.id.tv_reasons_for_refunds, R.id.tv_tijioa})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_goods_state:
                storeValueDialog(1);
                break;
            case R.id.tv_reasons_for_refunds:
                storeValueDialog(2);
                break;
            case R.id.tv_tijioa:

                if (2 == type) {
                    if (TextUtils.isEmpty(goods_reasonText)) {
                        ToastUtil.showToast(ApplicationForRefundActivity.this, "请选择货物状态");
                        return;
                    }
                }

                if (TextUtils.isEmpty(Reasons_reasonText)) {
                    ToastUtil.showToast(ApplicationForRefundActivity.this, "请选择退款原因");
                    return;
                }

                if (TextUtils.isEmpty(etRefundAmount.getText().toString().trim())) {
                    ToastUtil.showToast(ApplicationForRefundActivity.this, "请选择退款金额");
                    return;
                }

                if (isMX) {
                    ToastUtil.showToast(ApplicationForRefundActivity.this, "最多可退款项" + goodsM);
                    return;
                }

                builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                builder.addFormDataPart("token", new PreferencesHelper(ApplicationForRefundActivity.this).getToken())
                        .addFormDataPart("tui_price", etRefundAmount.getText().toString().trim())
                        .addFormDataPart("tui_content", Reasons_reasonText)
                        .addFormDataPart("tui_desc", tvExplain.getText().toString().trim())
                        .addFormDataPart("order_id", number_bh)
                        .addFormDataPart("goods_id", goodsInfoBean.getId());
                Log.i("XXX", number_bh + " " + goodsInfoBean.getId());
                if (type == 2) {
                    if (goods_reasonText.equals("未收到货")) {
                        Log.i("XXX", "aaaaaaaaaaaaaaaaaaaaa");
                        builder.addFormDataPart("tui_huo_status", "1");
                    } else {
                        Log.i("XXX", "bbbbbbbbbbbbbbbbbbbbbbb");
                        builder.addFormDataPart("tui_huo_status", "2");
                    }

                }

                applyRefund(builder);


                break;
        }
    }

    private void applyRefund(MultipartBody.Builder builder) {

        for (int i = 0; i < listPic.size(); i++) {
            modifyAvatar(listPic.get(i));
        }

        if (numC == listPic.size()) {
            for (int k = 0; k < fileList.size(); k++) {
                RequestBody IDFrontBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileList.get(k));
                builder.addFormDataPart("file" + String.valueOf(k), fileList.get(k).getName(), IDFrontBody);
            }
        }


        HttpHelp.getInstance().create(RemoteApi.class).applyRefund(builder.build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        Log.i("XXX", baseBean.code + "  " + baseBean.message);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {
                            EventBus.getDefault().post(new PendingPaymentEvent());
                            finish();
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(ApplicationForRefundActivity.this);
                        }
                        ToastUtil.showToast(ApplicationForRefundActivity.this, baseBean.message);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                        Log.i("XXX", throwable.getLocalizedMessage());
                        ToastUtil.showToast(ApplicationForRefundActivity.this, "提交失败");
                    }
                });
    }


    /**************** 上传凭证 ******************/


    /**
     * 弹框
     */
    public void DialogPic() {
        mDialog = new Dialog(ApplicationForRefundActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(ApplicationForRefundActivity.this).inflate(R.layout.dialog_phone_picture, null);

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
                    takePhotoFile = TakePhotoUtils.takePhoto(ApplicationForRefundActivity.this, "nwbPhotos", REQUEST_CODE_TAKE_PHOTO);
                } catch (IOException e) {
                    ToastUtil.showToast(ApplicationForRefundActivity.this, "拍照失败");
                    e.printStackTrace();
                }
            }
        });
        photo_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//相册
                mDialog.dismiss();
                //从相册选择
                startActivityForResult(BGAPhotoPickerActivity.newIntent(ApplicationForRefundActivity.this, null, 3 - listPic.size(), null, false), REQUEST_CODE_CHOICE_PHOTO);
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
                listPic.add(path);
                commentPicListAdapter.setList(listPic);

            }
        }

        //选择图片回调
        if (requestCode == REQUEST_CODE_CHOICE_PHOTO && resultCode == RESULT_OK) {
            try {
                List<String> listImg = BGAPhotoPickerActivity.getSelectedImages(data);
                listPic.addAll(listImg);
                commentPicListAdapter.setList(listPic);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩图片
     */
    private void modifyAvatar(String path) {
        Luban.with(ApplicationForRefundActivity.this)
                .load(new File(path))//传人要压缩的图片
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        //  压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        //  压缩成功后调用，返回压缩后的图片文件
                        fileList.add(file);
                        numC++;
                    }

                    @Override
                    public void onError(Throwable e) {
                        //  当压缩过程出现问题时调用
                    }
                }).launch();    //启动压缩

    }


    /**
     * 退款原因
     */

    private void storeValueDialog(final int types) {
        mDialogT = new Dialog(ApplicationForRefundActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(ApplicationForRefundActivity.this).inflate(R.layout.cancel_reason, null);

        initDialogView(inflate, types);

        id_reason.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setFalse();
                reasonList.get(position).setTrue(true);
                if (1 == types) {
                    goods_reasonText = reasonList.get(position).getTitle();
                    tvGoodsStateContent.setText(goods_reasonText);
                } else if (2 == types) {
                    Reasons_reasonText = reasonList.get(position).getTitle();
                    tvReasonsForContent.setText(Reasons_reasonText);
                }
                reasonAdapter.notifyDataSetChanged();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogT.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (1 == types) {
                    if (TextUtils.isEmpty(goods_reasonText)) {
                        ToastUtil.showToast(ApplicationForRefundActivity.this, "请选择货物状态");
                    } else {
                        mDialogT.dismiss();
                    }
                } else if (2 == types) {
                    if (TextUtils.isEmpty(Reasons_reasonText)) {
                        ToastUtil.showToast(ApplicationForRefundActivity.this, "请选择退款原因");
                    } else {
                        mDialogT.dismiss();
                    }
                }
            }
        });

        mDialogT.setContentView(inflate);
        Window dialogwindow = mDialogT.getWindow();
        dialogwindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogwindow.setAttributes(lp);
        mDialogT.show();
    }

    private void initDialogView(View inflate, int types) {
        no = (TextView) inflate.findViewById(R.id.no);
        yes = (TextView) inflate.findViewById(R.id.yes);
        id_reason = (ListView) inflate.findViewById(R.id.id_reason);

        reasonList.clear();

        if (1 == types) {
            for (int i = 0; i < 2; i++) {
                SelectedStateBean selectedStateBean = new SelectedStateBean();
                selectedStateBean.setTrue(false);
                switch (i) {
                    case 0:
                        selectedStateBean.setTitle("未收到货");
                        break;
                    case 1:
                        selectedStateBean.setTitle("已收到货");
                        break;
                }
                reasonList.add(selectedStateBean);
            }
        } else if (2 == types) {
            for (int i = 0; i < 6; i++) {
                SelectedStateBean selectedStateBean = new SelectedStateBean();
                selectedStateBean.setTrue(false);
                switch (i) {
                    case 0:
                        selectedStateBean.setTitle("七天无理由退换货");
                        break;
                    case 1:
                        selectedStateBean.setTitle("退运费");
                        break;
                    case 2:
                        selectedStateBean.setTitle("发票问题");
                        break;
                    case 3:
                        selectedStateBean.setTitle("拍错了");
                        break;
                    case 4:
                        selectedStateBean.setTitle("卖家发错货");
                        break;
                    case 5:
                        selectedStateBean.setTitle("卖家服务态度不好");
                        break;
                }
                reasonList.add(selectedStateBean);
            }
        }

        reasonAdapter = new ReasonAdapter(id_reason);
        id_reason.setAdapter(reasonAdapter);
        reasonAdapter.setData(reasonList);

    }

    private void setFalse() {
        for (int i = 0; i < reasonList.size(); i++) {
            reasonList.get(i).setTrue(false);
        }
    }

}
