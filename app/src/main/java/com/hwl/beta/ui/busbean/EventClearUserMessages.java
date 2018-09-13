package com.hwl.beta.ui.busbean;

public class EventClearUserMessages {
    private int actionType;
    private long myUserId;
    private long viewUserId;

    public EventClearUserMessages(int actionType, long myUserId, long viewUserId) {
        this.actionType = actionType;
        this.myUserId = myUserId;
        this.viewUserId = viewUserId;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
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
