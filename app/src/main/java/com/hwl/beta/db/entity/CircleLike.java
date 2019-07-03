package com.hwl.beta.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.OrderBy;

import java.io.Serializable;
import java.util.Date;

@Entity
public class CircleLike implements Serializable {
    private static final long serialVersionUID = 8L;
    private long circleId;
    private long likeUserId;
    private String likeUserName;
    private String likeUserImage;
    @OrderBy("likeTime desc")
    private Date likeTime;
    @Transient
    private String lastUpdateTime;

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    @Generated(hash = 273748114)
    public CircleLike(long circleId, long likeUserId, String likeUserName,
            String likeUserImage, Date likeTime) {
        this.circleId = circleId;
        this.likeUserId = likeUserId;
        this.likeUserName = likeUserName;
        this.likeUserImage = likeUserImage;
        this.likeTime = likeTime;
    }
    @Generated(hash = 1721050327)
    public CircleLike() {
    }
    public long getCircleId() {
        return this.circleId;
    }
    public void setCircleId(long circleId) {
        this.circleId = circleId;
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
        if (obj instanceof CircleLike) {
            CircleLike like = (CircleLike) obj;
            return this.getCircleId() == like.getCircleId()
                    && this.getLikeUserId() == like.getLikeUserId();
        }
        return super.equals(obj);
    }
}
