package com.hwl.beta.net.user.body;

public class GetUserDetailsRequest {
    /// <summary>
    /// 当前登录的用户id
    /// </summary>
    private long UserId;

    /// <summary>
    /// 获取详情的用户id
    /// </summary>
    private long GetUserId;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public long getGetUserId() {
        return GetUserId;
    }

    public void setGetUserId(long getUserId) {
        GetUserId = getUserId;
    }
}
