package com.hwl.beta.net.user.body;

/**
 * Created by Administrator on 2018/2/15.
 */

public class GetFriendsRequest {
    private long UserId;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }
}
