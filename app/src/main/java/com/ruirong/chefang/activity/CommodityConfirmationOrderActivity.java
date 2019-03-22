package com.ruirong.chefang.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.adapter.ComBolAdaper;
import com.ruirong.chefang.bean.ComBinBean;
import com.ruirong.chefang.bean.ReservepackageBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/23.
 * 商品确认订单2
 */

public class CommodityConfirmationOrderActivity extends BaseActivity {
    @BindView(R.id.shop_name)
    TextView shopName;
    @BindView(R.id.goods_pic)
    ImageView goodsPic;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_dicinfo)
    TextView tvDicinfo;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.can_content_view)
    com.ruirong.chefang.view.CustomExpandableListView expandableListView;
    @BindView(R.id.tv_shop_price)
    TextView tvShopPrice;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.et_edit)
    TextView etEdit;

    private String price;
    private String package_id;
    private String shop_id;

    private ComBolAdaper comBolAdaper;

    private List<ComBinBean.GoodidsBean> monthBeans = new ArrayList<>();

    private List<String> headYears = new ArrayList<>();

    private Dialog mDialog;
    private String myEtCall = "" ;
    private String myEtPhone = "" ;
    @Override
    public int getContentView() {
        return R.layout.activity_commodity_confirmation_order;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("确认订单");


        package_id = getIntent().getStringExtra("package_id");
        shop_id = getIntent().getStringExtra("shop_id");
        if (package_id == null || shop_id == null) {
            finish();
        }


    }

    @Override
    public void getData() {
        // reservepackage(new PreferencesHelper(CommodityConfirmationOrderActivity.this).getToken(), package_id);

        getCombinData(new PreferencesHelper(CommodityConfirmationOrderActivity.this).getToken(), package_id, "1");

    }

    /**
     * 填写称呼和电话号码的弹框
     */
    private void uploadHeadImage() {

        mDialog = new Dialog(CommodityConfirmationOrderActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(CommodityConfirmationOrderActivity.this).inflate(R.layout.dialog_edit_gain, null);
        mDialog.setContentView(inflate);
        Window dialogwindow = mDialog.getWindow();
        dialogwindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//      lp.y = ScreenUtil.getBottomStatusHeight(PersionMessageActivity.this);
        dialogwindow.setAttributes(lp);
        mDialog.show();

        TextView tvCancle,tvSure;
        final EditText etCall ,etPhone,etEdit;
        tvSure = (TextView) inflate.findViewById(R.id.tv_sure);
        tvCancle = (TextView) inflate.findViewById(R.id.tv_cancle);
        etEdit = (EditText) inflate.findViewById(R.id.et_edit);

        etCall = (EditText)inflate.findViewById(R.id.et_call);
        etPhone = (EditText)inflate.findViewById(R.id.et_phone);

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                myEtCall = etCall.getText().toString().trim();
                myEtPhone = etPhone.getText().toString().trim();
                mDialog.dismiss();

                if (TextUtils.isEmpty(myEtCall)){
                    ToastUtil.showToast(CommodityConfirmationOrderActivity.this,"请输入您的称呼");
                    return;
                }
                if (TextUtils.isEmpty(myEtPhone)){
                    ToastUtil.showToast(CommodityConfirmationOrderActivity.this,"请输入您的电话号码");
                    return;
                }


                okorder(new PreferencesHelper(CommodityConfirmationOrderActivity.this).getToken(), package_id, shop_id,
                        etEdit.getText().toString().trim(), null, null,myEtCall,myEtPhone);

            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();


            }
        });
    }

    /**
     * 获取套餐详情
     *
     * @param token
     * @param id
     * @param xinban 新版本时候需要传递 1 。
     */
    public void getCombinData(String token, String id, String xinban) {
        HttpHelp.getInstance().create(RemoteApi.class).CombinData(token, id, xinban)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ComBinBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<ComBinBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            GlideUtil.display(CommodityConfirmationOrderActivity.this,
                                    Constants.IMG_HOST + baseBean.data.getPic(), goodsPic);
                            shopName.setText(baseBean.data.getName());
                            shopName.getPaint().setFakeBoldText(true);   //加粗

                            etEdit.setText(baseBean.data.getDicinfo());
                            tvUserName.setText(baseBean.data.getName());
                            tvDicinfo.setText(baseBean.data.getBiaoqian());
                            tvPrice.setText(baseBean.data.getAll_price());
                            tvShopPrice.setText(baseBean.data.getShop_price());
                            tvShopPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            total.setText("￥" + baseBean.data.getAll_price());
                            price = baseBean.data.getAll_price();



                            monthBeans = baseBean.data.getGoodids();
                            if (monthBeans != null && monthBeans.size() > 0) {
                                // 遍历所有group,将所有项设置成默认展开
                                for (int i = 0; i < monthBeans.size(); i++) {
                                    headYears.add(monthBeans.get(i).getTitle());
                                }
                                Log.e("year",headYears.size()+"year长度");
                            }

                            comBolAdaper = new ComBolAdaper(monthBeans,CommodityConfirmationOrderActivity.this,headYears);
                            expandableListView.setAdapter(comBolAdaper);
                            expandableListView.setGroupIndicator(null);

                            if (monthBeans != null && monthBeans.size() > 0) {
                                int groupCount = comBolAdaper.getGroupCount();
                                for (int i = 0; i < groupCount; i++) {
                                    expandableListView .expandGroup(i);
                                    Log.e("year",monthBeans.size()+"year长度");
                                }
                            }


                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });


    }


    private void reservepackage(String token, String package_id) {

        HttpHelp.getInstance().create(RemoteApi.class).reservepackage(token, package_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ReservepackageBean>>(this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<ReservepackageBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (baseBean.data != null) {
                                GlideUtil.display(CommodityConfirmationOrderActivity.this,
                                        Constants.IMG_HOST + baseBean.data.getPic(), goodsPic);
                                shopName.setText(baseBean.data.getShopname());
                                tvUserName.setText(baseBean.data.getName());
                                tvDicinfo.setText(baseBean.data.getDicinfo());
                                tvPrice.setText("￥" + baseBean.data.getAll_price());
                                tvShopPrice.setText("￥" + baseBean.data.getShop_price());
                                tvShopPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                total.setText("￥" + baseBean.data.getAll_price());
                                price = baseBean.data.getAll_price();
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(CommodityConfirmationOrderActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.i("XXX", throwable.getLocalizedMessage());
                    }
                });
    }

    @OnClick({R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:

                uploadHeadImage();

                break;

        }
    }


    private void okorder(final String token, String id, String shop_id, String content, String time, String time_duan,String pName,String pHone) {
        HttpHelp.getInstance().create(RemoteApi.class).okorder(token, id, shop_id, content, time, time_duan,pName,pHone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        Log.i("XXX", baseBean.code + " " + baseBean.message);
                        if (baseBean.code == 0) {
                            Intent intent = new Intent(CommodityConfirmationOrderActivity.this, ImmediatelyPaymentActivity.class);
                            intent.putExtra("place_type", "2");
                            intent.putExtra("shop_price", price);
                            intent.putExtra("order_sn", baseBean.data);
                            startActivity(intent);
                            finish();
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(CommodityConfirmationOrderActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });


    }

}
