package com.ruirong.chefang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.google.GoogleCircleHookRefreshView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.LifeServiceExpaandListviewAdapter;
import com.ruirong.chefang.shoppingcart.bean.ShoppingCartBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenlipeng on 2018/1/3 0003
 * describe:  生活服务页面
 */
public class LifeServiceActivity extends BaseActivity implements LifeServiceExpaandListviewAdapter.OnItemChildClickListener, CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener {
    @BindView(R.id.can_content_view)
    ExpandableListView canContentView;
    @BindView(R.id.can_refresh_header)
    GoogleCircleHookRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    GoogleCircleHookRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    private LifeServiceExpaandListviewAdapter shoppingCartAdapter;
    private int page = 1;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_life_service);
//    }

    @Override
    public int getContentView() {
        return R.layout.activity_life_service;
    }

    @Override
    public void initView() {

        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("生活服务");

        shoppingCartAdapter = new LifeServiceExpaandListviewAdapter(LifeServiceActivity.this);
        canContentView.setAdapter(shoppingCartAdapter);
        shoppingCartAdapter.setOnItemChildClickListener(this);
        canContentView.setGroupIndicator(null); // 去掉默认带的箭头
        canContentView.setSelection(0);// 设置默认选中项
        canContentView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //返回true则父item不可点击，否则点击父item字布局会折叠

                Intent intent1 = new Intent(LifeServiceActivity.this, BuildingHomeDetailsActivity.class);
                intent1.putExtra("int_data", 100);
                intent1.putExtra("titleName", "生活服务");


                startActivity(intent1);


                return true;
            }
        });

        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
        refresh.setStyle(1, 0);
    }

    @Override
    public void getData() {
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

        shoppingCartAdapter.setData(listData);
        // 遍历所有group,将所有项设置成默认展开
        int groupCount = shoppingCartAdapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            canContentView.expandGroup(i);
        }
        shoppingCartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMore() {
        page++;
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

    @Override
    public void onItemChildClick(View view, int groupPosition, int childPosition) {

    }
}
