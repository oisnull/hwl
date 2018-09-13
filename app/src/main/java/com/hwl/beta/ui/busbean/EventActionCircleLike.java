package com.hwl.beta.ui.busbean;

import com.hwl.beta.db.entity.CircleLike;

public class EventActionCircleLike {
    private int actionType;
    private CircleLike like;

    public EventActionCircleLike() {
    }

    public EventActionCircleLike(int actionType, CircleLike like) {
        this.actionType = actionType;
        this.like = like;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public CircleLike getLike() {
        return like;
    }

    public void setLike(CircleLike like) {
        this.like = like;
    }
}
