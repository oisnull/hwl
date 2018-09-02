package com.hwl.beta.net.near.body;

public class GetNearCommentsRequest {
    private long NearCircleId;
    private int Count;
    private int LastCommentId;

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

    public int getLastCommentId() {
        return LastCommentId;
    }

    public void setLastCommentId(int lastCommentId) {
        LastCommentId = lastCommentId;
    }
}
