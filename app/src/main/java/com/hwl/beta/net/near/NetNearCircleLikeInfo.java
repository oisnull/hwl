package com.hwl.beta.net.near;

import java.util.Date;

/**
 * Created by Administrator on 2018/3/17.
 */

public class NetNearCircleLikeInfo {
    private int LikeId ;
    private int NearCircleId ;
    private int LikeUserId ;
    private String LikeUserName ;
    private String LikeUserImage;
    private Date LikeTime ;

    public int getLikeId() {
        return LikeId;
    }

    public void setLikeId(int likeId) {
        LikeId = likeId;
    }

    public int getNearCircleId() {
        return NearCircleId;
    }

    public void setNearCircleId(int nearCircleId) {
        NearCircleId = nearCircleId;
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
