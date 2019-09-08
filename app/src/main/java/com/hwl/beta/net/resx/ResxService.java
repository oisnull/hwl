package com.hwl.beta.net.resx;

import com.hwl.beta.net.NetDefaultFunction;
import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.RetrofitUtils;
import com.hwl.beta.net.resx.body.AudioUploadResponse;
import com.hwl.beta.net.resx.body.ImageUploadResponse;
import com.hwl.beta.net.resx.body.ResxUploadRequest;
import com.hwl.beta.net.resx.body.VideoUploadResponse;
import com.hwl.beta.sp.UserSP;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Administrator on 2019/9/3.
 */

public class ResxService {

    public static Observable<ImageUploadResponse> imageUpload(byte[] bitmap, int resxType) {
        return imageUpload(null, bitmap, resxType);
    }

    public static Observable<ImageUploadResponse> imageUpload(File file, int resxType) {
        return imageUpload(file, null, resxType);
    }

//    private static Observable<ImageUploadResponse> imageUpload(File file, byte[] bitmap,
//                                                                int resxType) {
//        RequestBody requestFile = null;
//        String fileName = null;
//        if (file != null) {
//            requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            fileName = file.getName();
//        } else {
//            requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bitmap);
//            fileName = "user-head.png";
//        }
//        MultipartBody.Part fileContent = MultipartBody.Part.createFormData("image", fileName,
//                requestFile);
//
//        ResxUploadRequest requestBody = new ResxUploadRequest();
//        requestBody.setUserId(UserSP.getUserId());
//        requestBody.setToken(UserSP.getUserToken());
//        requestBody.setResxType(resxType);
////        requestBody.setFiles(fileContent);
//
//        return RetrofitUtils.createResxApi(IResxService.class)
//                .imageUpload(requestBody)
//                .map(new NetDefaultFunction<ImageUploadResponse>())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }

    private static Observable<ImageUploadResponse> imageUpload(File file, byte[] bitmap,
                                                               int resxType) {
        RequestBody requestFile = null;
        String fileName = null;
        if (file != null) {
            requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            fileName = file.getName();
        } else {
            requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bitmap);
            fileName = "user-head.png";
        }
        MultipartBody.Part fileContent = MultipartBody.Part.createFormData("image", fileName,
                requestFile);

        ResxUploadRequest requestBody = new ResxUploadRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setToken(UserSP.getUserToken());
        requestBody.setResxType(resxType);
//        requestBody.setFiles(fileContent);

        return RetrofitUtils.createResxApi(IResxService.class)
                .imageUpload(fileContent, requestBody)
                .map(new NetDefaultFunction<ImageUploadResponse>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<AudioUploadResponse> audioUpload(File file, int resxType) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileContent = MultipartBody.Part.createFormData("audio",
                file.getName(), requestFile);

        ResxUploadRequest requestBody = new ResxUploadRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setToken(UserSP.getUserToken());
        requestBody.setResxType(resxType);
//        requestBody.setFiles(fileContent);

        return RetrofitUtils.createResxApi(IResxService.class)
                .audioUpload(requestBody)
                .map(new NetDefaultFunction<AudioUploadResponse>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<VideoUploadResponse> videoUpload(File file, int resxType) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileContent = MultipartBody.Part.createFormData("video",
                file.getName(), requestFile);

        ResxUploadRequest requestBody = new ResxUploadRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setToken(UserSP.getUserToken());
        requestBody.setResxType(resxType);
//        requestBody.setFiles(fileContent);

        return RetrofitUtils.createResxApi(IResxService.class)
                .videoUpload(requestBody)
                .map(new NetDefaultFunction<VideoUploadResponse>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public interface IResxService {
        @Multipart
        @POST("resx/ImageUpload")
        Observable<ResponseBase<ImageUploadResponse>> imageUpload(@Part MultipartBody.Part file,
                                                                  @Part ResxUploadRequest request);

        @Multipart
        @POST("resx/AudioUpload")
        Observable<ResponseBase<AudioUploadResponse>> audioUpload(@Part ResxUploadRequest request);

        @Multipart
        @POST("resx/VideoUpload")
        Observable<ResponseBase<VideoUploadResponse>> videoUpload(@Part ResxUploadRequest request);
    }
}
