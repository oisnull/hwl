package com.hwl.beta.ui.entry.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * Created by Administrator on 2018/3/30.
 */

public class MainBean extends BaseObservable {
    private int chatMessageCount;
    private int nearMessageCount;
    private int friendMessageCount;
    private int circleMessageCount;
    private int meMessageCount;

    public MainBean(int chatMessageCount, int nearMessageCount, int friendMessageCount,
                    int circleMessageCount, int meMessageCount) {
        this.chatMessageCount = chatMessageCount;
        this.nearMessageCount = nearMessageCount;
        this.friendMessageCount = friendMessageCount + circleMessageCount;
//        this.circleMessageCount = circleMessageCount;
        this.meMessageCount = meMessageCount;
    }

    @Bindable
    public int getChatMessageCount() {
        return chatMessageCount;
    }

    public void setChatMessageCount(int chatMessageCount) {
        this.chatMessageCount = chatMessageCount;
//        notifyPropertyChanged(BR.chatMessageCount);
    }

    @Bindable
    public int getNearMessageCount() {
        return nearMessageCount;
    }

    public void setNearMessageCount(int nearMessageCount) {
        this.nearMessageCount = nearMessageCount;
//        notifyPropertyChanged(BR.nearMessageCount);
    }

    @Bindable
    public int getFriendMessageCount() {
        return friendMessageCount;
    }

    public void setFriendMessageCount(int friendMessageCount) {
        this.friendMessageCount = friendMessageCount;
//        notifyPropertyChanged(BR.friendMessageCount);
    }

    @Bindable
    public int getCircleMessageCount() {
        return circleMessageCount;
    }

    public void setCircleMessageCount(int circleMessageCount) {
        this.circleMessageCount = circleMessageCount;
//        notifyPropertyChanged(BR.circleMessageCount);
    }

    @Bindable
    public int getMeMessageCount() {
        return meMessageCount;
    }

    public void setMeMessageCount(int meMessageCount) {
        this.meMessageCount = meMessageCount;
//        notifyPropertyChanged(BR.meMessageCount);
    }

    private String chatMessageCountDesc;
    private String friendRequestCountDesc;

    public MainBean(String chatMessageCountDesc, String friendRequestCountDesc) {
        this.chatMessageCountDesc = chatMessageCountDesc;
        this.friendRequestCountDesc = friendRequestCountDesc;
    }

    @Bindable
    public String getChatMessageCountDesc() {
        return chatMessageCountDesc;
    }

    public void setChatMessageCountDesc(String chatMessageCountDesc) {
        this.chatMessageCountDesc = chatMessageCountDesc;
//        notifyPropertyChanged(BR.chatMessageCountDesc);
    }

    @Bindable
    public String getFriendRequestCountDesc() {
        return friendRequestCountDesc;
    }

    public void setFriendRequestCountDesc(String friendRequestCountDesc) {
        this.friendRequestCountDesc = friendRequestCountDesc;
//        notifyPropertyChanged(BR.friendRequestCountDesc);
    }

    public MainBean() {
    }


}
