package com.hwl.beta.net.near.body;

import com.hwl.beta.net.near.NetNearCircleCommentInfo;

/**
 * Created by Administrator on 2018/3/14.
 */

public class AddNearCommentResponse {

    public NetNearCircleCommentInfo getNearCircleCommentInfo() {
        return NearCircleCommentInfo;
    }

    public long getNearCirclePublishUserId() {
        return NearCirclePublishUserId;
    }

    private NetNearCircleCommentInfo NearCircleCommentInfo;
    private long NearCirclePublishUserId;
    private String NearCircleLastUpdateTime;

    public String getNearCircleLastUpdateTime() {
        return NearCircleLastUpdateTime;
    }
}