package com.hwl.beta.net.near.body;

/**
 * Created by Administrator on 2018/3/8.
 */

public class GetNearCircleDetailRequest {
    private long UserId;
    private long NearCircleId;
	private String UpdateTime;

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

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