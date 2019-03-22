package com.ruirong.chefang.shoppingcart.cart;

import android.os.Bundle;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;

import butterknife.ButterKnife;

/**
 * describe:  购物车页面
 * Created by chenlipeng on 2017/12/21 15:43
 */


public class ShoppingCartActivity extends BaseActivity

{
    @Override
    public int getContentView() {
        return R.layout.activity_shopping_cart;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();/***/
        ShoppingCartFragment shoppingCartFragment = new ShoppingCartFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("fromCarActivity", true);
        shoppingCartFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, shoppingCartFragment).commit();
    }

    @Override
    public void getData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
