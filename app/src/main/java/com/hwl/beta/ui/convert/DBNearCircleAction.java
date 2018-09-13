package com.hwl.beta.ui.convert;

import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleImage;
import com.hwl.beta.db.entity.NearCircleLike;
import com.hwl.beta.net.near.NetImageInfo;
import com.hwl.beta.net.near.NetNearCircleCommentInfo;
import com.hwl.beta.net.near.NetNearCircleInfo;
import com.hwl.beta.net.near.NetNearCircleLikeInfo;
import com.hwl.beta.ui.widget.MultiImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public class DBNearCircleAction {

    public static NearCircle convertToNearCircleInfo(NetNearCircleInfo info) {
        if (info == null) return null;
        NearCircle model = new NearCircle();
        model.setNearCircleId(info.getNearCircleId());
        model.setPublishUserId(info.getPublishUserId());
        model.setPublishUserName(info.getPublishUserName());
        model.setPublishUserImage(info.getPublishUserImage());
        model.setFromPosDesc(info.getPosDesc());
        model.setPublishTime(info.getPublishTime());
        model.setUpdateTime(info.getUpdateTime());
        model.setLikeCount(info.getLikeCount());
        model.setContentType(info.getContentType());
        model.setContent(info.getContent());
        model.setCommentCount(info.getCommentCount());
        model.setLinkImage(info.getLinkImage());
        model.setLinkUrl(info.getLinkUrl());
        model.setLinkTitle(info.getLinkTitle());
        model.setIsLiked(info.isLiked());
        return model;
    }

    public static List<NearCircleImage> convertToNearCircleImageInfos(long nearCircleId, long publishUserId, List<NetImageInfo> images) {
        if (nearCircleId <= 0 || images == null || images.size() <= 0) return null;
        List<NearCircleImage> circleImages = new ArrayList<>(images.size());
        NearCircleImage imageModel;
        for (int i = 0; i < images.size(); i++) {
            imageModel = new NearCircleImage();
            imageModel.setNearCircleId(nearCircleId);
            imageModel.setPostUserId(publishUserId);
            imageModel.setImageUrl(images.get(i).getUrl());
            imageModel.setImageHeight(images.get(i).getHeight());
            imageModel.setImageWidth(images.get(i).getWidth());
            circleImages.add(imageModel);
        }
        return circleImages;
    }

    public static List<NearCircleComment> convertToNearCircleCommentInfos(List<NetNearCircleCommentInfo> comments) {
        if (comments == null || comments.size() <= 0) return null;
        List<NearCircleComment> circleComments = new ArrayList<>(comments.size());
        for (int i = 0; i < comments.size(); i++) {
            circleComments.add(convertToNearCircleCommentInfo(comments.get(i)));
        }
        return circleComments;
    }

    public static NearCircleComment convertToNearCircleCommentInfo(NetNearCircleCommentInfo comment) {
        if (comment == null) return null;
        NearCircleComment commentModel = new NearCircleComment();
        commentModel.setNearCircleId(comment.getNearCircleId());
        commentModel.setCommentId(comment.getCommentId());
        commentModel.setCommentTime(comment.getCommentTime());
        commentModel.setCommentUserId(comment.getCommentUserId());
        commentModel.setCommentUserImage(comment.getCommentUserImage());
        commentModel.setCommentUserName(comment.getCommentUserName());
        commentModel.setContent(comment.getContent());
        commentModel.setReplyUserId(comment.getReplyUserId());
        commentModel.setReplyUserImage(comment.getReplyUserImage());
        commentModel.setReplyUserName(comment.getReplyUserName());
        return commentModel;
    }

    public static List<NearCircleLike> convertToNearCircleLikeInfos(List<NetNearCircleLikeInfo> likes) {
        if (likes == null || likes.size() <= 0) return null;
        List<NearCircleLike> circleLikes = new ArrayList<>(likes.size());
        NearCircleLike likeModel ;
        for (int i = 0; i < likes.size(); i++) {
            likeModel = new NearCircleLike();
            likeModel.setNearCircleId(likes.get(i).getNearCircleId());
            likeModel.setLikeUserId(likes.get(i).getLikeUserId());
            likeModel.setLikeUserImage(likes.get(i).getLikeUserImage());
            likeModel.setLikeUserName(likes.get(i).getLikeUserName());
            likeModel.setLikeTime(likes.get(i).getLikeTime());
            circleLikes.add(likeModel);
        }
        return circleLikes;
    }

    public static List<MultiImageView.ImageBean> convertToMultiImages(List<NearCircleImage> images) {
        if (images == null || images.size() <= 0) return null;
        List<MultiImageView.ImageBean> imageBeans = new ArrayList<>(images.size());
        MultiImageView.ImageBean imageModel = null;
        for (int i = 0; i < images.size(); i++) {
            imageBeans.add(new MultiImageView.ImageBean(images.get(i).getImageWidth(), images.get(i).getImageHeight(), images.get(i).getImageUrl()));
        }
        return imageBeans;
    }
}
