package com.hwl.beta.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;

import java.util.Date;

/**
 * Created by Administrator on 2018/2/10.
 */
@Entity
public class GroupUserInfo {
    @Id(autoincrement = true)
    private Long id;
    private String groupGuid;
    private long userId;
    @OrderBy("addTime desc")
    private Date addTime;

    @Generated(hash = 2110531183)
    public GroupUserInfo(Long id, String groupGuid, long userId, Date addTime) {
        this.id = id;
        this.groupGuid = groupGuid;
        this.userId = userId;
        this.addTime = addTime;
    }

    @Generated(hash = 397523636)
    public GroupUserInfo() {
    }

    @Override
    public String toString() {
        return String.format("%s-%s", this.userId, this.groupGuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GroupUserInfo) {
            GroupUserInfo u = (GroupUserInfo) obj;
            return this.userId == u.getUserId()
                    && this.groupGuid.equals(u.getGroupGuid());
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

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getAddTime() {
        return this.addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
