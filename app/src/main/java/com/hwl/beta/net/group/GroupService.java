package com.hwl.beta.net.group;

import com.hwl.beta.net.RequestBase;
import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.RetrofitUtils;
import com.hwl.beta.net.group.body.AddGroupRequest;
import com.hwl.beta.net.group.body.AddGroupResponse;
import com.hwl.beta.net.group.body.AddGroupUsersRequest;
import com.hwl.beta.net.group.body.AddGroupUsersResponse;
import com.hwl.beta.net.group.body.DeleteGroupRequest;
import com.hwl.beta.net.group.body.DeleteGroupResponse;
import com.hwl.beta.net.group.body.DeleteGroupUserRequest;
import com.hwl.beta.net.group.body.DeleteGroupUserResponse;
import com.hwl.beta.net.group.body.GetGroupsRequest;
import com.hwl.beta.net.group.body.GetGroupsResponse;
import com.hwl.beta.net.group.body.GroupUsersRequest;
import com.hwl.beta.net.group.body.GroupUsersResponse;
import com.hwl.beta.net.group.body.SetGroupNameRequest;
import com.hwl.beta.net.group.body.SetGroupNameResponse;
import com.hwl.beta.net.group.body.SetGroupNoteRequest;
import com.hwl.beta.net.group.body.SetGroupNoteResponse;
import com.hwl.beta.sp.UserSP;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class GroupService {

    public static Observable<ResponseBase<GetGroupsResponse>> getGroups() {
        GetGroupsRequest requestBody = new GetGroupsRequest();
        requestBody.setUserId(UserSP.getUserId());
        Observable<ResponseBase<GetGroupsResponse>> response = RetrofitUtils.createApi(IGroupService.class)
                .getGroups(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public static Observable<ResponseBase<GroupUsersResponse>> groupUsers(String groupGuid) {
        GroupUsersRequest requestBody = new GroupUsersRequest();
        requestBody.setGroupGuid(groupGuid);
        Observable<ResponseBase<GroupUsersResponse>> response = RetrofitUtils.createApi(IGroupService.class)
                .groupUsers(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io());
//                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public static Observable<ResponseBase<AddGroupResponse>> addGroup(String groupName, List<Long> groupUserIds) {
        AddGroupRequest requestBody = new AddGroupRequest();
        requestBody.setBuildUserId(UserSP.getUserId());
        requestBody.setGroupName(groupName);
        requestBody.setGroupUserIds(groupUserIds);
        Observable<ResponseBase<AddGroupResponse>> response = RetrofitUtils.createApi(IGroupService.class)
                .addGroup(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public static Observable<ResponseBase<AddGroupUsersResponse>> addGroupUsers(String groupGuid, List<Long> groupUserIds) {
        AddGroupUsersRequest requestBody = new AddGroupUsersRequest();
        requestBody.setGroupGuid(groupGuid);
        requestBody.setGroupUserIds(groupUserIds);
        Observable<ResponseBase<AddGroupUsersResponse>> response = RetrofitUtils.createApi(IGroupService.class)
                .addGroupUsers(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public static Observable<ResponseBase<DeleteGroupResponse>> deleteGroup(String groupGuid) {
        DeleteGroupRequest requestBody = new DeleteGroupRequest();
        requestBody.setBuildUserId(UserSP.getUserId());
        requestBody.setGroupGuid(groupGuid);
        Observable<ResponseBase<DeleteGroupResponse>> response = RetrofitUtils.createApi(IGroupService.class)
                .deleteGroup(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public static Observable<ResponseBase<SetGroupNameResponse>> setGroupName(String groupGuid, String groupName) {
        SetGroupNameRequest requestBody = new SetGroupNameRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setGroupGuid(groupGuid);
        requestBody.setGroupName(groupName);
        Observable<ResponseBase<SetGroupNameResponse>> response = RetrofitUtils.createApi(IGroupService.class)
                .setGroupName(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public static Observable<ResponseBase<SetGroupNoteResponse>> setGroupNote(String groupGuid, String groupNote) {
        SetGroupNoteRequest requestBody = new SetGroupNoteRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setGroupGuid(groupGuid);
        requestBody.setGroupNote(groupNote);
        Observable<ResponseBase<SetGroupNoteResponse>> response = RetrofitUtils.createApi(IGroupService.class)
                .setGroupNote(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public static Observable<ResponseBase<DeleteGroupUserResponse>> deleteGroupUser(String groupGuid) {
        DeleteGroupUserRequest requestBody = new DeleteGroupUserRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setGroupGuid(groupGuid);
        Observable<ResponseBase<DeleteGroupUserResponse>> response = RetrofitUtils.createApi(IGroupService.class)
                .deleteGroupUser(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    private interface IGroupService {
        @POST("api/GetGroups")
        Observable<ResponseBase<GetGroupsResponse>> getGroups(@Body RequestBase<GetGroupsRequest>
                                                                      request);

        @POST("api/GroupUsers")
        Observable<ResponseBase<GroupUsersResponse>> groupUsers(@Body RequestBase<GroupUsersRequest> request);

        @POST("api/AddGroup")
        Observable<ResponseBase<AddGroupResponse>> addGroup(@Body RequestBase<AddGroupRequest> request);

        @POST("api/AddGroupUsers")
        Observable<ResponseBase<AddGroupUsersResponse>> addGroupUsers(@Body RequestBase<AddGroupUsersRequest> request);

        @POST("api/DeleteGroup")
        Observable<ResponseBase<DeleteGroupResponse>> deleteGroup(@Body RequestBase<DeleteGroupRequest> request);

        @POST("api/DeleteGroupUser")
        Observable<ResponseBase<DeleteGroupUserResponse>> deleteGroupUser(@Body RequestBase<DeleteGroupUserRequest> request);

        @POST("api/SetGroupName")
        Observable<ResponseBase<SetGroupNameResponse>> setGroupName(@Body RequestBase<SetGroupNameRequest> request);

        @POST("api/SetGroupNote")
        Observable<ResponseBase<SetGroupNoteResponse>> setGroupNote(@Body RequestBase<SetGroupNoteRequest> request);
    }
}
