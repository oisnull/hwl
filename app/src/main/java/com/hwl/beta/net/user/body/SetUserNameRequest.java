package com.hwl.beta.net.user.body;

/**
 * Created by Administrator on 2018/1/26.
 */
public class SetUserNameRequest {
    private long UserId;
    private String UserName;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}