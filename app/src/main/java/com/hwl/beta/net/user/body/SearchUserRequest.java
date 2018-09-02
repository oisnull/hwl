package com.hwl.beta.net.user.body;

/**
 * Created by Administrator on 2018/1/27.
 */

public class SearchUserRequest {
    private long UserId;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public String getUserKey() {
        return UserKey;
    }

    public void setUserKey(String userKey) {
        UserKey = userKey;
    }

    private String UserKey;
}