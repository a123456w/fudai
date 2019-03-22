package com.ruirong.chefang.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.CommenFragmentPagerAdapter;
import com.ruirong.chefang.fragment.LuckbackHotelAllFragment;
import com.ruirong.chefang.fragment.LuckbackHotelCheapFragment;
import com.ruirong.chefang.fragment.LuckbackHotelGoodReceptionFragment;
import com.ruirong.chefang.fragment.LuckbackHotelNearbyFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chenlipeng on 2017/12/28 0028
   describe:  福返酒店
 */
public class LuckbackHotelActivity extends BaseActivity {

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
        return R.layout.activity_luckback_hotel;
    }


    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("福返酒店");
         PreferencesHelper preferencesHelper =new PreferencesHelper(LuckbackHotelActivity.this);
        Log.e("lllllllllll","福返酒店"+preferencesHelper.getLongTitude()+"llll"+preferencesHelper.getLatitude());
        titleList.add("全部");
        titleList.add("附近");
        titleList.add("好评");
        titleList.add("星级");

//        然后尝试了动态添加Tab的方式，也就是用 addTab(TabLayout.Tab tab, boolean setSelected) 的方式，这样就会默认的选中了，
        fragments.add(new LuckbackHotelAllFragment());//
        fragments.add(new LuckbackHotelNearbyFragment());//
        fragments.add(new LuckbackHotelGoodReceptionFragment());//
        fragments.add(new LuckbackHotelCheapFragment());//


        adapter = new CommenFragmentPagerAdapter(LuckbackHotelActivity.this.getSupportFragmentManager(), titleList, fragments);
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

}
