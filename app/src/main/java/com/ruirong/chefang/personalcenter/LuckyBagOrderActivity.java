package com.ruirong.chefang.personalcenter;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.CommenFragmentPagerAdapter;
import com.ruirong.chefang.fragment.LuckyBagListAllFragment;
import com.ruirong.chefang.fragment.LuckyBagListCommentFragment;
import com.ruirong.chefang.fragment.LuckyBagListServiceFragment;
import com.ruirong.chefang.fragment.LuckyBagListWaitPayFragment;
import com.ruirong.chefang.fragment.LuckyBagListWaitReceiveFragment;
import com.ruirong.chefang.fragment.LuckyBagListWaitSendFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wu on 2018/4/3.
 * describe: 福袋订单
 */

public class LuckyBagOrderActivity extends BaseActivity {

    @BindView(R.id.my_tablayout)
    TabLayout myTablayout;
    @BindView(R.id.my_viewpager)
    ViewPager myViewpager;
    Unbinder unbinder;

    private ArrayList<String> titleList = new ArrayList<>();
    ArrayList<Fragment> fragments = new ArrayList<>();
    private CommenFragmentPagerAdapter adapter;


    @Override
    public int getContentView() {
        return R.layout.activity_luckbag_order;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("待返金订单");

        titleList.add("全部");
        titleList.add("待付款");
        titleList.add("待发货");
        titleList.add("待收货");
        titleList.add("待评价");
//        titleList.add("售后");


        fragments.add(new LuckyBagListAllFragment());//全部
        fragments.add(new LuckyBagListWaitPayFragment());//待付款
        fragments.add(new LuckyBagListWaitSendFragment());//待发货
        fragments.add(new LuckyBagListWaitReceiveFragment());//待收货
        fragments.add(new LuckyBagListCommentFragment());//待评价
//        fragments.add(new LuckyBagListServiceFragment());//售后


        adapter = new CommenFragmentPagerAdapter(LuckyBagOrderActivity.this.getSupportFragmentManager(), titleList, fragments);
        myViewpager.setAdapter(adapter);
        myTablayout.setupWithViewPager(myViewpager);

    }

    @Override
    public void getData() {

    }
}
