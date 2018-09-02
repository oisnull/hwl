package com.hwl.beta.db.entity;

import com.hwl.beta.utils.DateUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.OrderBy;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/14.
 */

@Entity
public class NearCircleComment implements Serializable {
    private static final long serialVersionUID = 11L;
    private int commentId;
    private long nearCircleId;
    private long commentUserId;
    private String commentUserName;
    private String commentUserImage;
    private long replyUserId;
    private String replyUserName;
    private String replyUserImage;
    private String content;
    @OrderBy("commentTime desc")
    private Date commentTime;

    public String getShowTime() {
        if (this.commentTime != null)
            return DateUtils.getChatShowTime(this.commentTime);
        return null;
    }

    @Generated(hash = 1892715940)
    public NearCircleComment(int commentId, long nearCircleId, long commentUserId,
            String commentUserName, String commentUserImage, long replyUserId,
            String replyUserName, String replyUserImage, String content, Date commentTime) {
        this.commentId = commentId;
        this.nearCircleId = nearCircleId;
        this.commentUserId = commentUserId;
        this.commentUserName = commentUserName;
        this.commentUserImage = commentUserImage;
        this.replyUserId = replyUserId;
        this.replyUserName = replyUserName;
        this.replyUserImage = replyUserImage;
        this.content = content;
        this.commentTime = commentTime;
    }

    @Generated(hash = 2010827811)
    public NearCircleComment() {
    }

    public int getCommentId() {
        return this.commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public long getNearCircleId() {
        return this.nearCircleId;
    }

    public void setNearCircleId(long nearCircleId) {
        this.nearCircleId = nearCircleId;
    }

    public long getCommentUserId() {
        return this.commentUserId;
    }

    public void setCommentUserId(long commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getCommentUserName() {
        return this.commentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    public String getCommentUserImage() {
        return this.commentUserImage;
    }

    public void setCommentUserImage(String commentUserImage) {
        this.commentUserImage = commentUserImage;
    }

    public long getReplyUserId() {
        return this.replyUserId;
    }

    public void setReplyUserId(long replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getReplyUserName() {
        return this.replyUserName;
    }

    public void setReplyUserName(String replyUserName) {
        this.replyUserName = replyUserName;
    }

    public String getReplyUserImage() {
        return this.replyUserImage;
    }

    public void setReplyUserImage(String replyUserImage) {
        this.replyUserImage = replyUserImage;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCommentTime() {
        return this.commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NearCircleComment) {
            NearCircleComment comment = (NearCircleComment) obj;
            return this.getNearCircleId() == comment.getNearCircleId()
                    && this.getCommentId() == comment.getCommentId();
        }
        return super.equals(obj);
    }
}
