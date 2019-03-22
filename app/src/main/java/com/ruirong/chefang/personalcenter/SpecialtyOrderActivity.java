package com.ruirong.chefang.personalcenter;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.CommenFragmentPagerAdapter;
import com.ruirong.chefang.fragment.OrderListAfterSaleFragment;
import com.ruirong.chefang.fragment.OrderListAllFragment;
import com.ruirong.chefang.fragment.OrderListDeliverGoodsFragment;
import com.ruirong.chefang.fragment.OrderListWaitEvaluateFragment;
import com.ruirong.chefang.fragment.OrderListWaitPayMoneyFragment;
import com.ruirong.chefang.fragment.OrderListWaitTakeDeliveryOfGoodsFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chenlipeng on 2017/12/27 0027
 * describe:  特产订单
 */
public class SpecialtyOrderActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener {

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
        return R.layout.activity_specialty_order;
    }


    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("特产订单");

        titleList.add("全部");
        titleList.add("待付款");
        titleList.add("待发货");
        titleList.add("待收货");
        titleList.add("待评价");
        titleList.add("售后");

//        然后尝试了动态添加Tab的方式，也就是用 addTab(TabLayout.Tab tab, boolean setSelected) 的方式，这样就会默认的选中了，
        fragments.add(new OrderListAllFragment());//全部
        fragments.add(new OrderListWaitPayMoneyFragment());//待付款
        fragments.add(new OrderListDeliverGoodsFragment());//待发货
        fragments.add(new OrderListWaitTakeDeliveryOfGoodsFragment());//待收货
        fragments.add(new OrderListWaitEvaluateFragment());//待评价
        fragments.add(new OrderListAfterSaleFragment());//售后


        adapter = new CommenFragmentPagerAdapter(SpecialtyOrderActivity.this.getSupportFragmentManager(), titleList, fragments);
        myViewpager.setAdapter(adapter);
        myTablayout.setupWithViewPager(myViewpager);

//        Intent intent = getIntent();
//        String type = intent.getStringExtra("type");
//        if (!StringUtil.isBlank(type)) {
//            myTablayout.getTabAt(Integer.parseInt(type)).select();//根据传递的类型选择不同的tab
//        }

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
