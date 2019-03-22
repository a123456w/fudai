package com.ruirong.chefang.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.ruirong.chefang.R;
import com.ruirong.chefang.activity.EnteringActivity;
import com.ruirong.chefang.bean.ShopDetailBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by guo on 2017/4/7.
 * 已入驻
 */

public class EnteringFragment2 extends BaseFragment implements OnBannerListener {
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_shop_type)
    TextView tvShopType;
    @BindView(R.id.tv_per_capita)
    TextView tvPerCapita;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.tv_shop_location)
    TextView tvShopLocation;
    @BindView(R.id.tv_shop_phone)
    TextView tvShopPhone;
    @BindView(R.id.tv_shop_intro)
    TextView tvShopIntro;
    private BaseSubscriber<BaseBean<ShopDetailBean>> getDetailSubscriber;
    private List<String> bannerList;

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_entering_2, frameLayout, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {

    }

    @Override
    public void getData() {
        getDetailSubscriber = new BaseSubscriber<BaseBean<ShopDetailBean>>(mContext, loadingLayout) {
            @Override
            public void onNext(BaseBean<ShopDetailBean> shopDetailBeanBaseBean) {
                super.onNext(shopDetailBeanBaseBean);
                if (shopDetailBeanBaseBean.code == 0) {
                    bindData(shopDetailBeanBaseBean.data);
                } else if (shopDetailBeanBaseBean.code == 1) {
                    //非法商家
                    ((EnteringActivity) getActivity()).showIllegalShop(shopDetailBeanBaseBean.message);
                }else if (shopDetailBeanBaseBean.code==4){
                    ToolUtil.loseToLogin(mContext);
                }

            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).getEnteringDetail(new PreferencesHelper(mContext).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getDetailSubscriber);
    }

    private void bindData(ShopDetailBean data) {
        bannerList = data.getPics();

        //banner
        banner.setImages(bannerList)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {

                        GlideUtil.display(mContext, Constants.IMG_HOST + (String) path, imageView);
                    }
                })
                .setOnBannerListener(EnteringFragment2.this)
                .start();

        //商家名
        tvShopName.setText(data.getSp_name());
        //所属行业
        tvShopType.setText(data.getTrade_name());
        //人均消费
        tvPerCapita.setText(data.getPerpeo());
        //商家简介
        tvShopIntro.setText(data.getContent());
        //商家地址
        tvShopLocation.setText(data.getSp_address());
        //商家电话
        tvShopPhone.setText(data.getMobile());
    }

    @Override
    public void onReload(View v) {
        super.onReload(v);
        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getDetailSubscriber != null && getDetailSubscriber.isUnsubscribed()) {
            getDetailSubscriber.unsubscribe();
        }
    }

    @Override
    public void OnBannerClick(int position) {

    }
}
