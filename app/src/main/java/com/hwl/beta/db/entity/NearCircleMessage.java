package com.hwl.beta.db.entity;

import com.hwl.beta.utils.DateUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

@Entity
public class NearCircleMessage {
    @Id(autoincrement = true)
    private Long id;
    private int type;
    private long nearCircleId;
    private long userId;
    private String userName;
    private String comment;
    private long commentId;
    private long replyUserId;
    private String replyUserName;
    private String content;
    private String userImage;
    private Date actionTime;
    private int status;

    @Generated(hash = 1478070669)
    public NearCircleMessage(Long id, int type, long nearCircleId, long userId,
            String userName, String comment, long commentId, long replyUserId,
            String replyUserName, String content, String userImage, Date actionTime,
            int status) {
        this.id = id;
        this.type = type;
        this.nearCircleId = nearCircleId;
        this.userId = userId;
        this.userName = userName;
        this.comment = comment;
        this.commentId = commentId;
        this.replyUserId = replyUserId;
        this.replyUserName = replyUserName;
        this.content = content;
        this.userImage = userImage;
        this.actionTime = actionTime;
        this.status = status;
    }

    @Generated(hash = 871113473)
    public NearCircleMessage() {
    }

    public String getShowTime() {
        if (this.actionTime != null)
            return DateUtils.getChatShowTime(this.actionTime);
        return null;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getNearCircleId() {
        return this.nearCircleId;
    }

    public void setNearCircleId(long nearCircleId) {
        this.nearCircleId = nearCircleId;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getCommentId() {
        return this.commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
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

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserImage() {
        return this.userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Date getActionTime() {
        return this.actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
