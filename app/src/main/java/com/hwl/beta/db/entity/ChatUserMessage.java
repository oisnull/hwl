package com.hwl.beta.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2018/2/8.
 */
@Entity
public class ChatUserMessage implements Serializable {
    private static final long serialVersionUID = 4L;
    @Id(autoincrement = true)
    private Long msgId;
    private long fromUserId;
    private String fromUserName;
    private String fromUserHeadImage;
    private long toUserId;
    private int contentType;
    private String content;
    private String localUrl;
    private String previewUrl;
    private String originalUrl;
    private int imageHeight;
    private int imageWidth;
    private long size;
    private long playTime;
    private int sendStatus;
    @OrderBy("sendTime desc")
    private Date sendTime;
    @Generated(hash = 1420243968)
    public ChatUserMessage(Long msgId, long fromUserId, String fromUserName,
            String fromUserHeadImage, long toUserId, int contentType,
            String content, String localUrl, String previewUrl, String originalUrl,
            int imageHeight, int imageWidth, long size, long playTime,
            int sendStatus, Date sendTime) {
        this.msgId = msgId;
        this.fromUserId = fromUserId;
        this.fromUserName = fromUserName;
        this.fromUserHeadImage = fromUserHeadImage;
        this.toUserId = toUserId;
        this.contentType = contentType;
        this.content = content;
        this.localUrl = localUrl;
        this.previewUrl = previewUrl;
        this.originalUrl = originalUrl;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.size = size;
        this.playTime = playTime;
        this.sendStatus = sendStatus;
        this.sendTime = sendTime;
    }
    @Generated(hash = 1461743047)
    public ChatUserMessage() {
    }
    public Long getMsgId() {
        return this.msgId;
    }
    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }
    public long getFromUserId() {
        return this.fromUserId;
    }
    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }
    public String getFromUserName() {
        return this.fromUserName;
    }
    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }
    public String getFromUserHeadImage() {
        return this.fromUserHeadImage;
    }
    public void setFromUserHeadImage(String fromUserHeadImage) {
        this.fromUserHeadImage = fromUserHeadImage;
    }
    public long getToUserId() {
        return this.toUserId;
    }
    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
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
    public String getLocalUrl() {
        return this.localUrl;
    }
    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }
    public String getPreviewUrl() {
        return this.previewUrl;
    }
    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }
    public String getOriginalUrl() {
        return this.originalUrl;
    }
    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
    public int getImageHeight() {
        return this.imageHeight;
    }
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }
    public int getImageWidth() {
        return this.imageWidth;
    }
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }
    public long getSize() {
        return this.size;
    }
    public void setSize(long size) {
        this.size = size;
    }
    public long getPlayTime() {
        return this.playTime;
    }
    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }
    public int getSendStatus() {
        return this.sendStatus;
    }
    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }
    public Date getSendTime() {
        return this.sendTime;
    }
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChatUserMessage) {
            ChatUserMessage like = (ChatUserMessage) obj;
            return this.getMsgId() == like.getMsgId();
        }
        return super.equals(obj);
    }
}
