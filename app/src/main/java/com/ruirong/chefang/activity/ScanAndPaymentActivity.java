package com.ruirong.chefang.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.ruirong.chefang.R;
import com.ruirong.chefang.bean.GoodsBuyConfirmBean;
import com.ruirong.chefang.bean.PayCodeBean;
import com.ruirong.chefang.bean.ReceiptBean;
import com.ruirong.chefang.http.RemoteApi;
import com.ruirong.chefang.util.ToolUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext;

/**
 * Created by chenlipeng on 2018/1/4 0004
 * describe:  扫描付款
 */
public class ScanAndPaymentActivity extends BaseActivity {


    @BindView(R.id.ll_pay_code)
    LinearLayout llPayCode;
    @BindView(R.id.ll_scan_pay)
    LinearLayout llScanPay;
    @BindView(R.id.ll_receipt_code)
    LinearLayout llReceiptCode;
    @BindView(R.id.iv_pay)
    ImageView ivPay;
    @BindView(R.id.iv_receipt)
    ImageView ivReceipt;
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;

    private PreferencesHelper helper;
    private String receiptUrl;
    private String payUrl;
    private String paySn;

    private Timer timer;
    private TimerTask task;
    private Dialog mDialog;
    private BaseSubscriber<BaseBean<GoodsBuyConfirmBean>> sweepOrderSubscriber;

    @Override
    public int getContentView() {
        return R.layout.activity_scan_and_payment;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("扫描付款");


        helper = new PreferencesHelper(this);
        barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), null);
        capture.decode();

//        task = new TimerTask() {
//            @Override
//            public void run() {
//                getData();
//            }
//        };
//        timer = new Timer();
//        timer.schedule(task, 0, 60000);

//        ToastUtil.showToast(this,"将条形码置于取景框内");

    }

    @Override
    public void getData() {
        getPayCode(helper.getToken());
        getReceiptCode(helper.getToken());

    }


    /**
     * 获取收款码
     *
     * @param token
     */
    private void getReceiptCode(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getReceiptCode(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ReceiptBean>>(ScanAndPaymentActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<ReceiptBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            receiptUrl = baseBean.data.getQrcode();
                            if (ivReceipt.isShown()) {
                                GlideUtil.display(ScanAndPaymentActivity.this, Constants.IMG_HOST + receiptUrl, ivReceipt);
                            }
                        }else if (baseBean.code==4){
                            ToolUtil.loseToLogin(ScanAndPaymentActivity.this);
                        } else {
                            ToastUtil.showToast(ScanAndPaymentActivity.this, "获取收款码失败");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }


    /**
     * 获取付款码
     *
     * @param token
     */
    private void getPayCode(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getPayCode(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<PayCodeBean>>(ScanAndPaymentActivity.this, loadingLayout) {
                    @Override
                    public void onNext(BaseBean<PayCodeBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            payUrl = baseBean.data.getPic();
                            paySn = baseBean.data.getSn();
                            if (ivPay.isShown()) {
                                GlideUtil.display(ScanAndPaymentActivity.this, Constants.IMG_HOST + payUrl, ivPay);
                            }
                        }else if (baseBean.code==4){
                            ToolUtil.loseToLogin(ScanAndPaymentActivity.this);
                        } else {
                            ToastUtil.showToast(ScanAndPaymentActivity.this, "获取付款码失败");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }


    @OnClick({R.id.ll_pay_code, R.id.ll_scan_pay, R.id.ll_receipt_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_pay_code:
                ToastUtil.showToast(ScanAndPaymentActivity.this, "付款码");
                capture.onPause();
                barcodeScannerView.setVisibility(View.GONE);
                ivReceipt.setVisibility(View.GONE);
                ivPay.setVisibility(View.VISIBLE);
                GlideUtil.display(ScanAndPaymentActivity.this, Constants.IMG_HOST + payUrl, ivPay);
                break;
            case R.id.ll_scan_pay:
                ToastUtil.showToast(ScanAndPaymentActivity.this, "扫码付");
                ivReceipt.setVisibility(View.GONE);
                ivPay.setVisibility(View.GONE);
                barcodeScannerView.setVisibility(View.VISIBLE);
                capture.onResume();


                break;
            case R.id.ll_receipt_code:
                ToastUtil.showToast(ScanAndPaymentActivity.this, "收款码");
                capture.onPause();
                barcodeScannerView.setVisibility(View.GONE);
                ivPay.setVisibility(View.GONE);
                ivReceipt.setVisibility(View.VISIBLE);
                GlideUtil.display(ScanAndPaymentActivity.this, Constants.IMG_HOST + receiptUrl, ivReceipt);
                break;
        }
    }


    /****
     *
     */


    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

}
