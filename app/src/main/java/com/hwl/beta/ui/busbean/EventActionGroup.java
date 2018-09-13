package com.hwl.beta.ui.busbean;

import com.hwl.beta.db.entity.GroupInfo;

public class EventActionGroup {
    private int actionType;
    private GroupInfo groupInfo;

    public EventActionGroup() {
    }

    public EventActionGroup(int actionType, GroupInfo groupInfo) {
        this.actionType = actionType;
        this.groupInfo = groupInfo;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }
}
