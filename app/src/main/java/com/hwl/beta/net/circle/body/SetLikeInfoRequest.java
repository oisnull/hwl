package com.hwl.beta.net.circle.body;

public class SetLikeInfoRequest {
    /// <summary>
    /// 0表示取消 1表示点赞
    /// </summary>
    private int ActionType;
    /// <summary>
    /// 点赞的用户id
    /// </summary>
    private long LikeUserId;
    private long CircleId;
	private String CircleUpdateTime;

    public long getCircleUpdateTime() {
        return CircleUpdateTime;
    }

    public void setCircleUpdateTime(String circleUpdateTime) {
        CircleUpdateTime = circleUpdateTime;
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

    public long getCircleId() {
        return CircleId;
    }

    public void setCircleId(long circleId) {
        CircleId = circleId;
    }
}
