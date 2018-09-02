package com.hwl.beta.db.ext;

import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleImage;
import com.hwl.beta.db.entity.NearCircleLike;
import com.hwl.beta.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/11.
 */

public class NearCircleExt implements Serializable {
    private NearCircle info;
    private List<NearCircleImage> images;
    private List<NearCircleComment> comments;
    private List<NearCircleLike> likes;

    public NearCircleExt() {
    }

    public NearCircleExt(long nearCircleId) {
        this.info = new NearCircle();
        this.info.setNearCircleId(nearCircleId);
    }

    public NearCircleExt(int contentType) {
        this.info = new NearCircle();
        this.info.setContentType(contentType);
    }

    public NearCircleExt(NearCircle info) {
        this.info = info;
    }

    public NearCircleExt(NearCircle info, List<NearCircleImage> images, List<NearCircleComment> comments, List<NearCircleLike> likes) {
        this(info);
        this.images = images;
        this.comments = comments;
        this.likes = likes;
    }

    public NearCircle getInfo() {
        return info;
    }

    public void setInfo(NearCircle info) {
        this.info = info;
    }

    public List<NearCircleImage> getImages() {
        return images;
    }

    public void setImages(List<NearCircleImage> images) {
        this.images = images;
    }

    public List<NearCircleComment> getComments() {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        return comments;
    }

    public void setComments(List<NearCircleComment> comments) {
        this.comments = comments;
    }

    public List<NearCircleLike> getLikes() {
        if (likes == null) {
            likes = new ArrayList<>();
        }
        return likes;
    }

    public void setLikes(List<NearCircleLike> likes) {
        this.likes = likes;
    }

    public String getNearCircleMessageContent() {
        if (this.info != null && this.info.getNearCircleId() > 0) {
            if (StringUtils.isNotBlank(this.info.getContent())) {
                return this.info.getContent();
            }
//            if (this.images != null && this.images.size() > 0) {
//                return this.images.get(0).getImageUrl();
//            }
        }
        return "";
    }
}
