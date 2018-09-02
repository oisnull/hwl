package com.hwl.beta.net.resx;

import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.RetrofitUtils;
import com.hwl.beta.net.resx.body.UpResxResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.utils.StringUtils;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2018/1/24.
 */

public class UploadService {

    public final static int CHUNKED_LENGTH = 700 * 1024;

    public static Observable<ResponseBase<UpResxResponse>> upImage(File file, int resxType) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        return RetrofitUtils.createResxApi(IUploadService.class)
                .upImage(body, UserSP.getUserToken(), resxType)
                .subscribeOn(Schedulers.io());
//                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<ResponseBase<UpResxResponse>> upImage(byte[] bitmap, int resxType) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bitmap);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "userhead.png", requestFile);
        return RetrofitUtils.createResxApi(IUploadService.class)
                .upImage(body, UserSP.getUserToken(), resxType)
                .subscribeOn(Schedulers.io());
//                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<ResponseBase<UpResxResponse>> upAudio(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("audio", file.getName(), requestFile);
        return RetrofitUtils.createResxApi(IUploadService.class)
                .upAudio(body, UserSP.getUserToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //chunkIndex 分块索引
    //chunkCount 分块总数量
    //tempFileUrl为服务端生成后返回的临时文件地址
    //tempFileUrl第一次上传为空，后面需要将服务端返回的地址传进来
    public static Observable<ResponseBase<UpResxResponse>> upVideo(byte[] byteContent, String fileName, int chunkIndex, int chunkCount, String tempFileUrl) {
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

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), byteContent);
        builder.addFormDataPart("video", fileName, requestFile);
        return RetrofitUtils.createResxApi(IUploadService.class).upVideo(builder.build(), UserSP.getUserToken());
    }

    public interface IUploadService {
        @Multipart
        @POST("resx/image")
        Observable<ResponseBase<UpResxResponse>> upImage(@Part MultipartBody.Part file, @Query
                ("token") String token, @Query("resxtype") int resxType);

        @Multipart
        @POST("resx/audio")
        Observable<ResponseBase<UpResxResponse>> upAudio(@Part MultipartBody.Part file, @Query("token") String token);

        @POST("resx/video")
        Observable<ResponseBase<UpResxResponse>> upVideo(@Body MultipartBody file, @Query("token") String token);
    }
}
