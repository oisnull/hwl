package com.hwl.beta.net.user.body;

/**
 * Created by Administrator on 2018/2/15.
 */

public class DeleteFriendRequest {
    public long MyUserId;
    public long FriendUserId;

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
}
