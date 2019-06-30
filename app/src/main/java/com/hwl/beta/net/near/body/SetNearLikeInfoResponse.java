package com.hwl.beta.net.near.body;

/**
 * Created by Administrator on 2018/3/14.
 */

public class SetNearLikeInfoResponse {
    private int Status;
    private String NearCircleLastUpdateTime;

    public String getNearCircleLastUpdateTime() {
        return NearCircleLastUpdateTime;
    }

    public int getStatus() {
        return Status;
    }
}
