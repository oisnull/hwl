package com.hwl.beta.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/1/14.
 */

public class RetrofitUtils {
    private static Retrofit singleton;
    private static Retrofit resxSingleton;
    private static Retrofit resxDownton;
    private static Retrofit streamDownton;

    public static final Gson gsonTimeFormat = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static <T> T createApi(Class<T> clazz) {
        if (singleton == null) {
            synchronized (RetrofitUtils.class) {
                if (singleton == null) {
                    Retrofit.Builder builder = new Retrofit.Builder();
                    builder.baseUrl(BuildConfig.ApiHost);
                    builder.addConverterFactory(GsonConverterFactory.create(gsonTimeFormat));
                    builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
                    builder.client(OkHttpUtils.getInstance());
                    singleton = builder.build();
                }
            }
        }
        return singleton.create(clazz);
    }

    public static <T> T createResxApi(Class<T> clazz) {
        if (resxSingleton == null) {
            synchronized (RetrofitUtils.class) {
                if (resxSingleton == null) {
                    Retrofit.Builder builder = new Retrofit.Builder();
                    builder.baseUrl(BuildConfig.ApiResxHost);
                    builder.addConverterFactory(GsonConverterFactory.create());
                    builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
                    builder.client(OkHttpUtils.getInstance());
                    resxSingleton = builder.build();
                }
            }
        }
        return resxSingleton.create(clazz);
    }

    public static <T> T createDownApi(Class<T> clazz) {
        if (resxDownton == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl(BuildConfig.ApiResxHost)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .callbackExecutor(Executors.newFixedThreadPool(1));
            resxDownton = builder.build();
        }
        return resxDownton.create(clazz);
    }

//    public static <T> T createStreamDownApi(Class<T> clazz, IDownloadProgressListener progressListener) {
//        if (streamDownton == null) {
//            Retrofit.Builder builder = new Retrofit.Builder();
//            builder.baseUrl(ApiConfig.NET_RESX_HOST);
//            builder.client(OkHttpUtils.getDownloadStreamInstance(progressListener))
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .callbackExecutor(Executors.newFixedThreadPool(1));
//            streamDownton = builder.build();
//        }
//        return streamDownton.create(clazz);
//    }
}
