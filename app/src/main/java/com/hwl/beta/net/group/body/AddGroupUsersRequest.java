package com.hwl.beta.net.group.body;

import java.util.List;

public class AddGroupUsersRequest {
    private String GroupGuid;
    private List<Long> GroupUserIds;

    public String getGroupGuid() {
        return GroupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        GroupGuid = groupGuid;
    }

    public List<Long> getGroupUserIds() {
        return GroupUserIds;
    }

    public void setGroupUserIds(List<Long> groupUserIds) {
        GroupUserIds = groupUserIds;
    }
}
