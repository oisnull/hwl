package com.hwl.beta.net.circle.body;

public class GetUserCircleInfosRequest {
    /// <summary>
    /// 当前登录的用户id
    /// </summary>
    private long UserId;
    /// <summary>
    /// 查看的用户id
    /// </summary>
    private long ViewUserId;
    /// <summary>
    /// 如果有值，则获取比这个值小的数据列表
    /// </summary>
    private long MinCircleId;
    private int Count;
    private List<NetCircleMatchInfo> CircleMatchInfos;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public long getViewUserId() {
        return ViewUserId;
    }

    public void setViewUserId(long viewUserId) {
        ViewUserId = viewUserId;
    }

    public List<NetCircleMatchInfo> getCircleMatchInfos() {
        return CircleMatchInfos;
    }

    public void setCircleMatchInfos(List<NetCircleMatchInfo> circleMatchInfos) {
        CircleMatchInfos = circleMatchInfos;
    }

    public long getMinCircleId() {
        return MinCircleId;
    }

    public void setMinCircleId(long minCircleId) {
        MinCircleId = minCircleId;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}
