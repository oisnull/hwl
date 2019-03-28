package com.hwl.beta.net.user;

import com.hwl.beta.net.NetDefaultFunction;
import com.hwl.beta.net.RequestBase;
import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.RetrofitUtils;
import com.hwl.beta.net.user.body.AddFriendRequest;
import com.hwl.beta.net.user.body.AddFriendResponse;
import com.hwl.beta.net.user.body.DeleteFriendRequest;
import com.hwl.beta.net.user.body.DeleteFriendResponse;
import com.hwl.beta.net.user.body.GetFriendsRequest;
import com.hwl.beta.net.user.body.GetFriendsResponse;
import com.hwl.beta.net.user.body.GetUserDetailsRequest;
import com.hwl.beta.net.user.body.GetUserDetailsResponse;
import com.hwl.beta.net.user.body.GetUserRelationInfoRequest;
import com.hwl.beta.net.user.body.GetUserRelationInfoResponse;
import com.hwl.beta.net.user.body.ResetUserPasswordRequest;
import com.hwl.beta.net.user.body.ResetUserPasswordResponse;
import com.hwl.beta.net.user.body.SearchUserRequest;
import com.hwl.beta.net.user.body.SearchUserResponse;
import com.hwl.beta.net.user.body.SetFriendRemarkRequest;
import com.hwl.beta.net.user.body.SetFriendRemarkResponse;
import com.hwl.beta.net.user.body.SetUserCircleBackImageRequest;
import com.hwl.beta.net.user.body.SetUserCircleBackImageResponse;
import com.hwl.beta.net.user.body.SetUserHeadImageRequest;
import com.hwl.beta.net.user.body.SetUserInfoResponse;
import com.hwl.beta.net.user.body.SetUserLifeNotesRequest;
import com.hwl.beta.net.user.body.SetUserNameRequest;
import com.hwl.beta.net.user.body.SetUserPasswordRequest;
import com.hwl.beta.net.user.body.SetUserPasswordResponse;
import com.hwl.beta.net.user.body.SetUserPosRequest;
import com.hwl.beta.net.user.body.SetUserPosResponse;
import com.hwl.beta.net.user.body.SetUserSexRequest;
import com.hwl.beta.net.user.body.SetUserSymbolRequest;
import com.hwl.beta.net.user.body.UserLoginRequest;
import com.hwl.beta.net.user.body.UserLoginResponse;
import com.hwl.beta.net.user.body.UserRegisterRequest;
import com.hwl.beta.net.user.body.UserRegisterResponse;
import com.hwl.beta.sp.UserSP;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2018/3/28.
 */

public class UserService {

    public static Observable<UserLoginResponse> userLogin(String email, String mobile, String password) {
        UserLoginRequest requestBody = new UserLoginRequest();
        requestBody.setEmail(email);
        requestBody.setMobile(mobile);
        requestBody.setPassword(password);
        return RetrofitUtils.createApi(IUserService.class)
                .userLogin(new RequestBase(requestBody))
                .map(new NetDefaultFunction<ResponseBase<UserLoginResponse>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<UserRegisterResponse> userRegister(String email,
                                                                String mobile,
                                                                String password,
                                                                String passwordOK,
                                                                String code) {
        UserRegisterRequest requestBody = new UserRegisterRequest();
        requestBody.setEmail(email);
        requestBody.setMobile(mobile);
        requestBody.setPassword(password);
        requestBody.setPasswordOK(passwordOK);
        requestBody.setCheckCode(code);
        return RetrofitUtils.createApi(IUserService.class)
                .userRegister(new RequestBase(requestBody))
                .map(new NetDefaultFunction<ResponseBase<UserRegisterResponse>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<SetUserPasswordResponse> setUserPassword(String email,
																		String mobile,
																		String password,
																		String passwordOK,
																		String code) {
        SetUserPasswordRequest requestBody = new SetUserPasswordRequest();
        requestBody.setEmail(email);
        requestBody.setMobile(mobile);
        requestBody.setPassword(password);
        requestBody.setPasswordOK(passwordOK);
        requestBody.setCheckCode(code);
        return RetrofitUtils.createApi
                (IUserService.class)
                .setUserPassword(new RequestBase(requestBody))
                .map(new NetDefaultFunction<ResponseBase<SetUserPasswordResponse>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<ResetUserPasswordResponse> resetUserPassword(String oldPassword, String password, String passwordOK) {
        ResetUserPasswordRequest requestBody = new ResetUserPasswordRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setOldPassword(oldPassword);
        requestBody.setPassword(password);
        requestBody.setPasswordOK(passwordOK);
        return RetrofitUtils.createApi
                (IUserService.class)
                .resetUserPassword(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<ResetUserPasswordResponse>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<SetUserPosResponse> setUserPos(SetUserPosRequest
                                                                                  requestBody) {
        return RetrofitUtils.createApi
                (IUserService.class)
                .setUserPos(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<SetUserPosResponse>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<SearchUserResponse> searchUser(String userKey) {
        SearchUserRequest requestBody = new SearchUserRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setUserKey(userKey);
        return RetrofitUtils.createApi
                (IUserService.class)
                .searchUser(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<SearchUserResponse>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<AddFriendResponse> addFriend(long addUserId, String
            remark) {
        AddFriendRequest requestBody = new AddFriendRequest();
        requestBody.setMyUserId(UserSP.getUserId());
        requestBody.setFriendUserId(addUserId);
        requestBody.setMyRemark(remark);
        return RetrofitUtils.createApi
                (IUserService.class)
                .addFriend(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<AddFriendResponse>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<SetUserInfoResponse> setUserSymbol(String symbol) {
        SetUserSymbolRequest requestBody = new SetUserSymbolRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setSymbol(symbol);
        return RetrofitUtils.createApi
                (IUserService.class)
                .setUserSymbol(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<SetUserInfoResponse>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<SetUserInfoResponse> setUserName(String userName) {
        SetUserNameRequest requestBody = new SetUserNameRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setUserName(userName);
        return RetrofitUtils.createApi
                (IUserService.class)
                .setUserName(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<SetUserInfoResponse>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<SetUserInfoResponse> setUserLifeNotes(String lifeNotes) {
        SetUserLifeNotesRequest requestBody = new SetUserLifeNotesRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setLifeNotes(lifeNotes);
        return RetrofitUtils.createApi
                (IUserService.class)
                .setUserLifeNotes(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<SetUserInfoResponse>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<SetUserInfoResponse> setUserSex(int sex) {
        SetUserSexRequest requestBody = new SetUserSexRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setUserSex(sex);
        return RetrofitUtils.createApi
                (IUserService.class)
                .setUserSex(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<SetUserInfoResponse>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<SetUserInfoResponse> setUserHeadImage(String headImageUrl) {
        SetUserHeadImageRequest requestBody = new SetUserHeadImageRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setHeadImageUrl(headImageUrl);
        return RetrofitUtils.createApi
                (IUserService.class)
                .setUserHeadImage(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<SetUserInfoResponse>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<SetUserCircleBackImageResponse> setUserCircleBackImage
            (String circleBackImageUrl) {
        SetUserCircleBackImageRequest requestBody = new SetUserCircleBackImageRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setCircleBackImageUrl(circleBackImageUrl);
        return RetrofitUtils
                .createApi(IUserService.class)
                .setUserCircleBackImage(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<SetUserCircleBackImageResponse>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<SetFriendRemarkResponse> setFriendRemark(long friendId
            , String remark) {
        SetFriendRemarkRequest requestBody = new SetFriendRemarkRequest();
        requestBody.setMyUserId(UserSP.getUserId());
        requestBody.setFriendUserId(friendId);
        requestBody.setFriendUserRemark(remark);
        return RetrofitUtils.createApi
                (IUserService.class)
                .setFriendRemark(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<SetFriendRemarkResponse>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<GetFriendsResponse> getFriends() {
        GetFriendsRequest requestBody = new GetFriendsRequest();
        requestBody.setUserId(UserSP.getUserId());
        return RetrofitUtils.createApi(IUserService.class)
                .getFriends(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<GetFriendsResponse>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<DeleteFriendResponse> deleteFriend(long friendUserId) {
        DeleteFriendRequest requestBody = new DeleteFriendRequest();
        requestBody.setMyUserId(UserSP.getUserId());
        requestBody.setFriendUserId(friendUserId);
        return RetrofitUtils.createApi
                (IUserService.class)
                .deleteFriend(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<DeleteFriendResponse>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<GetUserDetailsResponse> getUserDetails(long viewUserId) {
        GetUserDetailsRequest requestBody = new GetUserDetailsRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setGetUserId(viewUserId);
        return RetrofitUtils.createApi
                (IUserService.class)
                .getUserDetails(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<GetUserDetailsResponse>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<GetUserRelationInfoResponse> getUserRelationInfo(long relationUserId) {
        GetUserRelationInfoRequest requestBody = new GetUserRelationInfoRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setRelationUserId(relationUserId);
        return RetrofitUtils.createApi
                (IUserService.class)
                .getUserRelationInfo(new RequestBase(UserSP.getUserToken(), requestBody))
                .map(new NetDefaultFunction<ResponseBase<GetUserRelationInfoResponse>>())
                .subscribeOn(Schedulers.io());
    }


    private interface IUserService {
        @POST("api/UserLogin")
        Observable<ResponseBase<UserLoginResponse>> userLogin(@Body RequestBase<UserLoginRequest>
                                                                      request);

        @POST("api/UserRegister")
        Observable<ResponseBase<UserRegisterResponse>> userRegister(@Body RequestBase<UserRegisterRequest> request);

        @POST("api/SetUserPassword")
        Observable<ResponseBase<SetUserPasswordResponse>> setUserPassword(@Body RequestBase<SetUserPasswordRequest> request);

        @POST("api/ResetUserPassword")
        Observable<ResponseBase<ResetUserPasswordResponse>> resetUserPassword(@Body RequestBase<ResetUserPasswordRequest> request);

        @POST("api/SetUserPos")
        Observable<ResponseBase<SetUserPosResponse>> setUserPos(@Body RequestBase<SetUserPosRequest> request);

        @POST("api/SearchUser")
        Observable<ResponseBase<SearchUserResponse>> searchUser(@Body RequestBase<SearchUserRequest> request);

        @POST("api/SetUserHeadImage")
        Observable<ResponseBase<SetUserInfoResponse>> setUserHeadImage(@Body RequestBase<SetUserHeadImageRequest> request);

        @POST("api/SetUserCircleBackImage")
        Observable<ResponseBase<SetUserCircleBackImageResponse>> setUserCircleBackImage(@Body RequestBase<SetUserCircleBackImageRequest> request);

        @POST("api/SetUserName")
        Observable<ResponseBase<SetUserInfoResponse>> setUserName(@Body RequestBase<SetUserNameRequest> request);

        @POST("api/SetUserLifeNotes")
        Observable<ResponseBase<SetUserInfoResponse>> setUserLifeNotes(@Body RequestBase<SetUserLifeNotesRequest> request);

        @POST("api/SetUserSex")
        Observable<ResponseBase<SetUserInfoResponse>> setUserSex(@Body RequestBase<SetUserSexRequest> request);

        @POST("api/SetUserSymbol")
        Observable<ResponseBase<SetUserInfoResponse>> setUserSymbol(@Body RequestBase<SetUserSymbolRequest> request);

        @POST("api/AddFriend")
        Observable<ResponseBase<AddFriendResponse>> addFriend(@Body RequestBase<AddFriendRequest>
                                                                      request);

        @POST("api/GetFriends")
        Observable<ResponseBase<GetFriendsResponse>> getFriends(@Body RequestBase<GetFriendsRequest> request);

        @POST("api/DeleteFriend")
        Observable<ResponseBase<DeleteFriendResponse>> deleteFriend(@Body RequestBase<DeleteFriendRequest> request);

        @POST("api/SetFriendRemark")
        Observable<ResponseBase<SetFriendRemarkResponse>> setFriendRemark(@Body RequestBase<SetFriendRemarkRequest> request);

        @POST("api/GetUserDetails")
        Observable<ResponseBase<GetUserDetailsResponse>> getUserDetails(@Body RequestBase<GetUserDetailsRequest> request);

        @POST("api/GetUserRelationInfo")
        Observable<ResponseBase<GetUserRelationInfoResponse>> getUserRelationInfo(@Body RequestBase<GetUserRelationInfoRequest> request);
    }
}
