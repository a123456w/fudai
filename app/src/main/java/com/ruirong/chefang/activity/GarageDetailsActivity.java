package com.ruirong.chefang.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
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
import com.ruirong.chefang.adapter.CarAndRoomAdapter;
import com.ruirong.chefang.adapter.CarSetailsAdapter;
import com.ruirong.chefang.adapter.CarpinglunAdapter;
import com.ruirong.chefang.adapter.ProductAdapter;
import com.ruirong.chefang.bean.CarAndRoomsBean;
import com.ruirong.chefang.bean.HousedetailsBean;
import com.ruirong.chefang.bean.ObjectBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.SharedUtil;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.widget.MarqueTextView;
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
 * 车房详情
 * Created by BX on 2017/12/26.
 */

public class GarageDetailsActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener {

    @BindView(R.id.banner_top)
    Banner bannerTop;
    @BindView(R.id.tv_table_name)
    MarqueTextView tvTableName;
    @BindView(R.id.tv_preview)
    TextView tvPreview;
    @BindView(R.id.tv_preview_num)
    TextView tvPreviewNum;
    @BindView(R.id.tv_real_estate)
    TextView tvRealEstate;
    @BindView(R.id.tv_name)
    TextView tvName;
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
    @BindView(R.id.detailed_information)
    RelativeLayout detailedInformation;
    @BindView(R.id.tv_company_profile)
    TextView tvCompanyProfile;
    @BindView(R.id.list_product)
    NoScrollListView listProduct;
    @BindView(R.id.tv_punglun)
    TextView tvPunglun;
    @BindView(R.id.cb_evaluate)
    CircleBar cbEvaluate;
    @BindView(R.id.list_evaluate)
    NoScrollListView listEvaluate;
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
    @BindView(R.id.tv_collection)
    TextView tvCollection;
    @BindView(R.id.tv_showings)
    TextView tvShowings;
    //轮播图
    private List<String> picList = new ArrayList<>();

    //产品
    private CarAndRoomAdapter lightListHouseAdapter;
    private List<CarAndRoomsBean.GoodsBean> listData = new ArrayList<>();
    private List<CarAndRoomsBean.CanshuBean> listDatas = new ArrayList<>();
    private List<CarAndRoomsBean.PinglunBean> pinglunBeans = new ArrayList<>();
    //评论
    private CarpinglunAdapter garadeCommentAdapter;
    //详情信息
    private CarSetailsAdapter adapter;
    private ProductAdapter productAdapter;
    private Dialog mDialog;
    private View inflate;
    private List<HousedetailsBean> housedetailsBeans = new ArrayList<>();

    private String myshopid = "";
    private String myTitleName = "";
    private String phone;
    private static final String GARAGEDETAILHOUSEID = "GARAGEDETAILHOUSEID";
    private static final String GARAGEDETALTITLENAME = "GARAGEDETALTITLENAME";
    private CarAndRoomsBean carAndRoomsBean;
    private int page = 1;

    @Override
    public int getContentView() {
        return R.layout.activity_garagedetalis;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
//        showLoadingDialog("加载中...");
        initData();
    }


    private void initData() {
        myTitleName = getIntent().getStringExtra(GARAGEDETALTITLENAME);
        myshopid = getIntent().getStringExtra(GARAGEDETAILHOUSEID);
        if (myTitleName != null) {
            titleBar.setTitleText("详情");
           // titleBar.setTitleText(myTitleName + "详情");

        }
        titleBar.setRightImageRes(R.drawable.share_icon);
        titleBar.setRightImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedUtil sharedUtil = new SharedUtil(GarageDetailsActivity.this);
                sharedUtil.initShared(GarageDetailsActivity.this);
            }
        });
        Log.i("XXX", new PreferencesHelper(GarageDetailsActivity.this).getToken() + "  " + myshopid);
        //产品
        lightListHouseAdapter = new CarAndRoomAdapter(listProduct);
        listProduct.setAdapter(lightListHouseAdapter);
        listProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("XXX", lightListHouseAdapter.getData().get(position).getId() + "  " + myshopid);
                propertyProduct(lightListHouseAdapter.getData().get(position).getId(), myshopid);
            }
        });
        //评论
        garadeCommentAdapter = new CarpinglunAdapter(listEvaluate);
        listEvaluate.setAdapter(garadeCommentAdapter);

        cbEvaluate.setRoundProgressColor(Color.parseColor("#fe9d00"));


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


    }

    //产品详情
    private void propertyProduct(String id, String shop_check_id) {
        HttpHelp.getInstance().create(RemoteApi.class).Propertyproduct(id, shop_check_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<HousedetailsBean>>(this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<HousedetailsBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            if (listBaseBean.data != null) {
                                Unitdetails(listBaseBean.data);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });

    }

    @Override
    public void getData() {
        if (TextUtils.isEmpty(new PreferencesHelper(GarageDetailsActivity.this).getToken())) {
//            ToolUtil.goToLogin(GarageDetailsActivity.this);
            getlistData(null, myshopid, new PreferencesHelper(GarageDetailsActivity.this).getLongTitude(),
                    new PreferencesHelper(GarageDetailsActivity.this).getLatitude(), page);
        } else {
            getlistData(new PreferencesHelper(GarageDetailsActivity.this).getToken(),
                    myshopid, new PreferencesHelper(GarageDetailsActivity.this).getLongTitude(),
                    new PreferencesHelper(GarageDetailsActivity.this).getLatitude(), page);
        }

    }

    /**
     * 详情
     *
     * @param token
     * @param myshopid
     */
    private void getlistData(String token, String myshopid, String longtitude, String latitude, final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).Propertydetails(token, myshopid, longtitude, latitude, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<CarAndRoomsBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<CarAndRoomsBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (listBaseBean.code == 0) {
                            carAndRoomsBean = listBaseBean.data;

                            if (carAndRoomsBean != null) {
                                if (carAndRoomsBean.getPic().size() > 0) {
                                    picList = carAndRoomsBean.getPic();
                                    //绑定轮播图的地址
                                    bindBannerData();
                                }
                                //喇叭赋值
                                if (!TextUtils.isEmpty(carAndRoomsBean.getGonggao())) {
                                    tvTableName.setText(carAndRoomsBean.getGonggao());
                                }

                                //浏览人数
                                if (carAndRoomsBean.getBrowse() != null) {
                                    tvPreviewNum.setText(carAndRoomsBean.getBrowse());
                                } else {
                                    tvPreviewNum.setText("0");

                                }
                                //商家名称
                                tvRealEstate.setText(listBaseBean.data.getSp_name());

                                //评论得分
                                if (carAndRoomsBean.getDp_grade() != null) {
                                    tvGrade.setText(carAndRoomsBean.getDp_grade()+"分");
                                    rbGrade.setRating(Float.parseFloat(carAndRoomsBean.getDp_grade()));
                                    //评分
                                    cbEvaluate.setProgress(Float.parseFloat(carAndRoomsBean.getDp_grade()));
                                }
                                //设置地址
                                if (carAndRoomsBean.getSp_address() != null) {
                                    tvAddress.setText(carAndRoomsBean.getSp_address());
                                }

                                listDatas = carAndRoomsBean.getCanshu();

                                tvCompanyProfile.setText(carAndRoomsBean.getContent());

                                listData = carAndRoomsBean.getGoods();
                                if (listData != null && listData.size() > 0) {
                                    lightListHouseAdapter.setData(listData);
                                }

                                //获取电话号码
                                phone = carAndRoomsBean.getMobile();



                                //收藏和未收藏
                                if (1 == carAndRoomsBean.getCollection()) {
                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_collected);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    tvCollection.setCompoundDrawables(null, drawable, null, null);
                                } else if (0 == carAndRoomsBean.getCollection()) {
                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_collect);
                                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    tvCollection.setCompoundDrawables(null, drawable, null, null);
                                }
                                pinglunBeans = carAndRoomsBean.getPinglun();
                                //评论
                                if (page == 1) {
                                    if (pinglunBeans != null && pinglunBeans.size() > 0) {
                                        refresh.setLoadMoreEnabled(true);
                                       // listRl.setVisibility(View.VISIBLE);
                                       // rlEmpty.setVisibility(View.GONE);
                                        garadeCommentAdapter.setData(pinglunBeans);
                                    } else {
                                        refresh.setLoadMoreEnabled(false);
                                      //  listRl.setVisibility(View.GONE);
                                      //  rlEmpty.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    if (pinglunBeans != null && pinglunBeans.size() > 0) {
                                        garadeCommentAdapter.addMoreData(pinglunBeans);
                                        refresh.setLoadMoreEnabled(true);
                                    } else {
                                        ToastUtil.showToast(GarageDetailsActivity.this, getResources().getString(R.string.no_more));
                                    }
                                }

                                //用户评论数
                                tvPunglun.setText("用户评价（" + carAndRoomsBean.getNumber() + "）");
                            }
                        } else if (listBaseBean.code == 4) {
                            ToolUtil.loseToLogin(GarageDetailsActivity.this);
                        }


                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                    }
                });
    }

    /**
     * 传递房产的id和房字的名字。
     *
     * @param context
     * @param houseId
     * @param houseName
     */
    public static void startActivityWithParmeter(Context context, String houseId, String houseName) {
        Intent intent = new Intent(context, GarageDetailsActivity.class);
        intent.putExtra(GARAGEDETAILHOUSEID, houseId);
        intent.putExtra(GARAGEDETALTITLENAME, houseName);
        context.startActivity(intent);
    }

    /**
     * 轮播图
     */
    public void bindBannerData() {
        bannerTop.setImages(picList)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        String url = Constants.IMG_HOST + path.toString();
                        GlideUtil.display(GarageDetailsActivity.this, url, imageView);

                    }
                }).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                // startActivity((BGAPhotoPreviewActivity.newIntent(CommodityDetailsActivity.this, null,  bannerList, position)));
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                List bGAPhotoPreviewBaseList = new ArrayList<>();

                for (int i = 0; i < picList.size(); i++) {
                    bGAPhotoPreviewBaseList.add(Constants.IMG_HOST + picList.get(i));
                }

                startActivity((BGAPhotoPreviewActivity.newIntent(GarageDetailsActivity.this, null, (ArrayList<String>) bGAPhotoPreviewBaseList, position)));
            }
        }).start();
    }

    @OnClick({R.id.tv_collection, R.id.detailed_information, R.id.tv_showings, R.id.iv_phone, R.id.tv_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_address:
                if (carAndRoomsBean.getPosition_y() != null && carAndRoomsBean.getPosition_x() != null) {
                    MapActivity.startActivityWithParmeter(GarageDetailsActivity.this, carAndRoomsBean.getPosition_y(), carAndRoomsBean.getPosition_x(), carAndRoomsBean.getSp_name(), carAndRoomsBean.getDistence(), carAndRoomsBean.getSp_address());
                }

                break;
            case R.id.tv_collection:   // 收藏
                if (TextUtils.isEmpty(new PreferencesHelper(GarageDetailsActivity.this).getToken())) {
                    ToolUtil.goToLogin(GarageDetailsActivity.this);
                } else {
                    if (myshopid != null) {
                        Collection(myshopid, new PreferencesHelper(GarageDetailsActivity.this).getToken());
                    }
                }

                break;
            case R.id.detailed_information:
                detailed_information();
                break;
            case R.id.iv_phone:
                // 直接拨打电话
                if (!TextUtils.isEmpty(phone)) {
                    ToolUtil.goToCall(GarageDetailsActivity.this, phone);
                }
                break;
            case R.id.tv_showings:


                if (TextUtils.isEmpty(new PreferencesHelper(GarageDetailsActivity.this).getToken())) {
                    ToolUtil.goToLogin(GarageDetailsActivity.this);
                } else {
                    if (myshopid != null) {
                        Intent it = new Intent(GarageDetailsActivity.this, ReservationsActivity.class);
                        it.putExtra("id", myshopid);
                        startActivity(it);
                    }

                }

                break;

        }
    }

    /**
     * 收藏
     */
    private void Collection(String id, String token) {
        HttpHelp.getInstance().create(RemoteApi.class).Propertyshop_Collection(id, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ObjectBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<ObjectBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            if ("1".equals(listBaseBean.data.getStatus())) {
                                Drawable drawable = getResources().getDrawable(R.drawable.ic_collected);
                                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                tvCollection.setCompoundDrawables(null, drawable, null, null);
                            } else if ("0".equals(listBaseBean.data.getStatus())) {
                                Drawable drawable = getResources().getDrawable(R.drawable.ic_collect);
                                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                tvCollection.setCompoundDrawables(null, drawable, null, null);
                            } else if (listBaseBean.code == 1) {

                            }
                            ToastUtil.showToast(GarageDetailsActivity.this, listBaseBean.message);
                        } else if (listBaseBean.code == 4) {
                            ToolUtil.loseToLogin(GarageDetailsActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    //楼盘详情的弹框
    private void detailed_information() {
        if (listDatas != null && listDatas.size() > 0) {
            mDialog = new Dialog(GarageDetailsActivity.this, R.style.ActionSheetDialogStyle);
            inflate = LayoutInflater.from(GarageDetailsActivity.this).inflate(R.layout.dialog_real_estate_details, null);

            ListView listView = (ListView) inflate.findViewById(R.id.lv_listview);
            ImageView tvClose = (ImageView) inflate.findViewById(R.id.iv_close);
            tvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();

                }
            });
            adapter = new CarSetailsAdapter(listView);
            listView.setAdapter(adapter);
            adapter.setData(listDatas);
            mDialog.setContentView(inflate);

            Window dialogwindow = mDialog.getWindow();
            dialogwindow.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogwindow.getAttributes();
            lp.windowAnimations = R.style.PopUpBottomAnimation;
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialogwindow.setAttributes(lp);
            mDialog.show();
        } else {
            ToastUtil.showToast(GarageDetailsActivity.this, "暂无数据");
        }
    }

    //户型详情的弹框
    private void Unitdetails(final HousedetailsBean housedetailsBean) {
        mDialog = new Dialog(GarageDetailsActivity.this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(GarageDetailsActivity.this).inflate(R.layout.dialog_unit_details, null);
        mDialog.setContentView(inflate);
        Window dialogwindow = mDialog.getWindow();
        dialogwindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogwindow.setAttributes(lp);
        mDialog.show();

        TextView tvName, tv_score, tv_average, title_name, tv_jj;
        Banner banner;
        GridView product_list;
        ImageView del;
        RatingBar ratingBar;
        del = (ImageView) inflate.findViewById(R.id.del);
        tvName = (TextView) inflate.findViewById(R.id.tv_real_estate);
        tv_jj = (TextView) inflate.findViewById(R.id.tv_jj);
        tv_score = (TextView) inflate.findViewById(R.id.tv_score);
        tv_average = (TextView) inflate.findViewById(R.id.tv_average);
        title_name = (TextView) inflate.findViewById(R.id.title_name);
        product_list = (GridView) inflate.findViewById(R.id.product_list);

        if (1 == housedetailsBean.getType()) {
            title_name.setText("户型详情");
            tv_jj.setText(housedetailsBean.getPricetitle());
        } else if (2 == housedetailsBean.getType()) {
            title_name.setText("车型详情");
            tv_jj.setText(housedetailsBean.getPricetitle());
        }

        productAdapter = new ProductAdapter(product_list);
        product_list.setAdapter(productAdapter);
        if (housedetailsBean.getContent() != null && housedetailsBean.getContent().size() > 0) {
            productAdapter.setData(housedetailsBean.getContent());
        }

        banner = (Banner) inflate.findViewById(R.id.iv_pic);
        ratingBar = (RatingBar) inflate.findViewById(R.id.rb_grade);

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();

            }
        });

        tvName.setText(housedetailsBean.getTitle());
        tv_score.setText(housedetailsBean.getDp_grade() + "分");
        tv_average.setText(housedetailsBean.getAverage());
        if (housedetailsBean.getDp_grade() != null) {
            ratingBar.setRating(Float.parseFloat(housedetailsBean.getDp_grade()));
        }

        banner.setImages(housedetailsBean.getPic())
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        String url = path.toString();
                        GlideUtil.display(GarageDetailsActivity.this, Constants.IMG_HOST + url, imageView);

                    }
                }).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                // startActivity((BGAPhotoPreviewActivity.newIntent(CommodityDetailsActivity.this, null,  bannerList, position)));

                List bGAPhotoPreviewBaseList = new ArrayList<>();

                for (int i = 0; i <housedetailsBean.getPic().size(); i++) {
                    bGAPhotoPreviewBaseList.add(Constants.IMG_HOST + housedetailsBean.getPic().get(i));
                }

                startActivity((BGAPhotoPreviewActivity.newIntent(GarageDetailsActivity.this, null, (ArrayList<String>) bGAPhotoPreviewBaseList, position)));
            }
        }).start();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
