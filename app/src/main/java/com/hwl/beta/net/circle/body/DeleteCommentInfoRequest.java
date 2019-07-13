package com.hwl.beta.net.circle.body;

public class DeleteCommentInfoRequest {
    private long UserId;
    private long CommentId;
	private String CircleUpdateTime;

    public String getCircleUpdateTime() {
        return CircleUpdateTime;
    }

    public void setCircleUpdateTime(String circleUpdateTime) {
        CircleUpdateTime = circleUpdateTime;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public long getCommentId() {
        return CommentId;
    }

    public void setCommentId(long commentId) {
        CommentId = commentId;
    }
}
