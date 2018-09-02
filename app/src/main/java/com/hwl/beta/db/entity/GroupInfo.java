package com.hwl.beta.db.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.hwl.beta.BR;
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
    private String groupImage;
    private int groupUserCount;
    private long buildUserId;
    @Convert(columnType = String.class, converter = ListStringConverter.class)
    private List<String> userImages;
    private String groupNote;
    @OrderBy("buildTime desc")
    private Date buildTime;
    private String updateTime;

    private String myUserName;
    private String groupBackImage;
    private boolean isShield;
    private boolean isDismiss;

    @Generated(hash = 1250265142)
    public GroupInfo() {
    }

    @Generated(hash = 312376364)
    public GroupInfo(Long id, String groupGuid, String groupName, String groupImage,
            int groupUserCount, long buildUserId, List<String> userImages, String groupNote,
            Date buildTime, String updateTime, String myUserName, String groupBackImage,
            boolean isShield, boolean isDismiss) {
        this.id = id;
        this.groupGuid = groupGuid;
        this.groupName = groupName;
        this.groupImage = groupImage;
        this.groupUserCount = groupUserCount;
        this.buildUserId = buildUserId;
        this.userImages = userImages;
        this.groupNote = groupNote;
        this.buildTime = buildTime;
        this.updateTime = updateTime;
        this.myUserName = myUserName;
        this.groupBackImage = groupBackImage;
        this.isShield = isShield;
        this.isDismiss = isDismiss;
    }

    public String getGroupGuid() {
        return this.groupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        this.groupGuid = groupGuid;
    }

    @Bindable
    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
        notifyPropertyChanged(BR.groupName);
    }

    public String getGroupImage() {
        return this.groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public int getGroupUserCount() {
        return this.groupUserCount;
    }

    public void setGroupUserCount(int groupUserCount) {
        this.groupUserCount = groupUserCount;
    }

    @Bindable
    public String getGroupNote() {
        return this.groupNote;
    }

    public void setGroupNote(String groupNote) {
        this.groupNote = groupNote;
        notifyPropertyChanged(BR.groupNote);
    }

    public Date getBuildTime() {
        return this.buildTime;
    }

    public void setBuildTime(Date buildTime) {
        this.buildTime = buildTime;
    }

    public List<String> getUserImages() {
        return this.userImages;
    }

    public void setUserImages(List<String> userImages) {
        this.userImages = userImages;
    }

    @Bindable
    public String getMyUserName() {
        return this.myUserName;
    }

    public void setMyUserName(String myUserName) {
        this.myUserName = myUserName;
        notifyPropertyChanged(BR.myUserName);
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

    public long getBuildUserId() {
        return this.buildUserId;
    }

    public void setBuildUserId(long buildUserId) {
        this.buildUserId = buildUserId;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsDismiss() {
        return this.isDismiss;
    }

    public void setIsDismiss(boolean isDismiss) {
        this.isDismiss = isDismiss;
    }

}
