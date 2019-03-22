package com.ruirong.chefang.personalcenter;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.CommenFragmentPagerAdapter;
import com.ruirong.chefang.fragment.ReserveOrderListAllFragment;
import com.ruirong.chefang.fragment.ReserveOrderListWaitEvaluateFragment;
import com.ruirong.chefang.fragment.ReserveOrderListWaitPayMoneyFragment;
import com.ruirong.chefang.fragment.ReserveOrderListWaitUseFragment;
import com.ruirong.chefang.fragment.ReserveOrderListWaitUsingFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chenlipeng on 2017/12/27 0027
   describe:  预定订单
 activity_reserve_order
 */
public class ReserveOrderActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener {

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
        return R.layout.activity_reserve_order;
    }


    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("预订订单");

        titleList.add("全部");
        titleList.add("待付款");
        titleList.add("待入住");
        titleList.add("已入住");
        titleList.add("待评价");

//        然后尝试了动态添加Tab的方式，也就是用 addTab(TabLayout.Tab tab, boolean setSelected) 的方式，这样就会默认的选中了，
        fragments.add(new ReserveOrderListAllFragment());//全部
        fragments.add(new ReserveOrderListWaitPayMoneyFragment());//待付款
        fragments.add(new ReserveOrderListWaitUseFragment());//待使用
        fragments.add(new ReserveOrderListWaitUsingFragment());//使用中
        fragments.add(new ReserveOrderListWaitEvaluateFragment());//待评价


        adapter = new CommenFragmentPagerAdapter(ReserveOrderActivity.this.getSupportFragmentManager(), titleList, fragments);
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
