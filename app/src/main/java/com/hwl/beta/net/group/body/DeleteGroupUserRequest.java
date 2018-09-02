package com.hwl.beta.net.group.body;

public class DeleteGroupUserRequest {
    private String GroupGuid;
    private long UserId;

    public String getGroupGuid() {
        return GroupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        GroupGuid = groupGuid;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }
}
