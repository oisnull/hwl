package com.hwl.beta.ui.user.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/2.
 */

public class UserIndexBean {

    public static final int IDCARD_FRIEND = 1;
    public static final int IDCARD_MINE = 2;
    public static final int IDCARD_OTHER = 3;

    private int idcard;
    private long userId;
    private String showName;
    private String userImage;
    private String symbol;
    private String remark;
    private String registerAddress;
    private String userCircleBackImage;
    private String userLifeNotes;
    private List<String> circleImages;
    private List<String> circleTexts;

    public List<String> getCircleImages() {
        return circleImages;
    }

    public void setCircleImages(List<String> circleImages) {
        this.circleImages = circleImages;
    }

    public List<String> getCircleTexts() {
        return circleTexts;
    }

    public void setCircleTexts(List<String> circleTexts) {
        this.circleTexts = circleTexts;
    }

    public boolean isFriend;

    public int getIdcard() {
        return idcard;
    }

    public void setIdcard(int idcard) {
        this.idcard = idcard;
    }

    public String getUserCircleBackImage() {
        return userCircleBackImage;
    }

    public void setUserCircleBackImage(String userCircleBackImage) {
        this.userCircleBackImage = userCircleBackImage;
    }

    public String getUserLifeNotes() {
        return userLifeNotes;
    }

    public void setUserLifeNotes(String userLifeNotes) {
        this.userLifeNotes = userLifeNotes;
    }

    public UserIndexBean() {
    }

    public UserIndexBean(long userId, String showName, String userImage) {
        this.userId = userId;
        this.showName = showName;
        this.userImage = userImage;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
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

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }
}
