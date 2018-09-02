package com.hwl.beta.net.near.body;

import com.hwl.beta.net.near.NetImageInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/3/8.
 */

public class AddNearCircleInfoRequest {
    private long UserId;
    private String Content;
    private int PosId;
    private String PosDesc;
    private List<NetImageInfo> Images;
    private String LinkTitle;
    private String LinkUrl;
    private String LinkImage;
    private double Lon;
    private double Lat;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getPosId() {
        return PosId;
    }

    public void setPosId(int posId) {
        PosId = posId;
    }

    public String getPosDesc() {
        return PosDesc;
    }

    public void setPosDesc(String posDesc) {
        PosDesc = posDesc;
    }

    public List<NetImageInfo> getImages() {
        return Images;
    }

    public void setImages(List<NetImageInfo> images) {
        Images = images;
    }

    public String getLinkTitle() {
        return LinkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        LinkTitle = linkTitle;
    }

    public String getLinkUrl() {
        return LinkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        LinkUrl = linkUrl;
    }

    public String getLinkImage() {
        return LinkImage;
    }

    public void setLinkImage(String linkImage) {
        LinkImage = linkImage;
    }

    public double getLon() {
        return Lon;
    }

    public void setLon(double lon) {
        Lon = lon;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }
}
