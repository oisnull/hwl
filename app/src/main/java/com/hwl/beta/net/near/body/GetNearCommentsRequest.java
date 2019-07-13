package com.hwl.beta.net.near.body;

public class GetNearCommentsRequest {
    private long UserId;
    private long NearCircleId;
    private int Count;
    private long LastCommentId;

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
}
