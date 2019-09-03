package com.hwl.beta.net.resx;

import com.hwl.beta.net.NetDefaultFunction;
import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.RetrofitUtils;
import com.hwl.beta.net.resx.body.UpResxResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.utils.StringUtils;

import java.io.File;

import io.reactivex.Observable;
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
 * Created by Administrator on 2019/9/3.
 */

public class ResxService {

    public static Observable<UpResxResponse> imageUpload(byte[] bitmap, int resxType) {
		return imageUpload(null,bitmap,resxType);
    }

    public static Observable<UpResxResponse> imageUpload(File file, int resxType) {
		return imageUpload(file,null,resxType);
    }

    private static Observable<ImageUploadResponse> imageUpload(File file, byte[] bitmap, int resxType) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file==null?bitmap: file);
        MultipartBody.Part fileContent = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        ResxUploadRequest requestBody = new ResxUploadRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setToken(UserSP.getUserToken());
        requestBody.setResxType(resxType);
        requestBody.setFiles(fileContent);

        return RetrofitUtils.createApi(IResxService.class)
                .imageUpload(requestBody)
                .map(new NetDefaultFunction<ResponseBase<ImageUploadResponse>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<AudioUploadResponse> audioUpload(File file, int resxType) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileContent = MultipartBody.Part.createFormData("audio", file.getName(), requestFile);

        ResxUploadRequest requestBody = new ResxUploadRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setToken(UserSP.getUserToken());
        requestBody.setResxType(resxType);
        requestBody.setFiles(fileContent);

        return RetrofitUtils.createApi(IResxService.class)
                .audioUpload(requestBody)
                .map(new NetDefaultFunction<ResponseBase<AudioUploadResponse>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<VideoUploadResponse> videoUpload(File file, int resxType) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileContent = MultipartBody.Part.createFormData("video", file.getName(), requestFile);

        ResxUploadRequest requestBody = new ResxUploadRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setToken(UserSP.getUserToken());
        requestBody.setResxType(resxType);
        requestBody.setFiles(fileContent);

        return RetrofitUtils.createApi(IResxService.class)
                .videoUpload(requestBody)
                .map(new NetDefaultFunction<ResponseBase<VideoUploadResponse>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public interface IResxService {
        @Multipart
        @POST("resx/ImageUpload")
        Observable<ResponseBase<ImageUploadResponse>> imageUpload(@Part ResxUploadRequest request);

        @Multipart
        @POST("resx/AudioUpload")
        Observable<ResponseBase<AudioUploadResponse>> audioUpload(@Part ResxUploadRequest request);
		
        @Multipart
        @POST("resx/VideoUpload")
        Observable<ResponseBase<VideoUploadResponse>> videoUpload(@Part ResxUploadRequest request);
    }
}
