package com.hwl.beta.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

@Entity
public class CircleImage implements Serializable {
    private static final long serialVersionUID = 7L;
    private long circleId;
    private long postUserId;
    private String imageUrl;
    private int imageHeight;
    private int imageWidth;
    @Generated(hash = 1963167170)
    public CircleImage(long circleId, long postUserId, String imageUrl,
            int imageHeight, int imageWidth) {
        this.circleId = circleId;
        this.postUserId = postUserId;
        this.imageUrl = imageUrl;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
    }
    @Generated(hash = 413471763)
    public CircleImage() {
    }
    public long getCircleId() {
        return this.circleId;
    }
    public void setCircleId(long circleId) {
        this.circleId = circleId;
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
