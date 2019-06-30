package com.hwl.beta.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/14.
 */

@Entity
public class NearCircleLike implements Serializable {
    private static final long serialVersionUID = 12L;
    private long nearCircleId;
    private long likeUserId;
    private String likeUserName;
    private String likeUserImage;
    @OrderBy("likeTime desc")
    private Date likeTime;
    @Transient
    private String lastUpdateTime;

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Generated(hash = 684760537)
    public NearCircleLike(long nearCircleId, long likeUserId, String likeUserName,
                          String likeUserImage, Date likeTime) {
        this.nearCircleId = nearCircleId;
        this.likeUserId = likeUserId;
        this.likeUserName = likeUserName;
        this.likeUserImage = likeUserImage;
        this.likeTime = likeTime;
    }

    @Generated(hash = 559624202)
    public NearCircleLike() {
    }

    public long getNearCircleId() {
        return this.nearCircleId;
    }

    public void setNearCircleId(long nearCircleId) {
        this.nearCircleId = nearCircleId;
    }

    public long getLikeUserId() {
        return this.likeUserId;
    }

    public void setLikeUserId(long likeUserId) {
        this.likeUserId = likeUserId;
    }

    public String getLikeUserName() {
        return this.likeUserName;
    }

    public void setLikeUserName(String likeUserName) {
        this.likeUserName = likeUserName;
    }

    public String getLikeUserImage() {
        return this.likeUserImage;
    }

    public void setLikeUserImage(String likeUserImage) {
        this.likeUserImage = likeUserImage;
    }

    public Date getLikeTime() {
        return this.likeTime;
    }

    public void setLikeTime(Date likeTime) {
        this.likeTime = likeTime;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NearCircleLike) {
            NearCircleLike like = (NearCircleLike) obj;
            return this.getNearCircleId() == like.getNearCircleId()
                    && this.getLikeUserId() == like.getLikeUserId();
        }
        return super.equals(obj);
    }
}
