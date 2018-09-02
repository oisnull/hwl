package com.hwl.beta.net.circle.body;

import com.hwl.beta.net.near.NetImageInfo;

import java.util.List;

public class AddCircleInfoRequest {
    private long UserId ;
    private String Content ;
    private List<NetImageInfo> Images ;
    private int PosId ;
    private String PosDesc ;
    private double Lat ;
    private double Lon ;
    private String LinkUrl ;
    private String LinkTitle ;
    private String LinkImage ;

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

    public List<NetImageInfo> getImages() {
        return Images;
    }

    public void setImages(List<NetImageInfo> images) {
        Images = images;
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

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLon() {
        return Lon;
    }

    public void setLon(double lon) {
        Lon = lon;
    }

    public String getLinkUrl() {
        return LinkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        LinkUrl = linkUrl;
    }

    public String getLinkTitle() {
        return LinkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        LinkTitle = linkTitle;
    }

    public String getLinkImage() {
        return LinkImage;
    }

    public void setLinkImage(String linkImage) {
        LinkImage = linkImage;
    }
}
