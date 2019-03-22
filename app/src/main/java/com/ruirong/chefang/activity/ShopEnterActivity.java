package com.ruirong.chefang.activity;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;

import butterknife.ButterKnife;

/**
 * Created by chenlipeng on 2018/1/3 0003
   describe:  商家入驻
 */
public class ShopEnterActivity extends BaseActivity {



    @Override
    public int getContentView() {
        return R.layout.activity_shop_settled;
    }

    @Override
    public void initView() {
     ButterKnife.bind(this);
             loadingLayout.setStatus(LoadingLayout.Success);
             titleBar.setTitleText("商家入驻");

    }

    @Override
    public void getData() {

    }


}
