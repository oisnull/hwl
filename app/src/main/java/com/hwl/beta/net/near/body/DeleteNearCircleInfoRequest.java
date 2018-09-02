package com.hwl.beta.net.near.body;

public class DeleteNearCircleInfoRequest {
    private long UserId;
    private long NearCircleId;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public long getNearCircleId() {
        return NearCircleId;
    }

    public void setNearCircleId(long nearCircleId) {
        NearCircleId = nearCircleId;
    }
}
