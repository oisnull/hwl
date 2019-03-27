package com.hwl.beta.net.near;

import com.hwl.beta.net.RequestBase;
import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.RetrofitUtils;
import com.hwl.beta.net.near.body.AddNearCircleInfoRequest;
import com.hwl.beta.net.near.body.AddNearCircleInfoResponse;
import com.hwl.beta.net.near.body.AddNearCommentRequest;
import com.hwl.beta.net.near.body.AddNearCommentResponse;
import com.hwl.beta.net.near.body.DeleteNearCircleInfoRequest;
import com.hwl.beta.net.near.body.DeleteNearCircleInfoResponse;
import com.hwl.beta.net.near.body.DeleteNearCommentRequest;
import com.hwl.beta.net.near.body.DeleteNearCommentResponse;
import com.hwl.beta.net.near.body.GetNearCircleDetailRequest;
import com.hwl.beta.net.near.body.GetNearCircleDetailResponse;
import com.hwl.beta.net.near.body.GetNearCircleInfosRequest;
import com.hwl.beta.net.near.body.GetNearCircleInfosResponse;
import com.hwl.beta.net.near.body.GetNearCommentsRequest;
import com.hwl.beta.net.near.body.GetNearCommentsResponse;
import com.hwl.beta.net.near.body.SetNearLikeInfoRequest;
import com.hwl.beta.net.near.body.SetNearLikeInfoResponse;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.sp.UserSP;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2018/3/8.
 */

public class NearCircleService {

    public static Observable<ResponseBase<GetNearCircleInfosResponse>> getNearCircleInfos(long minNearCircleId, int pageCount) {
        return getNearCircleInfos(minNearCircleId, pageCount);
    }

    public static Observable<GetNearCircleInfosResponse> getNearCircleInfos(long minNearCircleId, int pageCount, List<NetNearCircleMatchInfo> nearCircleMatchInfos) {
        GetNearCircleInfosRequest requestBody = new GetNearCircleInfosRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setLat(UserPosSP.getLatitude());
        requestBody.setLon(UserPosSP.getLontitude());
        requestBody.setMinNearCircleId(minNearCircleId);
        requestBody.setNearCircleMatchInfos(nearCircleMatchInfos);
        requestBody.setCount(pageCount <= 0 ? 15 : pageCount);
        return RetrofitUtils.createApi(INearCircleService.class)
                .getNearCircleInfos(new RequestBase(UserSP.getUserToken(), requestBody))
				.Map(new NetDefaultFunction<ResponseBase<GetNearCircleInfosResponse>,GetNearCircleInfosResponse>())
                .subscribeOn(Schedulers.io());
    }

    public static Observable<ResponseBase<GetNearCommentsResponse>> getNearComments(long nearCircleId, int lastCommentId) {
        GetNearCommentsRequest requestBody = new GetNearCommentsRequest();
        requestBody.setCount(3);
        requestBody.setLastCommentId(lastCommentId);
        requestBody.setNearCircleId(nearCircleId);
        Observable<ResponseBase<GetNearCommentsResponse>> response = RetrofitUtils.createApi(INearCircleService.class)
                .getNearComments(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public static Observable<ResponseBase<GetNearCircleDetailResponse>> getNearCircleDetail(long nearCircleId) {
        GetNearCircleDetailRequest requestBody = new GetNearCircleDetailRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setNearCircleId(nearCircleId);
        Observable<ResponseBase<GetNearCircleDetailResponse>> response = RetrofitUtils.createApi(INearCircleService.class)
                .getNearCircleDetail(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public static Observable<ResponseBase<AddNearCircleInfoResponse>> addNearCircleInfo(String content, List<NetImageInfo> images) {
        return addNearCircleInfo(content, images, null, null, null);
    }

    public static Observable<ResponseBase<AddNearCircleInfoResponse>> addNearCircleInfo(String content, List<NetImageInfo> images, String linkTitle, String linkUrl, String linkImage) {
        AddNearCircleInfoRequest requestBody = new AddNearCircleInfoRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setLat(UserPosSP.getLatitude());
        requestBody.setLon(UserPosSP.getLontitude());
        requestBody.setContent(content);
        requestBody.setPosId(UserPosSP.getUserPosId());
        requestBody.setPosDesc(UserPosSP.getPublishDesc());
        requestBody.setImages(images);
        requestBody.setLinkImage(linkImage);
        requestBody.setLinkTitle(linkTitle);
        requestBody.setLinkUrl(linkUrl);
        Observable<ResponseBase<AddNearCircleInfoResponse>> response = RetrofitUtils.createApi(INearCircleService.class)
                .addNearCircleInfo(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public static Observable<ResponseBase<SetNearLikeInfoResponse>> setNearLikeInfo(int actionType, long nearCircleId) {
        SetNearLikeInfoRequest requestBody = new SetNearLikeInfoRequest();
        requestBody.setLikeUserId(UserSP.getUserId());
        requestBody.setActionType(actionType);
        requestBody.setNearCircleId(nearCircleId);
        Observable<ResponseBase<SetNearLikeInfoResponse>> response = RetrofitUtils.createApi(INearCircleService.class)
                .setNearLikeInfo(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public static void addComment(long nearCircleId, String content) {
        addComment(nearCircleId, content, 0);
    }

    public static Observable<ResponseBase<AddNearCommentResponse>> addComment(long nearCircleId, String content, long replyUserId) {
        AddNearCommentRequest requestBody = new AddNearCommentRequest();
        requestBody.setCommentUserId(UserSP.getUserId());
        requestBody.setNearCircleId(nearCircleId);
        requestBody.setContent(content);
        requestBody.setReplyUserId(replyUserId);
        Observable<ResponseBase<AddNearCommentResponse>> response = RetrofitUtils.createApi(INearCircleService.class)
                .addNearComment(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public static Observable<ResponseBase<DeleteNearCommentResponse>> addComment(int commentId) {
        DeleteNearCommentRequest requestBody = new DeleteNearCommentRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setCommentId(commentId);
        Observable<ResponseBase<DeleteNearCommentResponse>> response = RetrofitUtils.createApi(INearCircleService.class)
                .deleteNearComment(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public static Observable<ResponseBase<DeleteNearCircleInfoResponse>> deleteNearCircleInfo(long nearCircleId) {
        DeleteNearCircleInfoRequest requestBody = new DeleteNearCircleInfoRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setNearCircleId(nearCircleId);
        Observable<ResponseBase<DeleteNearCircleInfoResponse>> response = RetrofitUtils.createApi(INearCircleService.class)
                .deleteNearCircleInfo(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public interface INearCircleService {
        @POST("api/GetNearCircleInfos")
        Observable<ResponseBase<GetNearCircleInfosResponse>> getNearCircleInfos(@Body RequestBase
                <GetNearCircleInfosRequest> request);

        @POST("api/AddNearCircleInfo")
        Observable<ResponseBase<AddNearCircleInfoResponse>> addNearCircleInfo(@Body RequestBase<AddNearCircleInfoRequest> request);

        @POST("api/GetNearCircleDetail")
        Observable<ResponseBase<GetNearCircleDetailResponse>> getNearCircleDetail(@Body RequestBase<GetNearCircleDetailRequest> request);

        @POST("api/SetNearLikeInfo")
        Observable<ResponseBase<SetNearLikeInfoResponse>> setNearLikeInfo(@Body RequestBase<SetNearLikeInfoRequest> request);

        @POST("api/AddNearComment")
        Observable<ResponseBase<AddNearCommentResponse>> addNearComment(@Body RequestBase<AddNearCommentRequest> request);

        @POST("api/GetNearComments")
        Observable<ResponseBase<GetNearCommentsResponse>> getNearComments(@Body RequestBase<GetNearCommentsRequest> request);

        @POST("api/DeleteNearComment")
        Observable<ResponseBase<DeleteNearCommentResponse>> deleteNearComment(@Body RequestBase<DeleteNearCommentRequest> request);

        @POST("api/DeleteNearCircleInfo")
        Observable<ResponseBase<DeleteNearCircleInfoResponse>> deleteNearCircleInfo(@Body RequestBase<DeleteNearCircleInfoRequest> request);
    }
}
