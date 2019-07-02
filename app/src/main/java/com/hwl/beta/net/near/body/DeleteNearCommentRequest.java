package com.hwl.beta.net.near.body;

public class DeleteNearCommentRequest {
    private long UserId;
    private int CommentId;
    private String NearCircleUpdateTime;

    public String getNearCircleUpdateTime() {
        return NearCircleUpdateTime;
    }

    public void setNearCircleUpdateTime(String nearCircleUpdateTime) {
        NearCircleUpdateTime = nearCircleUpdateTime;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public int getCommentId() {
        return CommentId;
    }

    public void setCommentId(int commentId) {
        CommentId = commentId;
    }
}
