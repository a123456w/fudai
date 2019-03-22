package com.ruirong.chefang.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import android.widget.Toast;

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
import com.ruirong.chefang.adapter.LuckyBagPrefectureDialogAdapter;
import com.ruirong.chefang.bean.EachChildDetailsBean;
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
import cn.iwgang.countdownview.CountdownView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2017/12/29 0029
 * describe:  福袋专区
 */
public class LuckyBagPrefectureActivity extends BaseActivity {

    @BindView(R.id.bag_pic)
    Banner bagPic;
    @BindView(R.id.cv_countdownView)
    CountdownView cvCountdownView;
    @BindView(R.id.sale_name)
    TextView saleName;
    @BindView(R.id.sale_new_price)
    TextView saleNewPrice;
    @BindView(R.id.sale_old_price)
    TextView saleOldPrice;
    @BindView(R.id.sale_amount)
    TextView saleAmount;
    @BindView(R.id.detailed_information)
    RelativeLayout detailedInformation;
    @BindView(R.id.evaluate_num)
    TextView evaluateNum;
    @BindView(R.id.rl_product_comment)
    RelativeLayout rlProductComment;
    @BindView(R.id.tv_commodity_details)
    TextView tvCommodityDetails;
    @BindView(R.id.details)
    WebView details;
    @BindView(R.id.tv_showings)
    TextView tvShowings;
    private List<SpecialtyDetailsBean> lists = new ArrayList<>();
    private String blessing_id;
    private List<String> baseList = new ArrayList<>();
    private List<EachChildDetailsBean.GoodsBean> goodsList = new ArrayList<>();
    private Dialog mDialog;


    @Override
    public int getContentView() {
        return R.layout.activity_lucky_bag_prefecture;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("详情");
//        showLoadingDialog("加载中...");
        titleBar.setRightImageRes(R.drawable.share_icon);
        titleBar.setRightImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedUtil sharedUtil = new SharedUtil(LuckyBagPrefectureActivity.this);
                sharedUtil.initShared(LuckyBagPrefectureActivity.this);
            }
        });


        blessing_id = getIntent().getStringExtra("blessing_id");
        if (blessing_id == null) {
            finish();
        }

        cvCountdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                tvShowings.setText("商品已过期");
                tvShowings.setFocusable(false);
                tvShowings.setBackgroundResource(R.color.gray_text);
            }
        });
    }

    @Override
    public void getData() {
        getListDate(blessing_id);
    }

    private void getListDate(String id) {
        hideLoadingDialog();
        HttpHelp.getInstance().create(RemoteApi.class). bag_details(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<EachChildDetailsBean>>(LuckyBagPrefectureActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<EachChildDetailsBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            baseList = baseBean.data.getLoop_pics();
                            if (baseList.size() > 0 && baseList != null) {
                                bindBannerData();
                            }

                            if (0 == baseBean.data.getDeadline()) {
                                tvShowings.setText("商品已过期");
                                tvShowings.setFocusable(false);
                                tvShowings.setBackgroundResource(R.color.gray_text);
                            } else {
                                cvCountdownView.start((baseBean.data.getDeadline() * 1000));
                            }

                            saleName.setText(baseBean.data.getTitle() + " " + baseBean.data.getAttr());
                            saleNewPrice.setText(baseBean.data.getNow_price());

                            saleOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            saleOldPrice.setText("￥" + baseBean.data.getBefore_price());
                            saleAmount.setText("销量:" + baseBean.data.getSales());
                            evaluateNum.setText("(" + baseBean.data.getTs_num() + "人评价)");

                            details.loadUrl(Constants.IMG_HOST + baseBean.data.getBagdetails());
                            details.getSettings().setUseWideViewPort(true);
                            details.getSettings().setLoadWithOverviewMode(true);
                            details.getSettings().setLoadsImagesAutomatically(true); //支持自动加载图片
                            details.getSettings().setDefaultTextEncodingName("utf-8");//设置编码格式

                            goodsList = baseBean.data.getGoods();

                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                    }
                });
    }

    @OnClick({R.id.tv_showings, R.id.detailed_information, R.id.rl_product_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_showings:
                if (TextUtils.isEmpty(new PreferencesHelper(this).getToken())) {
                    ToolUtil.goToLogin(this);
                } else {
                    Intent intent1 = new Intent(LuckyBagPrefectureActivity.this, EachChildConfirmationOfOrderActivity.class);
                    intent1.putExtra("specialty_id", blessing_id);
                    startActivity(intent1);
                }

                break;
            case R.id.detailed_information:

                productParaDialog();

                break;
            case R.id.rl_product_comment:
                //评论页面
                Intent intent12 = new Intent(LuckyBagPrefectureActivity.this, EachChildCommentActivity.class);
                intent12.putExtra("specialty_id", blessing_id);
                Log.i("XX", blessing_id);
                startActivity(intent12);

                break;
        }
    }

    /**
     * 产品参数弹框
     */
    private void productParaDialog() {
        if (goodsList != null && goodsList.size() > 0) {
            if (mDialog == null) {
                mDialog = new Dialog(LuckyBagPrefectureActivity.this, R.style.ActionSheetDialogStyle);
                View inflate = LayoutInflater.from(LuckyBagPrefectureActivity.this).inflate(R.layout.dialog_specialty_details_product_parameter, null);

                ListView viewById = (ListView) inflate.findViewById(R.id.lv_parameter);
                LuckyBagPrefectureDialogAdapter adapter = new LuckyBagPrefectureDialogAdapter(viewById);
                viewById.setAdapter(adapter);

                ImageView del = (ImageView) inflate.findViewById(R.id.del);
                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });

                adapter.setData(goodsList);

                mDialog.setContentView(inflate);
                Window dialogwindow = mDialog.getWindow();
                dialogwindow.setGravity(Gravity.BOTTOM);
                WindowManager.LayoutParams lp = dialogwindow.getAttributes();
                lp.windowAnimations = R.style.PopUpBottomAnimation;
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                dialogwindow.setAttributes(lp);
                mDialog.show();
            } else {
                mDialog.show();
            }
        } else {
            ToastUtil.showToast(LuckyBagPrefectureActivity.this, "数据有误！");
        }


    }

    /**
     * 为顶部轮播添加图片
     */
    public void bindBannerData() {
        bagPic.setImages(baseList)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        String imgUrl = Constants.IMG_HOST + path.toString();
                        GlideUtil.display(LuckyBagPrefectureActivity.this, imgUrl, imageView);

                    }
                }).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                // startActivity((BGAPhotoPreviewActivity.newIntent(mContext, null, bannerList, position)));

            }
        }).start();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
