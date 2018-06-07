package com.xx.gamelibrary.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RetrofitManager
 * (๑• . •๑)
 * 类描述:
 * Created by 雷小星🍀 on 2017/6/21 11:12
 */

public class RetrofitManager {

    private static Retrofit retrofit;
    private static OkHttpClient mOkHttpClient;

    /**
     * 创建Api对象
     *
     * @param service Api接口
     * @param <T>     接口
     * @param baseUrl 接口基础url
     * @return Api对象
     */
    public static <T> T createApi(Class<T> service, String baseUrl) {
        return getRetrofit2(baseUrl).create(service);
    }

    /**
     * 获取Retrofit2
     *
     * @return Retrofit
     */
    private static Retrofit getRetrofit2(String baseUrl) {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(getHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /**
     * 获取配置好的 OkHttpClient
     *
     * @return OkHttpClient
     */
    private static OkHttpClient getHttpClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor()//okhttp日志拦截器
                            .setLevel(HttpLoggingInterceptor.Level.BODY))
                    .readTimeout(10, TimeUnit.SECONDS)//读取时超时时间
                    .writeTimeout(10, TimeUnit.SECONDS)//写入时超时时间
                    .connectTimeout(15, TimeUnit.SECONDS)//链接超时时间
                    .build();
        }
        return mOkHttpClient;
    }

}
