package com.ruirong.chefang.personalcenter;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.CommenFragmentPagerAdapter;
import com.ruirong.chefang.fragment.ShopOrderListServiceFragment;
import com.ruirong.chefang.fragment.ShopOrderListWaitCommentFragment;
import com.ruirong.chefang.fragment.ShopOrderListUseingFragment;
import com.ruirong.chefang.fragment.ShopOrderListWaitPayFragment;
import com.ruirong.chefang.fragment.ShopOrderListAllFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chenlipeng on 2017/12/28 0028
   describe:  商城订单
 */
public class ShopOrderActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener {

    @BindView(R.id.my_tablayout)
    TabLayout myTablayout;
    @BindView(R.id.my_viewpager)
    ViewPager myViewpager;
    Unbinder unbinder;

    private ArrayList<String> titleList = new ArrayList<>();
    ArrayList<Fragment> fragments = new ArrayList<>();
    private CommenFragmentPagerAdapter adapter;
    private int page = 1;


    @Override
    public int getContentView() {
        return R.layout.activity_shop_order;
    }


    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("商城订单");

        titleList.add("全部");
        titleList.add("待付款");
        titleList.add("使用中");
        titleList.add("待评价");
        titleList.add("售后");

//        然后尝试了动态添加Tab的方式，也就是用 addTab(TabLayout.Tab tab, boolean setSelected) 的方式，这样就会默认的选中了，

        fragments.add(new ShopOrderListAllFragment());//全部
        fragments.add(new ShopOrderListWaitPayFragment());//待付款
        fragments.add(new ShopOrderListUseingFragment());//使用中
        fragments.add(new ShopOrderListWaitCommentFragment());//待评价
        fragments.add(new ShopOrderListServiceFragment());//售后


        adapter = new CommenFragmentPagerAdapter(ShopOrderActivity.this.getSupportFragmentManager(), titleList, fragments);
        myViewpager.setAdapter(adapter);
        myTablayout.setupWithViewPager(myViewpager);

    }

    @Override
    public void getData() {
        page = 1;
        getDataList(page);
    }

    public void getDataList(final int page) {

    }


//    @OnClick({R.id.tv_verification_picnum, R.id.tv_verification_code, R.id.tv_forgetpwdnextstep})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.tv_verification_picnum:  //获取图片码
//                break;
//            case R.id.tv_verification_code:    //获取手机验证码
//                break;
//            case R.id.tv_forgetpwdnextstep:
//                //忘记密码第二步
////                ForgetPwdNextActivity.startWithParameter(ForgetPwdActivity.this);
//                break;
//        }
//    }

    @Override
    public void onLoadMore() {
        page++;
        getDataList(page);
    }


    @Override
    public void onRefresh() {
        page = 1;
        getDataList(page);
    }
}
