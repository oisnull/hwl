package com.hwl.beta.db.entity;

import androidx.databinding.BaseObservable;

import com.hwl.beta.db.ListStringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/2/10.
 */
@Entity
public class GroupInfo extends BaseObservable {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String groupGuid;
    private String groupName;
    private int groupUserCount;
    private long buildUserId;
    @Convert(columnType = String.class, converter = ListStringConverter.class)
    private List<String> groupImages;
    private String groupNote;
    @OrderBy("buildTime desc")
    private Date buildTime;
    private String updateTime;
    private boolean isSystem;

    private String myUserName;
    private String groupBackImage;
    private boolean isShield;
    private boolean isDismiss;
    private boolean isLoadUser;

    @Generated(hash = 731591539)
    public GroupInfo(Long id, String groupGuid, String groupName,
                     int groupUserCount, long buildUserId, List<String> groupImages,
                     String groupNote, Date buildTime, String updateTime, boolean isSystem,
                     String myUserName, String groupBackImage, boolean isShield,
                     boolean isDismiss, boolean isLoadUser) {
        this.id = id;
        this.groupGuid = groupGuid;
        this.groupName = groupName;
        this.groupUserCount = groupUserCount;
        this.buildUserId = buildUserId;
        this.groupImages = groupImages;
        this.groupNote = groupNote;
        this.buildTime = buildTime;
        this.updateTime = updateTime;
        this.isSystem = isSystem;
        this.myUserName = myUserName;
        this.groupBackImage = groupBackImage;
        this.isShield = isShield;
        this.isDismiss = isDismiss;
        this.isLoadUser = isLoadUser;
    }

    @Generated(hash = 1250265142)
    public GroupInfo() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof GroupInfo) {
            GroupInfo g = (GroupInfo) obj;
            return this.getGroupGuid().equals(g.getGroupGuid());
        }
        return super.equals(obj);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getGroupUserCount() {
        return this.groupUserCount;
    }

    public void setGroupUserCount(int groupUserCount) {
        this.groupUserCount = groupUserCount;
    }

    public long getBuildUserId() {
        return this.buildUserId;
    }

    public void setBuildUserId(long buildUserId) {
        this.buildUserId = buildUserId;
    }

    public List<String> getGroupImages() {
        return this.groupImages;
    }

    public void setGroupImages(List<String> groupImages) {
        this.groupImages = groupImages;
    }

    public String getGroupNote() {
        return this.groupNote;
    }

    public void setGroupNote(String groupNote) {
        this.groupNote = groupNote;
    }

    public Date getBuildTime() {
        return this.buildTime;
    }

    public void setBuildTime(Date buildTime) {
        this.buildTime = buildTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean getIsSystem() {
        return this.isSystem;
    }

    public void setIsSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public String getMyUserName() {
        return this.myUserName;
    }

    public void setMyUserName(String myUserName) {
        this.myUserName = myUserName;
    }

    public String getGroupBackImage() {
        return this.groupBackImage;
    }

    public void setGroupBackImage(String groupBackImage) {
        this.groupBackImage = groupBackImage;
    }

    public boolean getIsShield() {
        return this.isShield;
    }

    public void setIsShield(boolean isShield) {
        this.isShield = isShield;
    }

    public boolean getIsDismiss() {
        return this.isDismiss;
    }

    public void setIsDismiss(boolean isDismiss) {
        this.isDismiss = isDismiss;
    }

    public boolean getIsLoadUser() {
        return this.isLoadUser;
    }

    public void setIsLoadUser(boolean isLoadUser) {
        this.isLoadUser = isLoadUser;
    }

}
