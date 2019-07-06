package com.hwl.beta.net.circle.body;

public class GetCircleCommentsRequest {
    private long UserId;
    private long CircleId;
    private int Count;
    private long LastCommentId;

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public long getLastCommentId() {
        return LastCommentId;
    }

    public void setLastCommentId(long lastCommentId) {
        LastCommentId = lastCommentId;
    }

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
