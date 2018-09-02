package com.hwl.beta.net.circle;

import com.hwl.beta.net.near.NetImageInfo;

import java.util.Date;
import java.util.List;

public class NetCircleInfo {
    private long CircleId;
    private long UserId;
    private int ContentType;
    private String CircleContent;
    private Date PublishTime;
    private String UpdateTime;
    private int PosId;
    private String PosDesc;
    private double Lon;
    private double Lat;
    private String LinkUrl;
    private String LinkTitle;
    private String LinkImage;
    private int ImageCount;
    private int CommentCount;
    private int LikeCount;
    /// <summary>
    /// 是否点赞过
    /// </summary>
    private boolean IsLiked;

    private List<NetImageInfo> Images;

    private int PublishUserId;
    private String PublishUserName;
    private String PublishUserImage;

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    private List<NetCircleLikeInfo> LikeInfos;

    private List<NetCircleCommentInfo> CommentInfos;

    public List<NetImageInfo> getImages() {
        return Images;
    }

    public void setImages(List<NetImageInfo> images) {
        Images = images;
    }

    public boolean isLiked() {
        return IsLiked;
    }

    public void setLiked(boolean liked) {
        IsLiked = liked;
    }

    public long getCircleId() {
        return CircleId;
    }

    public void setCircleId(long circleId) {
        CircleId = circleId;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public int getContentType() {
        return ContentType;
    }

    public void setContentType(int contentType) {
        ContentType = contentType;
    }

    public String getCircleContent() {
        return CircleContent;
    }

    public void setCircleContent(String circleContent) {
        CircleContent = circleContent;
    }

    public Date getPublishTime() {
        return PublishTime;
    }

    public void setPublishTime(Date publishTime) {
        PublishTime = publishTime;
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

    public int getImageCount() {
        return ImageCount;
    }

    public void setImageCount(int imageCount) {
        ImageCount = imageCount;
    }

    public int getCommentCount() {
        return CommentCount;
    }

    public void setCommentCount(int commentCount) {
        CommentCount = commentCount;
    }

    public int getLikeCount() {
        return LikeCount;
    }

    public void setLikeCount(int likeCount) {
        LikeCount = likeCount;
    }

    public int getPublishUserId() {
        return PublishUserId;
    }

    public void setPublishUserId(int publishUserId) {
        PublishUserId = publishUserId;
    }

    public String getPublishUserName() {
        return PublishUserName;
    }

    public void setPublishUserName(String publishUserName) {
        PublishUserName = publishUserName;
    }

    public String getPublishUserImage() {
        return PublishUserImage;
    }

    public void setPublishUserImage(String publishUserImage) {
        PublishUserImage = publishUserImage;
    }

    public List<NetCircleLikeInfo> getLikeInfos() {
        return LikeInfos;
    }

    public void setLikeInfos(List<NetCircleLikeInfo> likeInfos) {
        LikeInfos = likeInfos;
    }

    public List<NetCircleCommentInfo> getCommentInfos() {
        return CommentInfos;
    }

    public void setCommentInfos(List<NetCircleCommentInfo> commentInfos) {
        CommentInfos = commentInfos;
    }
}
