package com.ruirong.chefang.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.StringUtil;
import com.qlzx.mylibrary.widget.CircleBar;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.HotelDetailCommentAdapter;
import com.ruirong.chefang.adapter.NearbyShopDetailsChoseOneAdapter;
import com.ruirong.chefang.adapter.NearbyShopDetailsComboChooseAdapter;
import com.ruirong.chefang.bean.ConfirmationOrderBean;
import com.ruirong.chefang.bean.HotelDetailChoseOneBean;
import com.ruirong.chefang.bean.HotelDetailCommentBean;
import com.ruirong.chefang.util.CashierInputFilter;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenlipeng on 2018/1/3 0003
 * describe:  建材家居详情  详情2
 */
public class BuildingHomeDetailsActivity extends BaseActivity {


    @BindView(R.id.banner_top)
    Banner bannerTop;
    @BindView(R.id.tv_real_estate)
    TextView tvRealEstate;
    @BindView(R.id.rb_grade)
    RatingBar rbGrade;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.tv_position)
    ImageView tvPosition;
    @BindView(R.id.iv_phone)
    ImageView ivPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_company_profile)
    TextView tvCompanyProfile;
    @BindView(R.id.cb_evaluate)
    CircleBar cbEvaluate;
    @BindView(R.id.nslv_shopmall_comment)
    NoScrollListView nslvShopmallComment;
    @BindView(R.id.btn_storemoney)
    Button btnStoremoney;
    @BindView(R.id.btn_paybill)
    Button btnPaybill;
    //套餐
    private NearbyShopDetailsComboChooseAdapter hotSellProductDetailsComboChooseAdapter;
    private List<HotelDetailCommentBean> hotelDetailComboChooseBeanList = new ArrayList<>();
    //    单品
    private NearbyShopDetailsChoseOneAdapter hotelDetailChoseOneAdapter;
    private List<HotelDetailChoseOneBean> hotelDetailChoseOneBeanList = new ArrayList<>();

    //    评论
    private HotelDetailCommentAdapter hotelDetailCommentAdapter;
    private List<HotelDetailCommentBean> hotelDetailCommentBeanList = new ArrayList<>();
    private String titleName;

    private List<ConfirmationOrderBean> lists = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_building_home;
    }

    @Override
    public void initView() {
        titleName = getIntent().getStringExtra("titleName");
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);

        if (!StringUtil.isEmpty(titleName)) {
            titleBar.setTitleText(titleName);
        } else {
            titleBar.setTitleText("建材家居");
        }

        titleBar.setRightImageRes(R.drawable.ic_share);
        titleBar.setRightImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ToastUtil.showToast(NearbyShopDetailsActivity.this, "分享");
            }
        });
        initData();
        bindBannerData();
    }

    /**
     * 初始化适配器数据。
     */
    public void initData() {

        hotelDetailCommentAdapter = new HotelDetailCommentAdapter(nslvShopmallComment);
        nslvShopmallComment.setFocusable(false);
        nslvShopmallComment.setAdapter(hotelDetailCommentAdapter);


        cbEvaluate.setRoundProgressColor(Color.parseColor("#fe9d00"));

    }

    @Override
    public void getData() {

        for (int i = 0; i < 5; i++) {
//            评论
            HotelDetailCommentBean hotelDetailCommentBean = new HotelDetailCommentBean();
            hotelDetailCommentBeanList.add(hotelDetailCommentBean);
        }

        hotelDetailCommentAdapter.setData(hotelDetailCommentBeanList);
    }

    /**
     * 为顶部轮播添加图片
     */
    public void bindBannerData() {
        List<String> baseList = new ArrayList<>();
        baseList.add("http://n.sinaimg.cn/news/transform/w1000h500/20171212/xH_r-fypnsip9129414.jpg");
        baseList.add("http://n.sinaimg.cn/news/transform/w1000h500/20171208/Fbej-fypnsin8780163.jpg");
        baseList.add("http://n.sinaimg.cn/news/transform/w1000h500/20171212/xH_r-fypnsip9129414.jpg");
        baseList.add("http://n.sinaimg.cn/news/transform/w1000h500/20171208/Fbej-fypnsin8780163.jpg");
        bannerTop.setImages(baseList)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        String imgUrl = (String) path;
                        GlideUtil.display(BuildingHomeDetailsActivity.this, imgUrl, imageView);

                    }
                }).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                // startActivity((BGAPhotoPreviewActivity.newIntent(mContext, null, bannerList, position)));

            }
        }).start();


    }


    @OnClick({R.id.tv_position, R.id.tv_address, R.id.btn_storemoney, R.id.btn_paybill})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_position:
            case R.id.tv_address:
                Intent intent1 = new Intent(BuildingHomeDetailsActivity.this, MapActivity.class);
                startActivity(intent1);


                break;
            case R.id.btn_storemoney:
                storeValueDialog();

                break;
            case R.id.btn_paybill:
                minePayBillDialog();


                break;


        }
    }


    private void storeValueDialog() {
        final Dialog mDialog = new Dialog(BuildingHomeDetailsActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(BuildingHomeDetailsActivity.this).inflate(R.layout.nearby_shop_store_value_dialog, null);

        View tv_submit = inflate.findViewById(R.id.tv_submit);
        EditText tvMoney = inflate.findViewById(R.id.tv_peice);
        //设置输入金额的格式。
        InputFilter[] inputFilter = {new CashierInputFilter()};
        tvMoney.setFilters(inputFilter);
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent1 = new Intent(BuildingHomeDetailsActivity.this, StoredValueActivity.class);
                startActivity(intent1);

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
    }

    /**
     * w我要购买弹框
     */
    private void minePayBillDialog() {
        final Dialog mDialog = new Dialog(BuildingHomeDetailsActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(BuildingHomeDetailsActivity.this).inflate(R.layout.my_pay_bill__dialog, null);

        inflate.findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(BuildingHomeDetailsActivity.this, StoredValueActivity.class);
                startActivity(intent1);
//                买金豆
//                RechargeActivity
//                立即预定
//                ImmediateReservationActivity
//                设置支付密码
//                SettingPaymentPasswordActivity

                // TODO: 2018/1/3 0003   这的逻辑处理 怎么处理


                mDialog.dismiss();
            }
        });
        inflate.findViewById(R.id.tv_pay_fortunella_venosa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                跳到充值页面
                Intent intent1 = new Intent(BuildingHomeDetailsActivity.this, RechargeActivity.class);
                startActivity(intent1);

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
    }
}
