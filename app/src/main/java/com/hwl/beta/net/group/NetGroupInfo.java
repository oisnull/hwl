package com.hwl.beta.net.group;

import com.hwl.beta.net.user.NetGroupUserInfo;

import java.util.Date;
import java.util.List;

public class NetGroupInfo {
    private String GroupGuid;
    private String GroupName;
    private String GroupNote;
    private long BuildUserId;
    private int GroupUserCount;
    private Date BuildDate;
    private String UpdateDate;
    private List<NetGroupUserInfo> GroupUsers;

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

    public String getGroupNote() {
        return GroupNote;
    }

    public void setGroupNote(String groupNote) {
        GroupNote = groupNote;
    }

    public long getBuildUserId() {
        return BuildUserId;
    }

    public void setBuildUserId(long buildUserId) {
        BuildUserId = buildUserId;
    }

    public int getGroupUserCount() {
        return GroupUserCount;
    }

    public void setGroupUserCount(int groupUserCount) {
        GroupUserCount = groupUserCount;
    }

    public Date getBuildDate() {
        return BuildDate;
    }

    public void setBuildDate(Date buildDate) {
        BuildDate = buildDate;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public List<NetGroupUserInfo> getGroupUsers() {
        return GroupUsers;
    }

    public void setGroupUsers(List<NetGroupUserInfo> groupUsers) {
        GroupUsers = groupUsers;
    }
}
