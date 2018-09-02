package com.hwl.beta.net.circle.body;

public class GetCircleDetailRequest {
    private long UserId ;
    private long CircleId ;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public long getCircleId() {
        return CircleId;
    }

    public void setCircleId(long circleId) {
        CircleId = circleId;
    }
}
