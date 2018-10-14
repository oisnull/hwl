package com.hwl.beta.ui.ebus;

public class EventMessageModel {
    private int messageType;
    private Object messageModel;

    public EventMessageModel(int messageType) {
        this.messageType = messageType;
    }

    public EventMessageModel(int messageType, Object messageModel) {
        this(messageType);
        this.messageModel = messageModel;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Object getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(Object messageModel) {
        this.messageModel = messageModel;
    }
}
