package com.hwl.beta.net.user;

/**
 * Created by Administrator on 2018/2/11.
 */

public class NetNearUserInfo {
    private long UserId;
    private String UserName;
    private String UserImage;
    private double Distance;

    public double getDistance() {
        return Distance;
    }

    public void setDistance(double distance) {
        Distance = distance;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }
}
