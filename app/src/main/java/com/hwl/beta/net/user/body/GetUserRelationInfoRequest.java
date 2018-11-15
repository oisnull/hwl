package com.hwl.beta.net.user.body;

public class GetUserRelationInfoRequest {
    private long UserId;
    private long RelationUserId;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long UserId) {
        this.UserId = UserId;
    }

    public long getRelationUserId() {
        return RelationUserId;
    }

    public void setRelationUserId(long RelationUserId) {
        this.RelationUserId = RelationUserId;
    }

}
