package com.hwl.beta.ui.user.bean;

/**
 * Created by Administrator on 2018/4/3.
 */

public class UserEditBean {

    private long userId;
    private String symbol;
    private String name;
    private String sex;
    private String lifeNotes;
    private String headImage;

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
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
//        notifyPropertyChanged(BR.symbol);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
//        notifyPropertyChanged(BR.name);
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
//        notifyPropertyChanged(BR.sex);
    }

    public String getLifeNotes() {
        return lifeNotes;
    }

    public void setLifeNotes(String lifeNotes) {
        this.lifeNotes = lifeNotes;
//        notifyPropertyChanged(BR.lifeNotes);
    }
}
