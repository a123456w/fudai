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
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.AllProductDialogListviewAdapter;
import com.ruirong.chefang.adapter.CartListBean;
import com.ruirong.chefang.bean.DanpinDetail;
import com.ruirong.chefang.event.CommodityStoreEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 单品详情
 * Created by Administrator on 2018/3/7.
 */

public class ClassifiedGoodsActivity extends BaseActivity implements OnRVItemClickListener, OnItemChildClickListener, CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener {

    @BindView(R.id.goods_pic)
    Banner goodsPic;
    @BindView(R.id.goods_out)
    ImageView goodsOut;
    @BindView(R.id.goods_head)
    RelativeLayout goodsHead;
    @BindView(R.id.goods_name)
    TextView goodsName;
    @BindView(R.id.goods_one)
    TextView goodsOne;
    @BindView(R.id.goods_single_price)
    TextView goodsSinglePrice;
    @BindView(R.id.add_goods)
    TextView addGoods;
    @BindView(R.id.iv_reduce)
    ImageView ivReduce;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.iv_plus)
    ImageView ivPlus;
    @BindView(R.id.goods_selected)
    LinearLayout goodsSelected;
    @BindView(R.id.wv_goodsdetails)
    WebView wvGoodsdetails;
    @BindView(R.id.can_content_view)
    ScrollView canContentView;
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
    @BindView(R.id.goods_price)
    TextView goodsPrice;
    @BindView(R.id.goods_showings)
    TextView goodsShowings;
    @BindView(R.id.goods_bottom)
    RelativeLayout goodsBottom;
    @BindView(R.id.goods_shopping_bag)
    ImageView goodsShoppingBag;
    @BindView(R.id.goods_number)
    TextView goodsNumber;
    private List<String> baseList = new ArrayList<>();
    private String goods_id;
    private int number = 0;
    private String shop_id;
    private List<CartListBean.CartlistBean> cartlist = new ArrayList<>();
    private PopupWindow popupSort;

    private boolean isFirst = true;//true 加 false减
    private boolean isElastic = true;
    private boolean ischu = false;
    private boolean isjin = false;
    private CartListBean cartListBean;
    private List<CartListBean.CartlistBean> cartlistData;
    private AllProductDialogListviewAdapter allProductDialogListviewAdapter;
    private DanpinDetail goodDetailsBean;
    private int page = 1;
    private String myisStart = "2";


    private static final String SHOPCARTTVCALL = "SHOPCARTTVCALL";
    private static final String SHOPCARTTVPHONE = "SHOPCARTTVPHONE";
    private static final String SHOPCARTTVTIME = "SHOPCARTTVTIME";
    private static final String SHOPCARTTVPEOPLENUM = "SHOPCARTTVPEOPLENUM";
    private static final String SHOPMYCANZHUONNUM = "SHOPMYCANZHUONNUM";
    private String mTvCall = "";
    private String mTvPhone = "";
    private String mTvTime = "";
    private String mTvPeople = "";
    private String mCanZhuoNum = "";


    @Override
    public int getContentView() {
        return R.layout.activity_classified_goods;
    }

    @Override
    public void initView() {
        hideTitleBar();/***/
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        goods_id = getIntent().getStringExtra("goods_id");
        shop_id = getIntent().getStringExtra("shop_id");
        myisStart = getIntent().getStringExtra("is_start");

        goodsOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTvCall = getIntent().getStringExtra(SHOPCARTTVCALL);
        mTvPeople = getIntent().getStringExtra(SHOPCARTTVPEOPLENUM);
        mTvTime = getIntent().getStringExtra(SHOPCARTTVTIME);
        mTvPhone = getIntent().getStringExtra(SHOPCARTTVPHONE);

        mCanZhuoNum = getIntent().getStringExtra(SHOPMYCANZHUONNUM);

        allProductDialogListviewAdapter = new AllProductDialogListviewAdapter(listview);
        allProductDialogListviewAdapter.setOnRVItemClickListener(this);
        allProductDialogListviewAdapter.setOnItemChildClickListener(this);
        listview.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        listview.setAdapter(allProductDialogListviewAdapter);


    }

    @Override
    public void getData() {

        if (TextUtils.isEmpty(new PreferencesHelper(ClassifiedGoodsActivity.this).getToken())) {

            goodinfo(goods_id, null, page);

        } else {
            CartList(new PreferencesHelper(ClassifiedGoodsActivity.this).getToken(), shop_id);

            goodinfo(goods_id, new PreferencesHelper(ClassifiedGoodsActivity.this).getToken(), page);
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getData();
    }

    /**
     * 单凭详情页
     *
     * @param id
     */
    private void goodinfo(String id, String token, final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).goodinfo(id, token, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<DanpinDetail>>(this, null) {
                    @Override
                    public void onNext(BaseBean<DanpinDetail> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();

                        if (baseBean.code == 0) {

                            goodDetailsBean = baseBean.data;

                            baseList = baseBean.data.getInfo().getLoop_pics();

                            wvGoodsdetails.loadUrl(Constants.IMG_HOST + goodDetailsBean.getUrl());

                            if (baseList != null && baseList.size() > 0) {
                                bindBannerData();
                            }
                            if (baseBean.data.getCartNum() != null) {
                                if (Integer.parseInt(baseBean.data.getCartNum()) > 0) {
                                    addGoods.setVisibility(View.GONE);
                                    goodsSelected.setVisibility(View.VISIBLE);
                                    number = Integer.parseInt(baseBean.data.getCartNum());
                                    tvCount.setText(baseBean.data.getCartNum());
                                }
                            } else {
                                number = 0;
                                addGoods.setVisibility(View.VISIBLE);
                                goodsSelected.setVisibility(View.GONE);
                            }

                            if (baseBean.data.getInfo() != null) {
                                goodsName.setText(baseBean.data.getInfo().getName());
                              /*  if (baseBean.data.getInfo().getSpecif() != null) {
                                    String str = "";
                                    for (int i = 0; i < baseBean.data.getInfo().getSpecif().size(); i++) {
                                        if (i != baseBean.data.getInfo().getSpecif().size() - 1) {
                                            str += baseBean.data.getInfo().getSpecif().get(i) + "  ";
                                        } else {
                                            str += baseBean.data.getInfo().getSpecif().get(i);
                                        }
                                    }
                                    if (!TextUtils.isEmpty(str)) {
                                        goodsOne.setText(str);
                                    }
                                }*/
                                goodsSinglePrice.setText(baseBean.data.getInfo().getPrice());
                            }
                            canContentView.smoothScrollTo(0, 0);
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(ClassifiedGoodsActivity.this);
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

        goodsPic.setImages(baseList)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        String imgUrl = Constants.IMG_HOST + path.toString();
                        GlideUtil.display(ClassifiedGoodsActivity.this, imgUrl, imageView);

                    }
                }).setOnBannerListener(new OnBannerListener() {
            /**
             * @param position
             */
            @Override
            public void OnBannerClick(int position) {
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                List bGAPhotoPreviewBaseList = new ArrayList<>();

                for (int i = 0; i < baseList.size(); i++) {
                    bGAPhotoPreviewBaseList.add(Constants.IMG_HOST + baseList.get(i));
                }

                startActivity((BGAPhotoPreviewActivity.newIntent(ClassifiedGoodsActivity.this, null, (ArrayList<String>) bGAPhotoPreviewBaseList, position)));

            }
        }).start();


    }


    @OnClick({R.id.add_goods, R.id.iv_reduce, R.id.iv_plus, R.id.goods_showings, R.id.goods_bottom,
            R.id.category_top_view, R.id.goods_shopping_bag, R.id.tv_empty})
    public void onViewClicked(View view) {
        if (TextUtils.isEmpty(new PreferencesHelper(ClassifiedGoodsActivity.this).getToken())) {
            ToolUtil.loseToLogin(ClassifiedGoodsActivity.this);

        } else {
            switch (view.getId()) {
                case R.id.add_goods:
                    isFirst = true;
                    Log.i("XXX", "add_goods" + "++++++++++++++++" + isFirst);
                    number++;
                    addcart(new PreferencesHelper(ClassifiedGoodsActivity.this).getToken(), shop_id, goods_id, number + "");

                    break;
                case R.id.iv_reduce:
                    isFirst = false;
                    Log.i("XXX", "iv_reduce" + "-----------------" + isFirst);
                    number--;
                    addcart(new PreferencesHelper(ClassifiedGoodsActivity.this).getToken(), shop_id, goods_id, number + "");

                    break;
                case R.id.iv_plus:
                    isFirst = true;
                    Log.i("XXX", "iv_plus" + "-----------------" + isFirst);
                    number++;
                    addcart(new PreferencesHelper(ClassifiedGoodsActivity.this).getToken(), shop_id, goods_id, number + "");

                    break;
                case R.id.goods_showings:

                    Log.e("myisStart",myisStart);

                    if (Integer.parseInt(goodsNumber.getText().toString().trim()) > 0) {
                        if (TextUtils.isEmpty(new PreferencesHelper(ClassifiedGoodsActivity.this).getToken())) {
                            ToolUtil.goToLogin(this);
                        } else {
                            if ("2".equals(myisStart)) {
                                Intent intent = new Intent(ClassifiedGoodsActivity.this, ShopCartConfirmActivity.class);
                                intent.putExtra("shop_id", shop_id);
                                startActivity(intent);
                            } else if ("1".equals(myisStart)) {
                                CookingOrderActivity.startActivityWithParmeter(ClassifiedGoodsActivity.this, shop_id, mTvCall, mTvPhone, mTvTime, mTvPeople, mCanZhuoNum);

                            }
                        }
                    } else {
                        ToastUtil.showToast(ClassifiedGoodsActivity.this, "购物车中暂无商品！");
                    }

                    break;
                case R.id.goods_bottom:
                    if (Integer.parseInt(goodsNumber.getText().toString().trim()) > 0) {
                        changeIcon();
                    } else {
                        ToastUtil.showToast(ClassifiedGoodsActivity.this, "购物车中暂无商品！");
                    }
                    break;
                case R.id.tv_empty:
                    delcart(new PreferencesHelper(ClassifiedGoodsActivity.this).getToken(), shop_id);
                    break;
                case R.id.category_top_view:
                    changeIcon();
                    break;
                case R.id.goods_shopping_bag:
                    if (Integer.parseInt(goodsNumber.getText().toString().trim()) > 0) {
                        changeIcon();
                    } else {
                        ToastUtil.showToast(ClassifiedGoodsActivity.this, "购物车中暂无商品！");
                    }
                    break;
            }
        }
    }

    /**
     * 清空购物车
     *
     * @param token
     * @param shop_id
     */
    private void delcart(String token, final String shop_id) {
        HttpHelp.getInstance().create(RemoteApi.class).delcart(token, shop_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            EventBus.getDefault().post(new CommodityStoreEvent());
                            goodinfo(goods_id, new PreferencesHelper(ClassifiedGoodsActivity.this).getToken(), page);
                            CartList(new PreferencesHelper(ClassifiedGoodsActivity.this).getToken(), shop_id);
                            allProductDialogListviewAdapter.getData().removeAll(allProductDialogListviewAdapter.getData());
                            allProductDialogListviewAdapter.notifyDataSetChanged();
                            changeIcon();
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(ClassifiedGoodsActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }


    /**
     * 单品购物车列表
     */
    private void CartList(String token, String shop_id) {
        HttpHelp.getInstance().create(RemoteApi.class).cartlist(token, shop_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<CartListBean>>(ClassifiedGoodsActivity.this, loadingLayout) {
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
                                    goodsPrice.setText("0.00");
                                } else {
                                    goodsPrice.setText(baseBean.data.getZong_price());
                                }
                                if (TextUtils.isEmpty(baseBean.data.getCount())) {
                                    goodsNumber.setText("0");
                                } else {
                                    goodsNumber.setText(baseBean.data.getCount());
                                }

                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(ClassifiedGoodsActivity.this);
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
     * 购物车
     */
    private void changeIcon() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        float curTranslationY = goodsBottom.getTranslationY();

        ObjectAnimator transparent = null;
        ObjectAnimator transparentView = null;
        ObjectAnimator animator = null;

        if (isElastic) {
            CartList(new PreferencesHelper(ClassifiedGoodsActivity.this).getToken(), shop_id);
            //透明动画
            transparentView = ObjectAnimator.ofFloat(categoryTopView, "alpha", 0f, 1f);

            transparent = ObjectAnimator.ofFloat(categoryBottomLl, "alpha", 0f, 1f);

            animator = ObjectAnimator.ofFloat(categoryBottomLl, "translationY",
                    height - goodsBottom.getHeight(), curTranslationY);
            isElastic = false;

            isjin = true;

            ischu = false;
        } else {
            //透明动画
            transparentView = ObjectAnimator.ofFloat(categoryTopView, "alpha", 1f, 0f);

            transparent = ObjectAnimator.ofFloat(categoryBottomLl, "alpha", 1f, 0f);

            animator = ObjectAnimator.ofFloat(categoryBottomLl, "translationY",
                    curTranslationY, height - goodsBottom.getHeight());
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

    }


    /**
     * 加入购物车
     *
     * @param token
     * @param id
     * @param num
     */
    private void addcart(String token, final String shop_id, String id, String num) {
        HttpHelp.getInstance().create(RemoteApi.class).addcart(token, shop_id, id, num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(ClassifiedGoodsActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            CartList(new PreferencesHelper(ClassifiedGoodsActivity.this).getToken(), shop_id);
                            if (isFirst) {//加
                                Log.i("XXX", "加" + "+++++++++++");
                                tvCount.setText(number + "");
                                addGoods.setVisibility(View.GONE);
                                goodsSelected.setVisibility(View.VISIBLE);
//                                int numc = Integer.parseInt(goodsNumber.getText().toString().trim()) + 1;
//                                goodsNumber.setText(numc + "");
//                                int pricec = Integer.parseInt(goodsPrice.getText().toString().trim()) + Integer.parseInt(goodsSinglePrice.getText().toString().trim());
//                                goodsPrice.setText(pricec + "");

                            } else {//减
                                Log.i("XXX", "减" + "+++++++++++");
                                if (number > 0) {
                                    addGoods.setVisibility(View.GONE);
                                    goodsSelected.setVisibility(View.VISIBLE);
                                    tvCount.setText(number + "");
                                } else {
                                    number = 0;
                                    addGoods.setVisibility(View.VISIBLE);
                                    goodsSelected.setVisibility(View.GONE);
                                }

//                                int numj = Integer.parseInt(goodsNumber.getText().toString().trim()) - 1;
//                                if (numj < 0) {
//                                    CartList(new PreferencesHelper(ClassifiedGoodsActivity.this).getToken(), shop_id);
//                                    goodsNumber.setText("0");
//                                } else {
//                                    goodsNumber.setText("" + numj);
//                                }
//
//                                int pricej = Integer.parseInt(goodsPrice.getText().toString().trim()) - Integer.parseInt(goodsSinglePrice.getText().toString().trim());
//                                if (pricej < 0) {
//                                    goodsPrice.setText("0");
//                                } else {
//                                    goodsPrice.setText(pricej + "");
//                                }

                            }
//                            CartList(ClassifiedGoodsActivity.myToken, shop_id);
                            EventBus.getDefault().post(new CommodityStoreEvent());
                        } else if (baseBean.code == 1) {
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(ClassifiedGoodsActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }


    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {

    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        switch (childView.getId()) {
            case R.id.iv_add:
                isFirst = true;
                allProductDialogListviewAdapter.getData().get(position).setNum((Integer.parseInt(allProductDialogListviewAdapter.getData().get(position).getNum()) + 1) + "");
                addcart(new PreferencesHelper(ClassifiedGoodsActivity.this).getToken(), shop_id, allProductDialogListviewAdapter.getData().get(position).getId(), allProductDialogListviewAdapter.getData().get(position).getNum());

                allProductDialogListviewAdapter.notifyDataSetChanged();

                synchronization(allProductDialogListviewAdapter.getData().get(position).getId(), allProductDialogListviewAdapter.getData().get(position).getNum());

                break;


            case R.id.iv_subtract:
                isFirst = false;
                if (Integer.parseInt(allProductDialogListviewAdapter.getData().get(position).getNum()) - 1 <= 0) {
                    addcart(new PreferencesHelper(ClassifiedGoodsActivity.this).getToken(), shop_id, allProductDialogListviewAdapter.getData().get(position).getId(), "0");
                    allProductDialogListviewAdapter.getData().get(position).setNum("0");
//                            allProductDialogListviewAdapter.getData().remove(allProductDialogListviewAdapter.getData().get(position));
                } else {
                    allProductDialogListviewAdapter.getData().get(position).setNum(Integer.parseInt(allProductDialogListviewAdapter.getData().get(position).getNum()) - 1 + "");
                    addcart(new PreferencesHelper(ClassifiedGoodsActivity.this).getToken(), shop_id, allProductDialogListviewAdapter.getData().get(position).getId(), allProductDialogListviewAdapter.getData().get(position).getNum());
                }

                allProductDialogListviewAdapter.notifyDataSetChanged();

                synchronization(allProductDialogListviewAdapter.getData().get(position).getId(), allProductDialogListviewAdapter.getData().get(position).getNum());

                break;
        }
    }

    private void synchronization(String id, String num) {
        if (id == null || num == null) {
            ToastUtil.showToast(ClassifiedGoodsActivity.this, "失败");
        } else {

            if (id.equals(goods_id)) {
                if ("0".equals(num)) {
                    addGoods.setVisibility(View.VISIBLE);
                    goodsSelected.setVisibility(View.GONE);
                }
                number = Integer.parseInt(num);
            }
        }
    }


    @Override
    public void onLoadMore() {
        page++;
        goodinfo(goods_id, new PreferencesHelper(ClassifiedGoodsActivity.this).getToken(), page);
    }

    @Override
    public void onRefresh() {
        page = 1;
        goodinfo(goods_id, new PreferencesHelper(ClassifiedGoodsActivity.this).getToken(), page);
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
    public static void startActivityWithParmeter(Context context, String shopid, String tvCall, String tvPhone, String tvTime, String peopleNum, String myzhuonum, String goodsid,String isStart) {
        Intent intent = new Intent(context, ClassifiedGoodsActivity.class);
        intent.putExtra("shop_id", shopid);
        intent.putExtra(SHOPCARTTVCALL, tvCall);
        intent.putExtra(SHOPCARTTVPHONE, tvPhone);
        intent.putExtra(SHOPCARTTVTIME, tvTime);
        intent.putExtra(SHOPCARTTVPEOPLENUM, peopleNum);
        intent.putExtra(SHOPMYCANZHUONNUM, myzhuonum);
        intent.putExtra("goods_id", goodsid);
        intent.putExtra("is_start",isStart);
        context.startActivity(intent);
    }


}
