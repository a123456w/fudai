package com.qlzx.mylibrary.http;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 87901 on 2016/12/14.
 */

public class OkHttpDownloadManager {

    private static OkHttpDownloadManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;

    private OkHttpDownloadManager() {
        mOkHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS)
                .build();
        mDelivery = new Handler(Looper.getMainLooper());

    }

    public static OkHttpDownloadManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpDownloadManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpDownloadManager();
                }
            }
        }
        return mInstance;
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }


    public void download(String url, FileCallBack callback) {


        final FileCallBack finalCallback = callback;

        final Request request = new Request.Builder().url(url).build();
        final Call call = new OkHttpClient().newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailResultCallback(e, finalCallback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.code() >= 400 && response.code() <= 599) {
                    try {
                        sendFailResultCallback(new RuntimeException(response.body().string()), finalCallback);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                try {
                    File file = finalCallback.parseNetworkResponse(response);
                    sendSuccessResultCallback(file, finalCallback);

                } catch (IOException e) {
                    sendFailResultCallback(e, finalCallback);
                }
            }

        });
    }

    private void sendSuccessResultCallback(final File file, final FileCallBack callback) {

        if (callback == null) {
            return;
        }

        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(file);
                callback.onAfter();
            }
        });
    }


    private void sendFailResultCallback(final Exception e, final FileCallBack callback) {
        if (callback == null) {
            return;
        }

        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(e);
                callback.onAfter();
            }
        });
    }


}
