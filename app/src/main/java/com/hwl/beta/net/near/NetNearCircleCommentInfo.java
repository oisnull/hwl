package com.hwl.beta.net.near;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2018/3/17.
 */

public class NetNearCircleCommentInfo implements Serializable {
    private int CommentId;
    private int NearCircleId ;

    private int CommentUserId;
    private String CommentUserName;
    private String CommentUserImage;

    private int ReplyUserId;
    private String ReplyUserName ;
    private String ReplyUserImage ;

    private String Content ;
    private Date CommentTime ;

    public int getCommentId() {
        return CommentId;
    }

    public void setCommentId(int commentId) {
        CommentId = commentId;
    }

    public int getNearCircleId() {
        return NearCircleId;
    }

    public void setNearCircleId(int nearCircleId) {
        NearCircleId = nearCircleId;
    }

    public int getCommentUserId() {
        return CommentUserId;
    }

    public void setCommentUserId(int commentUserId) {
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

    public int getReplyUserId() {
        return ReplyUserId;
    }

    public void setReplyUserId(int replyUserId) {
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

    public Date getCommentTime() {
        return CommentTime;
    }

    public void setCommentTime(Date commentTime) {
        CommentTime = commentTime;
    }
}
