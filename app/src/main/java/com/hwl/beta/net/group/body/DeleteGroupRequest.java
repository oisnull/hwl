package com.hwl.beta.net.group.body;

public class DeleteGroupRequest {
    private String GroupGuid;
    private long BuildUserId;

    public String getGroupGuid() {
        return GroupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        GroupGuid = groupGuid;
    }

    public long getBuildUserId() {
        return BuildUserId;
    }

    public void setBuildUserId(long buildUserId) {
        BuildUserId = buildUserId;
    }
}
