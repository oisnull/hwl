package com.hwl.beta.ui.ebus.bean;

import com.hwl.beta.db.entity.CircleComment;

public class EventActionCircleComment {
    private int actionType;
    private CircleComment comment;

    public EventActionCircleComment() {
    }

    public EventActionCircleComment(int actionType, CircleComment comment) {
        this.actionType = actionType;
        this.comment = comment;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public CircleComment getComment() {
        return comment;
    }

    public void setComment(CircleComment comment) {
        this.comment = comment;
    }
}
