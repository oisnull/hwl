package com.hwl.beta.net.near.body;

import java.util.Date;

/**
 * Created by Administrator on 2018/3/8.
 */

public class AddNearCircleInfoResponse {

    private int NearCircleId;
    private int ContentType;
    private Date PublishTime;

    public int getNearCircleId() {
        return NearCircleId;
    }

    public int getContentType() {
        return ContentType;
    }

    public Date getPublishTime() {
        return PublishTime;
    }
}
