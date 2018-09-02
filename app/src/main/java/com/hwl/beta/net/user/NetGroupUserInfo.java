package com.hwl.beta.net.user;

/**
 * Created by Administrator on 2018/2/11.
 */

public class NetGroupUserInfo {
    private String GroupGuid;
    private int UserId;
    private String UserName;
    private String UserHeadImage;

    public String getGroupGuid() {
        return GroupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        GroupGuid = groupGuid;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserHeadImage() {
        return UserHeadImage;
    }

    public void setUserHeadImage(String userHeadImage) {
        UserHeadImage = userHeadImage;
    }
}
