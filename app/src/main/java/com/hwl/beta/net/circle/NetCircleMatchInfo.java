package com.hwl.beta.net.circle;

public class NetCircleMatchInfo {
    private long CircleId;
    private String UpdateTime;

    public NetCircleMatchInfo() {
    }

    public NetCircleMatchInfo(long circleId, String updateTime) {
        CircleId = circleId;
        UpdateTime = updateTime;
    }

    public long getCircleId() {
        return CircleId;
    }

    public void setCircleId(long circleId) {
        CircleId = circleId;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }
}
