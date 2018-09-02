package com.hwl.beta.net.user.body;

/**
 * Created by Administrator on 2018/1/26.
 */
public class SetUserHeadImageRequest {
    private long UserId;
    private String HeadImageUrl;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public String getHeadImageUrl() {
        return HeadImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        HeadImageUrl = headImageUrl;
    }
}