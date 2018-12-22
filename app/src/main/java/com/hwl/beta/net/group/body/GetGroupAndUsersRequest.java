package com.hwl.beta.net.group.body;

public class GetGroupAndUsersRequest {
    private long UserId;
    private String groupGuid;

    public String getGroupGuid() {
        return groupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        this.groupGuid = groupGuid;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }
}
