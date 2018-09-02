package com.hwl.beta.db.ext;

import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleImage;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/11.
 */

public class CircleExt implements Serializable {

    public static final int CircleHeadItem = 0;
    public static final int CircleNullItem = 1;
    public static final int CircleIndexItem = 2;
    public static final int CircleMsgcountItem = 3;

    private int circleItemType;
    private long viewUserId;
    private String viewUserName;
    private String viewUserImage;
    private String viewUserLifeNotes;
    private String viewCircleBackImage;
    private Circle info;
    private List<CircleImage> images;
    private List<CircleComment> comments;
    private List<CircleLike> likes;

    public CircleExt(int circleItemType) {
        this.circleItemType = circleItemType;
    }

    public CircleExt(long circleId) {
        this.info = new Circle();
        this.info.setCircleId(circleId);
    }

    public CircleExt(int circleItemType, long viewUserId, String viewUserName, String viewUserImage, String viewCircleBackImage, String viewUserLifeNotes) {
        this.circleItemType = circleItemType;
        this.viewUserId = viewUserId;
        this.viewUserName = viewUserName;
        this.viewUserImage = viewUserImage;
        this.viewCircleBackImage = viewCircleBackImage;
        this.viewUserLifeNotes = viewUserLifeNotes;
    }

    public CircleExt(int circleItemType, Circle info, List<CircleImage> images, List<CircleComment> comments, List<CircleLike> likes) {
        this(circleItemType);
        this.images = images;
        this.info = info;
        this.comments = comments;
        this.likes = likes;
    }

    public CircleExt(Circle info, List<CircleImage> images, List<CircleComment> comments, List<CircleLike> likes) {
        this(CircleIndexItem, info, images, comments, likes);
    }

    public String getViewUserLifeNotes() {
        return viewUserLifeNotes;
    }

    public void setViewUserLifeNotes(String viewUserLifeNotes) {
        this.viewUserLifeNotes = viewUserLifeNotes;
    }

    public String getViewCircleBackImage() {
        return viewCircleBackImage;
    }

    public void setViewCircleBackImage(String viewCircleBackImage) {
        this.viewCircleBackImage = viewCircleBackImage;
    }

    public long getViewUserId() {
        return viewUserId;
    }

    public void setViewUserId(long viewUserId) {
        this.viewUserId = viewUserId;
    }

    public String getViewUserName() {
        return viewUserName;
    }

    public void setViewUserName(String viewUserName) {
        this.viewUserName = viewUserName;
    }

    public String getViewUserImage() {
        return viewUserImage;
    }

    public void setViewUserImage(String viewUserImage) {
        this.viewUserImage = viewUserImage;
    }

    public int getCircleItemType() {
        return circleItemType;
    }

    public void setCircleItemType(int circleItemType) {
        this.circleItemType = circleItemType;
    }

    public Circle getInfo() {
        return info;
    }

    public void setInfo(Circle info) {
        this.info = info;
    }

    public List<CircleImage> getImages() {
        return images;
    }

    public void setImages(List<CircleImage> images) {
        this.images = images;
    }

    public List<CircleComment> getComments() {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        return comments;
    }

    public void setComments(List<CircleComment> comments) {
        this.comments = comments;
    }

    public List<CircleLike> getLikes() {
        if (likes == null) {
            likes = new ArrayList<>();
        }
        return likes;
    }

    public void setLikes(List<CircleLike> likes) {
        this.likes = likes;
    }

    public String getCircleMessageContent() {
        if (this.info != null && this.info.getCircleId() > 0) {
            if (StringUtils.isNotBlank(this.info.getContent())) {
                return this.info.getContent();
            }
        }
        return "";
    }
}
