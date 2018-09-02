package com.hwl.beta.net.user;

/**
 * Created by Administrator on 2018/1/27.
 */

public class NetUserFriendInfo {
    private long Id;
    private String Symbol;
    private String Name;
    private String NameRemark;
    private String HeadImage;
    private String Country;
    private String Province;
    private int Sex;
    public String CircleBackImage;
    public String LifeNotes;
    public String UpdateTime;

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
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

    public long getId() {
        return Id;
    }

    public void setId(long id) {
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

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

}
