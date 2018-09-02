package com.hwl.beta.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class ChatUserSetting {

    @Id
    private long userId;
    private String chatBackImage;
    private boolean isShield;
    @Generated(hash = 746822151)
    public ChatUserSetting(long userId, String chatBackImage, boolean isShield) {
        this.userId = userId;
        this.chatBackImage = chatBackImage;
        this.isShield = isShield;
    }
    @Generated(hash = 381245338)
    public ChatUserSetting() {
    }
    public long getUserId() {
        return this.userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getChatBackImage() {
        return this.chatBackImage;
    }
    public void setChatBackImage(String chatBackImage) {
        this.chatBackImage = chatBackImage;
    }
    public boolean getIsShield() {
        return this.isShield;
    }
    public void setIsShield(boolean isShield) {
        this.isShield = isShield;
    }

}
