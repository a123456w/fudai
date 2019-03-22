package com.ruirong.chefang.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.AllProductDialogListviewAdapter;
import com.ruirong.chefang.adapter.CartListBean;
import com.ruirong.chefang.adapter.CategoryLeftAdapter;
import com.ruirong.chefang.adapter.CategoryRightAdapter;
import com.ruirong.chefang.bean.LargeCategoryBean;
import com.ruirong.chefang.bean.SmallCategoryBean;
import com.ruirong.chefang.event.CommodityStoreEvent;
import com.ruirong.chefang.event.UpdateAllGoodsEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.SharedUtil;
import com.ruirong.chefang.util.ToolUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2017/12/29 0029
 * describe:  全部商品
 */
public class AllProductActivity extends BaseActivity implements CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener, OnItemChildClickListener {
    @BindView(R.id.head_left)
    ImageView headLeft;
    @BindView(R.id.head_right)
    ImageView headRight;
    @BindView(R.id.head_coat)
    RelativeLayout headCoat;
    @BindView(R.id.haed_top_pic)
    ImageView haedTopPic;
    @BindView(R.id.category_title)
    TextView categoryTitle;
    @BindView(R.id.category_fraction)
    TextView categoryFraction;
    @BindView(R.id.category_evaluate)
    TextView categoryEvaluate;
    @BindView(R.id.category_comment)
    TextView categoryComment;
    @BindView(R.id.category_distance)
    TextView categoryDistance;
    @BindView(R.id.category_introduce)
    TextView categoryIntroduce;
    @BindView(R.id.left_listview)
    RecyclerView leftListview;
    @BindView(R.id.category_type)
    TextView categoryType;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.can_content_view)
    RecyclerView canContentView;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.category_top_view)
    View categoryTopView;
    @BindView(R.id.tv_empty)
    LinearLayout tvEmpty;
    @BindView(R.id.category_top)
    RelativeLayout categoryTop;
    @BindView(R.id.listview)
    RecyclerView listview;
    @BindView(R.id.category_bottom_ll)
    LinearLayout categoryBottomLl;
    @BindView(R.id.category_price_qian)
    TextView categoryPriceQian;
    @BindView(R.id.category_price)
    TextView categoryPrice;
    @BindView(R.id.tv_showings)
    TextView tvShowings;
    @BindView(R.id.category_bottom)
    RelativeLayout categoryBottom;
    @BindView(R.id.iv_shopping_bag)
    ImageView ivShoppingBag;
    @BindView(R.id.choice_number)
    TextView choiceNumber;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.rl_head_left)
    RelativeLayout rlHeadLeft;
    private PopupWindow popupSort;

    private List<SmallCategoryBean.ListBean> lists = new ArrayList<>();
    private List<LargeCategoryBean.CateBean> largeCategoryList = new ArrayList<>();

    private CategoryLeftAdapter categoryLeftAdapter;
    private CategoryRightAdapter categoryRightAdapter;

    // 声明PopupWindow
    private PopupWindow popupWindow;
    // 声明PopupWindow对应的视图
    private View popupView;

    // 声明平移动画
    private TranslateAnimation animation;

    private boolean isFirst = true;
    private boolean isElastic = true;
    private boolean ischu = false;
    private boolean isjin = false;
    private String type = "1";//1热销 2折扣
    private String cate;//分类id
    private String shop_id = "";//店铺id
    private String lat;//纬度
    private String lng;//经度
    private int page = 1;//页数
    private List<CartListBean.CartlistBean> cartlistData = new ArrayList<>();
    private AllProductDialogListviewAdapter allProductDialogListviewAdapter;
    private CartListBean cartListBean;//购物车列表
    private static final String ALLPRODUCTVCALL = "ALLPRODUCTVCALL";
    private static final String ALLPRODUCTVPHONE = "ALLPRODUCTVPHONE";
    private static final String ALLPRODUCTVTIME = "ALLPRODUCTVTIME";
    private static final String ALLPRODUCTVPEOPLENUM = "ALLPRODUCTVPEOPLENUM";
    private static final String ALLPRODUCMYCANZHUONUM= "ALLPRODUCMYCANZHUONUM";
    private static final String ALLPRODUCMISSTART= "ALLPRODUCMISSTART";


    private String mTvCall  = "";
    private String mTvPhone  = "";
    private String mTvTime = "";
    private String mTvPeople = "";
    private String myzhuoNum = "";

    private String myIsStart = "3" ;   // 1是餐饮2是KTV 3是普通
    @Override
    public int getContentView() {
        return R.layout.activity_all_product;
    }

    @Override
    public void initView() {

        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();/***/
        EventBus.getDefault().register(this);

        shop_id = getIntent().getStringExtra("shop_id");

        mTvCall = getIntent().getStringExtra(ALLPRODUCTVCALL);
        mTvPeople = getIntent().getStringExtra(ALLPRODUCTVPEOPLENUM);
        mTvTime = getIntent().getStringExtra(ALLPRODUCTVTIME);
        mTvPhone = getIntent().getStringExtra(ALLPRODUCTVPHONE);
        myzhuoNum = getIntent().getStringExtra(ALLPRODUCMYCANZHUONUM);
        myIsStart = getIntent().getStringExtra(ALLPRODUCMISSTART);
        if (shop_id == null) {
            finish();
        }

        Log.i("XXX", shop_id);


        lat = new PreferencesHelper(AllProductActivity.this).getLatitude();

        lng = new PreferencesHelper(AllProductActivity.this).getLongTitude();

        categoryLeftAdapter = new CategoryLeftAdapter(leftListview);
        categoryLeftAdapter.setOnRVItemClickListener(this);
        leftListview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        leftListview.setAdapter(categoryLeftAdapter);

        categoryRightAdapter = new CategoryRightAdapter(canContentView);
        categoryRightAdapter.setOnRVItemClickListener(this);
        categoryRightAdapter.setOnItemChildClickListener(this);
        canContentView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        canContentView.setAdapter(categoryRightAdapter);


        refresh.setOnLoadMoreListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(AllProductActivity.this, 150));
        refresh.setStyle(0, 0);
        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");


        allProductDialogListviewAdapter = new AllProductDialogListviewAdapter(listview);
        allProductDialogListviewAdapter.setOnRVItemClickListener(this);
        allProductDialogListviewAdapter.setOnItemChildClickListener(this);
        listview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        listview.setAdapter(allProductDialogListviewAdapter);

        headLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void getData() {
        if (TextUtils.isEmpty(new PreferencesHelper(AllProductActivity.this).getToken())) {
            ToolUtil.goToLogin(AllProductActivity.this);
        } else {
            if (isFirst) {
                goodsCate(new PreferencesHelper(AllProductActivity.this).getToken(), shop_id);
                isFirst = false;
            }
            goodsList(new PreferencesHelper(AllProductActivity.this).getToken(), type, cate, shop_id, lat, lng, page);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        isFirst = true;
        getData();
    }

    /**
     * 单品购物车列表
     */
    private void CartList(String token, String shop_id) {
        HttpHelp.getInstance().create(RemoteApi.class).cartlist(token, shop_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<CartListBean>>(AllProductActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<CartListBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            cartListBean = baseBean.data;
                            if (cartListBean != null) {
                                cartlistData = cartListBean.getCartlist();
                                if (cartlistData != null && cartlistData.size() > 0) {
                                    allProductDialogListviewAdapter.setData(cartlistData);
                                } else {
                                    allProductDialogListviewAdapter.getData().removeAll(allProductDialogListviewAdapter.getData());
                                    allProductDialogListviewAdapter.notifyDataSetChanged();
                                }

                                if (TextUtils.isEmpty(baseBean.data.getZong_price())) {
                                    categoryPrice.setText("0");
                                } else {
                                    categoryPrice.setText(baseBean.data.getZong_price());
                                }
                                if (baseBean.data.getCount() == null) {
                                    choiceNumber.setText("0");
                                } else {
                                    choiceNumber.setText(baseBean.data.getCount());
                                }
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(AllProductActivity.this);
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.i("goodsCate", "onError" + throwable.getLocalizedMessage());
                    }
                });
    }

    /**
     * 右侧分类
     *
     * @param type
     * @param cate
     * @param shop_id
     * @param lat
     * @param lng
     * @param page
     */
    private void goodsList(String token, String type, String cate, String shop_id, String lat, String lng, final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).goodsList(token, type, cate, shop_id, lat, lng, page + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<SmallCategoryBean>>(AllProductActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<SmallCategoryBean> baseBean) {
                        super.onNext(baseBean);
                        refresh.loadMoreComplete();
                        if (baseBean.code == 0) {
                            lists = baseBean.data.getList();
                            if (page == 1) {
                                if (lists != null && lists.size() > 0) {
                                    rlEmpty.setVisibility(View.GONE);
                                    canContentView.setVisibility(View.VISIBLE);
                                    categoryRightAdapter.setData(lists);
                                } else {
                                    canContentView.setVisibility(View.GONE);
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }

                            } else {
                                if (lists != null && lists.size() > 0) {
                                    categoryRightAdapter.addMoreData(lists);
                                } else {
                                    ToastUtil.showToast(AllProductActivity.this,getString(R.string.no_more));
                                }
                            }

                            if (baseBean.data.getShopinfo() != null) {
                                categoryTitle.setText(baseBean.data.getShopinfo().getSp_name());
                                GlideUtil.display(AllProductActivity.this,
                                        Constants.IMG_HOST + baseBean.data.getShopinfo().getLogo(), haedTopPic);
                            }

                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(AllProductActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }


    /**
     * 左侧分类
     *
     * @param shop_id 店铺id
     */
    private void goodsCate(String token, String shop_id) {
        HttpHelp.getInstance().create(RemoteApi.class).goodsCate(token, shop_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<LargeCategoryBean>>(AllProductActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<LargeCategoryBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            largeCategoryList.clear();
                            if (baseBean.data.getCate() != null && baseBean.data.getCate().size() > 0) {
                                LargeCategoryBean.CateBean largeCategoryBean = new LargeCategoryBean.CateBean();
                                largeCategoryBean.setName("热销");
                                largeCategoryBean.setTrue(true);
                                LargeCategoryBean.CateBean largeCategoryBean1 = new LargeCategoryBean.CateBean();
                                largeCategoryBean1.setName("折扣");
                                largeCategoryBean1.setTrue(false);
                                largeCategoryList.add(largeCategoryBean);
                                largeCategoryList.add(largeCategoryBean1);
                                largeCategoryList.addAll(baseBean.data.getCate());
                                categoryLeftAdapter.setData(largeCategoryList);

                                if (baseBean.data.getPrice() == null) {
                                    categoryPrice.setText("0");
                                } else {
                                    categoryPrice.setText(baseBean.data.getPrice());
                                }
                                if (baseBean.data.getCartcounut() == null) {
                                    choiceNumber.setText("0");
                                } else {
                                    choiceNumber.setText(baseBean.data.getCartcounut());
                                }

                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(AllProductActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.i("goodsCate", "onError" + throwable.getLocalizedMessage());
                    }
                });

    }

    @Override
    public void onLoadMore() {
        page++;
        getData();
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        switch (parent.getId()) {
            case R.id.left_listview:
                categoryType.setText(categoryLeftAdapter.getData().get(position).getName());
                setLeftFalse();
                categoryLeftAdapter.getData().get(position).setTrue(true);
                categoryLeftAdapter.notifyDataSetChanged();
                page = 1;
                if (position == 0) {
                    type = "1";
                    cate = null;
                } else if (position == 1) {
                    type = "2";
                    cate = null;
                } else {
                    type = null;
                    cate = categoryLeftAdapter.getData().get(position).getId();
                }

                getData();

                break;

            case R.id.can_content_view:
                if ("1".equals(myIsStart)){

                    ClassifiedGoodsActivity.startActivityWithParmeter(AllProductActivity.this,shop_id,mTvCall,mTvPhone,mTvTime,mTvPeople,myzhuoNum,categoryRightAdapter.getData().get(position).getId(),myIsStart);

                }else if ("2".equals(myIsStart)){
                    //单品详情
                    Intent intent = new Intent(AllProductActivity.this, ClassifiedGoodsActivity.class);
                    intent.putExtra("goods_id", categoryRightAdapter.getData().get(position).getId());
                    intent.putExtra("shop_id", shop_id);
                    intent.putExtra("is_start",myIsStart);
                    startActivity(intent);
                }

                break;
        }
    }

    private void setLeftFalse() {
        for (int i = 0; i < categoryLeftAdapter.getData().size(); i++) {
            categoryLeftAdapter.getData().get(i).setTrue(false);
        }
    }

    @OnClick({R.id.tv_showings, R.id.category_bottom, R.id.category_top_view, R.id.tv_empty
            , R.id.iv_shopping_bag, R.id.head_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_showings:
                if (Integer.parseInt(choiceNumber.getText().toString().trim()) > 0) {
                    if (TextUtils.isEmpty(new PreferencesHelper(AllProductActivity.this).getToken())) {
                        ToolUtil.goToLogin(this);
                    } else {
                        if (TextUtils.isEmpty(mTvCall)&&TextUtils.isEmpty(myzhuoNum)){
                            Intent intent = new Intent(AllProductActivity.this, ShopCartConfirmActivity.class);
                            intent.putExtra("shop_id", shop_id);
                            intent.putExtra("is_start",myIsStart);
                            startActivity(intent);
                        }else {
                            CookingOrderActivity.startActivityWithParmeter(AllProductActivity.this,shop_id,mTvCall,mTvPhone,mTvTime,mTvPeople,myzhuoNum);

                        }

          /*

*/
                    }
                } else {
                    ToastUtil.showToast(AllProductActivity.this, "购物车中暂无商品！");
                }
                break;
            case R.id.category_bottom:
                if (Integer.parseInt(choiceNumber.getText().toString().trim()) > 0) {
                    changeIcon();
                } else {
                    ToastUtil.showToast(AllProductActivity.this, "购物车中暂无商品！");
                }

                break;
            case R.id.category_top_view:

                changeIcon();

                break;
            case R.id.iv_shopping_bag:
                if (Integer.parseInt(choiceNumber.getText().toString().trim()) > 0) {
                    changeIcon();
                } else {
                    ToastUtil.showToast(AllProductActivity.this, "购物车中暂无商品！");
                }
                break;
            case R.id.tv_empty:
                if (TextUtils.isEmpty(new PreferencesHelper(AllProductActivity.this).getToken())) {
                    ToolUtil.goToLogin(AllProductActivity.this);
                } else {
                    delcart(new PreferencesHelper(AllProductActivity.this).getToken(), shop_id);
                }
                break;
            case R.id.head_right:
                if (TextUtils.isEmpty(new PreferencesHelper(AllProductActivity.this).getToken())) {
                    ToolUtil.goToLogin(AllProductActivity.this);
                } else {
                    SharedUtil sharedUtil = new SharedUtil(AllProductActivity.this);
                    sharedUtil.initShared(AllProductActivity.this);
                }
                break;

        }
    }

    /**
     * 清空购物车
     *
     * @param token
     * @param shop_id
     */
    private void delcart(final String token, final String shop_id) {
        HttpHelp.getInstance().create(RemoteApi.class).delcart(token, shop_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            goodsCate(token, shop_id);
                            CartList(token, shop_id);
                            goodsList(token, type, cate, shop_id, lat, lng, page);
                            allProductDialogListviewAdapter.getData().removeAll(allProductDialogListviewAdapter.getData());
                            allProductDialogListviewAdapter.notifyDataSetChanged();
                            changeIcon();
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(AllProductActivity.this);
                        }
                        ToastUtil.showToast(AllProductActivity.this, baseBean.message);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 购物车
     */
    private void changeIcon() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        float curTranslationY = categoryBottom.getTranslationY();

        ObjectAnimator transparent = null;
        ObjectAnimator transparentView = null;
        ObjectAnimator animator = null;

        if (isElastic) {
            CartList(new PreferencesHelper(AllProductActivity.this).getToken(), shop_id);
            //透明动画
            transparentView = ObjectAnimator.ofFloat(categoryTopView, "alpha", 0f, 1f);

            transparent = ObjectAnimator.ofFloat(categoryBottomLl, "alpha", 0f, 1f);

            animator = ObjectAnimator.ofFloat(categoryBottomLl, "translationY",
                    height - categoryBottom.getHeight(), curTranslationY);
            isElastic = false;

            isjin = true;

            ischu = false;
        } else {
            //透明动画
            transparentView = ObjectAnimator.ofFloat(categoryTopView, "alpha", 1f, 0f);

            transparent = ObjectAnimator.ofFloat(categoryBottomLl, "alpha", 1f, 0f);

            animator = ObjectAnimator.ofFloat(categoryBottomLl, "translationY",
                    curTranslationY, height - categoryBottom.getHeight());
            isElastic = true;

            isjin = false;

            ischu = true;
        }

        AnimatorSet set = new AnimatorSet();

        set.play(animator).with(transparentView).with(transparent);

        transparent.setDuration(500);
        animator.setDuration(500);
        transparentView.setDuration(300);

        //添加监听事件
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //动画开始的时候调用
                if (isjin) {
                    categoryBottomLl.setVisibility(View.VISIBLE);
                    categoryTopView.setVisibility(View.VISIBLE);
                } else {

                }

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //画结束的时候调用
                if (ischu) {
                    categoryBottomLl.setVisibility(View.GONE);
                    categoryTopView.setVisibility(View.GONE);
                } else {

                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //动画被取消的时候调用
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //动画重复执行的时候调用

            }
        });

        set.start();


//            if (popupWindow == null) {
//                popupView = LayoutInflater.from(AllProductActivity.this).inflate(R.layout.all_product_activity_buy_product_dialog, null);
//
//                ListView listView = (ListView) popupView.findViewById(R.id.listview);
//                View viewc = popupView.findViewById(R.id.category_top_view);
//                AllProductDialogListviewAdapter allProductDialogListviewAdapter = new AllProductDialogListviewAdapter(listView);
//                listView.setAdapter(allProductDialogListviewAdapter);
//                allProductDialogListviewAdapter.setData(list);
//
//                viewc.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        popupWindow.dismiss();
//                    }
//                });
//
//                popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                //测量view 注意这里，如果没有测量  ，下面的popupHeight高度为-2  ,因为LinearLayout.LayoutParams.WRAP_CONTENT这句自适应造成的
//                popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//
//                // 允许点击外部消失
//                popupWindow.setBackgroundDrawable(new BitmapDrawable());//注意这里如果不设置，下面的setOutsideTouchable(true);允许点击外部消失会失效
//                popupWindow.setOutsideTouchable(true);   //设置外部点击关闭ppw窗口
//                popupWindow.setFocusable(true);
//
//                // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
//                popupWindow.setAnimationStyle(R.style.PopupWindow);  //设置动画
//
//            }
//
//            // 在点击之后设置popupwindow的销毁
//            if (popupWindow.isShowing()) {
//                popupWindow.dismiss();
//            }
//            int popupWidth = popupView.getMeasuredWidth();    //  获取测量后的宽度
//            int popupHeight = popupView.getMeasuredHeight();  //获取测量后的高度
//            int[] location = new int[2];
//            // 获得位置 这里的v是目标控件，就是你要放在这个v的上面还是下面
//            v.getLocationOnScreen(location);
//            //这里就可自定义在上方和下方了 ，这种方式是为了确定在某个位置，某个控件的左边，右边，上边，下边都可以
//            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//            popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
//            popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, 0, location[1] - popupHeight);
//
//            //因为ppw提供了在某个控件下方的方法，所以有些时候需要直接定位在下方时并不用上面的这个方法
//            //        ppwfilter.showAs
//            // DropDown(parent, xoff, yoff, gravity) //parent:传你当前Layout的id; gravity:Gravity.BOTTOM（以屏幕左下角为参照）... 偏移量会以它为基准点 当x y为0,0是出现在底部居中

    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        switch (parent.getId()) {
            case R.id.can_content_view:
                switch (childView.getId()) {
                    case R.id.iv_plus_add:
                        categoryRightAdapter.getData().get(position).setCartcount(
                                (Integer.parseInt(categoryRightAdapter.getData().get(position).getCartcount()) + 1) + "");
                        addcart(new PreferencesHelper(AllProductActivity.this).getToken(), shop_id, categoryRightAdapter.getData().get(position).getId(),
                                categoryRightAdapter.getData().get(position).getCartcount());
                        categoryRightAdapter.notifyDataSetChanged();

//                        int numb = Integer.parseInt(choiceNumber.getText().toString().trim()) + 1;
//                        choiceNumber.setText("" + numb);
//                        int pic = Integer.parseInt(categoryPrice.getText().toString().trim()) + Integer.parseInt(categoryRightAdapter.getData().get(position).getPrice());
//                        categoryPrice.setText(pic + "");

                        break;
                    case R.id.iv_reduce_less:
                        if (Integer.parseInt(categoryRightAdapter.getData().get(position).getCartcount()) - 1 <= 0) {
                            categoryRightAdapter.getData().get(position).setCartcount("0");
                        } else {
                            categoryRightAdapter.getData().get(position).setCartcount(
                                    Integer.parseInt(categoryRightAdapter.getData().get(position).getCartcount()) - 1 + "");
                        }
                        addcart(new PreferencesHelper(AllProductActivity.this).getToken(), shop_id, categoryRightAdapter.getData().get(position).getId(),
                                categoryRightAdapter.getData().get(position).getCartcount());
                        categoryRightAdapter.notifyDataSetChanged();

//                        int numc = Integer.parseInt(choiceNumber.getText().toString().trim()) - 1;
//                        if (numc < 0) {
//                            choiceNumber.setText("0");
//                        } else {
//                            choiceNumber.setText("" + numc);
//                        }
//
//                        int pics = Integer.parseInt(categoryPrice.getText().toString().trim()) - Integer.parseInt(categoryRightAdapter.getData().get(position).getPrice());
//                        if (pics < 0) {
//                            categoryPrice.setText("0");
//                        } else {
//                            categoryPrice.setText(pics + "");
//                        }

                        break;
                }
                break;


            case R.id.listview:
                switch (childView.getId()) {
                    case R.id.iv_add:

                        allProductDialogListviewAdapter.getData().get(position).setNum((Integer.parseInt(allProductDialogListviewAdapter.getData().get(position).getNum()) + 1) + "");
                        addcart(new PreferencesHelper(AllProductActivity.this).getToken(), shop_id, allProductDialogListviewAdapter.getData().get(position).getId(), allProductDialogListviewAdapter.getData().get(position).getNum());

                        synchronization(allProductDialogListviewAdapter.getData().get(position).getId(), allProductDialogListviewAdapter.getData().get(position).getNum());

                        allProductDialogListviewAdapter.notifyDataSetChanged();

//                        goodsList(myToken, type, cate, shop_id, lat, lng, page);

//                        int numb = Integer.parseInt(choiceNumber.getText().toString().trim()) + 1;
//                        choiceNumber.setText("" + numb);
//                        int pic = Integer.parseInt(categoryPrice.getText().toString().trim()) + Integer.parseInt(allProductDialogListviewAdapter.getData().get(position).getPrice());
//                        categoryPrice.setText(pic + "");
                        break;

                    case R.id.iv_subtract:
                        if (Integer.parseInt(allProductDialogListviewAdapter.getData().get(position).getNum()) - 1 <= 0) {
                            addcart(new PreferencesHelper(AllProductActivity.this).getToken(), shop_id, allProductDialogListviewAdapter.getData().get(position).getId(), "0");
                            allProductDialogListviewAdapter.getData().get(position).setNum("0");
//                            allProductDialogListviewAdapter.getData().remove(allProductDialogListviewAdapter.getData().get(position));
                        } else {
                            allProductDialogListviewAdapter.getData().get(position).setNum(Integer.parseInt(allProductDialogListviewAdapter.getData().get(position).getNum()) - 1 + "");
                            addcart(new PreferencesHelper(AllProductActivity.this).getToken(), shop_id, allProductDialogListviewAdapter.getData().get(position).getId(), allProductDialogListviewAdapter.getData().get(position).getNum());
                        }

                        synchronization(allProductDialogListviewAdapter.getData().get(position).getId(), allProductDialogListviewAdapter.getData().get(position).getNum());


//                        goodsList(myToken, type, cate, shop_id, lat, lng, page);

//                        int numc = Integer.parseInt(choiceNumber.getText().toString().trim()) - 1;
//                        if (numc < 0) {
//                            CartList(new PreferencesHelper(AllProductActivity.this).getToken(), shop_id);
//                            choiceNumber.setText("0");
//                        } else {
//                            choiceNumber.setText("" + numc);
//                        }
//
//                        int pics = Integer.parseInt(categoryPrice.getText().toString().trim()) - Integer.parseInt(allProductDialogListviewAdapter.getData().get(position).getPrice());
//                        if (pics < 0) {
//                            categoryPrice.setText("0");
//                        } else {
//                            categoryPrice.setText(pics + "");
//                        }
                        if (Integer.parseInt(allProductDialogListviewAdapter.getData().get(position).getNum()) <= 0) {
                            allProductDialogListviewAdapter.getData().remove(allProductDialogListviewAdapter.getData().get(position));
                        }
                        allProductDialogListviewAdapter.notifyDataSetChanged();

                        break;
                }

                break;
        }
    }

    /**
     * 加入购物车
     *
     * @param token
     * @param id
     * @param num
     */
    private void addcart(final String token, final String shop_id, String id, String num) {
        HttpHelp.getInstance().create(RemoteApi.class).addcart(token, shop_id, id, num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(AllProductActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            CartList(token, shop_id);

                        } else if (baseBean.code == 1) {
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(AllProductActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }

    private void synchronization(String id, String num) {
        if (id == null || num == null) {
            ToastUtil.showToast(AllProductActivity.this, "失败");
        } else {
            for (int i = 0; i < categoryRightAdapter.getData().size(); i++) {
                if (id.equals(categoryRightAdapter.getData().get(i).getId())) {
                    categoryRightAdapter.getData().get(i).setCartcount(num);
                    categoryRightAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(CommodityStoreEvent messageEvent) {
        goodsList(new PreferencesHelper(AllProductActivity.this).getToken(), type, cate, shop_id, lat, lng, page);
        CartList(new PreferencesHelper(AllProductActivity.this).getToken(), shop_id);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateEvent(UpdateAllGoodsEvent event) {
        isFirst = true;
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



    /**
     * 传递数据给这个页面
     *
     * @param context
     * @param tvCall    称呼
     * @param tvPhone   电话
     * @param tvTime    时间
     * @param peopleNum 人数
     */
    public static void startActivityWithParmeter(Context context, String tvCall, String tvPhone, String tvTime, String peopleNum,String shopid,String isStart) {
        Intent intent = new Intent(context, AllProductActivity.class);
        intent.putExtra(ALLPRODUCTVCALL, tvCall);
        intent.putExtra(ALLPRODUCTVPHONE, tvPhone);
        intent.putExtra(ALLPRODUCTVTIME, tvTime);
        intent.putExtra(ALLPRODUCTVPEOPLENUM, peopleNum);
        intent.putExtra("shop_id",shopid);
        intent.putExtra(ALLPRODUCMISSTART,isStart);
        context.startActivity(intent);
    }

    public static void startActivityWith(Context context,String shopid,String myzhuonum,String isStart) {
        Intent intent = new Intent(context, AllProductActivity.class);
        intent.putExtra("shop_id",shopid);
        intent.putExtra(ALLPRODUCMYCANZHUONUM,myzhuonum);
        intent.putExtra(ALLPRODUCMISSTART,isStart);
        context.startActivity(intent);
    }

}