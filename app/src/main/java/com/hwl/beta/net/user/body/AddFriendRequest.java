package com.hwl.beta.net.user.body;

/**
 * Created by Administrator on 2018/1/27.
 */

public class AddFriendRequest {
    private long MyUserId;
    private long FriendUserId;
    private String MyRemark;

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

    public String getMyRemark() {
        return MyRemark;
    }

    public void setMyRemark(String myRemark) {
        MyRemark = myRemark;
    }
}
