package com.hwl.beta.net.circle;

import java.util.Date;

public class NetCircleCommentInfo {
    private int CommentId;
    private long CircleId;

    private long CommentUserId;
    private String CommentUserName;
    private String CommentUserImage;

    private long ReplyUserId;
    private String ReplyUserName;
    private String ReplyUserImage;

    private String Content;
    private Date CommentTime;

    public int getCommentId() {
        return CommentId;
    }

    public void setCommentId(int commentId) {
        CommentId = commentId;
    }

    public Date getCommentTime() {
        return CommentTime;
    }

    public void setCommentTime(Date commentTime) {
        CommentTime = commentTime;
    }

    public long getCircleId() {
        return CircleId;
    }

    public void setCircleId(long circleId) {
        CircleId = circleId;
    }

    public long getCommentUserId() {
        return CommentUserId;
    }

    public void setCommentUserId(long commentUserId) {
        CommentUserId = commentUserId;
    }

    public String getCommentUserName() {
        return CommentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        CommentUserName = commentUserName;
    }

    public String getCommentUserImage() {
        return CommentUserImage;
    }

    public void setCommentUserImage(String commentUserImage) {
        CommentUserImage = commentUserImage;
    }

    public long getReplyUserId() {
        return ReplyUserId;
    }

    public void setReplyUserId(long replyUserId) {
        ReplyUserId = replyUserId;
    }

    public String getReplyUserName() {
        return ReplyUserName;
    }

    public void setReplyUserName(String replyUserName) {
        ReplyUserName = replyUserName;
    }

    public String getReplyUserImage() {
        return ReplyUserImage;
    }

    public void setReplyUserImage(String replyUserImage) {
        ReplyUserImage = replyUserImage;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
