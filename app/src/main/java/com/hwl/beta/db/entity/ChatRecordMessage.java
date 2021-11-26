package com.hwl.beta.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/2/8.
 */
@Entity
public class ChatRecordMessage implements Serializable {
    private static final long serialVersionUID = 2L;

    @Id(autoincrement = true)
    private Long recordId;
    private int recordType;
    private long fromUserId;
    private String fromUserName;
    private String fromUserHeadImage;
    private long toUserId;
    private String toUserName;
    private String toUserHeadImage;
    private String groupGuid;
    private String groupName;
    private String title;
    private int contentType;
    private String content;
    private int unreadCount;
    @OrderBy("sendTime desc")
    private Date sendTime;

    @Transient
    private boolean isShield;
    @Transient
    private boolean isCurrentLocation;
    @Transient
    private List<String> groupUserImages;

    public boolean isCurrentLocation() {
        return isCurrentLocation;
    }

    public void setCurrentLocation(boolean currentLocation) {
        isCurrentLocation = currentLocation;
    }

    @Generated(hash = 292329467)
    public ChatRecordMessage(Long recordId, int recordType, long fromUserId,
                             String fromUserName, String fromUserHeadImage, long toUserId,
                             String toUserName, String toUserHeadImage, String groupGuid,
                             String groupName, String title, int contentType, String content,
                             int unreadCount, Date sendTime) {
        this.recordId = recordId;
        this.recordType = recordType;
        this.fromUserId = fromUserId;
        this.fromUserName = fromUserName;
        this.fromUserHeadImage = fromUserHeadImage;
        this.toUserId = toUserId;
        this.toUserName = toUserName;
        this.toUserHeadImage = toUserHeadImage;
        this.groupGuid = groupGuid;
        this.groupName = groupName;
        this.title = title;
        this.contentType = contentType;
        this.content = content;
        this.unreadCount = unreadCount;
        this.sendTime = sendTime;
    }

    @Generated(hash = 1590399531)
    public ChatRecordMessage() {
    }

    public Long getRecordId() {
        return this.recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public int getRecordType() {
        return this.recordType;
    }

    public void setRecordType(int recordType) {
        this.recordType = recordType;
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

    public String getToUserName() {
        return this.toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getToUserHeadImage() {
        return this.toUserHeadImage;
    }

    public void setToUserHeadImage(String toUserHeadImage) {
        this.toUserHeadImage = toUserHeadImage;
    }

    public String getGroupGuid() {
        return this.groupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        this.groupGuid = groupGuid;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getUnreadCount() {
        return this.unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Date getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChatRecordMessage) {
            ChatRecordMessage record = (ChatRecordMessage) obj;
            return this.getRecordId().equals(record.getRecordId());
        }
        return super.equals(obj);
    }

    public boolean hasRecordId() {
        return this.recordId != null && this.recordId > 0;
    }

    public String getRecordImage(long myUserId) {
        if (this.fromUserId == myUserId)
            return this.toUserHeadImage;
        return this.fromUserHeadImage;
    }

    public boolean isShield() {
        return isShield;
    }

    public void setShield(boolean shield) {
        isShield = shield;
    }

    public List<String> getGroupUserImages() {
        return groupUserImages;
    }

    public void setGroupUserImages(List<String> groupUserImages) {
        this.groupUserImages = groupUserImages;
    }
}
