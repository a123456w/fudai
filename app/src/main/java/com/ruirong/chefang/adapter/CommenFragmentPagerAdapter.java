package com.ruirong.chefang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**tablelayout和viewpager的通用适配器
 * Created by dillon on 2017/12/15.
 */

public class CommenFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String> titles;
    private List<Fragment> fragments ;

    public CommenFragmentPagerAdapter(FragmentManager fm, ArrayList<String> titles, List<Fragment> fragments) {
        super(fm);
        this.titles = titles;
        this.fragments = fragments;
    }

    public CommenFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);    }

    @Override
    public int getCount() {
        return titles.size();    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

}
