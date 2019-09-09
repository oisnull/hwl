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
	private static final MediaType RESX_MEDIA_TYPE = MediaType.parse("multipart/form-data");

	private static MultipartBody getResxMultipartBody(File file, byte[] imageBytes,int resxType){
        RequestBody requestFile = null;
        String fileName = null;
		long userId = UserSP.getUserId();
        if (file != null) {
            requestFile = RequestBody.create(RESX_MEDIA_TYPE, file);
            fileName = file.getName();
        } else {
            requestFile = RequestBody.create(RESX_MEDIA_TYPE, imageBytes);
            fileName = String.format("uhead-%d.png", userId);
        }
	
        return new MultipartBody.Builder().setType(MultipartBody.FORM)
		.addFormDataPart("Files", fileName, requestFile)
		.addFormDataPart("Token", UserSP.getUserToken())
		.addFormDataPart("UserId", userId.toString())
		.addFormDataPart("ResxType", resxType.toString())
		.build();
	}

    public static Observable<ImageUploadResponse> imageUpload(byte[] imageBytes, int resxType) {
        return imageUpload(null, imageBytes, resxType);
    }

    public static Observable<ImageUploadResponse> imageUpload(File file, int resxType) {
        return imageUpload(file, null, resxType);
    }

    private static Observable<ImageUploadResponse> imageUpload(File file, byte[] imageBytes,
                                                               int resxType) {
        MultipartBody request = getResxMultipartBody(file,imageBytes,resxType);
        return RetrofitUtils.createResxApi(IResxService.class)
                .imageUpload(request)
                .map(new NetDefaultFunction<ImageUploadResponse>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<AudioUploadResponse> audioUpload(File file, int resxType) {
        MultipartBody request = getResxMultipartBody(file,null,resxType);
        return RetrofitUtils.createResxApi(IResxService.class)
                .audioUpload(request)
                .map(new NetDefaultFunction<AudioUploadResponse>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<VideoUploadResponse> videoUpload(File file, int resxType) {
        MultipartBody request = getResxMultipartBody(file,null,resxType);
        return RetrofitUtils.createResxApi(IResxService.class)
                .videoUpload(request)
                .map(new NetDefaultFunction<VideoUploadResponse>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public interface IResxService {
        @POST("resx/ImageUpload")
        Observable<ResponseBase<ImageUploadResponse>> imageUpload(@Body MultipartBody request);

        @POST("resx/AudioUpload")
        Observable<ResponseBase<AudioUploadResponse>> audioUpload(@Body MultipartBody request);

        @POST("resx/VideoUpload")
        Observable<ResponseBase<VideoUploadResponse>> videoUpload(@Body MultipartBody request);
    }
}
