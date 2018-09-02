package com.hwl.beta.net.circle.body;

import com.hwl.beta.net.circle.NetCircleInfo;

import java.util.List;

public class GetUserCircleInfosResponse {
    public int ViewUserId;
    public String ViewUserName;
    public String ViewUserImage;
    public List<NetCircleInfo> CircleInfos;

    public int getViewUserId() {
        return ViewUserId;
    }

    public void setViewUserId(int viewUserId) {
        ViewUserId = viewUserId;
    }

    public String getViewUserName() {
        return ViewUserName;
    }

    public void setViewUserName(String viewUserName) {
        ViewUserName = viewUserName;
    }

    public String getViewUserImage() {
        return ViewUserImage;
    }

    public void setViewUserImage(String viewUserImage) {
        ViewUserImage = viewUserImage;
    }

    public List<NetCircleInfo> getCircleInfos() {
        return CircleInfos;
    }

    public void setCircleInfos(List<NetCircleInfo> circleInfos) {
        CircleInfos = circleInfos;
    }
}
