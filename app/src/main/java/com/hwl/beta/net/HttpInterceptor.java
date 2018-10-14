package com.hwl.beta.net;

import android.util.Log;

import com.hwl.beta.AppConfig;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by Administrator on 2018/3/28.
 */

public class HttpInterceptor implements Interceptor {
    private static final boolean isDebug = false;
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;
        String urlPath = chain.request().url().encodedPath();
        String tag = AppConfig.NET_API_DEBUG_TAG + "-" + urlPath.substring(urlPath.lastIndexOf
                ("/") + 1);

        if (chain != null && chain.request().body() != null) {
            Buffer bufferedSink = new Buffer();
            chain.request().body().writeTo(bufferedSink);
            Log.d(tag, bufferedSink.buffer().readUtf8());
        }

        //本地测试
        if (isDebug) {
            Response.Builder builder1 = new Response.Builder();
            Headers headers = new Headers.Builder().add("Content-Type", "application/json; " +
                    "charset=utf-8").build();
            builder1.request(chain.request());
            //根据方法名获取本地json文件
//            InputStream is = MCApplication.getContext().getAssets().open("json" + urlPath
// .substring(urlPath.lastIndexOf("/")) + ".json");
//            ResponseBody body = new RealResponseBody(headers, Okio.buffer(Okio.source(is)));
            builder1.protocol(Protocol.HTTP_2);
            builder1.code(200);
            //builder1.body(body);
            response = builder1.build();
        } else {
            Request originalRequest = chain.request();
            if (originalRequest.body() == null || originalRequest.header("Content-Encoding") !=
                    null) {
                response = chain.proceed(originalRequest);
            } else {
                //gzip压缩请求体
                Request compressedRequest = originalRequest.newBuilder()
                        .header("Content-Encoding", "gzip")
                        .header("User-Agent", getUserAgent())
//                                                .method(originalRequest.method(), gzip
// (originalRequest.body()))
                        .build();
                response = chain.proceed(compressedRequest);
            }
        }

        if (response != null && response.body() != null) {
            BufferedSource source = response.body().source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = UTF8;
            MediaType contentType = response.body().contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            if (response.body().contentLength() != 0) {
                Log.d(tag, buffer.clone().readString(charset));
            }
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

}
