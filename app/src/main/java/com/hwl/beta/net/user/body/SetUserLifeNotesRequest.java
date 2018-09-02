package com.hwl.beta.net.user.body;

/**
 * Created by Administrator on 2018/1/26.
 */

public class SetUserLifeNotesRequest {
    private long UserId;
    private String LifeNotes;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public String getLifeNotes() {
        return LifeNotes;
    }

    public void setLifeNotes(String lifeNotes) {
        LifeNotes = lifeNotes;
    }
}