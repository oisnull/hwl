package com.hwl.beta.ui.ebus.bean;

public class EventDeleteFriend {
    private long friendId;

    public EventDeleteFriend(long friendId) {
        this.friendId = friendId;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }
}
