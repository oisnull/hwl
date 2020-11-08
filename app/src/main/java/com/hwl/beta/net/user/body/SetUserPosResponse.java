package com.hwl.beta.net.user.body;


import com.hwl.beta.net.user.NetNearUserInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/1/20.
 */

public class SetUserPosResponse {

    private int Status;
    private String ErrorMessage;
    private int UserPosId;
    private String UserGroupGuid;
    private List<NetNearUserInfo> GroupUserInfos;

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getUserPosId() {
        return UserPosId;
    }

    public void setUserPosId(int userPosId) {
        UserPosId = userPosId;
    }

    public String getUserGroupGuid() {
        return UserGroupGuid;
    }

    public void setUserGroupGuid(String userGroupGuid) {
        UserGroupGuid = userGroupGuid;
    }

    public List<NetNearUserInfo> getGroupUserInfos() {
        return GroupUserInfos;
    }

    public void setGroupUserInfos(List<NetNearUserInfo> groupUserInfos) {
        GroupUserInfos = groupUserInfos;
    }
}