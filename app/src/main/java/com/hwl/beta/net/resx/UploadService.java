package com.hwl.beta.net.resx;

import com.hwl.beta.net.NetDefaultFunction;
import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.RetrofitUtils;
import com.hwl.beta.net.resx.body.UpResxResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.utils.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2018/1/24.
 */

public class UploadService {

    public final static int CHUNKED_LENGTH = 700 * 1024;

    private static MultipartBody.Builder getDefaultParam(int resxType) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"ResxType\""),
                RequestBody.create(null, String.valueOf(resxType)));
        builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"Token\""),
                RequestBody.create(null, UserSP.getUserToken()));
        builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"UserId\""),
                RequestBody.create(null, UserSP.getUserId() + ""));
        return builder;
    }

    public static Observable<UpResxResponse> upImage(File file, int resxType) {
        MultipartBody.Builder builder = getDefaultParam(resxType);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),
                file);
        builder.addFormDataPart("Files", file.getName(), requestFile);
        return RetrofitUtils.createResxApi(IUploadService.class).upImage(builder.build())
                .map(new NetDefaultFunction<UpResxResponse>());
    }

//    public static Observable<UpResxResponse> upImage(byte[] bitmap, int resxType) {
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),
//                bitmap);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "userhead.png",
//                requestFile);
//        return RetrofitUtils.createResxApi(IUploadService.class)
//                .upImage(body, UserSP.getUserToken(), resxType)
//                .map(new NetDefaultFunction<UpResxResponse>())
//                .subscribeOn(Schedulers.io());
////                .observeOn(AndroidSchedulers.mainThread());
//    }

    public static Observable<UpResxResponse> upVoice(File file) {
        MultipartBody.Builder builder = getDefaultParam(0);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),
                file);
        builder.addFormDataPart("Files", file.getName(), requestFile);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),
//        file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("audio", file.getName(),
//                requestFile);
        return RetrofitUtils.createResxApi(IUploadService.class)
                .upVoice(builder.build())
                .map(new NetDefaultFunction<UpResxResponse>())
                .subscribeOn(Schedulers.io());
        // .observeOn(AndroidSchedulers.mainThread());
    }

    //chunkIndex 分块索引
    //chunkCount 分块总数量
    //tempFileUrl为服务端生成后返回的临时文件地址
    //tempFileUrl第一次上传为空，后面需要将服务端返回的地址传进来
    public static Observable<UpResxResponse> upVideo(byte[] byteContent,
                                                     String fileName,
                                                     int chunkIndex, int chunkCount
            , String tempFileUrl) {
        if (StringUtils.isBlank(fileName)) {
            fileName = "video.mp4";
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"tempFileUrl\""),
                RequestBody.create(null, tempFileUrl));
        builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"chunkIndex\""),
                RequestBody.create(null, chunkIndex + ""));
        builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"chunkCount\""),
                RequestBody.create(null, chunkCount + ""));

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),
                byteContent);
        builder.addFormDataPart("video", fileName, requestFile);
        return RetrofitUtils.createResxApi(IUploadService.class).upVideo(builder.build())
                .map(new NetDefaultFunction<UpResxResponse>());
    }

    public interface IUploadService {
        @POST("resx/ImageUpload")
        Observable<ResponseBase<UpResxResponse>> upImage(@Body MultipartBody file);

        @POST("resx/AudioUpload")
        Observable<ResponseBase<UpResxResponse>> upVoice(@Body MultipartBody file);

        @POST("resx/VideoUpload")
        Observable<ResponseBase<UpResxResponse>> upVideo(@Body MultipartBody file);
    }
}
