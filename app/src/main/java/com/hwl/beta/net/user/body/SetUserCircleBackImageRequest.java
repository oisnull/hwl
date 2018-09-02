package com.hwl.beta.net.user.body;

/**
 * Created by Administrator on 2018/1/26.
 */
public class SetUserCircleBackImageRequest {
    private long UserId;
    private String CircleBackImageUrl;

    public String getCircleBackImageUrl() {
        return CircleBackImageUrl;
    }

    public void setCircleBackImageUrl(String circleBackImageUrl) {
        CircleBackImageUrl = circleBackImageUrl;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }
}