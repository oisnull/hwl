package com.hwl.beta.net.user.body;

import java.util.List;

/**
 * Created by Administrator on 2018/1/26.
 */
public class SetUserInfoRequest {
    private int UserId;
    private String UserName;
    private String HeadImage;
    private String UserSex;
    private List<String> LifeNotes;
    private List<String> PosIdList;

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

    public String getHeadImage() {
        return HeadImage;
    }

    public void setHeadImage(String headImage) {
        HeadImage = headImage;
    }

    public String getUserSex() {
        return UserSex;
    }

    public void setUserSex(String userSex) {
        UserSex = userSex;
    }

    public List<String> getLifeNotes() {
        return LifeNotes;
    }

    public void setLifeNotes(List<String> lifeNotes) {
        LifeNotes = lifeNotes;
    }

    public List<String> getPosIdList() {
        return PosIdList;
    }

    public void setPosIdList(List<String> posIdList) {
        PosIdList = posIdList;
    }
}
