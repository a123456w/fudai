package com.ruirong.chefang.personalcenter;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.GRecordBean;
import com.ruirong.chefang.bean.PurchaseHistoryDetailsBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by EDZ on 2018/3/29.
 */

public class PurchaseHistoryDetailsActivity extends BaseActivity {

    @BindView(R.id.img_deails)
    ImageView imgDeails;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_deal)
    TextView tvDeal;
    @BindView(R.id.tv_pay_style)
    TextView tvPayStyle;
    @BindView(R.id.tv_good_state)
    TextView tvGoodState;
    @BindView(R.id.tv_order_type)
    TextView tvOrderType;
    @BindView(R.id.tv_creat_time)
    TextView tvCreatTime;
    @BindView(R.id.tv_order_number)
    TextView tvOrderNumber;

    private int id;
    private PurchaseHistoryDetailsBean bean;
    @Override
    public int getContentView() {
        return R.layout.activity_purchase_deails;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        id=getIntent().getIntExtra("purchase_id",0);
        titleBar.setTitleText("消费记录");
        showLoadingDialog("加载中...");
        //token= "8de5408169fbe28d388e9d68b7ed28ae7121";
    }

    @Override
    public void getData() {
        HttpHelp.getRetrofit(this).create(RemoteApi.class).getPurchaseHistoryDetails(id, new PreferencesHelper(this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<PurchaseHistoryDetailsBean>>>(this,null){
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                        ToastUtil.showToast(PurchaseHistoryDetailsActivity.this,"获取数据失败");
                    }

                    @Override
                    public void onNext(BaseBean<List<PurchaseHistoryDetailsBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        hideLoadingDialog();
                        if (listBaseBean.code==0){
                            bean=listBaseBean.data.get(0);
                            tvPrice.setText(bean.getSymbol()+bean.getPrice());
                            changPayType(bean.getPay_type());
                            changType(bean.getType());
                            times(bean.getCreate_time());
                            tvOrderNumber.setText(bean.getOrder_id());
                            tvGoodState.setText(bean.getMsg());
                        }else if (listBaseBean.code==4){
                            ToolUtil.loseToLogin(PurchaseHistoryDetailsActivity.this);
                        }
                    }
                });
    }

    private void changPayType(String pay_type) {
        //1金豆银豆2支付宝3微信4银联5储值
        switch (pay_type){
            case "1":tvPayStyle.setText("余额");break;
            case "2":tvPayStyle.setText("支付宝");break;
            case "3":tvPayStyle.setText("微信");break;
            case "4":tvPayStyle.setText("银联");break;
            case "5":tvPayStyle.setText("储值");break;
        }
    }
    private void changType(String type) {
        //类型 1商品消费4转账5充值6商家消费
        switch (type){
            case "1":tvOrderType.setText("商品消费");break;
            case "4":tvOrderType.setText("转账");break;
            case "5":tvOrderType.setText("充值");break;
            case "6":tvOrderType.setText("商家消费");break;
        }
    }

    public  void times(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        tvCreatTime.setText(times);
    }
}
