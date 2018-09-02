package com.hwl.beta.net.circle;

import java.util.Date;

public class NetCircleLikeInfo {
    private int LikeId;
    private int CircleId;
    private int LikeUserId;
    private String LikeUserName;
    private String LikeUserImage;
    private Date LikeTime;

    public int getLikeId() {
        return LikeId;
    }

    public void setLikeId(int likeId) {
        LikeId = likeId;
    }

    public int getCircleId() {
        return CircleId;
    }

    public void setCircleId(int circleId) {
        CircleId = circleId;
    }

    public int getLikeUserId() {
        return LikeUserId;
    }

    public void setLikeUserId(int likeUserId) {
        LikeUserId = likeUserId;
    }

    public String getLikeUserName() {
        return LikeUserName;
    }

    public void setLikeUserName(String likeUserName) {
        LikeUserName = likeUserName;
    }

    public String getLikeUserImage() {
        return LikeUserImage;
    }

    public void setLikeUserImage(String likeUserImage) {
        LikeUserImage = likeUserImage;
    }

    public Date getLikeTime() {
        return LikeTime;
    }

    public void setLikeTime(Date likeTime) {
        LikeTime = likeTime;
    }
}
