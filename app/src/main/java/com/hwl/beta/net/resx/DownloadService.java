package com.hwl.beta.net.resx;

import com.hwl.beta.net.RetrofitUtils;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2018/3/2.
 */

public class DownloadService {

    public static Observable<ResponseBody> downloadFile(String remoteUrl) {
        return RetrofitUtils.createDownApi(IDownloadService.class)
                .downloadFile(remoteUrl);
    }

    public static Observable<Response<ResponseBody>> downloadStream(String remoteUrl, IDownloadProgressListener progressListener) {
        return RetrofitUtils.createStreamDownApi(IDownloadService.class, progressListener)
                .downloadStreamFile(remoteUrl);

    }

    interface IDownloadService {
        @Streaming
        @GET
        Observable<ResponseBody> downloadFile(@Url String fileUrl);

        @Streaming
        @GET
        Observable<Response<ResponseBody>> downloadStreamFile(@Url String fileUrl);
    }

//    public static Observable<ResponseBody> downloadStream(String remoteUrl, final String localFilePath, IDownloadProgressListener progressListener) {
//        if (completeListener == null || handler == null) return;
//        final File downloadFile = new File(localFilePath);
//        Call call = RetrofitUtils.createStreamDownApi(IDownloadService.class, progressListener).downloadFile(remoteUrl);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    BufferedSink sink = null;
//                    try {
//                        sink = Okio.buffer(Okio.sink(downloadFile));
//                        sink.writeAll(response.body().source());
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                completeListener.onSuccess(localFilePath);
//                            }
//                        });
//                    } catch (final Exception e) {
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                completeListener.onFiald(e.getMessage());
//                            }
//                        });
//                    } finally {
//                        try {
//                            if (sink != null) sink.close();
//                        } catch (final IOException e) {
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    completeListener.onFiald(e.getMessage());
//                                }
//                            });
//                        }
//                    }
//                } else {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            completeListener.onFiald("文件下载失败");
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                completeListener.onFiald(t.getMessage());
//            }
//        });
//    }
}
