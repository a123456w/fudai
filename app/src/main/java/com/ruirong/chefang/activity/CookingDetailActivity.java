package com.ruirong.chefang.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.CircleBar;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.ShopHeadlineCommentAdapter;
import com.ruirong.chefang.adapter.ShopReserveAdapter;
import com.ruirong.chefang.adapter.SingleProductAdapter;
import com.ruirong.chefang.bean.ComboBean;
import com.ruirong.chefang.bean.ConfirmationOrderBean;
import com.ruirong.chefang.bean.FuBackHotelDetailsBean;
import com.ruirong.chefang.bean.StorgeBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.CashierInputFilter;
import com.ruirong.chefang.util.SharedUtil;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.view.MyRatingBar;
import com.ruirong.chefang.widget.MarqueTextView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 美食详情
 * Created by dillon on 2018/5/5.
 */

public class CookingDetailActivity extends BaseActivity implements OnRVItemClickListener, OnItemChildClickListener, CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener {

    @BindView(R.id.banner_top)
    Banner bannerTop;
    @BindView(R.id.tv_table_name)
    MarqueTextView tvTableName;
    @BindView(R.id.tv_browsernum)
    TextView tvBrowsernum;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rating_bar)
    MyRatingBar ratingBar;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.per_capita)
    TextView perCapita;
    @BindView(R.id.address_details)
    TextView addressDetails;
    @BindView(R.id.call_phone)
    ImageView callPhone;
    @BindView(R.id.rv_taochan)
    NoScrollListView rvTaochan;
    @BindView(R.id.rl_more)
    RelativeLayout rlMore;
    @BindView(R.id.tv_ordercooking)
    TextView tvOrderCooking;
    @BindView(R.id.rv_danping)
    RecyclerView rvDanping;
    @BindView(R.id.tv_businessarea)
    TextView tvBusinessarea;
    @BindView(R.id.rl_all_product)
    RelativeLayout rlAllProduct;
    @BindView(R.id.comment_num)
    TextView commentNum;
    @BindView(R.id.cb_evaluate)
    CircleBar cbEvaluate;
    @BindView(R.id.nslv_shopmall_comment)
    NoScrollListView nslvShopmallComment;
    @BindView(R.id.list_rl)
    LinearLayout listRl;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.can_content_view)
    NestedScrollView canContentView;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.btn_storemoney)
    Button btnStoremoney;
    @BindView(R.id.btn_paybill)
    Button btnPaybill;
    private Boolean isChooseCombo = true;
    private List<String> baseList = new ArrayList<>();

    private FuBackHotelDetailsBean fuBackHotelDetailsBean;
    //套餐
    private ShopReserveAdapter shopReserveAdapter;
    private List<ComboBean> packageList = new ArrayList<>();
    //    单品
    private SingleProductAdapter singleProductAdapter;
    private List<FuBackHotelDetailsBean.GoodsBean> goodsList = new ArrayList<>();
    //    评论
    private ShopHeadlineCommentAdapter hotelDetailCommentAdapter;
    private List<FuBackHotelDetailsBean.PinglunaBean> pinglunaList = new ArrayList<>();

    private List<ConfirmationOrderBean> lists = new ArrayList<>();
    private String shop_id;
    private AlertDialog.Builder normalDialog;
    private String myMobile = "";
    private int maxLen = 10;
    private int page = 1;

    @Override
    public int getContentView() {
        return R.layout.activity_cookingdetail;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("商家详情");
        titleBar.setRightImageRes(R.drawable.ic_share);
        titleBar.setRightImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(new PreferencesHelper(CookingDetailActivity.this).getToken())) {
                    ToolUtil.goToLogin(CookingDetailActivity.this);
                } else {
                    SharedUtil sharedUtil = new SharedUtil(CookingDetailActivity.this);
                    sharedUtil.initShared(CookingDetailActivity.this);
                }
            }
        });


        shop_id = getIntent().getStringExtra("shop_id");

        Log.i("XXX", shop_id);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setRefreshEnabled(false);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");

//        showLoadingDialog("加载中...");
        initData();
        bindBannerData();
    }

    /**
     * 初始化适配器数据。
     */
    public void initData() {
        //套餐的
        //套餐的
        shopReserveAdapter = new ShopReserveAdapter(rvTaochan);
        rvTaochan.setFocusable(false);
        // rvTaochan.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        rvTaochan.setAdapter(shopReserveAdapter);
        rvTaochan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if ("1".equals(shopReserveAdapter.getData().get(position).getMoban())) {
                    if (TextUtils.isEmpty(new PreferencesHelper(CookingDetailActivity.this).getToken())) {
                        ToolUtil.goToLogin(CookingDetailActivity.this);
                    } else {
                        Intent intent1 = new Intent(CookingDetailActivity.this, EntertainmentAffirmOrderActivity.class);
                        intent1.putExtra("shop_id", shop_id);
                        intent1.putExtra("package_id", shopReserveAdapter.getData().get(position).getId());
                        startActivity(intent1);
                    }
                } else if ("2".equals(shopReserveAdapter.getData().get(position).getMoban())) {
                    if (TextUtils.isEmpty(new PreferencesHelper(CookingDetailActivity.this).getToken())) {
                        ToolUtil.goToLogin(CookingDetailActivity.this);
                    } else {
                        Intent intent = new Intent(CookingDetailActivity.this, CommodityConfirmationOrderActivity.class);
                        intent.putExtra("shop_id", shop_id);
                        intent.putExtra("package_id", shopReserveAdapter.getData().get(position).getId());
                        startActivity(intent);
                    }
                }
            }
        });
        //单品
        singleProductAdapter = new SingleProductAdapter(rvDanping);
        rvDanping.setFocusable(false);
        rvDanping.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        rvDanping.setAdapter(singleProductAdapter);
        singleProductAdapter.setOnRVItemClickListener(this);

        //评论
        hotelDetailCommentAdapter = new ShopHeadlineCommentAdapter(nslvShopmallComment);
        nslvShopmallComment.setFocusable(false);
        nslvShopmallComment.setAdapter(hotelDetailCommentAdapter);

        //环形评分
        cbEvaluate.setRoundProgressColor(Color.parseColor("#fe9d00"));
    }

    @Override
    public void getData() {

        hotelInfo(shop_id, new PreferencesHelper(CookingDetailActivity.this).getLatitude(),
                new PreferencesHelper(CookingDetailActivity.this).getLongTitude(), page);

    }


    private void hotelInfo(String id, String lat, String lng, final int limit) {
        Log.i("XXX", id + " " + lat + " " + lng + " " + limit);
        HttpHelp.getInstance().create(RemoteApi.class).hotelInfo(id, lat, lng, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<FuBackHotelDetailsBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<FuBackHotelDetailsBean> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {
                            fuBackHotelDetailsBean = baseBean.data;
                            if (fuBackHotelDetailsBean != null) {
                                baseList = fuBackHotelDetailsBean.getLunbo();
                                if (baseList != null && baseList.size() > 0) {
                                    bindBannerData();
                                }

                                tvTableName.setText(fuBackHotelDetailsBean.getGonggao());

                                tvName.setText(fuBackHotelDetailsBean.getSp_name());
                                if (fuBackHotelDetailsBean.getPerpeo() != null && !"".equals(fuBackHotelDetailsBean.getPerpeo())) {
                                    perCapita.setText("人均 : ￥" + fuBackHotelDetailsBean.getPerpeo());
                                }

                                addressDetails.setText(fuBackHotelDetailsBean.getSp_address());
                                if (fuBackHotelDetailsBean.getDp_grade() != null) {
                                    ratingBar.setRating(Float.parseFloat(fuBackHotelDetailsBean.getDp_grade()));
                                    tvScore.setText(fuBackHotelDetailsBean.getDp_grade() + "分");
                                }

                                if (baseBean.data.getBrowse() == null) {
                                    tvBrowsernum.setText("0人浏览");
                                } else {
                                    tvBrowsernum.setText(baseBean.data.getBrowse() + "人浏览");
                                }

                                if (baseBean.data.getMobile() != null) {
                                    myMobile = baseBean.data.getMobile();
                                }
                                if (fuBackHotelDetailsBean.getDp_grade() != null) {
                                    cbEvaluate.setProgress(Float.parseFloat(fuBackHotelDetailsBean.getDp_grade()));
                                }
/*

                                packageList = baseBean.data.getPackageX();
                                if (packageList != null && packageList.size() > 0) {
                                    shopReserveAdapter.setData(packageList);
                                }
*/
                  /*              packageList = baseBean.data.;
                                if (packageList != null && packageList.size() > 0) {
                                    shopReserveAdapter.setData(packageList);
                                }
*/
                                goodsList = baseBean.data.getGoods();
                                if (goodsList != null && goodsList.size() > 0) {
                                    singleProductAdapter.setData(goodsList);
                                }

                                commentNum.setText("用户评价（" + baseBean.data.getPl_num() + "）");

                                pinglunaList = baseBean.data.getPingluna();
                                if (limit == 1) {
                                    if (pinglunaList != null && pinglunaList.size() > 0) {
                                        refresh.setLoadMoreEnabled(true);
                                        listRl.setVisibility(View.VISIBLE);
                                        rlEmpty.setVisibility(View.GONE);
                                        hotelDetailCommentAdapter.setData(pinglunaList);
                                    } else {
                                        refresh.setLoadMoreEnabled(false);
                                        listRl.setVisibility(View.GONE);
                                        rlEmpty.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    if (pinglunaList != null && pinglunaList.size() > 0) {
                                        hotelDetailCommentAdapter.addMoreData(pinglunaList);
                                        refresh.setLoadMoreEnabled(true);
                                    } else {
                                        ToastUtil.showToast(CookingDetailActivity.this, getResources().getString(R.string.no_more));
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                    }
                });
    }

    /**
     * 为顶部轮播添加图片
     */
    public void bindBannerData() {
        bannerTop.setImages(baseList)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        String imgUrl = (String) path;
                        GlideUtil.display(CookingDetailActivity.this, Constants.IMG_HOST + imgUrl, imageView);

                    }
                }).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                // startActivity((BGAPhotoPreviewActivity.newIntent(mContext, null, bannerList, position)));
                List bGAPhotoPreviewBaseList = new ArrayList<>();

                for (int i = 0; i < baseList.size(); i++) {
                    bGAPhotoPreviewBaseList.add(Constants.IMG_HOST + baseList.get(i));
                }

                startActivity((BGAPhotoPreviewActivity.newIntent(CookingDetailActivity.this, null, (ArrayList<String>) bGAPhotoPreviewBaseList, position)));


            }
        }).start();


    }

    @OnClick({R.id.btn_storemoney, R.id.btn_paybill,
            R.id.rl_all_product, R.id.rl_more, R.id.call_phone, R.id.address_details})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.address_details:
                if (fuBackHotelDetailsBean.getPosition_y() != null && fuBackHotelDetailsBean.getPosition_x() != null) {
                    MapActivity.startActivityWithParmeter(CookingDetailActivity.this, fuBackHotelDetailsBean.getPosition_y(), fuBackHotelDetailsBean.getPosition_x(), fuBackHotelDetailsBean.getSp_name(), fuBackHotelDetailsBean.getDistance(), fuBackHotelDetailsBean.getSp_address());
                }
                break;
            case R.id.call_phone:  // 拨打电话
                if (TextUtils.isEmpty(myMobile)) {
                    ToastUtil.showToast(this, "没有商家电话号码！");
                    return;
                } else {
                    ToolUtil.goToCall(CookingDetailActivity.this, myMobile);
                }

                break;
            case R.id.btn_storemoney://我要储值

                if (TextUtils.isEmpty(new PreferencesHelper(CookingDetailActivity.this).getToken())) {
                    ToolUtil.goToLogin(this);
                } else {
                    getStorge(new PreferencesHelper(CookingDetailActivity.this).getToken(), shop_id);
                }

                break;

            case R.id.btn_paybill://我要买单

                if (TextUtils.isEmpty(new PreferencesHelper(CookingDetailActivity.this).getToken())) {
                    ToolUtil.goToLogin(this);
                } else {
                    if (fuBackHotelDetailsBean != null) {


                        Intent intentc = new Intent(CookingDetailActivity.this, GoToBuyABillActivity.class);
                        intentc.putExtra("shop_id", fuBackHotelDetailsBean.getId());
                        intentc.putExtra("mode_name", fuBackHotelDetailsBean.getSp_name());
                        startActivity(intentc);
                    }
                }
                break;

            case R.id.rl_all_product:
                Intent intent1 = new Intent(CookingDetailActivity.this, AllProductActivity.class);
                intent1.putExtra("shop_id", shop_id);
                startActivity(intent1);

                /**
                 *
                 * 地图
                 *
                 */
//                Intent intent2 = new Intent(NearbyShopDetailsActivity.this, MapActivity.class);
//                startActivity(intent2);

                break;

            case R.id.rl_more:
         /*       intent = new Intent(CookingDetailActivity.this, AllProductActivity.class);
                intent.putExtra("shop_id", shop_id);
                startActivity(intent);*/
     /*           intent = new Intent(CookingDetailActivity.this, ChooseOrderActivity.class);
                intent.putExtra("shop_id", shop_id);
                startActivity(intent);*/
                ChooseOrderActivity.startActivityWithParmeter(CookingDetailActivity.this, shop_id);
                break;

        }
    }


    public void getStorge(String token, String shop_id) {
        HttpHelp.getInstance().create(RemoteApi.class).getStorge(token, shop_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<StorgeBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<StorgeBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (baseBean.data != null) {
                                storeValueDialog(baseBean.data.getBfb(), baseBean.data.getEdu());
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(CookingDetailActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }


    /**
     * 储值的弹框
     */
    private void storeValueDialog(final String bfb, final String edu) {
        Log.i("XXX", bfb + "+" + edu);
        if (bfb != null && edu != null) {
            final Dialog mDialog = new Dialog(CookingDetailActivity.this, R.style.ActionSheetDialogStyle);
            View inflate = LayoutInflater.from(CookingDetailActivity.this).inflate(R.layout.nearby_shop_store_value_dialog, null);
            final EditText tv_peice = (EditText) inflate.findViewById(R.id.tv_peice);
            //设置输入金额的格式。
            InputFilter[] inputFilter = {new CashierInputFilter()};
            tv_peice.setFilters(inputFilter);
            final TextView give_prive = (TextView) inflate.findViewById(R.id.give_prive);
            TextView surplus_price = (TextView) inflate.findViewById(R.id.surplus_price);
            surplus_price.setText(edu + "");
            tv_peice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Editable editable = tv_peice.getText();
                    int len = editable.length();

                    //输完0x后把0删了，仿支付宝输入金额规则
                    if (len >= 2 && s.charAt(0) == '0' && s.charAt(1) != '0') {

                        String str = editable.toString();
                        //截取新字符串
                        String newStr = str.substring(1, len);
                        tv_peice.setText(newStr);
                        editable = tv_peice.getText();
                        //新字符串的长度
                        int newLen = editable.length();
                        //设置新光标所在的位置
                        Selection.setSelection(editable, newLen);

                    }

                    //输完00x后不能再输入
                    if (len >= 3 && s.charAt(0) == '0' && s.charAt(1) == '0') {

                        String str = editable.toString();
                        //截取新字符串
                        String newStr = str.substring(0, 2);
                        tv_peice.setText(newStr);
                        editable = tv_peice.getText();
                        //设置新光标所在的位置
                        Selection.setSelection(editable, 2);

                    }


                    if (len > maxLen) {
                        int selEndIndex = Selection.getSelectionEnd(editable);
                        String str = editable.toString();
                        //截取新字符串
                        String newStr = str.substring(0, maxLen);
                        tv_peice.setText(newStr);
                        editable = tv_peice.getText();

                        //新字符串的长度
                        int newLen = editable.length();
                        //旧光标位置超过字符串长度
                        if (selEndIndex > newLen) {
                            selEndIndex = editable.length();
                        }
                        //设置新光标所在的位置
                        Selection.setSelection(editable, selEndIndex);

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!TextUtils.isEmpty(s.toString().trim())) {
                        float v = Float.parseFloat(s.toString().trim()) * Float.parseFloat(bfb);
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                        give_prive.setText(decimalFormat.format(v));
                    } else {
                        give_prive.setText(0.0 + "");
                    }
                }
            });


            View tv_submit = inflate.findViewById(R.id.tv_submit);
            tv_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(tv_peice.getText().toString().trim())) {
                        Intent intent1 = new Intent(CookingDetailActivity.this, StoredValueActivity.class);
                        intent1.putExtra("shop_id", fuBackHotelDetailsBean.getId());
                        intent1.putExtra("shop_price", tv_peice.getText().toString().trim());
                        startActivity(intent1);
                        mDialog.dismiss();
                    } else {
                        ToastUtil.showToast(CookingDetailActivity.this, "请输入要储值的金额！");
                    }

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


    /**
     * 套餐详情的弹框
     *
     * @param position
     */
    private void comboDialog(FuBackHotelDetailsBean.PackageBean position) {
        if (position != null) {
            normalDialog = new AlertDialog.Builder(CookingDetailActivity.this);

            View inflate = LayoutInflater.from(CookingDetailActivity.this).inflate(R.layout.dialog_layout_nearbyshop_details_combo, null);

            TextView shop_name, shop_dicinfo, new_price, who_price, shop_content;
            ImageView iv_pic, shop_pic;

            shop_name = (TextView) inflate.findViewById(R.id.shop_name);
            shop_dicinfo = (TextView) inflate.findViewById(R.id.shop_dicinfo);
            new_price = (TextView) inflate.findViewById(R.id.new_price);
            who_price = (TextView) inflate.findViewById(R.id.who_price);
            shop_content = (TextView) inflate.findViewById(R.id.shop_content);
            iv_pic = (ImageView) inflate.findViewById(R.id.iv_pic);
            shop_pic = (ImageView) inflate.findViewById(R.id.shop_pic);

            shop_name.setText(position.getName());
            shop_dicinfo.setText(position.getDicinfo());
            new_price.setText("￥" + position.getAll_price());
            who_price.setText("￥" + position.getShop_price());
            who_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            GlideUtil.display(CookingDetailActivity.this, Constants.IMG_HOST + position.getPic(), shop_pic);

            if (position.getGoodids() != null) {
                String str = "";
                for (int i = 0; i < position.getGoodids().size(); i++) {
                    if (i != position.getGoodids().size() - 1) {
                        str += position.getGoodids().get(i).getName() + " x" + position.getGoodids().get(i).getNum() + "  ";
                    } else {
                        str += position.getGoodids().get(i).getName() + " x" + position.getGoodids().get(i).getNum();
                    }
                }
                if (!TextUtils.isEmpty(str)) {
                    shop_content.setText(str);
                }
            }
            normalDialog.setView(inflate);
            normalDialog.create();
            final AlertDialog normalDialogs = normalDialog.show();

            iv_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    normalDialogs.dismiss();
                }
            });
        } else {
            ToastUtil.showToast(CookingDetailActivity.this, "暂无信息！");
        }
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        switch (parent.getId()) {
/*            case R.id.rv_taochan:
*//*不再弹出套餐的详情
                comboDialog(shopReserveAdapter.getData().get(position));
*//*
                if (R.id.rv_taochan == parent.getId()) {
                    if ("1".equals(shopReserveAdapter.getData().get(position).getMoban())) {
                        if (TextUtils.isEmpty(new PreferencesHelper(CookingDetailActivity.this).getToken())) {
                            ToolUtil.goToLogin(CookingDetailActivity.this);
                        } else {
                            Intent intent1 = new Intent(CookingDetailActivity.this, EntertainmentAffirmOrderActivity.class);
                            intent1.putExtra("shop_id", shop_id);
                            intent1.putExtra("package_id", shopReserveAdapter.getData().get(position).getId());
                            startActivity(intent1);
                        }
                    } else if ("2".equals(shopReserveAdapter.getData().get(position).getMoban())) {
                        if (TextUtils.isEmpty(new PreferencesHelper(CookingDetailActivity.this).getToken())) {
                            ToolUtil.goToLogin(CookingDetailActivity.this);
                        } else {
                            Intent intent = new Intent(CookingDetailActivity.this, CommodityConfirmationOrderActivity.class);
                            intent.putExtra("shop_id", shop_id);
                            intent.putExtra("package_id", shopReserveAdapter.getData().get(position).getId());
                            startActivity(intent);
                        }
                    }
                }
                break;*/
            case R.id.rv_danping:
                //单品详情

                if (TextUtils.isEmpty(new PreferencesHelper(CookingDetailActivity.this).getToken())) {
                    ToolUtil.goToLogin(CookingDetailActivity.this);
                } else {

                    ChooseOrderActivity.startActivityWithParmeter(CookingDetailActivity.this, shop_id);





/*                    Intent intent = new Intent(CookingDetailActivity.this, ClassifiedGoodsActivity.class);
                    intent.putExtra("goods_id", singleProductAdapter.getData().get(position).getId());
                    intent.putExtra("shop_id", shop_id);
                    startActivity(intent);*/
                }

                break;
        }
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        if (TextUtils.isEmpty(new PreferencesHelper(CookingDetailActivity.this).getToken())) {
            ToolUtil.goToLogin(CookingDetailActivity.this);
        } else {
            if (R.id.rv_taochan == parent.getId()) {
                if (R.id.tv_reserve == childView.getId()) {
                    if ("1".equals(shopReserveAdapter.getData().get(position).getMoban())) {
                        if (TextUtils.isEmpty(new PreferencesHelper(CookingDetailActivity.this).getToken())) {
                            ToolUtil.goToLogin(CookingDetailActivity.this);
                        } else {
                            Intent intent1 = new Intent(CookingDetailActivity.this, EntertainmentAffirmOrderActivity.class);
                            intent1.putExtra("shop_id", shop_id);
                            intent1.putExtra("package_id", shopReserveAdapter.getData().get(position).getId());
                            startActivity(intent1);
                        }
                    } else if ("2".equals(shopReserveAdapter.getData().get(position).getMoban())) {
                        if (TextUtils.isEmpty(new PreferencesHelper(CookingDetailActivity.this).getToken())) {
                            ToolUtil.goToLogin(CookingDetailActivity.this);
                        } else {
                            Intent intent = new Intent(CookingDetailActivity.this, CommodityConfirmationOrderActivity.class);
                            intent.putExtra("shop_id", shop_id);
                            intent.putExtra("package_id", shopReserveAdapter.getData().get(position).getId());
                            startActivity(intent);
                        }
                    }
                }
            }

        }
    }


    @Override
    public void onLoadMore() {
        page++;
        getData();
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData();
    }
}
