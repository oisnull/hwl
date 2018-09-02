package com.hwl.beta.net.user.body;

/**
 * Created by Administrator on 2018/1/28.
 */
public class SetFriendRemarkRequest {

    private long MyUserId;
    private long FriendUserId;
    private String FriendUserRemark;

    public long getMyUserId() {
        return MyUserId;
    }

    public void setMyUserId(long myUserId) {
        MyUserId = myUserId;
    }

    public long getFriendUserId() {
        return FriendUserId;
    }

    public void setFriendUserId(long friendUserId) {
        FriendUserId = friendUserId;
    }

    public String getFriendUserRemark() {
        return FriendUserRemark;
    }

    public void setFriendUserRemark(String friendUserRemark) {
        FriendUserRemark = friendUserRemark;
    }
}