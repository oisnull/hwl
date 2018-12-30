package com.hwl.beta.ui.ebus.bean;

public class EventChatRecordClear {
    private long myUserId;
    private long viewUserId;

    public EventChatRecordClear(long myUserId, long viewUserId) {
        this.myUserId = myUserId;
        this.viewUserId = viewUserId;
    }

    public long getMyUserId() {
        return myUserId;
    }

    public void setMyUserId(long myUserId) {
        this.myUserId = myUserId;
    }

    public long getViewUserId() {
        return viewUserId;
    }

    public void setViewUserId(long viewUserId) {
        this.viewUserId = viewUserId;
    }
}
