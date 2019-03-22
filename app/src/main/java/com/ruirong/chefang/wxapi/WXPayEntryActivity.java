package com.ruirong.chefang.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.ruirong.chefang.R;
import com.ruirong.chefang.common.Constants;
import com.ruirong.chefang.event.WxPayEvent;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * Created by 87901 on 2016/10/26.
 * 微信支付回调
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtil.d( "onPayFinish, errCode = " + resp.errCode);

        if (resp.errCode==0){
            LogUtil.d("WxPayEntryActivity  微信支付成功");
        }else if (resp.errCode==-1){
            LogUtil.d("WxPayEntryActivity  支付失败");
        }else if (resp.errCode==-2){
            LogUtil.d("WxPayEntryActivity  支付取消");

        }

        EventBusUtil.post(new WxPayEvent(Constants.WEIXIN_PAY_TYPE,resp.errCode));
        finish();

    }


}
