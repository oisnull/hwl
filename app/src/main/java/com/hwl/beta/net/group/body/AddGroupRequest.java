package com.hwl.beta.net.group.body;

import java.util.List;

public class AddGroupRequest {
    private String GroupName;
    private long BuildUserId;
    private List<Long> GroupUserIds;

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public long getBuildUserId() {
        return BuildUserId;
    }

    public void setBuildUserId(long buildUserId) {
        BuildUserId = buildUserId;
    }

    public List<Long> getGroupUserIds() {
        return GroupUserIds;
    }

    public void setGroupUserIds(List<Long> groupUserIds) {
        GroupUserIds = groupUserIds;
    }
}
