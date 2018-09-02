package com.hwl.beta.net.near.body;

public class DeleteNearCommentRequest {
    private long UserId;
    private int CommentId;

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
