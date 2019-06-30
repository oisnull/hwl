package com.hwl.beta.net.near.body;

public class SetNearLikeInfoRequest {

    /// <summary>
    /// 0表示取消 1表示点赞
    /// </summary>
    private int ActionType;
    /// <summary>
    /// 点赞的用户id
    /// </summary>
    private long LikeUserId;
    private long NearCircleId;
    private String NearCircleUpdateTime;

    public String getNearCircleUpdateTime() {
        return NearCircleUpdateTime;
    }

    public void setNearCircleUpdateTime(String nearCircleUpdateTime) {
        NearCircleUpdateTime = nearCircleUpdateTime;
    }

    public int getActionType() {
        return ActionType;
    }

    public void setActionType(int actionType) {
        ActionType = actionType;
    }

    public long getLikeUserId() {
        return LikeUserId;
    }

    public void setLikeUserId(long likeUserId) {
        LikeUserId = likeUserId;
    }

    public long getNearCircleId() {
        return NearCircleId;
    }

    public void setNearCircleId(long nearCircleId) {
        NearCircleId = nearCircleId;
    }
}
