package com.hwl.beta.ui.ebus.bean;

import com.hwl.beta.db.entity.ChatRecordMessage;

public class EventActionChatRecord {
    private int actionType;
    private ChatRecordMessage record;

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public ChatRecordMessage getRecord() {
        return record;
    }

    public void setRecord(ChatRecordMessage record) {
        this.record = record;
    }

    public EventActionChatRecord(int actionType, ChatRecordMessage record) {
        this.actionType = actionType;
        this.record = record;
    }
}
