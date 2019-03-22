package com.ruirong.chefang.shoppingcart.cart;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.google.GoogleCircleHookRefreshView;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.LuckyBagPrefectureActivity;
import com.ruirong.chefang.activity.LuckybagPrefectureConfirmOrderActivity;
import com.ruirong.chefang.shoppingcart.adapter.ShoppingCartAdapter;
import com.ruirong.chefang.shoppingcart.adapter.ShoppingCartRecommendAdapter;
import com.ruirong.chefang.shoppingcart.bean.GetGoodsPriceBean;
import com.ruirong.chefang.shoppingcart.bean.RefreshCartEvent;
import com.ruirong.chefang.shoppingcart.bean.ShoppingCartBean;
import com.ruirong.chefang.shoppingcart.bean.ShoppingCartRecommendRean;
import com.ruirong.chefang.util.ToolUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * *
 *   ┏┓　　　┏┓
 * ┏┛┻━━━┛┻┓
 * ┃　　　　　　　┃
 * ┃　　　━　　　┃
 * ┃　┳┛　┗┳　┃
 * ┃　　　　　　　┃
 * ┃　　　┻　　　┃
 * ┃　　　　　　　┃
 * ┗━┓　　　┏━┛
 *     ┃　　　┃   神兽保佑
 *     ┃　　　┃   代码无BUG！
 *     ┃　　　┗━━━┓
 *     ┃　　　　      ┣┓
 *     ┃　　　　　　  ┏┛
 *     ┗┓┓┏━┳┓┏┛
 *       ┃┫┫　┃┫┫
 *       ┗┻┛　┗┻┛
 * Created by guo on 2017/7/7.
 * 购物车
 */

public class ShoppingCartFragment extends BaseFragment implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, ShoppingCartAdapter.OnItemChildClickListener, OnRVItemClickListener, ShoppingCartAdapter.OnItemChildLongClickListener {


    Unbinder unbinder;
    Unbinder unbinder1;
    @BindView(R.id.iv_titlebar_left)
    ImageView ivTitlebarLeft;
    @BindView(R.id.rl_left)
    RelativeLayout rlLeft;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_titlebar_right)
    TextView tvTitlebarRight;
    @BindView(R.id.rl_right)
    RelativeLayout rlRight;
    @BindView(R.id.can_content_view)
    ExpandableListView elvShoppingCart;
    @BindView(R.id.can_refresh_header)
    GoogleCircleHookRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    GoogleCircleHookRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.tv_check_all)
    TextView tvCheckAll;
    @BindView(R.id.tv_real_sum)
    TextView tvRealSum;
    //    @BindView(R.id.tv_sum)
//    TextView tvSum;
//    @BindView(R.id.tv_goto_settlement)
//    TextView tvGotoSettlement;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.rl_show_total_price)
    RelativeLayout rlShowTotalPrice;
    @BindView(R.id.tv_goto_settlement)
    TextView tvGotoSettlement;

    @BindView(R.id.tv_delete)
    TextView tvDelete;


    private ShoppingCartAdapter shoppingCartAdapter;
    private String countTemplate;
    private ShoppingCartRecommendAdapter shoppingCartRecommendAdapter;
    private View header;
    private PreferencesHelper preferencesHelper;
    private int page = 1;
    private int goodsCount;
    private boolean fromCarActivity;

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_shopping_cart, frameLayout, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);

//        titleBar.setVisibility(View.VISIBLE);
//        titleBar.setTitleText("购物车");
//        titleBar.getT
//        TextView rightTextRes = titleBar.getTvRight();
//        rightTextRes.setTextColor(Color.parseColor("#ffffff"));
//        titleBar.setRightTextRes("编辑");
//
//        titleBar.setRightTextClick(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext, "编辑", Toast.LENGTH_SHORT).show();
//            }
//        });

        fromCarActivity = getArguments().getBoolean("fromCarActivity", false);

        titleBar.setVisibility(View.GONE);


        page = 1;
        countTemplate = getResources().getString(R.string.goto_settlement);

        //添加头，购物车为空时
        header = LayoutInflater.from(mContext).inflate(R.layout.header_shopping_cart, null, false);


        //添加脚,推荐的商品
//        View foot = LayoutInflater.from(mContext).inflate(R.layout.foot_shopping_cart, null, false);
//        RecyclerView rvRecommend = (RecyclerView) foot.findViewById(R.id.rv_recommend);
//        shoppingCartRecommendAdapter = new ShoppingCartRecommendAdapter(rvRecommend);
//        rvRecommend.setLayoutManager(new GridLayoutManager(mContext,2));
//        rvRecommend.addItemDecoration(new DividerGridItemDecoration(mContext,R.drawable.rv_grid_wide_division_gray));
//        rvRecommend.setAdapter(shoppingCartRecommendAdapter);
//        shoppingCartRecommendAdapter.setOnRVItemClickListener(this);
//
//        elvShoppingCart.addFooterView(foot);

        //购物车适配器
        shoppingCartAdapter = new ShoppingCartAdapter(mContext);
        elvShoppingCart.setAdapter(shoppingCartAdapter);
        shoppingCartAdapter.setOnItemChildClickListener(this);
        shoppingCartAdapter.setOnItemChildLongClickListener(this);
        elvShoppingCart.setGroupIndicator(null); // 去掉默认带的箭头
        elvShoppingCart.setSelection(0);// 设置默认选中项
        elvShoppingCart.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //返回true则父item不可点击，否则点击父item字布局会折叠
                return true;
            }
        });

        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
        refresh.setStyle(1, 0);

        preferencesHelper = new PreferencesHelper(mContext);

        EventBusUtil.register(this);
    }

    @Override
    public void getData() {
//        String token = preferencesHelper.getToken();
//        if (TextUtils.isEmpty(token)){
//            elvShoppingCart.removeHeaderView(header);
//            elvShoppingCart.addHeaderView(header);
//            llBottom.setVisibility(View.GONE);
//        }else {
//            llBottom.setVisibility(View.VISIBLE);
//            getShopCartList();
//        }
//
//        getShopCartRecommend();

        llBottom.setVisibility(View.VISIBLE);
        getShopCartList();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            String token = preferencesHelper.getToken();
            if (!TextUtils.isEmpty(token)) {
                getShopCartList();
            }
        }
    }


    /**
     * 获取购物车推荐列表
     */
    private void getShopCartRecommend() {
        BaseSubscriber<BaseBean<ShoppingCartRecommendRean>> subscriber = new BaseSubscriber<BaseBean<ShoppingCartRecommendRean>>(mContext, null) {
            @Override
            public void onNext(BaseBean<ShoppingCartRecommendRean> shoppingCartRecommendReanBaseBean) {
                refresh.refreshComplete();
                refresh.loadMoreComplete();
                List<ShoppingCartRecommendRean.ListBean> list = shoppingCartRecommendReanBaseBean.data.getList();
                if (page == 1) {
                    shoppingCartRecommendAdapter.setData(list);
                } else {
                    if (list != null && list.size() > 0) {
                        shoppingCartRecommendAdapter.addMoreData(list);
                    } else {
                        ToastUtil.showToast(mContext, getString(R.string.no_more));
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                refresh.refreshComplete();
                refresh.loadMoreComplete();
            }
        };
//        HttpHelp.getInstance().create(RemoteApi.class).getShopCartRecommend(page)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);


    }

    /**
     * 获取购物车列表
     */
    private void getShopCartList() {
//        HttpHelp.getInstance().create(RemoteApi.class).getShopCartList(preferencesHelper.getToken())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscriber<BaseBean<List<ShoppingCartBean>>>(mContext,null){
//                    @Override
//                    public void onNext(BaseBean<List<ShoppingCartBean>> listBaseBean) {
//                        List<ShoppingCartBean> data = listBaseBean.data;
//                        if (data!=null&&data.size()>0){
//                            llBottom.setVisibility(View.VISIBLE);
//                            elvShoppingCart.removeHeaderView(header);
//                            shoppingCartAdapter.setData(listBaseBean.data);
//                            // 遍历所有group,将所有项设置成默认展开
//                            int groupCount = shoppingCartAdapter.getGroupCount();
//                            for (int i = 0; i < groupCount; i++) {
//                                elvShoppingCart.expandGroup(i);
//                            }
//                            initAllCheckStatus();
//                            shoppingCartAdapter.notifyDataSetChanged();
//                            refreshSum();
//
//                        }else {
//                            llBottom.setVisibility(View.GONE);
//                            shoppingCartAdapter.setData(null);
//                            elvShoppingCart.removeHeaderView(header);
//                            elvShoppingCart.addHeaderView(header);
//                        }
//
//                    }
//                });
        List<ShoppingCartBean> listData = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            ShoppingCartBean shoppingCartBean = new ShoppingCartBean();
            shoppingCartBean.setName("name");
            shoppingCartBean.setStore_id("333");

            List<ShoppingCartBean.GoodsBean> goodsBeans = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                ShoppingCartBean.GoodsBean goodsBean = new ShoppingCartBean.GoodsBean();
                /**
                 * id : 26
                 * goods_id : 173
                 * goods_num : 3
                 * selected : 1
                 * goods_name : 美食世界
                 * index_pic : /Uploads/Picture/2017-08-21/599a9218acbc9.jpg
                 * shop_price : 12.00
                 * spec_xuan : 3_167
                 * spec_xuan_name : 颜色3:绿色 大小:小
                 * shixiao : 0
                 */
                goodsBean.setId("22");
                goodsBean.setGoods_id("22");
                goodsBean.setGoods_num(4);
                goodsBean.setSelected(1);
                goodsBean.setGoods_name("美食世界");
                goodsBean.setIndex_pic("/Uploads/Picture/2017-08-21/599a9218acbc9.jpg");
                goodsBean.setShop_price("22.00");
                goodsBean.setSpec_xuan("3_167");
                goodsBean.setSpec_xuan_name(" 颜色3:绿色 大小:小");
                goodsBean.setShixiao(1);

                goodsBeans.add(goodsBean);
            }
            shoppingCartBean.setGoods(goodsBeans);
            listData.add(shoppingCartBean);
        }
        llBottom.setVisibility(View.VISIBLE);
        elvShoppingCart.removeHeaderView(header);
        shoppingCartAdapter.setData(listData);
        // 遍历所有group,将所有项设置成默认展开
        int groupCount = shoppingCartAdapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            elvShoppingCart.expandGroup(i);
        }
        initAllCheckStatus();
        shoppingCartAdapter.notifyDataSetChanged();
        refreshSum();
    }

    /**
     * 全选or取消全选
     */
    @OnClick(R.id.tv_check_all)
    public void checkAll() {
        boolean selected = tvCheckAll.isSelected();
        StringBuffer cardIds = new StringBuffer();
        for (ShoppingCartBean bean : shoppingCartAdapter.getData()) {
            for (ShoppingCartBean.GoodsBean goodsBean : bean.getGoods()) {
                if (goodsBean.getShixiao() == 1) {
                    cardIds.append(goodsBean.getId());
                    cardIds.append(",");
                }

            }

        }
        if (cardIds.length() > 0) {
            cardIds.deleteCharAt(cardIds.length() - 1);

            editManyGoods(null, cardIds.toString(), selected ? 0 : 1);
        }

    }


    /**
     * 刷新总金额
     */
    private void refreshSum() {
        BigDecimal sum = BigDecimal.valueOf(0);
        goodsCount = 0;

        for (ShoppingCartBean bean : shoppingCartAdapter.getData()) {
            for (ShoppingCartBean.GoodsBean goodsBean : bean.getGoods()) {
                if (goodsBean.getSelected() == 1 && goodsBean.getShixiao() == 1) {
                    goodsCount += goodsBean.getGoods_num();
                    sum = sum.add(BigDecimal.valueOf(Double.parseDouble(goodsBean.getShop_price())).multiply(new BigDecimal(goodsBean.getGoods_num())));
                }
            }

        }

        tvRealSum.setText("" + sum.doubleValue());
//        tvSum.setText("总额：" + sum.doubleValue());
        tvGotoSettlement.setText(String.format(countTemplate, goodsCount));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLoadMore() {
        page++;
        getShopCartRecommend();
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData();
    }

    /**
     * 下单
     */
    @OnClick(R.id.tv_goto_settlement)
    public void gotoWriteOrder() {
//        if (goodsCount == 0) {
//            ToastUtil.showToast(mContext, "您还没有选择商品哦");
//        } else {
//            StringBuffer cardIds = new StringBuffer();
//            for (ShoppingCartBean bean : shoppingCartAdapter.getData()) {
//                for (ShoppingCartBean.GoodsBean goodsBean : bean.getGoods()) {
//                    if (goodsBean.getSelected() == 1 && goodsBean.getShixiao() == 1) {
//                        cardIds.append(goodsBean.getId());
//                        cardIds.append(",");
//                    }
//                }
//
//            }
//            if (cardIds.length() > 0) {
//
//                cardIds.deleteCharAt(cardIds.length() - 1);
//
////                WriteOrderActivity.startActivity(mContext,cardIds.toString());
//            }
////            submit();
//        }
        if (TextUtils.isEmpty(new PreferencesHelper(getActivity()).getToken())) {
            ToolUtil.goToLogin(getContext());
        } else {
            Intent intent1 = new Intent(getActivity(), LuckybagPrefectureConfirmOrderActivity.class);
            startActivity(intent1);
        }
    }

    @Override
    public void onItemChildClick(View view, int groupPosition, int childPosition) {
        ShoppingCartBean shoppingCartBean = shoppingCartAdapter.getData().get(groupPosition);
        switch (view.getId()) {
            case R.id.iv_shop_status:
                //店铺全选或取消全选

                boolean isChecked = shoppingCartBean.isChecked;
                StringBuffer cardIds = new StringBuffer();
                for (ShoppingCartBean.GoodsBean goodsBean : shoppingCartBean.getGoods()) {
                    if (goodsBean.getShixiao() == 1) {
                        cardIds.append(goodsBean.getId());
                        cardIds.append(",");
                    }
                }
                if (cardIds.length() > 0) {
                    cardIds.deleteCharAt(cardIds.length() - 1);
                    editManyGoods(shoppingCartBean, cardIds.toString(), isChecked ? 0 : 1);
                }

                break;
            case R.id.iv_status:
                //商品选择框
                ShoppingCartBean.GoodsBean goodsBean1 = shoppingCartBean.getGoods().get(childPosition);
                int selected = goodsBean1.getSelected();

                editShopCart(shoppingCartBean, goodsBean1, 3, null, null, (selected == 0 ? 1 : 0) + "", false);
                break;
//            case R.id.tv_property:
//                //物品属性
//                ShoppingCartBean.GoodsBean goodsBean2 = shoppingCartBean.getGoods().get(childPosition);
////                showPropertyListDialog(shoppingCartBean,goodsBean2);
//                break;
//            case R.id.iv_subtract:
//                //数量减一
//                ShoppingCartBean.GoodsBean goodsBean3 = shoppingCartBean.getGoods().get(childPosition);
//
//                int count = goodsBean3.getGoods_num();
//                if (count > 1) {
//                    count--;
//
//                    editShopCart(shoppingCartBean, goodsBean3, 1, count + "", null, "1", false);
//                } else {
//                    ToastUtil.showToast(mContext, "不能再少啦");
//                }
//
//                break;
//            case R.id.iv_add:
//                //数量加一
//                ShoppingCartBean.GoodsBean goodsBean4 = shoppingCartBean.getGoods().get(childPosition);
//                int count2 = goodsBean4.getGoods_num();
//                count2++;
//
//                editShopCart(shoppingCartBean, goodsBean4, 1, count2 + "", null, "1", false);
//                break;
            case R.id.item_view:
                //商品详情
//                GoodsDetailActivity.startActivity(mContext,((ShoppingCartBean.GoodsBean)shoppingCartAdapter.getChild(groupPosition,childPosition)).getGoods_id(),true);
                break;
            default:

                Intent intent1 = new Intent(getActivity(), LuckyBagPrefectureActivity.class);
                intent1.putExtra("int_data", 100);
                startActivity(intent1);
                break;

        }
    }

    /**
     * 属性选择控件
     */
//    private void showPropertyListDialog(final ShoppingCartBean shoppingCartBean, final ShoppingCartBean.GoodsBean goodsBean) {
//        final String goodsId = goodsBean.getGoods_id();
//        final Map<String,String> selectProperty=new HashMap<>();//父id和子id
//        viewShade.setVisibility(View.VISIBLE);
//        final Dialog mDialog = new Dialog(mContext, R.style.dialog_in_bottom);
//        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_choice_goods_property, null);
//        mDialog.setContentView(contentView);
//        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
//        layoutParams.width = ScreenUtil.getScreenWidth(mContext);
//        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        contentView.setLayoutParams(layoutParams);
//        Window window = mDialog.getWindow();
//        window.setGravity(Gravity.BOTTOM);
//        mDialog.setCanceledOnTouchOutside(true);
//        mDialog.setCancelable(true);
//        mDialog.show();
//        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                viewShade.setVisibility(View.GONE);
//            }
//        });
//        ImageView ivGoods = (ImageView) contentView.findViewById(R.id.iv_goods);
//        final TextView tvPrice = (TextView) contentView.findViewById(R.id.tv_price);
//        final TextView tvInventoryCount = (TextView) contentView.findViewById(R.id.tv_inventory_count);
//        RecyclerView rvGoodsProperty = (RecyclerView) contentView.findViewById(R.id.rv_goods_property);
//        final GoodsPropertyAdapter goodsPropertyAdapter = new GoodsPropertyAdapter(rvGoodsProperty);
//        rvGoodsProperty.setLayoutManager(new LinearLayoutManager(mContext));
//        rvGoodsProperty.setAdapter(goodsPropertyAdapter);
//
//
//        final String[] properties = goodsBean.getSpec_xuan().split("_");
//
//
//        //获取商品属性
//        HttpHelp.getInstance().create(RemoteApi.class).getGoodsProperty(preferencesHelper.getToken(),goodsId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscriber<BaseBean<GoodsDetailBean.SpecBeanX>>(mContext,null){
//                    @Override
//                    public void onNext(BaseBean<GoodsDetailBean.SpecBeanX> specBeanXBaseBean) {
//                        List<GoodsPropertyBean> spec = specBeanXBaseBean.data.getSpec();
//                        int propertiesCount = spec.size();
//                        for (int i=0;i<propertiesCount;i++){
//                            String selectedProperty = properties[i];
//                            GoodsPropertyBean goodsPropertyBean = spec.get(i);
//                            List<GoodsPropertyBean.ItemBean> itemList = goodsPropertyBean.getItem();
//                            int itemCount = itemList.size();
//
//                            for (int j=0;j<itemCount;j++){
//                                if (selectedProperty.equals(itemList.get(j).getId())){
//                                    selectProperty.put(goodsPropertyBean.getId(),itemList.get(j).getId());
//                                    goodsPropertyBean.selectPosition=j;
//                                    break;
//                                }
//                            }
//                        }
//
//                        goodsPropertyAdapter.setData(spec);
//
//                    }
//                });
//
//        //选择属性更新价格
//        goodsPropertyAdapter.setOnItemClickListener(new GoodsPropertyAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int parentPosition, int childPosition) {
//                GoodsPropertyBean goodsPropertyBean = goodsPropertyAdapter.getData().get(parentPosition);
//                selectProperty.put(goodsPropertyBean.getId(),goodsPropertyBean.getItem().get(childPosition).getId());
//                StringBuffer properties=new StringBuffer();
//                for (String key:selectProperty.keySet()){
//                    properties.append(selectProperty.get(key));
//                    properties.append("_");
//                }
//                properties.deleteCharAt(properties.length()-1);
//                refreshPrice(goodsId,properties.toString(),tvPrice,tvInventoryCount);
//            }
//        });
//
//        GlideUtil.display(mContext,goodsBean.getIndex_pic(),ivGoods);
//        //初始化选中的属性的价格
//        refreshPrice(goodsId,goodsBean.getSpec_xuan(),tvPrice,tvInventoryCount);
//
//        contentView.findViewById(R.id.tv_commit).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int count = Integer.parseInt(tvInventoryCount.getText().toString());
//                if (count>0){
//                    mDialog.dismiss();
//                    StringBuffer properties=new StringBuffer();
//                    for (String key:selectProperty.keySet()){
//                        properties.append(selectProperty.get(key));
//                        properties.append("_");
//                    }
//                    properties.deleteCharAt(properties.length()-1);
//
//                    editShopCart(shoppingCartBean,goodsBean,2,null,properties.toString(),"1",true);
//                }else {
//                    ToastUtil.showToast(mContext,getString(R.string.inventory_count_hint));
//                }
//
//
//
//            }
//        });
//    }

    /**
     * 根据属性获取价格和库存
     *
     * @param goodsId
     * @param properties
     * @param tvPrice
     * @param tvInventoryCount
     */
    private void refreshPrice(String goodsId, String properties, final TextView tvPrice, final TextView tvInventoryCount) {
        showLoadingDialog("");

        BaseSubscriber<BaseBean<GetGoodsPriceBean>> getPriceSubscriber = new BaseSubscriber<BaseBean<GetGoodsPriceBean>>(mContext, null) {
            @Override
            public void onNext(BaseBean<GetGoodsPriceBean> getGoodsPriceBeanBaseBean) {
                hideLoadingDialog();
                GetGoodsPriceBean data = getGoodsPriceBeanBaseBean.data;
                tvPrice.setText("￥" + data.getPrice());
                tvInventoryCount.setText(data.getStore_count() + "");
            }

            @Override
            public void onError(Throwable throwable) {
                hideLoadingDialog();
            }
        };
//        HttpHelp.getInstance().create(RemoteApi.class).getGoodsPrice(goodsId,properties)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(getPriceSubscriber);
    }


    /**
     * 刷新所有checkBox状态
     *
     * @param shoppingCartBean
     */
    private void refreshAllCheckStatus(ShoppingCartBean shoppingCartBean) {
        //单选完商品后更改店铺选中状态
        boolean allChecked = true;
        for (ShoppingCartBean.GoodsBean goodsBean : shoppingCartBean.getGoods()) {
            if (!(goodsBean.getSelected() == 1)) {
                allChecked = false;
                break;
            }
        }
        shoppingCartBean.isChecked = allChecked;

        //更改店铺状态后还要改变底部全选状态
        boolean allShopIsChecked = true;
        for (ShoppingCartBean bean : shoppingCartAdapter.getData()) {
            if (!bean.isChecked) {
                allShopIsChecked = false;
                break;
            }
        }
        tvCheckAll.setSelected(allShopIsChecked);
    }

    /**
     * 初始化状态
     */
    private void initAllCheckStatus() {
        //每个店铺的选中状态
        for (ShoppingCartBean bean : shoppingCartAdapter.getData()) {
            boolean allChecked = true;
            for (ShoppingCartBean.GoodsBean goodsBean : bean.getGoods()) {
                if (!(goodsBean.getSelected() == 1 && goodsBean.getShixiao() == 1)) {
                    allChecked = false;
                    break;
                }
            }
            bean.isChecked = allChecked;
        }

        //更改店铺状态后还要改变底部全选状态
        boolean allShopIsChecked = true;
        for (ShoppingCartBean bean : shoppingCartAdapter.getData()) {
            if (!bean.isChecked) {
                allShopIsChecked = false;
                break;
            }
        }
        tvCheckAll.setSelected(allShopIsChecked);
    }


    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        //点击推荐列表item进详情
//        GoodsDetailActivity.startActivity(mContext,shoppingCartRecommendAdapter.getItem(position).getGoods_id(),true);

    }


    /**
     * 编辑购物车
     *
     * @param
     * @param type
     * @param num
     * @param properties
     * @param selected
     * @param refreshLst 成功后是否刷新列表
     */
    private void editShopCart(final ShoppingCartBean shoppingCartBean, final ShoppingCartBean.GoodsBean goodsBean, final int type, final String num, String properties, final String selected, final boolean refreshLst) {
//        HttpHelp.getInstance().create(RemoteApi.class).editShopCart(preferencesHelper.getToken(),goodsBean.getId(),type,num,properties,selected)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext,null){
//                    @Override
//                    public void onNext(BaseBean<Object> objectBaseBean) {
//                        if (objectBaseBean.code==0){
//                            if (type==1){
//                                goodsBean.setGoods_num(Integer.parseInt(num));
//                                goodsBean.setSelected(1);
//                                refreshAllCheckStatus(shoppingCartBean);
//
//                                shoppingCartAdapter.notifyDataSetChanged();
//                                refreshSum();
//                            }else if (type==3){
//                                goodsBean.setSelected(Integer.parseInt(selected));
//                                refreshAllCheckStatus(shoppingCartBean);
//
//                                shoppingCartAdapter.notifyDataSetChanged();
//                                refreshSum();
//                            }
//                            if (refreshLst){
//                                getShopCartList();
//                            }
//                        }else {
//                            ToastUtil.showToast(mContext,objectBaseBean.message);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        ToastUtil.showToast(mContext,"操作失败");
//                    }
//                });
        if (type == 1) {
            goodsBean.setGoods_num(Integer.parseInt(num));
            goodsBean.setSelected(1);
            refreshAllCheckStatus(shoppingCartBean);

            shoppingCartAdapter.notifyDataSetChanged();
            refreshSum();
        } else if (type == 3) {
            goodsBean.setSelected(Integer.parseInt(selected));
            refreshAllCheckStatus(shoppingCartBean);

            shoppingCartAdapter.notifyDataSetChanged();
            refreshSum();
        }
        if (refreshLst) {
            getShopCartList();
        }
    }

    /**
     * @param shoppingCartBean 不为null则表示一个店铺里的全选，为null则表示整个购物车的全选
     * @param cartIds
     * @param selected
     */
    private void editManyGoods(final ShoppingCartBean shoppingCartBean, String cartIds, final int selected) {
//        HttpHelp.getInstance().create(RemoteApi.class).editManyGoods(preferencesHelper.getToken(),cartIds,selected)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext,null){
//                    @Override
//                    public void onNext(BaseBean<Object> objectBaseBean) {
//                        if (objectBaseBean.code==0){
//                            if (shoppingCartBean==null){
//                                tvCheckAll.setSelected(selected==1);
//                                for (ShoppingCartBean bean:shoppingCartAdapter.getData()){
//                                    bean.isChecked=selected==1;
//                                    for (ShoppingCartBean.GoodsBean goodsBean:bean.getGoods()){
//                                        if (goodsBean.getShixiao()==1){
//                                            goodsBean.setSelected(selected);
//                                        }
//                                    }
//
//                                }
//
//                                shoppingCartAdapter.notifyDataSetChanged();
//                                refreshSum();
//
//                            }else {
//                                shoppingCartBean.isChecked=selected==1;
//                                for (ShoppingCartBean.GoodsBean goodsBean:shoppingCartBean.getGoods()){
//                                    if (goodsBean.getShixiao()==1){
//                                        goodsBean.setSelected(selected);
//                                    }
//
//                                }
//
//                                //更改店铺状态后还要改变底部全选状态
//                                boolean allShopIsChecked=true;
//                                for (ShoppingCartBean bean:shoppingCartAdapter.getData()){
//                                    if (!bean.isChecked){
//                                        allShopIsChecked=false;
//                                        break;
//                                    }
//                                }
//                                tvCheckAll.setSelected(allShopIsChecked);
//
//                                shoppingCartAdapter.notifyDataSetChanged();
//                                refreshSum();
//                            }
//                        }else {
//                            ToastUtil.showToast(mContext,"操作失败");
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        ToastUtil.showToast(mContext,"操作失败");
//                    }
//                });
    }

    /**
     * 商品item长按监听
     *
     * @param view
     * @param groupPosition
     * @param childPosition
     */
    @Override
    public void onItemLongChildClick(View view, final int groupPosition, final int childPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示")
                .setMessage("真的要删除吗")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCart(((ShoppingCartBean.GoodsBean) shoppingCartAdapter.getChild(groupPosition, childPosition)).getId());
                    }
                });
        builder.create().show();
    }

    /**
     * 购物车删除商品
     *
     * @param cartId
     */
    private void deleteCart(String cartId) {
//        HttpHelp.getInstance().create(RemoteApi.class).deleteCart(preferencesHelper.getToken(),cartId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext,null){
//                    @Override
//                    public void onNext(BaseBean<Object> objectBaseBean) {
//                        if (objectBaseBean.code==0){
//                            getShopCartList();
//                        }
//                    }
//                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    /**
     * 支付成功后或放弃支付后刷新购物车
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshCart(RefreshCartEvent event) {
        getShopCartList();
    }


    @Override
    public void onReload(View v) {
        page = 1;
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }


//    private void submit() {
//        Dialog mDialog = new Dialog(getActivity(), R.style.ActionSheetDialogStyle);
//        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.nwb_daglon, null);
//        mDialog.setContentView(inflate);
//        Window dialogwindow=mDialog.getWindow();
//
//        dialogwindow.setGravity(Gravity.BOTTOM);
//        WindowManager.LayoutParams lp=dialogwindow.getAttributes();
//        lp.windowAnimations = R.style.PopUpBottomAnimation;
//        lp.width=WindowManager.LayoutParams.MATCH_PARENT;
//
//        dialogwindow.setAttributes(lp);
//        mDialog.show();
//    }


    @OnClick({R.id.rl_left, R.id.rl_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_left:  //获取图片码

                getActivity().finish();
                break;
            case R.id.rl_right:  //获取图片码
                if (tvTitlebarRight.getText().equals("编辑")) {
//                          rl_show_total_price
//                          tv_goto_settlement
                    rlShowTotalPrice.setVisibility(View.GONE);
                    tvGotoSettlement.setVisibility(View.GONE);
                    tvDelete.setVisibility(View.VISIBLE);
                    tvTitlebarRight.setText("完成");
                } else {
                    tvDelete.setVisibility(View.GONE);

                    rlShowTotalPrice.setVisibility(View.VISIBLE);
                    tvGotoSettlement.setVisibility(View.VISIBLE);
                    tvTitlebarRight.setText("编辑");

                }

                break;
        }
    }
}
