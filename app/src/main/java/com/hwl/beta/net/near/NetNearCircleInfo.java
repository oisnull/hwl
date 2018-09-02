package com.hwl.beta.net.near;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/3/8.
 */

public class NetNearCircleInfo {
    private long NearCircleId;
    private long PublishUserId;
    private String PublishUserName;
    private String PublishUserImage;
    private int ContentType;
    private String Content;
    private String LinkTitle;
    private String LinkUrl;
    private String LinkImage;
    private Date PublishTime;
    private String UpdateTime;
    private String PosDesc;
    private List<NetImageInfo> Images;
    private int CommentCount;
    private int LikeCount;
    private boolean IsLiked;
    private List<NetNearCircleLikeInfo> LikeInfos;
    private List<NetNearCircleCommentInfo> CommentInfos;

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public void setPosDesc(String posDesc) {
        PosDesc = posDesc;
    }

    public NetNearCircleInfo() {
    }

    public NetNearCircleInfo(int contentType) {
        ContentType = contentType;
    }

    public boolean isLiked() {
        return IsLiked;
    }

    public void setLiked(boolean liked) {
        IsLiked = liked;
    }

    public List<NetNearCircleLikeInfo> getLikeInfos() {
        return LikeInfos;
    }

    public void setLikeInfos(List<NetNearCircleLikeInfo> likeInfos) {
        LikeInfos = likeInfos;
    }

    public List<NetNearCircleCommentInfo> getCommentInfos() {
        return CommentInfos;
    }

    public void setCommentInfos(List<NetNearCircleCommentInfo> commentInfos) {
        CommentInfos = commentInfos;
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

    public long getNearCircleId() {
        return NearCircleId;
    }

    public void setNearCircleId(long nearCircleId) {
        NearCircleId = nearCircleId;
    }

    public long getPublishUserId() {
        return PublishUserId;
    }

    public void setPublishUserId(long publishUserId) {
        PublishUserId = publishUserId;
    }

    public int getContentType() {
        return ContentType;
    }

    public void setContentType(int contentType) {
        ContentType = contentType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
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

    public Date getPublishTime() {
        return PublishTime;
    }

    public void setPublishTime(Date publishTime) {
        PublishTime = publishTime;
    }

    public String getPosDesc() {
        return PosDesc;
    }

    public List<NetImageInfo> getImages() {
        return Images;
    }

    public void setImages(List<NetImageInfo> images) {
        Images = images;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return true;
        if (!(obj instanceof NetNearCircleInfo))
            return true;

        NetNearCircleInfo newObj = (NetNearCircleInfo) obj;
        if (newObj.getNearCircleId() == this.getNearCircleId()) {
            return true;
        }
        return false;
    }
}
