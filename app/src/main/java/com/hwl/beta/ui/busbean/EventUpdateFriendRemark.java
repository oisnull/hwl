package com.hwl.beta.ui.busbean;

public class EventUpdateFriendRemark {
    private long friendId;
    private String firstLetter;
    private String friendRemark;

    public EventUpdateFriendRemark(long friendId, String firstLetter, String friendRemark) {
        this.friendId = friendId;
        this.firstLetter = firstLetter;
        this.friendRemark = friendRemark;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public String getFriendRemark() {
        return friendRemark;
    }

    public void setFriendRemark(String friendRemark) {
        this.friendRemark = friendRemark;
    }
}
