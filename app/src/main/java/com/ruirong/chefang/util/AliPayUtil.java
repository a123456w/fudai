package com.ruirong.chefang.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.ruirong.chefang.bean.AliPayResult;
import com.ruirong.chefang.event.OrderEvent;

import java.util.Map;

/**
 * Created by 87901 on 2016/10/26.
 * 支付宝支付
 */

public class AliPayUtil {

    private static Context context;
    private static String tag;
    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    AliPayResult aliPayResult = new AliPayResult((Map<String, String>) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = aliPayResult.getResult();

                    LogUtil.d("AliPayUtil resultInfo---" + resultInfo);

                    String resultStatus = aliPayResult.getResultStatus();
                    LogUtil.d("AliPayUtil resultStatus---" + resultStatus);
                    String memo = aliPayResult.getMemo();

                    LogUtil.d("AliPayUtil memo---" + memo);
                    String result = aliPayResult.getResult();
                    LogUtil.d("AliPayUtil result----" + result);
                    String re = result.replace("+", "!!");
                    LogUtil.d("AliPayUtil  re----"+re);

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {

                        EventBusUtil.post(new OrderEvent(true,tag));

                    } else {
                        EventBusUtil.post(new OrderEvent(false,tag));
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {

                        } else {

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };


    public void pay(final String orderInfo, final Activity activity, String tag) {
        this.tag = tag;
        this.context=activity;
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

}
