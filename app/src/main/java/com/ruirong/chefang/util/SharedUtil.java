package com.ruirong.chefang.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.ruirong.chefang.bean.ShareBean;
import com.ruirong.chefang.http.RemoteApi;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/3/31.
 * 分享
 */


    /* QQ分享必须放置的东西   复制粘贴即可

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    */

public class SharedUtil {
    private static String head = Constants.IMG_HOST + "/index.php?s=/Login/register.html&invite_phone=%s&platform=1";
    private static String url = "";
    private static Context mContext;
    private static String phoneNumber = "";

    public SharedUtil(Context mContext) {
        this.mContext = mContext;
        phoneNumber = new PreferencesHelper(mContext).getPhoneNum();
      //  url = head + phoneNumber + "&platform=1";
/*
        url = Constants.IMG_HOST + "/index.php?s=/Login/register.html&invite_phone="+phoneNumber+"&platform=1";
*/
      //  url = String.format(head,phoneNumber);
        Log.e("url",phoneNumber);
        Log.e("url",url);
    }

    /**
     * 分享
     * @param activity
     */
    public void initShared(final Activity activity) {
        HttpHelp.getInstance().create(RemoteApi.class).shareMessage(new PreferencesHelper(activity).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ShareBean>>(activity,null){
                    @Override
                    public void onNext(BaseBean<ShareBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code==0){
                            ShareBean shareBean = baseBean.data;
                            if (shareBean!=null){
                                //  url = shareBean.getUrl();
                                UMWeb web = new UMWeb(shareBean.getUrl());
                                web.setTitle(shareBean.getTitle());//标题
                                web.setThumb(new UMImage(mContext,Constants.IMG_HOST+shareBean.getPic()));  //缩略图
                                web.setDescription(shareBean.getContent());//描述
                                new ShareAction(activity)
                                        .withMedia(web)
                                        .setDisplayList(SHARE_MEDIA.WEIXIN,
                                                SHARE_MEDIA.WEIXIN_CIRCLE,
                                                SHARE_MEDIA.QQ,
                                                SHARE_MEDIA.QZONE)
                                        .setCallback(umShareListener).open();
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 分享
     * @param activity
     * @param myUrl
     * @param name
     * @param content
     */
    public void initSharedG(final Activity activity, final String myUrl, final String name, final String content) {
        HttpHelp.getInstance().create(RemoteApi.class).shareMessage(new PreferencesHelper(activity).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ShareBean>>(activity,null){
                    @Override
                    public void onNext(BaseBean<ShareBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code==0){
                            ShareBean shareBean = baseBean.data;
                            if (shareBean!=null){
                                UMWeb web = new UMWeb(shareBean.getUrl());
                                web.setTitle(name);//标题
                                web.setThumb(new UMImage(mContext, Constants.IMG_HOST+shareBean.getPic()));  //缩略图
                                web.setDescription(content);//描述
                                new ShareAction(activity)
                                        .withMedia(web)
                                        .setDisplayList(SHARE_MEDIA.WEIXIN,
                                                SHARE_MEDIA.WEIXIN_CIRCLE,
                                                SHARE_MEDIA.QQ,
                                                SHARE_MEDIA.QZONE)
                                        .setCallback(umShareListener).open();
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });




    }

    public static UMShareListener umShareListener = new UMShareListener() {


        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            ToastUtil.showToast(mContext, "失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            ToastUtil.showToast(mContext, "取消");
        }
    };

}
