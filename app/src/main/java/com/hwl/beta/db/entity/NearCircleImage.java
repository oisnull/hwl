package com.hwl.beta.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/8.
 */
@Entity
public class NearCircleImage implements Serializable {
    private static final long serialVersionUID = 5L;
    private long nearCircleId;
    private long postUserId;
    private String imageUrl;
    private int imageHeight;
    private int imageWidth;
    @Generated(hash = 1052541099)
    public NearCircleImage(long nearCircleId, long postUserId, String imageUrl,
            int imageHeight, int imageWidth) {
        this.nearCircleId = nearCircleId;
        this.postUserId = postUserId;
        this.imageUrl = imageUrl;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
    }
    @Generated(hash = 869754823)
    public NearCircleImage() {
    }
    public long getNearCircleId() {
        return this.nearCircleId;
    }
    public void setNearCircleId(long nearCircleId) {
        this.nearCircleId = nearCircleId;
    }
    public long getPostUserId() {
        return this.postUserId;
    }
    public void setPostUserId(long postUserId) {
        this.postUserId = postUserId;
    }
    public String getImageUrl() {
        return this.imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
}
