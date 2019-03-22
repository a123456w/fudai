package com.ruirong.chefang.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.CircleBar;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.ShopHeadlineChoseOneAdapter;
import com.ruirong.chefang.adapter.ShopHeadlineCommentAdapter;
import com.ruirong.chefang.adapter.ShopHeadlineDialogAdapter;
import com.ruirong.chefang.adapter.SingleProductAdapter;
import com.ruirong.chefang.bean.HotelDetailChoseOneBean;
import com.ruirong.chefang.bean.HotelDetailCommentBean;
import com.ruirong.chefang.bean.LuckBackRoomListviewBean;
import com.ruirong.chefang.bean.SingleProductBean;
import com.ruirong.chefang.widget.FlowLayout;
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
 * describe:  商家头条
 */
public class ShopHeadlineActivity extends
        BaseActivity implements AdapterView.OnItemClickListener {


    @BindView(R.id.banner_top)
    Banner bannerTop;
    @BindView(R.id.tv_table_name)
    MarqueTextView tvTableName;
    @BindView(R.id.tv_preview)
    TextView tvPreview;
    @BindView(R.id.tv_preview_num)
    TextView tvPreviewNum;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.tv_scorenum)
    TextView tvScorenum;
    @BindView(R.id.tv_workinggroup)
    TextView tvWorkinggroup;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_businessarea)
    TextView tvBusinessarea;
    @BindView(R.id.rl_all_product)
    RelativeLayout rlAllProduct;
    @BindView(R.id.rv_taochan)
    RecyclerView rvTaochan;
    @BindView(R.id.rl_more)
    RelativeLayout rlMore;
    @BindView(R.id.rv_danping)
    RecyclerView rvDanping;
    @BindView(R.id.cb_evaluate)
    CircleBar cbEvaluate;
    @BindView(R.id.fl_client_evaluation)
    FlowLayout flClientEvaluation;
    @BindView(R.id.list_evaluate)
    NoScrollListView listEvaluate;
    @BindView(R.id.can_content_view)
    NestedScrollView canContentView;
    @BindView(R.id.btn_storemoney)
    Button btnStoremoney;
    @BindView(R.id.btn_paybill)
    Button btnPaybill;
    private ShopHeadlineChoseOneAdapter hotelDetailChoseOneAdapter;
    private ShopHeadlineCommentAdapter hotelDetailCommentAdapter;
    private SingleProductAdapter singleProductAdapter;
    private List<SingleProductBean> singleProductBeans = new ArrayList<>();
    private List<HotelDetailCommentBean> hotelDetailCommentBeanList = new ArrayList<>();
    private List<HotelDetailChoseOneBean> hotelDetailChoseOneBeanList = new ArrayList<>();


    private TextView textView;

//    private ShopHeadLineChoseTwoAdapter shopHeadLineChoseTwoAdapter;

    @Override
    public int getContentView() {
        return R.layout.activity_shop_headline;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("商家头条");
        titleBar.setRightImageRes(R.drawable.ic_share);
        titleBar.setRightImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(ShopHeadlineActivity.this, "分享");
            }
        });
        initData();
        bindBannerData();
    }

    /**
     * 初始化适配器数据。
     */
    public void initData() {
        //预定
        hotelDetailChoseOneAdapter = new ShopHeadlineChoseOneAdapter(rvTaochan);
        rvTaochan.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        rvTaochan.setAdapter(hotelDetailChoseOneAdapter);
        //单品
        singleProductAdapter =new SingleProductAdapter(rvDanping);
        rvDanping.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        rvDanping.setAdapter(singleProductAdapter);

        //评论
        hotelDetailCommentAdapter = new ShopHeadlineCommentAdapter(listEvaluate);
        listEvaluate.setFocusable(false);
        listEvaluate.setAdapter(hotelDetailCommentAdapter);
    }

    @Override
    public void getData() {
//        for (int i = 0; i
    }

    /**
     * 为顶部轮播添加图片
     */
    public void bindBannerData() {
        List<String> baseList1 = new ArrayList<>();
        baseList1.add("http://n.sinaimg.cn/news/transform/w1000h500/20171212/xH_r-fypnsip9129414.jpg");
        baseList1.add("http://n.sinaimg.cn/news/transform/w1000h500/20171208/Fbej-fypnsin8780163.jpg");
        baseList1.add("http://n.sinaimg.cn/news/transform/w1000h500/20171212/xH_r-fypnsip9129414.jpg");
        baseList1.add("http://n.sinaimg.cn/news/transform/w1000h500/20171208/Fbej-fypnsin8780163.jpg");
        bannerTop.setImages(baseList1)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        String imgUrl = (String) path;
                        GlideUtil.display(ShopHeadlineActivity.this, imgUrl, imageView);

                    }
                }).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                // startActivity((BGAPhotoPreviewActivity.newIntent(mContext, null, bannerList, position)));

            }
        }).start();


    }

    @OnClick({R.id.btn_storemoney, R.id.btn_paybill, R.id.rl_all_product})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_storemoney:
                storeValueDialog();
//                Intent intent1 = new Intent(ShopHeadlineActivity.this, StoredValueActivity.class);
//                intent1.putExtra("int_data", 100);
//                startActivity(intent1);
                break;
            case R.id.btn_paybill:

                minePayBillDialog();

//                intent1 = new Intent(ShopHeadlineActivity.this, SettingPaymentPasswordActivity.class);
//                intent1.putExtra("int_data", 100);
//                startActivity(intent1);

//                买金豆
//                RechargeActivity
//                立即预定
//                ImmediateReservationActivity
//                设置支付密码
//                SettingPaymentPasswordActivity


                break;



            case R.id.rl_all_product:
                Intent intent2 = new Intent(ShopHeadlineActivity.this, AllProductActivity.class);
                startActivity(intent2);
                break;
        }
    }


    /**
     * 套餐和单品的点击事件
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        switch (view.getId()) {
            case R.id.iv_orderonline:


                break;
            default:
                productParaDialog();

                break;

        }
    }

    /**
     * 弹出详情的dialog
     */
//    private void Unitdetails() {
//        Dialog mDialog = new Dialog(ShopHeadlineActivity.this, R.style.ActionSheetDialogStyle);
//        View inflate = LayoutInflater.from(ShopHeadlineActivity.this).inflate(R.layout.dialog_shop_headline_details, null);
//        mDialog.setContentView(inflate);
//        Window dialogwindow = mDialog.getWindow();
//        dialogwindow.setGravity(Gravity.BOTTOM);
//        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
//        lp.windowAnimations = R.style.PopUpBottomAnimation;
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        dialogwindow.setAttributes(lp);
//        mDialog.show();
//    }


    /**
     * 弹出详情的dialog
     */
    private void productParaDialog() {
        Dialog mDialog = new Dialog(ShopHeadlineActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(ShopHeadlineActivity.this).inflate(R.layout.dialog_shop_headline_details, null);

        Banner banner_top = (Banner) inflate.findViewById(R.id.banner_top);

        List<String> baseList1 = new ArrayList<>();
        baseList1.add("http://n.sinaimg.cn/news/transform/w1000h500/20171212/xH_r-fypnsip9129414.jpg");
        baseList1.add("http://n.sinaimg.cn/news/transform/w1000h500/20171208/Fbej-fypnsin8780163.jpg");
        baseList1.add("http://n.sinaimg.cn/news/transform/w1000h500/20171212/xH_r-fypnsip9129414.jpg");
        baseList1.add("http://n.sinaimg.cn/news/transform/w1000h500/20171208/Fbej-fypnsin8780163.jpg");

        banner_top.setImages(baseList1)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        String imgUrl = (String) path;
                        GlideUtil.display(ShopHeadlineActivity.this, imgUrl, imageView);

                    }
                }).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                // startActivity((BGAPhotoPreviewActivity.newIntent(mContext, null, bannerList, position)));

            }
        }).start();

        ListView viewById = (ListView) inflate.findViewById(R.id.listview);
        ShopHeadlineDialogAdapter adapter = new ShopHeadlineDialogAdapter(viewById);
        viewById.setAdapter(adapter);

        List<LuckBackRoomListviewBean> baseList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            LuckBackRoomListviewBean myOrderListviewBean = new LuckBackRoomListviewBean();
            baseList.add(myOrderListviewBean);
        }
        adapter.setData(baseList);

        mDialog.setContentView(inflate);
        Window dialogwindow = mDialog.getWindow();
        dialogwindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogwindow.setAttributes(lp);
        mDialog.show();
    }

    /**
     * 储值的弹框
     */
    private void storeValueDialog() {
        final Dialog mDialog = new Dialog(ShopHeadlineActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(ShopHeadlineActivity.this).inflate(R.layout.shopheadline_store_value_dialog, null);

        View tv_submit = inflate.findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(ShopHeadlineActivity.this, StoredValueActivity.class);
                startActivity(intent1);

                mDialog.dismiss();
            }
        });

        mDialog.setContentView(inflate);
        Window dialogwindow = mDialog.getWindow();
        dialogwindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogwindow.setAttributes(lp);
        mDialog.show();
    }

    /**
     * w我要购买弹框
     */
    private void minePayBillDialog() {
        final Dialog mDialog = new Dialog(ShopHeadlineActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(ShopHeadlineActivity.this).inflate(R.layout.my_pay_bill__dialog, null);

        inflate.findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ShopHeadlineActivity.this, "分新老用户", Toast.LENGTH_SHORT).show();

//                Intent intent1 = new Intent(HotelDetailActivity.this, StoredValueActivity.class);
//                startActivity(intent1);
//                买金豆
//                RechargeActivity
//                立即预定
//                ImmediateReservationActivity
//                设置支付密码
//                SettingPaymentPasswordActivity

                // TODO: 2018/1/3 0003   这的逻辑处理 怎么处理


                mDialog.dismiss();
            }
        });
        inflate.findViewById(R.id.tv_pay_fortunella_venosa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                跳到充值页面
                Intent intent1 = new Intent(ShopHeadlineActivity.this, RechargeActivity.class);
                startActivity(intent1);

                mDialog.dismiss();
            }
        });
        mDialog.setContentView(inflate);
        Window dialogwindow = mDialog.getWindow();
        dialogwindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogwindow.setAttributes(lp);
        mDialog.show();
    }
}
