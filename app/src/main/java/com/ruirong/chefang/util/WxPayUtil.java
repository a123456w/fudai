package com.ruirong.chefang.util;

import android.content.Context;
import android.util.Log;

import com.qlzx.mylibrary.base.BaseSubscriber;

import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.LogUtil;

import com.qlzx.mylibrary.util.ToastUtil;
import com.ruirong.chefang.activity.ImmediatelyPaymentActivity;
import com.ruirong.chefang.bean.DoBuyConfirmBean;
import com.ruirong.chefang.bean.InfoWBean;
import com.ruirong.chefang.bean.StorageBean;
import com.ruirong.chefang.bean.WXOrderBean;
import com.ruirong.chefang.event.PendingPaymentEvent;
import com.ruirong.chefang.event.ReservationOrderEvent;
import com.ruirong.chefang.event.UpdateInformationEvent;
import com.ruirong.chefang.http.RemoteApi;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 87901 on 2016/11/22.
 * 微信支付
 */

public class WxPayUtil {
    /**
     * 充值金豆
     *
     * @param token
     * @param context
     * @param type
     * @param sum
     * @return
     */
    public static void wxPay(String token, final Context context, String sum, int type) {

        BaseSubscriber<BaseBean<WXOrderBean>> wxOrderSubscriber = new BaseSubscriber<BaseBean<WXOrderBean>>(context, null) {

            private IWXAPI wxApi;

            @Override
            public void onNext(BaseBean<WXOrderBean> wxOrderBean) {

                if (wxOrderBean != null && wxOrderBean.data != null) {

                    WXOrderBean order = wxOrderBean.data;
                    wxApi = WXAPIFactory.createWXAPI(context, order.getAppid());
                    wxApi.registerApp(order.getAppid());
                    LogUtil.d("appId---" + order.getAppid());

                    PayReq req = new PayReq();
                    //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                    req.appId = order.getAppid();
                    req.partnerId = order.getPartnerid();
                    req.prepayId = order.getPrepayid();
                    req.nonceStr = order.getNoncestr();
                    req.timeStamp = order.getTimestamp() + "";
                    req.packageValue = order.getPackageX();
                    req.sign = order.getSign();
//                    req.extData = "app data"; // optional

                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    wxApi.sendReq(req);

                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                Log.i("XXX", throwable.getLocalizedMessage());
            }
        };

        HttpHelp.getInstance().create(RemoteApi.class).wxRecharge(token, sum, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wxOrderSubscriber);

    }


    /**
     * 立即购买微信支付
     *
     * @param context
     * @param token
     * @param paytype
     * @param order_sn
     * @param type
     */
    public static void infoPreci(final Context context, String token, String paytype, String order_sn,
                                 String type) {

        HttpHelp.getInstance().create(RemoteApi.class).infoPreci(token, paytype, order_sn, type, null, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<InfoWBean>>(context, null) {

                    private IWXAPI wxApi;

                    @Override
                    public void onNext(BaseBean<InfoWBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean != null && baseBean.data != null) {

                            InfoWBean order = baseBean.data;
                            wxApi = WXAPIFactory.createWXAPI(context, order.getAppid());
                            wxApi.registerApp(order.getAppid());
                            LogUtil.d("appId---" + order.getAppid());

                            PayReq req = new PayReq();
                            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                            req.appId = order.getAppid();
                            req.partnerId = order.getPartnerid();
                            req.prepayId = order.getPrepayid();
                            req.nonceStr = order.getNoncestr();
                            req.timeStamp = order.getTimestamp() + "";
                            req.packageValue = order.getPackageX();
                            req.sign = order.getSign();
//                    req.extData = "app data"; // optional


                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            if (wxApi.isWXAppInstalled()){
                                wxApi.sendReq(req);
                            }else {
                                ToastUtil.showToast(context,"请先安装微信");
                            }


                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.i("XXX", throwable.getLocalizedMessage());
                    }
                });
    }


    /**
     * 线下支付 微信
     *
     * @param context
     * @param token
     */
    public static void doBuyConfirm(final Context context, String token, String shop_id, String money,
                                    String pay_type) {

        HttpHelp.getInstance().create(RemoteApi.class).doBuyConfirm(token, shop_id, money, pay_type, null, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<DoBuyConfirmBean>>(context, null) {

                    private IWXAPI wxApi;

                    @Override
                    public void onNext(BaseBean<DoBuyConfirmBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean != null && baseBean.data != null) {
                            DoBuyConfirmBean order = baseBean.data;
                            wxApi = WXAPIFactory.createWXAPI(context, order.getAppid());
                            wxApi.registerApp(order.getAppid());
                            LogUtil.d("appId---" + order.getAppid());

                            PayReq req = new PayReq();
                            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                            req.appId = order.getAppid();
                            req.partnerId = order.getPartnerid();
                            req.prepayId = order.getPrepayid();
                            req.nonceStr = order.getNoncestr();
                            req.timeStamp = order.getTimestamp() + "";
                            req.packageValue = order.getPackageX();
                            req.sign = order.getSign();
//                    req.extData = "app data"; // optional

                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            if (wxApi.isWXAppInstalled()){
                                wxApi.sendReq(req);
                            }else {
                                ToastUtil.showToast(context,"请先安装微信");
                            }

                        }
                        Log.i("XXX", baseBean.code + " " + baseBean.message);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.i("XXX", throwable.getLocalizedMessage());
                    }
                });
    }


    /**
     * 储值微信
     *
     * @param context
     * @param token
     */
    public static void storage(final Context context, String token, String shop_id, String money, String paytype) {

        HttpHelp.getInstance().create(RemoteApi.class).storage(token, shop_id, money, paytype)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<StorageBean>>(context, null) {

                    private IWXAPI wxApi;

                    @Override
                    public void onNext(BaseBean<StorageBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean != null && baseBean.data != null) {
                            StorageBean order = baseBean.data;
                            wxApi = WXAPIFactory.createWXAPI(context, order.getAppid());
                            wxApi.registerApp(order.getAppid());
                            LogUtil.d("appId---" + order.getAppid());

                            PayReq req = new PayReq();
                            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                            req.appId = order.getAppid();
                            req.partnerId = order.getPartnerid();
                            req.prepayId = order.getPrepayid();
                            req.nonceStr = order.getNoncestr();
                            req.timeStamp = order.getTimestamp() + "";
                            req.packageValue = order.getPackageX();
                            req.sign = order.getSign();
//                    req.extData = "app data"; // optional

                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            wxApi.sendReq(req);

                        }
                        Log.i("XXX", baseBean.code + " " + baseBean.message);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.i("XXX", throwable.getLocalizedMessage());
                    }
                });
    }

}
