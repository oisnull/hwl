package com.hwl.beta.net.user;

/**
 * Created by Administrator on 2018/2/11.
 */

public class NetSecretUserInfo {
    private int UserId;
    private String UserName;
    private String UserImage;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
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
