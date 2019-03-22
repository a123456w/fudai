package com.ruirong.chefang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qlzx.mylibrary.util.PreferencesHelper;
import com.ruirong.chefang.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by guo on 2017/5/9.
 * 引导页
 */

public class GuideActivity extends AppCompatActivity {
    @BindView(R.id.vp_guide)
    ViewPager vp_guide_bg;
    @BindView(R.id.tv_go)
    TextView tvGo;
    @BindView(R.id.ll_guide_points)
    LinearLayout ll_guide_points;
    @BindView(R.id.iv_red_point)
    ImageView iv_guide_redPoint;
    private ArrayList<ImageView> images;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        new PreferencesHelper(this).saveFirstTime(false);
        ActionBar supportActionBar = getSupportActionBar();
        if (null != supportActionBar) {
            supportActionBar.hide();  //隐藏掉标题栏
        }
        init();
    }

    @OnClick(R.id.tv_go)
    public void gotoMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void init() {


        // 准备数据
        initData();
        // 设置Adapter
        vp_guide_bg.setAdapter(new MyAdapter());

        // 监听ViewPager的滚动事件
        vp_guide_bg.addOnPageChangeListener(new MyOnPageChangeListener());
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {

            // positionOffset 手指在ViewPager上移动的与屏幕宽度的比例
            // 红点移动的距离 = positionOffset * 灰点的间距
            int redPointX = (int) ((positionOffset + position) * dp2px(16));
            // 让红点移动,修改红点的leftMargin
            android.widget.RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) iv_guide_redPoint
                    .getLayoutParams();
            layoutParams.leftMargin = redPointX;
            iv_guide_redPoint.setLayoutParams(layoutParams);
        }

        @Override
        public void onPageSelected(int position) {
            // 当选中某一页时回调
            if(position==images.size()-1){
                tvGo.setVisibility(View.VISIBLE);
            }else{
                tvGo.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }

    private void initData() {
        int[] imgIds = new int[] { R.drawable.guide_1, R.drawable.guide_2,
                R.drawable.guide_3
               // ,R.drawable.guide_4
        };
        // 给ViewPager准备图片数据
        images = new ArrayList<ImageView>();
        for (int i = 0; i < imgIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // imageView.setImageResource(imgIds[i]);
            imageView.setImageResource(imgIds[i]);
            images.add(imageView);

            // 添加灰点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.gray_point);
            int dp2px = dp2px(8);
            // 设置宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px, dp2px);
            // 设置边距
            if (i != 0) {
                params.leftMargin = dp2px;
            }
            point.setLayoutParams(params);
            ll_guide_points.addView(point);
        }
    }

    public int dp2px(int dp){
        // px = dp * 密度比 0.75 、1、 1.5、 2
        float density = getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5f);
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(images.get(position));
            return images.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

}
