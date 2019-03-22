package com.ruirong.chefang.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.CommenFragmentPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/*
 *  Created by 李  on 2018/11/27.
 */
public class HotelStayFragment extends BaseFragment {
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.vp_viewPager)
    ViewPager vpViewPager;
    Unbinder unbinder;
    CommenFragmentPagerAdapter mTabPagerAdapter;
    private ArrayList<String> titleList=new ArrayList<>();
    private ArrayList<Fragment> fragments=new ArrayList<>();


    @Override
    public View getContentView(FrameLayout frameLayout) {
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.fragment_stay_hotel, frameLayout, false);
        unbinder = ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        titleList.add("全部");
        titleList.add("附近");
        titleList.add("好评");
        titleList.add("星级");

        fragments.add(new LuckbackHotelAllFragment());//
        fragments.add(new LuckbackHotelNearbyFragment());//
        fragments.add(new LuckbackHotelGoodReceptionFragment());//
        fragments.add(new LuckbackHotelCheapFragment());//

        mTabPagerAdapter = new CommenFragmentPagerAdapter(getActivity().getSupportFragmentManager(), titleList, fragments);
        vpViewPager.setAdapter(mTabPagerAdapter);
        tabLayout.setupWithViewPager(vpViewPager);
    }

    @Override
    public void getData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
