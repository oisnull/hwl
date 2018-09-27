package com.hwl.beta.ui.busbean;

public class EventMessageModel<T> {
    private int messageType;
    private T messageModel;

    public EventMessageModel(int messageType) {
        this.messageType = messageType;
    }

    public EventMessageModel(int messageType, T messageModel) {
        this(messageType);
        this.messageModel = messageModel;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public T getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(T messageModel) {
        this.messageModel = messageModel;
    }
}
