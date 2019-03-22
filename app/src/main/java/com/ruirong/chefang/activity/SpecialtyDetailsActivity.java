package com.ruirong.chefang.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.SpecialtyDetailsProductParameterAdapter;
import com.ruirong.chefang.bean.ObjectBean;
import com.ruirong.chefang.bean.SpecialtyDetailsBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.SharedUtil;
import com.ruirong.chefang.util.ToolUtil;
import com.umeng.socialize.UMShareAPI;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2017/12/29 0029
 * describe:  特产详情
 */
public class SpecialtyDetailsActivity extends BaseActivity {
    @BindView(R.id.banner_top)
    Banner bannerTop;
    @BindView(R.id.specialty_name)
    TextView specialtyName;
    @BindView(R.id.specialty_price)
    TextView specialtyPrice;
    @BindView(R.id.specialty_oldPrice)
    TextView specialtyOldPrice;
    @BindView(R.id.specialty_sales)
    TextView specialtySales;
    @BindView(R.id.detailed_information)
    RelativeLayout detailedInformation;
    @BindView(R.id.rl_product_comment)
    RelativeLayout rlProductComment;
    @BindView(R.id.tv_commodity_details)
    TextView tvCommodityDetails;
    @BindView(R.id.tv_collection)
    TextView tvCollection;
    @BindView(R.id.tv_customer)
    TextView tvCustomer;
    @BindView(R.id.tv_showings)
    TextView tvShowings;
    @BindView(R.id.specialty_evaluate)
    TextView specialtyEvaluate;
    @BindView(R.id.web_pic)
    WebView webPic;
    @BindView(R.id.tv_add_shop)
    TextView tvAddShop;

    private String specialty_id;
    private SpecialtyDetailsBean specialtyDetailsBean;

    private List<String> carouselList = new ArrayList<>();//轮播图
    private List<SpecialtyDetailsBean.GoodsBean> lists = new ArrayList<>();


    @Override
    public int getContentView() {
        return R.layout.activity_specialty_details;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("福返特产");
        titleBar.setRightImageRes(R.drawable.share_icon);
        titleBar.setRightImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedUtil sharedUtil=new SharedUtil(SpecialtyDetailsActivity.this);
                sharedUtil.initShared(SpecialtyDetailsActivity.this);
            }
        });


        specialty_id = getIntent().getStringExtra("Specialty_id");
        Log.i("XXX", specialty_id);
        if (specialty_id == null) {
            ToastUtil.showToast(SpecialtyDetailsActivity.this, "选择错误！");
            finish();
        }

    }
    /*分享所需*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void getData() {
        if (TextUtils.isEmpty(new PreferencesHelper(SpecialtyDetailsActivity.this).getToken())) {
//            ToolUtil.goToLogin(SpecialtyDetailsActivity.this);
            getListDate(specialty_id, null);
        } else {
            getListDate(specialty_id, new PreferencesHelper(SpecialtyDetailsActivity.this).getToken());
        }
    }

    /**
     * 获取列表数据
     *
     * @param id
     * @param token
     */
    private void getListDate(String id, String token) {
        Log.i("XXX",id);
//        showLoadingDialog("加载中...");
        HttpHelp.getInstance().create(RemoteApi.class).Specialdetails(id, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<SpecialtyDetailsBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<SpecialtyDetailsBean> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {
                            specialtyDetailsBean = baseBean.data;
                            if (specialtyDetailsBean != null) {
                                carouselList = specialtyDetailsBean.getLb();
                                if (carouselList != null && carouselList.size() > 0) {
                                    bindBannerData();
                                }
                                specialtyName.setText(specialtyDetailsBean.getName());
                                specialtyPrice.setText("￥" + specialtyDetailsBean.getNow_price());
                                specialtyOldPrice.setText("￥" + specialtyDetailsBean.getBefore_price());
                                specialtyOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                specialtySales.setText("销量：" + specialtyDetailsBean.getXnum());
                                specialtyEvaluate.setText("(" + specialtyDetailsBean.getPinglun() + "人评价)");

                                lists = specialtyDetailsBean.getGoods();

                                if (0 == specialtyDetailsBean.getCollection()) {
                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_collect);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    tvCustomer.setCompoundDrawables(null, drawable, null, null);
                                } else if (1 == specialtyDetailsBean.getCollection()) {
                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_collected);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    tvCustomer.setCompoundDrawables(null, drawable, null, null);
                                }

                                webPic.loadUrl(Constants.IMG_HOST + specialtyDetailsBean.getYieldd());
                                webPic.getSettings().setUseWideViewPort(true);
                                webPic.getSettings().setLoadWithOverviewMode(true);
                                webPic.getSettings().setLoadsImagesAutomatically(true); //支持自动加载图片
                                webPic.getSettings().setDefaultTextEncodingName("utf-8");//设置编码格式

                            }
                        }else if (baseBean.code==4){
                            ToolUtil.loseToLogin(SpecialtyDetailsActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                    }
                });
    }

    @OnClick({R.id.tv_showings, R.id.detailed_information, R.id.tv_collection, R.id.rl_product_comment,
            R.id.tv_customer, R.id.tv_add_shop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_showings:
                if (TextUtils.isEmpty(new PreferencesHelper(SpecialtyDetailsActivity.this).getToken())) {
                    ToolUtil.goToLogin(SpecialtyDetailsActivity.this);
                } else {
                    Intent intent1 = new Intent(SpecialtyDetailsActivity.this, LuckybagPrefectureConfirmOrderActivity.class);
                    intent1.putExtra("specialty_id", specialty_id);
                    startActivity(intent1);
                }

                break;
            case R.id.detailed_information:
                productParaDialog();

                break;
            case R.id.tv_customer:
                if (specialtyDetailsBean != null) {
                    perhapsCollection(new PreferencesHelper(SpecialtyDetailsActivity.this).getToken(), specialtyDetailsBean.getId());
                } else {
                    ToastUtil.showToast(SpecialtyDetailsActivity.this, "收藏失败");
                }

                break;
            case R.id.tv_collection:
                WebActivity.startActivity(this, "客服热线", com.ruirong.chefang.common.Constants.SEARVICE);
                break;
            case R.id.rl_product_comment:
                if (specialty_id != null) {
                    Intent intent123 = new Intent(SpecialtyDetailsActivity.this, EvaluateActivity.class);
                    intent123.putExtra("specialty_id", specialty_id);
                    startActivity(intent123);
                }

                break;
            case R.id.tv_add_shop:

                shopping(new PreferencesHelper(SpecialtyDetailsActivity.this).getToken(), specialty_id, 1);

                Log.i("XXX", specialty_id);

                break;
        }

    }

    private void shopping(String token, String specialty_id, int i) {
        HttpHelp.getInstance().create(RemoteApi.class).shopping(token, specialty_id, i)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ObjectBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<ObjectBean> baseBean) {
                        super.onNext(baseBean);
                        Log.i("XXX", baseBean.code + "  " + baseBean.message);
                        if (baseBean.code == 0) {

                        }
                        else if (baseBean.code==4){
                            ToolUtil.loseToLogin(SpecialtyDetailsActivity.this);
                        }
                        ToastUtil.showToast(SpecialtyDetailsActivity.this, baseBean.message);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 收藏或者取消
     */
    private void perhapsCollection(String token, String id) {
        HttpHelp.getInstance().create(RemoteApi.class).collection(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ObjectBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<ObjectBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if ("1".equals(baseBean.data.getStatus())) {
                                Drawable drawable = getResources().getDrawable(R.drawable.ic_collected);
                                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                tvCustomer.setCompoundDrawables(null, drawable, null, null);
                            } else if ("0".equals(baseBean.data.getStatus())) {
                                Drawable drawable = getResources().getDrawable(R.drawable.ic_collect);
                                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                tvCustomer.setCompoundDrawables(null, drawable, null, null);
                            }
                        } else if (baseBean.code == 1) {

                        } else if (baseBean.code==4){
                            ToolUtil.loseToLogin(SpecialtyDetailsActivity.this);
                        }
                        ToastUtil.showToast(SpecialtyDetailsActivity.this, baseBean.message);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 为顶部轮播添加图片
     */
    public void bindBannerData() {
        bannerTop.setImages(carouselList)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        String imgUrl = Constants.IMG_HOST + path.toString();
                        GlideUtil.display(SpecialtyDetailsActivity.this, imgUrl, imageView);

                    }
                }).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                // startActivity((BGAPhotoPreviewActivity.newIntent(CommodityDetailsActivity.this, null,  bannerList, position)));
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                List bGAPhotoPreviewBaseList = new ArrayList<>();

                for (int i = 0; i < carouselList.size(); i++) {
                    bGAPhotoPreviewBaseList.add(Constants.IMG_HOST + carouselList.get(i));
                }

                startActivity((BGAPhotoPreviewActivity.newIntent(SpecialtyDetailsActivity.this, null, (ArrayList<String>) bGAPhotoPreviewBaseList, position)));


                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                // startActivity((BGAPhotoPreviewActivity.newIntent(mContext, null, bannerList, position)));
            }
        }).start();
    }

    /**
     * 产品参数弹框
     */
    private void productParaDialog() {
        if (lists != null && lists.size() > 0) {
            final Dialog mDialog = new Dialog(SpecialtyDetailsActivity.this, R.style.ActionSheetDialogStyle);
            View inflate = LayoutInflater.from(SpecialtyDetailsActivity.this).inflate(R.layout.dialog_specialty_details_product_parameter, null);
            ImageView del = (ImageView) inflate.findViewById(R.id.del);
            ListView viewById = (ListView) inflate.findViewById(R.id.lv_parameter);
            SpecialtyDetailsProductParameterAdapter adapter = new SpecialtyDetailsProductParameterAdapter(viewById);
            viewById.setAdapter(adapter);

            adapter.setData(lists);

            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });

            mDialog.setContentView(inflate);
            Window dialogwindow = mDialog.getWindow();
            dialogwindow.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogwindow.getAttributes();
            lp.windowAnimations = R.style.PopUpBottomAnimation;
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialogwindow.setAttributes(lp);
            mDialog.show();
        } else {
            ToastUtil.showToast(SpecialtyDetailsActivity.this, "暂无信息！");
        }

    }
}
