package com.hwl.beta.net.near.body;

public class AddNearCommentRequest {

    /// <summary>
    /// 附近信息id
    /// </summary>
    private long NearCircleId;
    /// <summary>
    /// 发评论的用户id
    /// </summary>
    private long CommentUserId;
    /// <summary>
    /// 回复用户id
    /// </summary>
    private long ReplyUserId;
    private String Content;
    private String NearCircleUpdateTime;

    public String getNearCircleUpdateTime() {
        return NearCircleUpdateTime;
    }

    public void setNearCircleUpdateTime(String nearCircleUpdateTime) {
        NearCircleUpdateTime = nearCircleUpdateTime;
    }

    public long getNearCircleId() {
        return NearCircleId;
    }

    public void setNearCircleId(long nearCircleId) {
        NearCircleId = nearCircleId;
    }

    public long getCommentUserId() {
        return CommentUserId;
    }

    public void setCommentUserId(long commentUserId) {
        CommentUserId = commentUserId;
    }

    public long getReplyUserId() {
        return ReplyUserId;
    }

    public void setReplyUserId(long replyUserId) {
        ReplyUserId = replyUserId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
