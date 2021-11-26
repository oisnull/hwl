package com.hwl.beta.net;

import android.util.Log;

import com.hwl.beta.AppConfig;
import com.hwl.beta.BuildConfig;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.GzipSink;
import okio.Okio;

/**
 * Created by Administrator on 2018/3/28.
 */

public class HttpInterceptor implements Interceptor {
    //    private static final boolean isDebug = false;
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;
        String url = chain.request().url().toString();
        if (chain != null && chain.request().body() != null) {
            Buffer bufferedSink = new Buffer();
            chain.request().body().writeTo(bufferedSink);
            if (BuildConfig.DEBUG)
                Log.d(url, bufferedSink.buffer().readUtf8());
        }

        Request originalRequest = chain.request();
        if (originalRequest.body() == null || originalRequest.header("Content-Encoding") !=
                null) {
            response = chain.proceed(originalRequest);
        } else {
            Request compressedRequest = originalRequest.newBuilder()
                    .header("User-Agent", getUserAgent())
//                    .header("Content-Encoding", "gzip")
//                    .method(originalRequest.method(), gzip(originalRequest.body()))
                    .build();
            response = chain.proceed(compressedRequest);
        }

        if (response != null && response.body() != null) {
            BufferedSource source = response.body().source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.getBuffer();
            Charset charset = UTF8;
            MediaType contentType = response.body().contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            if (BuildConfig.DEBUG)
                Log.d(url, buffer.clone().readString(charset));
        }

        return response;


    }

    /**
     * 获得请求的服务端数据的userAgent
     *
     * @return
     */
    private static String getUserAgent() {
        StringBuilder ua = new StringBuilder("Android_MCF");
        //ua.append('/' + MCApplication.getContext().getPackageInfo().versionName + '_'
        //       + MCApplication.getContext().getPackageInfo().versionCode);// app版本信息
        ua.append("/Android");// 手机api系统平台
        ua.append("/" + android.os.Build.VERSION.RELEASE);// 手机系统版本
        ua.append("/" + android.os.Build.MODEL); // 手机型号
        //ua.append("/" + DeviceUtils.getUUID(MCApplication.getContext()));// 客户端唯一标识
        return ua.toString();
    }

    private RequestBody gzip(final RequestBody body) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public long contentLength() {
                return -1; // 无法知道压缩后的数据大小
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        };
    }

}
