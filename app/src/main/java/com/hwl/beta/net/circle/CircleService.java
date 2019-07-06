package com.hwl.beta.net.circle;

import com.hwl.beta.net.NetDefaultFunction;
import com.hwl.beta.net.RequestBase;
import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.RetrofitUtils;
import com.hwl.beta.net.circle.body.AddCircleCommentInfoRequest;
import com.hwl.beta.net.circle.body.AddCircleCommentInfoResponse;
import com.hwl.beta.net.circle.body.AddCircleInfoRequest;
import com.hwl.beta.net.circle.body.AddCircleInfoResponse;
import com.hwl.beta.net.circle.body.DeleteCircleInfoRequest;
import com.hwl.beta.net.circle.body.DeleteCircleInfoResponse;
import com.hwl.beta.net.circle.body.DeleteCommentInfoRequest;
import com.hwl.beta.net.circle.body.DeleteCommentInfoResponse;
import com.hwl.beta.net.circle.body.GetCircleCommentsRequest;
import com.hwl.beta.net.circle.body.GetCircleCommentsResponse;
import com.hwl.beta.net.circle.body.GetCircleDetailRequest;
import com.hwl.beta.net.circle.body.GetCircleDetailResponse;
import com.hwl.beta.net.circle.body.GetCircleInfosRequest;
import com.hwl.beta.net.circle.body.GetCircleInfosResponse;
import com.hwl.beta.net.circle.body.GetUserCircleInfosRequest;
import com.hwl.beta.net.circle.body.GetUserCircleInfosResponse;
import com.hwl.beta.net.circle.body.SetLikeInfoRequest;
import com.hwl.beta.net.circle.body.SetLikeInfoResponse;
import com.hwl.beta.net.near.NetImageInfo;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.sp.UserSP;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class CircleService {

    public static Observable<GetCircleInfosResponse> getCircleInfos(long viewUserId,
                                                                    long minCircleId,
                                                                    int pageCount,
                                                                    List<NetCircleMatchInfo> circleMatchInfos) {
        GetCircleInfosRequest requestBody = new GetCircleInfosRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setViewUserId(viewUserId);
        requestBody.setMinCircleId(minCircleId);
        requestBody.setCircleMatchInfos(circleMatchInfos);
        requestBody.setCount(pageCount);
        return RetrofitUtils.createApi(ICircleService.class)
                .getCircleInfos(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<GetCircleInfosResponse>>())
                .subscribeOn(Schedulers.io());
    }

    public static Observable<AddCircleInfoResponse> addCircleInfo(String content,
                                                                  List<NetImageInfo> images) {
        return addCircleInfo(content, images, null, null, null);
    }

    public static Observable<AddCircleInfoResponse> addCircleInfo(String content,
                                                                  List<NetImageInfo> images,
                                                                  String linkTitle,
                                                                  String linkUrl,
                                                                  String linkImage) {
        AddCircleInfoRequest requestBody = new AddCircleInfoRequest();
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
        return RetrofitUtils.createApi(ICircleService.class)
                .addCircleInfo(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<AddCircleInfoResponse>>())
                .subscribeOn(Schedulers.io());
    }

    public static Observable<SetLikeInfoResponse> setLikeInfo(int actionType, long CircleId,
                                                              String lastUpdateTime) {
        SetLikeInfoRequest requestBody = new SetLikeInfoRequest();
        requestBody.setLikeUserId(UserSP.getUserId());
        requestBody.setActionType(actionType);
        requestBody.setCircleId(CircleId);
        requestBody.setCircleUpdateTime(lastUpdateTime);
        return RetrofitUtils.createApi(ICircleService.class)
                .setCircleLikeInfo(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<SetLikeInfoResponse>>())
                .subscribeOn(Schedulers.io());
    }

    public static void addComment(long nearCircleId, String content, String lastUpdateTime) {
        addComment(nearCircleId, content, 0, lastUpdateTime);
    }

    public static Observable<AddCircleCommentInfoResponse> addComment(long CircleId,
                                                                      String content,
                                                                      long replyUserId,
                                                                      String lastUpdateTime) {
        AddCircleCommentInfoRequest requestBody = new AddCircleCommentInfoRequest();
        requestBody.setCommentUserId(UserSP.getUserId());
        requestBody.setCircleId(CircleId);
        requestBody.setContent(content);
        requestBody.setReplyUserId(replyUserId);
        requestBody.setCircleUpdateTime(lastUpdateTime);
        return RetrofitUtils.createApi(ICircleService.class)
                .addCircleCommentInfo(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<AddCircleCommentInfoResponse>>())
                .subscribeOn(Schedulers.io());
    }

    public static Observable<GetCircleCommentsResponse> getCircleComments(long circleId,
                                                                          long lastCommentId,
                                                                          int count) {
        GetCircleCommentsRequest requestBody = new GetCircleCommentsRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setCircleId(circleId);
        requestBody.setCount(count);
        requestBody.setLastCommentId(lastCommentId);
        return RetrofitUtils.createApi(ICircleService.class)
                .getCircleComments(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<GetCircleCommentsResponse>>())
                .subscribeOn(Schedulers.io());
    }

    public static Observable<DeleteCommentInfoResponse> deleteCommentInfo(long commentId,
                                                                          String lastUpdateTime) {
        DeleteCommentInfoRequest requestBody = new DeleteCommentInfoRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setCommentId(commentId);
        requestBody.setCircleUpdateTime(lastUpdateTime);
        return RetrofitUtils.createApi(ICircleService.class)
                .deleteCommentInfo(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<DeleteCommentInfoResponse>>())
                .subscribeOn(Schedulers.io());
    }

    public static Observable<GetUserCircleInfosResponse> getUserCircleInfos(long viewUserId,
                                                                            long minCircleId,
                                                                            int pageCount,
                                                                            List<NetCircleMatchInfo> circleInfos) {
        GetUserCircleInfosRequest requestBody = new GetUserCircleInfosRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setViewUserId(viewUserId);
        requestBody.setMinCircleId(minCircleId);
        requestBody.setCircleMatchInfos(circleInfos);
        requestBody.setCount(pageCount <= 0 ? 15 : pageCount);

        return RetrofitUtils.createApi(ICircleService.class)
                .getUserCircleInfos(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<GetUserCircleInfosResponse>>())
                .subscribeOn(Schedulers.io());
    }

    public static Observable<GetCircleDetailResponse> getCircleDetail(long circleId) {
        GetCircleDetailRequest requestBody = new GetCircleDetailRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setCircleId(circleId);
        return RetrofitUtils.createApi(ICircleService.class)
                .getCircleDetail(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<GetCircleDetailResponse>>())
                .subscribeOn(Schedulers.io());
    }

    public static Observable<DeleteCircleInfoResponse> deleteCircleInfo(long circleId) {
        DeleteCircleInfoRequest requestBody = new DeleteCircleInfoRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setCircleId(circleId);
        return RetrofitUtils.createApi(ICircleService.class)
                .deleteCircleInfo(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<DeleteCircleInfoResponse>>())
                .subscribeOn(Schedulers.io());
    }

    public interface ICircleService {
        @POST("api/GetCircleInfos")
        Observable<ResponseBase<GetCircleInfosResponse>> getCircleInfos(@Body RequestBase
                <GetCircleInfosRequest> request);

        @POST("api/AddCircleInfo")
        Observable<ResponseBase<AddCircleInfoResponse>> addCircleInfo(@Body RequestBase<AddCircleInfoRequest> request);

        @POST("api/SetCircleLikeInfo")
        Observable<ResponseBase<SetLikeInfoResponse>> setCircleLikeInfo(@Body RequestBase<SetLikeInfoRequest> request);

        @POST("api/GetCircleComments")
        Observable<ResponseBase<GetCircleCommentsResponse>> getCircleComments(@Body RequestBase<GetCircleCommentsRequest> request);

        @POST("api/AddCircleCommentInfo")
        Observable<ResponseBase<AddCircleCommentInfoResponse>> addCircleCommentInfo(@Body RequestBase<AddCircleCommentInfoRequest> request);

        @POST("api/GetUserCircleInfos")
        Observable<ResponseBase<GetUserCircleInfosResponse>> getUserCircleInfos(@Body RequestBase<GetUserCircleInfosRequest> request);

        @POST("api/GetCircleDetail")
        Observable<ResponseBase<GetCircleDetailResponse>> getCircleDetail(@Body RequestBase<GetCircleDetailRequest> request);

        @POST("api/DeleteCircleInfo")
        Observable<ResponseBase<DeleteCircleInfoResponse>> deleteCircleInfo(@Body RequestBase<DeleteCircleInfoRequest> request);

        @POST("api/DeleteCircleCommentInfo ")
        Observable<ResponseBase<DeleteCommentInfoResponse>> deleteCommentInfo(@Body RequestBase<DeleteCommentInfoRequest> request);
    }
}
