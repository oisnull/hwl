package com.hwl.beta.net.circle.body;

public class AddCircleCommentInfoRequest {
    /// <summary>
    /// 评论文章的用户id
    /// </summary>
    private long CommentUserId ;
    /// <summary>
    /// 回复评论的用户id
    /// </summary>
    private long ReplyUserId ;
    private long CircleId ;
    private String Content ;

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

    public long getCircleId() {
        return CircleId;
    }

    public void setCircleId(long circleId) {
        CircleId = circleId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
