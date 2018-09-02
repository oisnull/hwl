package com.hwl.beta.net.near;

public class NetNearCircleMatchInfo {
    private long NearCircleId;
    private String UpdateTime;

    public NetNearCircleMatchInfo(long nearCircleId, String updateTime) {
        NearCircleId = nearCircleId;
        UpdateTime = updateTime;
    }

    public long getNearCircleId() {
        return NearCircleId;
    }

    public void setNearCircleId(long nearCircleId) {
        NearCircleId = nearCircleId;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }
}
