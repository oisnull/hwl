package com.hwl.beta.net.user.body;

/**
 * Created by Administrator on 2018/1/26.
 */

public class SetUserSexRequest {
    private long UserId;
    private int UserSex;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public int getUserSex() {
        return UserSex;
    }

    public void setUserSex(int userSex) {
        UserSex = userSex;
    }
}