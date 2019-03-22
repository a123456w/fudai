package com.qlzx.mylibrary.http;


import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 87901 on 2016/5/5.
 */
public class HttpHelp {

    private static Retrofit instance;
    private HttpHelp(){}

    public static OkHttpClient getOkHttp(final Context context) {
        Interceptor mTokenInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                PreferencesHelper preferencesHelper = new PreferencesHelper(context);
                String token = preferencesHelper.getToken();
                if (TextUtils.isEmpty(token)) {
                    LogUtil.d("token null");
                    return chain.proceed(originalRequest);
                }
                LogUtil.d("HttpHelp   token  "+token);
                Request authorised = originalRequest.newBuilder()
                        .header("appkey", token)
                        .build();
                return chain.proceed(authorised);
            }
        };




        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient mClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(600,TimeUnit.SECONDS)
                .writeTimeout(600,TimeUnit.SECONDS)
                .addNetworkInterceptor(mTokenInterceptor)
                .build();

        return mClient;
    }

    /**
     * 需要在请求头里添加东西
     * @param context
     * @return
     */
    public static Retrofit getRetrofit(Context context) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.HOST)
                .client(getOkHttp(context))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return mRetrofit;
    }

    /**
     * 不需要在请求头添加东西,可以做成单例模式
     * @return
     */
    public static Retrofit getInstance(){
        if (instance==null){
            synchronized (HttpHelp.class){
                if (instance==null){
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(interceptor)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(5, TimeUnit.SECONDS)
                            .readTimeout(600,TimeUnit.SECONDS)
                            .writeTimeout(600,TimeUnit.SECONDS)
                            .build();

                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .create();
                    instance = new Retrofit.Builder()
                            .baseUrl(Constants.HOST)
                            .client(client)
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                }
            }
        }

        return instance;
    }

}
