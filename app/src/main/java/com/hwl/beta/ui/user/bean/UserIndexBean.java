package com.hwl.beta.ui.user.bean;

import com.hwl.beta.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/4/2.
 */

public class UserIndexBean {
    private long userId;
    private String userName;
    private String userImage;
    private String symbol;
    private String remark;
    private String userLifeNotes;
    private String updateTime;
	private boolean misMe;
	private boolean misFriend;

    public boolean isFriend() {
        return this.misFriend;
    }

    public void SetFriend(boolean isFriend) {
        this.misFriend = isFriend;
    }

    public boolean isMe() {
        return this.misMe;
    }

    public void SetMe(boolean isMe) {
        this.misMe = isMe;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserLifeNotes() {
        return userLifeNotes;
    }

    public void setUserLifeNotes(String userLifeNotes) {
        this.userLifeNotes = userLifeNotes;
    }

    public UserIndexBean() {
    }

    public UserIndexBean(long userId, String userName, String userImage) {
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getShowName() {
        if (StringUtils.isNotBlank(this.remark))
            return this.remark;
        else if (StringUtils.isNotBlank(this.userName))
            return this.userName;

        return this.symbol;
    }
}
