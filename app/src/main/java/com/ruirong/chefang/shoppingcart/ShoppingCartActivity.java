package com.ruirong.chefang.shoppingcart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.LuckybagPrefectureConfirmOrderActivity;
import com.ruirong.chefang.activity.SpecialtyDetailsActivity;
import com.ruirong.chefang.bean.ShoppingCartBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.shoppingcart.adapter.ShoppingCartAdapter;
import com.ruirong.chefang.util.ToolUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by EDZ on 2018/3/28.
 * 购物车
 */

public class ShoppingCartActivity extends BaseActivity implements View.OnClickListener, CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnRVItemClickListener {
    @BindView(R.id.can_content_view)
    RecyclerView canContentView;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.tv_check_all)
    CheckBox tvCheckAll;
    @BindView(R.id.tv_real_sum)
    TextView tvRealSum;
    @BindView(R.id.rl_show_total_price)
    RelativeLayout rlShowTotalPrice;
    @BindView(R.id.tv_goto_settlement)
    TextView tvGotoSettlement;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.lls_bottom)
    RelativeLayout llsBottom;

    private int page = 1;
    Map<Integer, Integer> map = new HashMap<>();
    //    private Map<Integer, Boolean> checkStates = new HashMap<>();
//    List<ShoppingCartBean.ListBean> baseList = new ArrayList<>();
    private ShoppingCartAdapter adapter;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_left:
                finish();
                break;

        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_shop_cart;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("购物车");
        titleBar.setRightTextRes("编辑");
        titleBar.setRightTextClick(rightClickListener);

        adapter = new ShoppingCartAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(this));
        canContentView.addItemDecoration(new RecycleViewDivider(this));
        canContentView.setAdapter(adapter);
        adapter.setOnRVItemClickListener(this);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
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


        tvDelete.setOnClickListener(onClickListener);
        tvCheckAll.setOnClickListener(onClickListener);
        tvGotoSettlement.setOnClickListener(onClickListener);
    }

    View.OnClickListener rightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (titleBar.getRightTextRes().toString().equals("编辑")) {
                tvGotoSettlement.setVisibility(View.GONE);
                rlShowTotalPrice.setVisibility(View.GONE);
                tvDelete.setVisibility(VISIBLE);
                titleBar.setRightTextRes("完成");
                adapter.setIsDisplay(true);
//                if (checkStates.size() > 0 && checkStates != null) {   //把所有的都变成false
//                    for (int i = 0; i < checkStates.size(); i++) {
//                        checkStates.put(i, false);
//                    }
//                }

                List<ShoppingCartBean.ListBean> data = adapter.getData();
                if (null != data && data.size() != 0) {
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).setChoosed(false);
                    }
                }

                adapter.setIsDisplay(true);
                adapter.notifyDataSetChanged();

                refresh.loadMoreComplete();
                refresh.refreshComplete();
            } else if (titleBar.getRightTextRes().toString().equals("完成")) {
                tvGotoSettlement.setVisibility(VISIBLE);
                rlShowTotalPrice.setVisibility(VISIBLE);
                tvDelete.setVisibility(View.GONE);

//                if (checkStates.size() > 0 && checkStates != null) { //把所有的都变成false
//                    for (int i = 0; i < checkStates.size(); i++) {
//                        checkStates.put(i, false);
//                    }
//                }

                List<ShoppingCartBean.ListBean> data = adapter.getData();
                if (null != data && data.size() != 0) {
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).setChoosed(false);
                    }
                }

                if (map.size() > 0 && map != null) {
                    Set<Integer> keys = map.keySet();
                    Iterator iterator = keys.iterator();
                    List<Integer> keyset = new ArrayList<>();
                    while (iterator.hasNext()) {
                        keyset.add((Integer) iterator.next());
                    }
                    for (int i = 0; i < keyset.size(); i++) {
                        deleteGoods(keyset.get(i), map.get(keyset.get(i)));
                    }
                    map.clear();
                }
                adapter.clear();
                getDataList();
                onRefresh();
                titleBar.setRightTextRes("编辑");
                adapter.setIsDisplay(false);
                adapter.notifyDataSetChanged();
            }
        }
    };
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_delete://删除
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingCartActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("确定要删除该商品么？");
                    builder.setNegativeButton("取消", null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

//                            if (checkStates.size() > 0 && checkStates != null) {// 对
//                                for (int i = 0; i < checkStates.size(); i++) {
//                                    if (checkStates.get(i)) {
//                                        deleteShopping(Integer.valueOf(baseList.get(i).getId()));
//                                    }
//                                }
//                            }

                            List<ShoppingCartBean.ListBean> data = adapter.getData();
                            if (null != data && data.size() != 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    if (data.get(i).getChoosed()) {
                                        deleteShopping(Integer.valueOf(data.get(i).getId()));
                                    }
                                }
                            }

                        }
                    });
                    builder.create().show();

                    break;
                case R.id.tv_check_all://全选
                    Boolean isTrue = tvCheckAll.isChecked();
                    if (isTrue) {
//                        if (checkStates.size() > 0 && checkStates != null) {  //都变成true
//                            for (int i = 0; i < checkStates.size(); i++) {
//                                checkStates.put(i, true);
//                            }
//                        }

                        List<ShoppingCartBean.ListBean> data = adapter.getData();
                        if (null != data && data.size() != 0) {
                            for (int i = 0; i < data.size(); i++) {
                                data.get(i).setChoosed(true);
                            }
                        }

                    } else {
//                        if (checkStates.size() > 0 && checkStates != null) {  //都变成false
//                            for (int i = 0; i < checkStates.size(); i++) {
//                                checkStates.put(i, false);
//                            }
//                        }

                        List<ShoppingCartBean.ListBean> data = adapter.getData();
                        if (null != data && data.size() != 0) {
                            for (int i = 0; i < data.size(); i++) {
                                data.get(i).setChoosed(false);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.tv_goto_settlement://结算
                    List<ShoppingCartBean.ListBean> data = adapter.getData();

                    if (data.size() > 0 && data != null && !"￥0.0".equals(tvRealSum)) {
                        String id = "";
                        int count = 0;
                        List<String> idList = new ArrayList<>();
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i).getChoosed()) {
                                //商品ID
                                idList.add(data.get(i).getComid());
//                                //总数量
//                                int number = Integer.valueOf(baseList.get(i).getNumbers());
//                                count += number;
                            }
                        }

                        if (idList != null && idList.size() > 0) {
                            for (int i = 0; i < idList.size(); i++) {
                                if (i != idList.size() - 1) {
                                    id += idList.get(i) + ",";
                                } else {
                                    id += idList.get(i);
                                }
                            }
                        } else {
                            ToastUtil.showToast(ShoppingCartActivity.this, "购物车中暂无商品");
                            return;
                        }

                        if (TextUtils.isEmpty(id)) {
                            ToastUtil.showToast(ShoppingCartActivity.this, "数据错误");
                            return;
                        }

                        Intent intent = new Intent(ShoppingCartActivity.this, LuckybagPrefectureConfirmOrderActivity.class);
                        intent.putExtra("specialty_id", id);
                        intent.putExtra("Specialty_Type", "1");
                        if (TextUtils.isEmpty(new PreferencesHelper(ShoppingCartActivity.this).getToken())) {
                            ToolUtil.goToLogin(ShoppingCartActivity.this);
                        } else {
                            startActivity(intent);
                            finish();
                        }


                    }

                    break;
                default:
                    break;

            }
        }
    };

    @Override
    public void getData() {
        getDataList();
        tvCheckAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //  if(isChecked){
                totalPrice = 0f;
                List<ShoppingCartBean.ListBean> data = adapter.getData();
                if (null != data && data.size() != 0) {

                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).getChoosed())
                            addPrice(Float.valueOf(data.get(i).getNow_price()), Integer.parseInt(data.get(i).getNumbers()));
                    }
                }
                tvRealSum.setText("￥" + totalPrice);
                //       }
            }
        });
    }

    public void getDataList() {
        HttpHelp.getRetrofit(this).create(RemoteApi.class).getShoppingCart(page, new PreferencesHelper(ShoppingCartActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ShoppingCartBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<ShoppingCartBean> baseBean) {
                        super.onNext(baseBean);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            List<ShoppingCartBean.ListBean> baseList = baseBean.data.getList();

                            Boolean isTrue = tvCheckAll.isChecked();

                            if (isTrue) {
                                for (int i = 0; i < baseList.size(); i++) {
                                    baseList.get(i).setChoosed(true);
                                }
                            }
                            // TODO 测试让去掉和iOS一致。
                            // titleBar.setTitleText("购物车(" + baseList.size() + ")");
                            tvGotoSettlement.setText("结算" + "(" + baseList.size() + ")");
                            if (page == 1) {
                                if (baseList != null && baseList.size() > 0) {
                                    adapter.setData(baseList);
                                    refresh.setVisibility(VISIBLE);
                                    rlEmpty.setVisibility(GONE);
                                } else {
                                    refresh.setVisibility(GONE);
                                    rlEmpty.setVisibility(VISIBLE);
                                }
                            } else {
                                if (baseList != null && baseList.size() > 0) {
                                    adapter.addMoreData(baseList);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    ToastUtil.showToast(ShoppingCartActivity.this, getString(R.string.no_more));
                                }
                            }

//                            checkStates.clear();
//                            initCheckedData();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                    }
                });

    }

//    private void initCheckedData() {
//        for (int i = 0; i < adapter.getData().size(); i++) {
//            checkStates.put(i, false);
//        }
//    }

    @Override
    public void onLoadMore() {
        page++;
        refresh.loadMoreComplete();
        refresh.refreshComplete();
        getDataList();
    }

    @Override
    public void onRefresh() {
        page = 1;
        refresh.loadMoreComplete();
        refresh.refreshComplete();
        getDataList();
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        List<ShoppingCartBean.ListBean> data = adapter.getData();

        if ("1".equals(data.get(position).getState()) && "0".equals(data.get(position).getId_del())) {

        } else {
            Intent intent = new Intent(ShoppingCartActivity.this, SpecialtyDetailsActivity.class);
            intent.putExtra("Specialty_id", data.get(position).getComid());
            if (TextUtils.isEmpty(new PreferencesHelper(ShoppingCartActivity.this).getToken())) {
                ToolUtil.goToLogin(this);
            } else {
                startActivity(intent);
            }
        }
    }


    //adapter
    class ShoppingCartAdapter extends RecyclerViewAdapter<ShoppingCartBean.ListBean> {
        Boolean isDisplay = false;

        public void setIsDisplay(Boolean isDisplay) {
            this.isDisplay = isDisplay;
        }

        public ShoppingCartAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.item_shopping_cart_goods);
        }

        @Override
        protected void fillData(ViewHolderHelper viewHolderHelper, final int position, final ShoppingCartBean.ListBean model) {


            final float price = Float.valueOf(model.getNow_price());
            final int allNumber = Integer.valueOf(model.getNumbers());

            GlideUtil.display(ShoppingCartActivity.this, Constants.IMG_HOST + model.getPic(), viewHolderHelper.getImageView(R.id.iv_goods));
            viewHolderHelper.setText(R.id.tv_goods_name, model.getName());
            viewHolderHelper.setText(R.id.tv_price, "￥" + model.getNow_price());
            viewHolderHelper.setText(R.id.tv_number, "x" + model.getNumbers());

            ShoppingCartBean.ListBean bean = adapter.getData().get(position);

            CheckBox checkBox = (CheckBox) viewHolderHelper.getView(R.id.iv_status);
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(false);//设置默认的
            checkBox.setChecked(bean.getChoosed());//一开始都是false

            ImageView add = viewHolderHelper.getImageView(R.id.img_add);
            ImageView less = viewHolderHelper.getImageView(R.id.img_less);
            final TextView number = viewHolderHelper.getTextView(R.id.tv_delete_number);
            final TextView numbers = viewHolderHelper.getTextView(R.id.tv_number);
            number.setText(model.getNumbers());


            //显示结算按钮
            if (isDisplay) {
                viewHolderHelper.setVisibility(R.id.ll_add_less, VISIBLE);
                viewHolderHelper.setVisibility(R.id.tv_number, GONE);
                if (!"1".equals(model.getState()) && !"0".equals(model.getId_del())) {
                    viewHolderHelper.setVisibility(R.id.tv_shopping_is, VISIBLE);
                    viewHolderHelper.setVisibility(R.id.ll_add_less, GONE);
                    viewHolderHelper.setVisibility(R.id.iv_status, VISIBLE);
                } else {
                    viewHolderHelper.setVisibility(R.id.tv_shopping_is, GONE);
                }
            } else {
                viewHolderHelper.setVisibility(R.id.ll_add_less, GONE);
                viewHolderHelper.setVisibility(R.id.tv_number, VISIBLE);
                if (!"1".equals(model.getState()) && !"0".equals(model.getId_del())) {
                    viewHolderHelper.setVisibility(R.id.tv_shopping_is, VISIBLE);
                    viewHolderHelper.setVisibility(R.id.iv_status, INVISIBLE);
//                    checkBox.setChecked(false);
                } else {
                    viewHolderHelper.setVisibility(R.id.tv_shopping_is, GONE);
                }
            }

            final int id = Integer.valueOf(model.getId());
            //两个按钮的监听
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = Integer.valueOf(number.getText().toString());
                    count++;
                    number.setText(String.valueOf(count));
                    numbers.setText("x" + String.valueOf(count));
                    map.put(id, count);
                }
            });
            less.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = Integer.valueOf(number.getText().toString());
                    count--;
                    if (count < 1) {
                        number.setText("1");
                        numbers.setText("x" + "1");
                        map.put(id, 1);
                    } else {
                        number.setText(String.valueOf(count));
                        numbers.setText("x" + String.valueOf(count));
                        map.put(id, count);
                    }
                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
//                        checkStates.put(position, isChecked);

                        List<ShoppingCartBean.ListBean> data = adapter.getData();
                        data.get(position).setChoosed(isChecked);
//                        model.setChoosed(isChecked);
                        addPrice(price, allNumber);
                        tvCheckAll.setChecked(ifAll());
                    } else {
//                        checkStates.put(position, isChecked);
                        List<ShoppingCartBean.ListBean> data = adapter.getData();
                        data.get(position).setChoosed(isChecked);

                        lessPrice(price, allNumber);
                        tvCheckAll.setChecked(ifAll());
//                        model.setChoosed(isChecked);
//                        if (tvCheckAll.isChecked()) {
//                            tvCheckAll.setChecked(false);
//                        }

                    }
                }
            });
            if(position == getData().size() -1){
                tvCheckAll.setChecked(!ifAll());
                tvCheckAll.setChecked(ifAll());
            }
        }
    }

    /**
     * 判断是否全部选中
     *
     * @return
     */
    private boolean ifAll() {

        List<ShoppingCartBean.ListBean> data = adapter.getData();
        if (null != data && data.size() != 0) {
            for (int i = 0; i < data.size(); i++) {
                if (!data.get(i).getChoosed()) {
                    return false;
                }
            }
        }
        return true;
//        for (int i = 0; i < baseList.size(); i++) {
//            if (!baseList.get(i).getChoosed()) {
//                return false;
//            }
//        }
//        return true;
    }


    private Float totalPrice = 0f;

    //加钱
    private void addPrice(Float price, int allNumber) {
        if (price != 0 && allNumber != 0) {
            totalPrice += (price * allNumber);
        }
        tvRealSum.setText("￥" + totalPrice);
    }

    //减钱
    private void lessPrice(Float price, int allNumber) {
        if (price != 0 && allNumber != 0) {
            totalPrice -= (price * allNumber);
        }
        tvRealSum.setText("￥" + totalPrice);
    }

    //增加删除商品接口
    public void deleteGoods(int id, int number) {
        HttpHelp.getRetrofit(this).create(RemoteApi.class).deleteShoppingGoods(id, number, new PreferencesHelper(ShoppingCartActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this, null) {
                    @Override
                    public void onNext(BaseBean<String> stringBaseBean) {
                        super.onNext(stringBaseBean);
                        if (stringBaseBean.code == 0) {
                            ToastUtil.showToast(ShoppingCartActivity.this, stringBaseBean.message);
                        } else if (stringBaseBean.code == 1) {
                            ToastUtil.showToast(ShoppingCartActivity.this, stringBaseBean.message);
                        }
                        refresh.autoRefresh();
                    }
                });
    }

    //删除商品列表条目
    public void deleteShoppingItem() {

    }

    public void deleteShopping(int id) {
        HttpHelp.getRetrofit(this).create(RemoteApi.class).deleteShopping(id, new PreferencesHelper(ShoppingCartActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this, null) {
                    @Override
                    public void onNext(BaseBean<String> stringBaseBean) {
                        super.onNext(stringBaseBean);
                        if (stringBaseBean.code == 0) {
                            page = 1;
                            getData();
                        }
                        ToastUtil.showToast(ShoppingCartActivity.this, stringBaseBean.message);
                    }
                });

        adapter.clear();
        getDataList();
        onRefresh();
        adapter.notifyDataSetChanged();
    }
}
