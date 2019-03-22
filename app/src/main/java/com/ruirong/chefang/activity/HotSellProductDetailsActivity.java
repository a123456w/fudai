package com.ruirong.chefang.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.CircleBar;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.HotSellProductDetailsComboChooseAdapter;
import com.ruirong.chefang.adapter.HotelDetailCommentAdapter;
import com.ruirong.chefang.adapter.SingleProductAdapter;
import com.ruirong.chefang.bean.HotelDetailChoseOneBean;
import com.ruirong.chefang.bean.HotelDetailCommentBean;
import com.ruirong.chefang.bean.SingleProductBean;
import com.ruirong.chefang.widget.MarqueTextView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenlipeng on 2018/1/2 0002
 * describe:  热销产品详情
 */
public class HotSellProductDetailsActivity extends BaseActivity {


    @BindView(R.id.banner_top)
    Banner bannerTop;
    @BindView(R.id.tv_table_name)
    MarqueTextView tvTableName;
    @BindView(R.id.tv_browsernum)
    TextView tvBrowsernum;
    @BindView(R.id.tv_real_estate)
    TextView tvRealEstate;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rb_grade)
    RatingBar rbGrade;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.tv_position)
    ImageView tvPosition;
    @BindView(R.id.iv_phone)
    ImageView ivPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;

    @BindView(R.id.tv_businessarea)
    TextView tvBusinessarea;
    @BindView(R.id.rl_all_product)
    RelativeLayout rlAllProduct;
    @BindView(R.id.nslv_shopmall_comment)
    NoScrollListView nslvShopmallComment;
    @BindView(R.id.btn_storemoney)
    Button btnStoremoney;
    @BindView(R.id.btn_paybill)
    Button btnPaybill;
    @BindView(R.id.cb_evaluate)
    CircleBar cbEvaluate;
    @BindView(R.id.rv_taochan)
    RecyclerView rvTaochan;
    @BindView(R.id.rv_danping)
    RecyclerView rvDanping;
    @BindView(R.id.rl_more)
    RelativeLayout rlMore;


    private HotSellProductDetailsComboChooseAdapter hotSellProductDetailsComboChooseAdapter;

    private HotelDetailCommentAdapter hotelDetailCommentAdapter;
    private List<HotelDetailCommentBean> hotelDetailCommentBeanList = new ArrayList<>();
    private List<HotelDetailChoseOneBean> hotelDetailChoseOneBeanList = new ArrayList<>();

    private SingleProductAdapter singleProductAdapter;
    private List<SingleProductBean> singleProductBeans = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_hot_sell;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("热销产品");
        titleBar.setRightImageRes(R.drawable.ic_share);
        titleBar.setRightImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(HotSellProductDetailsActivity.this, "分享");
            }
        });
        initData();
        bindBannerData();
    }

    /**
     * 初始化适配器数据。
     */
    public void initData() {
//        套餐的
        hotSellProductDetailsComboChooseAdapter = new HotSellProductDetailsComboChooseAdapter(rvTaochan);
        rvTaochan.setFocusable(false);
        rvTaochan.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        rvTaochan.setAdapter(hotSellProductDetailsComboChooseAdapter);
//       单品
        singleProductAdapter = new SingleProductAdapter(rvDanping);
        rvDanping.setFocusable(false);
        rvDanping.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        rvDanping.setAdapter(singleProductAdapter);
//      评论
        hotelDetailCommentAdapter = new HotelDetailCommentAdapter(nslvShopmallComment);
        nslvShopmallComment.setFocusable(false);
        nslvShopmallComment.setAdapter(hotelDetailCommentAdapter);


        cbEvaluate.setRoundProgressColor(Color.parseColor("#fe9d00"));

//套餐和单品的跳转
//        nsglHotelProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                // TODO: 2018/1/2 0002 根据选择套餐和单品的不同。跳到不同的 确认订单
//                Intent intent1 = new Intent(HotSellProductDetailsActivity.this, ConfirmationOrderActivity.class);
//                intent1.putExtra("int_data", 100);
//                startActivity(intent1);
//            }
//        });

    }

    @Override
    public void getData() {
//        for (int i = 0; i < 5; i++) {
//            HotelDetailCommentBean hotelDetailChoseOneBean = new HotelDetailCommentBean();
//            hotelDetailCommentBeanList.add(hotelDetailChoseOneBean);
//        }
//        hotelDetailCommentAdapter.setData(hotelDetailCommentBeanList);
//
//
//        for (int i = 0; i < 5; i++) {
//
//            HotelDetailChoseOneBean hotelDetailChoseOneBean = new HotelDetailChoseOneBean();
//            hotelDetailChoseOneBeanList.add(hotelDetailChoseOneBean);
//        }
//        hotSellProductDetailsComboChooseAdapter.setData(hotelDetailChoseOneBeanList);
//        for (int i = 0; i < 5; i++) {
//
//            SingleProductBean hotelDetailChoseOneBean = new SingleProductBean();
//            singleProductBeans.add(hotelDetailChoseOneBean);
//        }
//        singleProductAdapter.setData(singleProductBeans);


    }

    /**
     * 为顶部轮播添加图片
     */
    public void bindBannerData() {
        List<String> baseList = new ArrayList<>();
        baseList.add("http://n.sinaimg.cn/news/transform/w1000h500/20171212/xH_r-fypnsip9129414.jpg");
        baseList.add("http://n.sinaimg.cn/news/transform/w1000h500/20171208/Fbej-fypnsin8780163.jpg");
        baseList.add("http://n.sinaimg.cn/news/transform/w1000h500/20171212/xH_r-fypnsip9129414.jpg");
        baseList.add("http://n.sinaimg.cn/news/transform/w1000h500/20171208/Fbej-fypnsin8780163.jpg");
        bannerTop.setImages(baseList)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        String imgUrl = (String) path;
                        GlideUtil.display(HotSellProductDetailsActivity.this, imgUrl, imageView);

                    }
                }).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                // startActivity((BGAPhotoPreviewActivity.newIntent(mContext, null, bannerList, position)));

            }
        }).start();


    }


    @OnClick({R.id.btn_storemoney, R.id.btn_paybill, R.id.rl_all_product,R.id.rl_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_storemoney:
                break;
            case R.id.btn_paybill:
                // TODO: 2018/2/24 我要买单
                Intent intent0 = new Intent(HotSellProductDetailsActivity.this, ConfirmationOrderActivity.class);
                intent0.putExtra("int_data", 100);
                startActivity(intent0);
                break;

            case R.id.rl_all_product:
                Intent intent1 = new Intent(HotSellProductDetailsActivity.this, AllProductActivity.class);
                intent1.putExtra("shop_id", 100);
                startActivity(intent1);
                break;
            case R.id.rl_more:
                Intent intent2 = new Intent(HotSellProductDetailsActivity.this, AllProductActivity.class);
                intent2.putExtra("int_data", 100);
                startActivity(intent2);
                break;
        }
    }

}
