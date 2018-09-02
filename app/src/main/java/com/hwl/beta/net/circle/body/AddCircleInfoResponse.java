package com.hwl.beta.net.circle.body;

import java.util.Date;

public class AddCircleInfoResponse {
    private int CircleId ;
    private int ContentType ;
    private Date PublishTime ;

    public int getCircleId() {
        return CircleId;
    }

    public void setCircleId(int circleId) {
        CircleId = circleId;
    }

    public int getContentType() {
        return ContentType;
    }

    public void setContentType(int contentType) {
        ContentType = contentType;
    }

    public Date getPublishTime() {
        return PublishTime;
    }

    public void setPublishTime(Date publishTime) {
        PublishTime = publishTime;
    }
}
