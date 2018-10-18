package com.hwl.beta.db.entity;

import com.hwl.beta.utils.DateUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Circle implements Serializable {
    private static final long serialVersionUID = 5L;

    @Id
    private long circleId;
    private long publishUserId;
    private String publishUserName;
    private String publishUserImage;
    private int contentType;
    private String content;
    private String linkTitle;
    private String linkUrl;
    private String linkImage;
    @OrderBy("publishTime desc")
    private Date publishTime;
    private String updateTime;
    private String fromPosDesc;
    private int commentCount;
    private int likeCount;
    private boolean isLiked;

    public String getShowTime() {
        if (this.publishTime != null)
            return DateUtils.getChatShowTime(this.publishTime);
        return null;
    }

    public String getShowDate() {
        if (this.publishTime != null)
            return DateUtils.dateToStrTime(this.publishTime);
        return null;
    }

    @Generated(hash = 1559919035)
    public Circle(long circleId, long publishUserId, String publishUserName,
                  String publishUserImage, int contentType, String content,
                  String linkTitle, String linkUrl, String linkImage, Date publishTime,
                  String updateTime, String fromPosDesc, int commentCount, int likeCount,
                  boolean isLiked) {
        this.circleId = circleId;
        this.publishUserId = publishUserId;
        this.publishUserName = publishUserName;
        this.publishUserImage = publishUserImage;
        this.contentType = contentType;
        this.content = content;
        this.linkTitle = linkTitle;
        this.linkUrl = linkUrl;
        this.linkImage = linkImage;
        this.publishTime = publishTime;
        this.updateTime = updateTime;
        this.fromPosDesc = fromPosDesc;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }

    @Generated(hash = 2062793395)
    public Circle() {
    }

    public long getCircleId() {
        return this.circleId;
    }

    public void setCircleId(long circleId) {
        this.circleId = circleId;
    }

    public long getPublishUserId() {
        return this.publishUserId;
    }

    public void setPublishUserId(long publishUserId) {
        this.publishUserId = publishUserId;
    }

    public String getPublishUserName() {
        return this.publishUserName;
    }

    public void setPublishUserName(String publishUserName) {
        this.publishUserName = publishUserName;
    }

    public String getPublishUserImage() {
        return this.publishUserImage;
    }

    public void setPublishUserImage(String publishUserImage) {
        this.publishUserImage = publishUserImage;
    }

    public int getContentType() {
        return this.contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLinkTitle() {
        return this.linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getLinkUrl() {
        return this.linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkImage() {
        return this.linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public Date getPublishTime() {
        return this.publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFromPosDesc() {
        return this.fromPosDesc;
    }

    public void setFromPosDesc(String fromPosDesc) {
        this.fromPosDesc = fromPosDesc;
    }

    public int getCommentCount() {
        return this.commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikeCount() {
        return this.likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean getIsLiked() {
        return this.isLiked;
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }
}
