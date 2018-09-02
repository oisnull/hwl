package com.hwl.beta.net;

import com.hwl.beta.net.resx.DownloadStreamResponseBody;
import com.hwl.beta.net.resx.IDownloadProgressListener;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/1/14.
 */

public class OkHttpUtils {
    private static OkHttpClient singleton;
    private static OkHttpClient streamSingleton;
    //private static final boolean isDebug = false;

    public static OkHttpClient getInstance() {
        if (singleton == null) {
            synchronized (OkHttpUtils.class) {
                if (singleton == null) {
                    try {
//                        //设置缓存
//                        File cacheDir = new File(getApplicationContext().getCacheDir(), ResponseCacheConfig.RESPONSE_CACHE);
                        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//                        builder.cache(new Cache(cacheDir, ResponseCacheConfig.RESPONSE_CACHE_SIZE));
                        //设置超时时间
                        builder.connectTimeout(NetConstant.HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
                        builder.readTimeout(NetConstant.HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS);
                        //增加拦截器
                        builder.addInterceptor(new HttpInterceptor());

                        singleton = builder.build();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return singleton;
    }

    public static OkHttpClient getDownloadStreamInstance(final IDownloadProgressListener progressListener) {
        if (streamSingleton == null) {
            synchronized (OkHttpUtils.class) {
                if (streamSingleton == null) {
                    streamSingleton = new OkHttpClient().newBuilder()//
                            .readTimeout(5, TimeUnit.SECONDS)//
                            .connectTimeout(5, TimeUnit.SECONDS)//
                            .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Response originalResponse = chain.proceed(chain.request());
                                    return originalResponse.newBuilder().body(new DownloadStreamResponseBody(originalResponse.body(), progressListener)).build();
                                }
                            })
                            .build();
                }
            }
        }
        return streamSingleton;
    }


}
