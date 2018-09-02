package com.hwl.beta.net.group.body;

public class SetGroupNameRequest {
    private long UserId;
    private String GroupGuid;
    private String GroupName;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public String getGroupGuid() {
        return GroupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        GroupGuid = groupGuid;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }
}
