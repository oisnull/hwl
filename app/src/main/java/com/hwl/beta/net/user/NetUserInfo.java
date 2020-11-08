package com.hwl.beta.net.user;

import com.hwl.beta.utils.StringUtils;

/**
 * Created by Administrator on 2018/3/28.
 */

public class NetUserInfo {
    public String getShowName() {
        String showName = this.Name;
        if (StringUtils.isBlank(showName)) {
            showName = Symbol;
        }
        return showName;
    }

    public String getRegisterAddress() {
        if (this.RegisterPosList != null && this.RegisterPosList.length >= 2) {
            return this.RegisterPosList[0] + " " + this.RegisterPosList[1];
        }
        return "";
    }

    private long Id;
    private String Symbol;
    private String Email;
    private String Mobile;
    private String Token;
    private String Name;
    private String HeadImage;
    private String CircleBackImage;
    private int UserSex;
    private String LifeNotes;
    private int[] RegisterPosIdList;
    private String[] RegisterPosList;
	private UserRegisterAreaInfo RegAreaInfo;
    private int FriendCount;
    private int GroupCount;

    public UserRegisterAreaInfo getRegAreaInfo() {
        return RegAreaInfo;
    }

    public void setRegAreaInfo(UserRegisterAreaInfo regAreaInfo) {
        RegAreaInfo = regAreaInfo;
    }

    public int getGroupCount() {
        return GroupCount;
    }

    public void setGroupCount(int groupCount) {
        GroupCount = groupCount;
    }

    public int getFriendCount() {
        return FriendCount;
    }

    public void setFriendCount(int friendCount) {
        FriendCount = friendCount;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getHeadImage() {
        return HeadImage;
    }

    public void setHeadImage(String headImage) {
        HeadImage = headImage;
    }

    public String getCircleBackImage() {
        return CircleBackImage;
    }

    public void setCircleBackImage(String circleBackImage) {
        CircleBackImage = circleBackImage;
    }

    public int getUserSex() {
        return UserSex;
    }

    public void setUserSex(int userSex) {
        UserSex = userSex;
    }

    public String getLifeNotes() {
        return LifeNotes;
    }

    public void setLifeNotes(String lifeNotes) {
        LifeNotes = lifeNotes;
    }

    public int[] getRegisterPosIdList() {
        return RegisterPosIdList;
    }

    public void setRegisterPosIdList(int[] registerPosIdList) {
        RegisterPosIdList = registerPosIdList;
    }

    public String[] getRegisterPosList() {
        return RegisterPosList;
    }

    public void setRegisterPosList(String[] registerPosList) {
        RegisterPosList = registerPosList;
    }
}
