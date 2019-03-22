package com.ruirong.chefang.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.google.gson.Gson;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.CircleBar;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.HotelReserveDetailAdapter;
import com.ruirong.chefang.adapter.HotelTaocanAdapter;
import com.ruirong.chefang.adapter.PinglunEvent;
import com.ruirong.chefang.adapter.ShopHeadlineCommentAdapter;
import com.ruirong.chefang.adapter.SingleProductAdapter;
import com.ruirong.chefang.bean.FuBackHotelDetailsBean;
import com.ruirong.chefang.bean.HotelReserveTaoCan;
import com.ruirong.chefang.bean.StorgeBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.CashierInputFilter;
import com.ruirong.chefang.util.SharedUtil;
import com.ruirong.chefang.util.ToolUtil;
import com.ruirong.chefang.view.MyRatingBar;
import com.ruirong.chefang.widget.MarqueTextView;
import com.umeng.socialize.UMShareAPI;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
 * 酒店详情
 * Created by dillon on 2017/12/26.
 */

public class HotelDetailActivity extends BaseActivity implements OnRVItemClickListener, OnItemChildClickListener, CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener {
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
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.address_details)
    TextView addressDetails;
    @BindView(R.id.call_phone)
    ImageView callPhone;
    @BindView(R.id.rv_taochan)
    NoScrollListView rvTaochan;

    @BindView(R.id.rl_more)
    RelativeLayout rlMore;
    @BindView(R.id.view_danpin)
    View viewDanpin;
    @BindView(R.id.rv_danping)
    RecyclerView rvDanping;
    @BindView(R.id.tv_businessarea)
    TextView tvBusinessarea;
    @BindView(R.id.rl_all_product)
    RelativeLayout rlAllProduct;
    @BindView(R.id.tv_pl)
    TextView tvPl;
    @BindView(R.id.cb_evaluate)
    CircleBar cbEvaluate;
    @BindView(R.id.can_content_view_list)
    NoScrollListView canContentViewList;
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
    @BindView(R.id.bll)
    LinearLayout bll;
    @BindView(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;
    @BindView(R.id.view_reserve)
    View viewReserve;
    @BindView(R.id.tv_reserve)
    TextView tvReserve;
    @BindView(R.id.view_tagflag)
    View viewTagFlag;


    private HotelTaocanAdapter hotelTaocanAdapter;  // 预订套餐适配器
    private List<String> titleList = new ArrayList<>();
    private List<HotelReserveTaoCan> hotelReserveTaoCanList = new ArrayList<>();

    private ShopHeadlineCommentAdapter hotelDetailCommentAdapter;
    private SingleProductAdapter singleProductAdapter;
    private List<FuBackHotelDetailsBean.GoodsBean> goodsList = new ArrayList<>();
    private List<FuBackHotelDetailsBean.PinglunaBean> pinglunaList = new ArrayList<>();
    private List<FuBackHotelDetailsBean.HotelsBean.AttributeBean> lists = new ArrayList<>();

    private List<FuBackHotelDetailsBean.KeytitleBean> keytitleBeanList = new ArrayList<>();
    private List<String> baseList = new ArrayList<>();
    private String hotelDetail_id;//店铺id
    private FuBackHotelDetailsBean fuBackHotelDetailsBean;

    private int maxLen = 10;
    private final static String HOTELDETAILIDOFYIJIA = "HOTELDETAILIDOFYIJIA";
    private int page = 1;


    @Override
    public int getContentView() {
        return R.layout.activity_hoteldetail;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("酒店详情");
        EventBusUtil.register(this);
        titleBar.setRightImageRes(R.drawable.ic_share);
        titleBar.setRightImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(new PreferencesHelper(HotelDetailActivity.this).getToken())) {
                    ToolUtil.goToLogin(HotelDetailActivity.this);
                } else {
                    SharedUtil sharedUtil = new SharedUtil(HotelDetailActivity.this);
                    sharedUtil.initShared(HotelDetailActivity.this);
                }
            }
        });


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
    }

    /**
     * 初始化适配器数据。
     */
    public void initData() {

        hotelDetail_id = getIntent().getStringExtra(HOTELDETAILIDOFYIJIA);
        //预订的筛选标题


        //预定
        hotelTaocanAdapter = new HotelTaocanAdapter(rvTaochan);
        rvTaochan.setFocusable(false);
        rvTaochan.setAdapter(hotelTaocanAdapter);

        rvTaochan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                productParaDialog(hotelTaocanAdapter.getData().get(position).getAttribute(), hotelTaocanAdapter.getData().get(position).getLoop_pics(), hotelTaocanAdapter.getData().get(position).getName());
            }
        });
        //单品
        singleProductAdapter = new SingleProductAdapter(rvDanping);
        rvDanping.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        rvDanping.setAdapter(singleProductAdapter);

        //评论
        hotelDetailCommentAdapter = new ShopHeadlineCommentAdapter(canContentViewList);
        canContentViewList.setFocusable(false);
        canContentViewList.setAdapter(hotelDetailCommentAdapter);

        singleProductAdapter.setOnRVItemClickListener(this);
        //环形评分
        cbEvaluate.setRoundProgressColor(Color.parseColor("#fe9d00"));


    }

    @Override
    public void getData() {
        hotelInfo(hotelDetail_id, new PreferencesHelper(HotelDetailActivity.this).getLongTitude(),
                new PreferencesHelper(HotelDetailActivity.this).getLatitude(), page);

    }

    /**
     * 刷新评论列表数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshPingLun(PinglunEvent event) {
        onRefresh();
    }


    private void hotelInfo(final String id, String lat, String lng, final int limit) {
        HttpHelp.getInstance().create(RemoteApi.class).hotelInfo(id, lat, lng, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<FuBackHotelDetailsBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<FuBackHotelDetailsBean> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            fuBackHotelDetailsBean = baseBean.data;
                            if (fuBackHotelDetailsBean != null) {
                                baseList = fuBackHotelDetailsBean.getLunbo();
                                if (baseList != null && baseList.size() > 0) {
                                    bindBannerData();
                                }

                                tvTableName.setText(fuBackHotelDetailsBean.getGonggao());

                                tvName.setText(fuBackHotelDetailsBean.getSp_name());
                                perCapita.setText("人均 : ￥" + fuBackHotelDetailsBean.getPerpeo());
                                addressDetails.setText(fuBackHotelDetailsBean.getSp_address());
                                if (fuBackHotelDetailsBean.getBrowse() != null) {
                                    tvBrowsernum.setText(fuBackHotelDetailsBean.getBrowse() + "人浏览");
                                } else {
                                    tvBrowsernum.setText("0人浏览");
                                }
                                tvPl.setText("用户评价（" + fuBackHotelDetailsBean.getPl_num() + "）");
                                if (fuBackHotelDetailsBean.getDp_grade() != null) {
                                    ratingBar.setRating(Float.parseFloat(fuBackHotelDetailsBean.getDp_grade()));
                                    tvScore.setText(fuBackHotelDetailsBean.getDp_grade() + "分");
                                }

                                if (fuBackHotelDetailsBean.getDp_grade() != null) {
                                    cbEvaluate.setProgress(Float.parseFloat(fuBackHotelDetailsBean.getDp_grade()));
                                }

                                keytitleBeanList = baseBean.data.getKeytitle();

                                if (keytitleBeanList.size() > 0) {
                                    viewTagFlag.setVisibility(View.VISIBLE);
                                    titleList.clear();
                                    for (int i = 0; i < keytitleBeanList.size(); i++) {
                                        titleList.add(keytitleBeanList.get(i).getTitle());
                                    }
                                    if (titleList.size() > 0) {
                                        Log.e("layouts", titleList.size() + "_____" + keytitleBeanList.size() + "+++++");
                                        mFlowLayout.setAdapter(new TagAdapter<String>(titleList) {
                                            @Override
                                            public View getView(FlowLayout parent, int position, String s) {
                                                TextView tv = (TextView) LayoutInflater.from(HotelDetailActivity.this).inflate(R.layout.tv,
                                                        mFlowLayout, false);
                                                tv.setText(s);
                                                return tv;
                                            }

                                        });
                                        getReserveTaoCan(id, titleList.get(0));

                                        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                                            @Override
                                            public boolean onTagClick(View view, int position, FlowLayout parent) {
                                                getReserveTaoCan(id, titleList.get(position));
                                                return true;
                                            }
                                        });
                                    }

                                }else {
                                    getReserveTaoCan(id, "");
                                    viewTagFlag.setVisibility(View.GONE);
                                }

                                if (keytitleBeanList.size() > 0||hotelReserveTaoCanList.size() > 0){
                                    viewReserve.setVisibility(View.VISIBLE);
                                    tvReserve.setVisibility(View.VISIBLE);
                                }else {
                                    viewReserve.setVisibility(View.GONE);
                                    tvReserve.setVisibility(View.GONE);
                                }



                                goodsList = baseBean.data.getGoods();
                                if (goodsList != null && goodsList.size() > 0) {
                                    viewDanpin.setVisibility(View.VISIBLE);
                                    rlMore.setVisibility(View.VISIBLE);
                                    singleProductAdapter.setData(goodsList);
                                }else {
                                    viewDanpin.setVisibility(View.GONE);
                                    rlMore.setVisibility(View.GONE);
                                }

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
                                      //  refresh.setLoadMoreEnabled(true);
                                    } else {
                                        ToastUtil.showToast(HotelDetailActivity.this, getResources().getString(R.string.no_more));
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                        Log.i("XXX", throwable.getLocalizedMessage());
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                    }
                });
    }


    /**
     * 预订订单套餐
     *
     * @param shopId
     * @param title
     */
    public void getReserveTaoCan(final String shopId, String title) {
        showLoadingDialog("");
        hotelTaocanAdapter.setData(null);
        HttpHelp.getInstance().create(RemoteApi.class).getReserveTaocan(shopId, title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<HotelReserveTaoCan>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<HotelReserveTaoCan>> listBaseBean) {
                        super.onNext(listBaseBean);
                        hideLoadingDialog();
                        if (listBaseBean.code == 0) {
                            hotelReserveTaoCanList = listBaseBean.data;
                            if (hotelReserveTaoCanList.size() > 0) {
                                viewReserve.setVisibility(View.VISIBLE);
                                tvReserve.setVisibility(View.VISIBLE);
                                hotelTaocanAdapter.setData(hotelReserveTaoCanList);
                                hotelTaocanAdapter.setMyDatas(hotelReserveTaoCanList, shopId, fuBackHotelDetailsBean);
                            }else {

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
                        String imgUrl = Constants.IMG_HOST + path.toString();
                        GlideUtil.display(HotelDetailActivity.this, imgUrl, imageView);

                    }
                }).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                // startActivity((BGAPhotoPreviewActivity.newIntent(mContext, null, bannerList, position)));

                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                List bGAPhotoPreviewBaseList = new ArrayList<>();

                for (int i = 0; i < baseList.size(); i++) {
                    bGAPhotoPreviewBaseList.add(Constants.IMG_HOST + baseList.get(i));
                }

                startActivity((BGAPhotoPreviewActivity.newIntent(HotelDetailActivity.this, null, (ArrayList<String>) bGAPhotoPreviewBaseList, position)));
            }
        }).start();


    }

    @OnClick({R.id.btn_storemoney, R.id.btn_paybill, R.id.rl_all_product, R.id.rl_more, R.id.call_phone, R.id.iv_location, R.id.address_details})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_location:       //跳转地图

                break;
            case R.id.address_details:   //跳转地图
                if (fuBackHotelDetailsBean.getPosition_x() != null && fuBackHotelDetailsBean.getPosition_y() != null) {
                    MapActivity.startActivityWithParmeter(HotelDetailActivity.this, fuBackHotelDetailsBean.getPosition_y(),
                            fuBackHotelDetailsBean.getPosition_x(), fuBackHotelDetailsBean.getSp_name(), fuBackHotelDetailsBean.getDistance(), fuBackHotelDetailsBean.getSp_address()
                    );
                }
                break;

            case R.id.btn_storemoney:
                if (hotelDetail_id != null) {
                    if (TextUtils.isEmpty(new PreferencesHelper(HotelDetailActivity.this).getToken())) {
                        ToolUtil.goToLogin(this);
                    } else {
                        getStorge(new PreferencesHelper(HotelDetailActivity.this).getToken(), hotelDetail_id);
                    }
                }
                break;
            case R.id.btn_paybill:
                if (TextUtils.isEmpty(new PreferencesHelper(HotelDetailActivity.this).getToken())) {
                    ToolUtil.goToLogin(this);
                } else {
                    if (fuBackHotelDetailsBean != null) {
                        Intent intent = new Intent(HotelDetailActivity.this, GoToBuyABillActivity.class);
                        intent.putExtra("shop_id", fuBackHotelDetailsBean.getId());
                        intent.putExtra("mode_name", fuBackHotelDetailsBean.getSp_name());
                        startActivity(intent);
                    }
                }
                break;
            case R.id.call_phone:

                if (null == fuBackHotelDetailsBean) {
                    return;
                }

                if (fuBackHotelDetailsBean.getMobile() != null && !TextUtils.isEmpty(fuBackHotelDetailsBean.getMobile())) {
                    ToolUtil.goToCall(HotelDetailActivity.this, fuBackHotelDetailsBean.getMobile());
                }

                break;
            case R.id.rl_all_product:
                if (TextUtils.isEmpty(new PreferencesHelper(HotelDetailActivity.this).getToken())) {
                    ToolUtil.goToLogin(HotelDetailActivity.this);
                } else {
                    Intent intent1 = new Intent(HotelDetailActivity.this, AllProductActivity.class);
                    intent1.putExtra("shop_id", hotelDetail_id);
                    startActivity(intent1);
                }
                break;
            case R.id.rl_more:
                if (TextUtils.isEmpty(new PreferencesHelper(HotelDetailActivity.this).getToken())) {
                    ToolUtil.goToLogin(HotelDetailActivity.this);
                } else {
                    Intent intent2 = new Intent(HotelDetailActivity.this, AllProductActivity.class);
                    intent2.putExtra("shop_id", hotelDetail_id);
                    startActivity(intent2);
                }

                break;
        }
    }


    /**
     * 储值的弹框
     */
    private void storeValueDialog(final String bfb, final String edu) {
        Log.i("XXX", bfb + "+" + edu);
        if (bfb != null && edu != null) {
            final Dialog mDialog = new Dialog(HotelDetailActivity.this, R.style.ActionSheetDialogStyle);
            View inflate = LayoutInflater.from(HotelDetailActivity.this).inflate(R.layout.nearby_shop_store_value_dialog, null);
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
                        Intent intent1 = new Intent(HotelDetailActivity.this, StoredValueActivity.class);
                        intent1.putExtra("shop_id", fuBackHotelDetailsBean.getId());
                        intent1.putExtra("shop_price", tv_peice.getText().toString().trim());
                        startActivity(intent1);
                        mDialog.dismiss();
                    } else {
                        ToastUtil.showToast(HotelDetailActivity.this, "请输入要储值的金额！");
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


    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        if (parent.getId() == R.id.rv_danping) {
            //单品详情
            Intent intent = new Intent(HotelDetailActivity.this, ClassifiedGoodsActivity.class);
            Log.i("XXX", singleProductAdapter.getData().get(position).getId() + " " + hotelDetail_id);
            intent.putExtra("goods_id", singleProductAdapter.getData().get(position).getId());
            intent.putExtra("shop_id", hotelDetail_id);
            startActivity(intent);
        }
    }


    /**
     * 产品参数弹框
     */
    private void productParaDialog(List<HotelReserveTaoCan.AttributeBean> lists, final List<String> bList, String tvMyTitle) {
        if (lists != null && lists.size() > 0) {
            final Dialog mDialog = new Dialog(HotelDetailActivity.this, R.style.ActionSheetDialogStyle);
            View inflate = LayoutInflater.from(HotelDetailActivity.this).inflate(R.layout.dialog_hotel_details_model, null);
            ListView viewById = (ListView) inflate.findViewById(R.id.lv_parameter);
            Banner banner = (Banner) inflate.findViewById(R.id.banner_top);
            TextView tvTitle = (TextView) inflate.findViewById(R.id.tv_titles);
            ImageView ivClose = (ImageView) inflate.findViewById(R.id.iv_close);

            tvTitle.setText(tvMyTitle);


            HotelReserveDetailAdapter adapter = new HotelReserveDetailAdapter(viewById);
            viewById.setAdapter(adapter);
            adapter.setData(lists);

            if (bList != null && bList.size() > 0) {
                banner.setImages(bList)
                        .setImageLoader(new ImageLoader() {
                            @Override
                            public void displayImage(Context context, Object path, ImageView imageView) {
                                String imgUrl = Constants.IMG_HOST + path.toString();
                                GlideUtil.display(HotelDetailActivity.this, imgUrl, imageView);

                            }
                        }).setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                        // startActivity((BGAPhotoPreviewActivity.newIntent(mContext, null, bannerList, position)));
                        List bGAPhotoPreviewBaseList = new ArrayList<>();

                        for (int i = 0; i < bList.size(); i++) {
                            bGAPhotoPreviewBaseList.add(Constants.IMG_HOST + bList.get(i));
                        }

                        startActivity((BGAPhotoPreviewActivity.newIntent(HotelDetailActivity.this, null, (ArrayList<String>) bGAPhotoPreviewBaseList, position)));


                    }
                }).start();
            }

            mDialog.setContentView(inflate);
            Window dialogwindow = mDialog.getWindow();
            dialogwindow.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogwindow.getAttributes();
            lp.windowAnimations = R.style.PopUpBottomAnimation;
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialogwindow.setAttributes(lp);
            mDialog.show();

            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                }
            });

        } else {
            ToastUtil.showToast(HotelDetailActivity.this, "有误！");
        }

    }


    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        if (TextUtils.isEmpty(new PreferencesHelper(HotelDetailActivity.this).getToken())) {
            ToolUtil.goToLogin(HotelDetailActivity.this);
        } else {
            switch (parent.getId()) {
                case R.id.rv_taochan:
                    switch (childView.getId()) {
                        case R.id.iv_orderonline:
                            if (hotelTaocanAdapter.getData().get(position).getIs_full() == 1) {
                                Intent intent1 = new Intent(HotelDetailActivity.this, ReserceActivity.class);
                                intent1.putExtra("house_id", hotelTaocanAdapter.getData().get(position).getId());
                                intent1.putExtra("position", position);
                                intent1.putExtra("shop_id", hotelDetail_id);
                                String house_xq = new Gson().toJson(fuBackHotelDetailsBean);
                                intent1.putExtra("house_xq", house_xq);
                                startActivity(intent1);
                            } else if (hotelTaocanAdapter.getData().get(position).getIs_full() == 1) {
                                ToastUtil.showToast(HotelDetailActivity.this, "房间已满");
                            }
                            break;

                        case R.id.tv_presentprice:
                            if (hotelTaocanAdapter.getData().get(position).getIs_full() == 1) {
                                Intent intent1 = new Intent(HotelDetailActivity.this, ReserceActivity.class);
                                intent1.putExtra("house_id", hotelTaocanAdapter.getData().get(position).getId());
                                intent1.putExtra("position", position);
                                intent1.putExtra("shop_id", hotelDetail_id);
                                String house_xq = new Gson().toJson(fuBackHotelDetailsBean);
                                intent1.putExtra("house_xq", house_xq);
                                startActivity(intent1);
                            } else if (hotelTaocanAdapter.getData().get(position).getIs_full() == 1) {
                                ToastUtil.showToast(HotelDetailActivity.this, "房间已满");
                            }
                            break;

                        case R.id.tv_oldprice:
                            if (hotelTaocanAdapter.getData().get(position).getIs_full() == 1) {
                                Intent intent1 = new Intent(HotelDetailActivity.this, ReserceActivity.class);
                                intent1.putExtra("house_id", hotelTaocanAdapter.getData().get(position).getId());
                                intent1.putExtra("position", position);
                                intent1.putExtra("shop_id", hotelDetail_id);
                                String house_xq = new Gson().toJson(fuBackHotelDetailsBean);
                                intent1.putExtra("house_xq", house_xq);
                                startActivity(intent1);
                            } else if (hotelTaocanAdapter.getData().get(position).getIs_full() == 1) {
                                ToastUtil.showToast(HotelDetailActivity.this, "房间已满");
                            }
                            break;

                    }
                    break;
            }
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
                            ToolUtil.loseToLogin(HotelDetailActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public static void startActivityWithParmeter(Context context, String hotelDetailId) {
        Intent intent = new Intent(context, HotelDetailActivity.class);
        intent.putExtra(HOTELDETAILIDOFYIJIA, hotelDetailId);
        context.startActivity(intent);
    }


    @Override
    public void onLoadMore() {
        page++;
        getData();
        Log.i("XXX", "111111111111111");
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData();
        Log.i("XXX", "2222222222222");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }
}
