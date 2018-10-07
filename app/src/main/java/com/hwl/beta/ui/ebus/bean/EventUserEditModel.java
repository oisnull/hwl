package com.hwl.beta.ui.ebus.bean;

public class EventUserEditModel {
   private String userHeadImageUrl;
   private String userSymbol;
   private String userName;
   private int userSex;
   private String userLifeNotes;

    public String getUserHeadImageUrl() {
        return userHeadImageUrl;
    }

    public void setUserHeadImageUrl(String userHeadImageUrl) {
        this.userHeadImageUrl = userHeadImageUrl;
    }

    public String getUserSymbol() {
        return userSymbol;
    }

    public void setUserSymbol(String userSymbol) {
        this.userSymbol = userSymbol;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserSex() {
        return userSex;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

    public String getUserLifeNotes() {
        return userLifeNotes;
    }

    public void setUserLifeNotes(String userLifeNotes) {
        this.userLifeNotes = userLifeNotes;
    }
}
