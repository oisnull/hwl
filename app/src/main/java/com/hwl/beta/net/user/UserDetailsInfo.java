package com.hwl.beta.net.user;

import java.util.List;

public class UserDetailsInfo {
    private int Id;
    private String Symbol;
    private String Name;
    private int Sex;
    private String NameRemark;
    private String HeadImage;
    private String CircleBackImage;
    private String LifeNotes;
    private boolean IsFriend;
    private String UpdateTime;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    public String getNameRemark() {
        return NameRemark;
    }

    public void setNameRemark(String nameRemark) {
        NameRemark = nameRemark;
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

    public String getLifeNotes() {
        return LifeNotes;
    }

    public void setLifeNotes(String lifeNotes) {
        LifeNotes = lifeNotes;
    }

    public boolean isFriend() {
        return IsFriend;
    }

    public void setFriend(boolean friend) {
        IsFriend = friend;
    }

    public String getFirstSpell() {
        return FirstSpell;
    }

    public void setFirstSpell(String firstSpell) {
        FirstSpell = firstSpell;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }
}
