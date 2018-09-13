package com.hwl.beta.ui.convert;

import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleImage;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.net.circle.NetCircleCommentInfo;
import com.hwl.beta.net.circle.NetCircleInfo;
import com.hwl.beta.net.circle.NetCircleLikeInfo;
import com.hwl.beta.net.near.NetImageInfo;
import com.hwl.beta.ui.widget.MultiImageView;

import java.util.ArrayList;
import java.util.List;

public class DBCircleAction {
    public static Circle convertToCircleInfo(NetCircleInfo info) {
        if (info == null) return null;
        Circle model = new Circle();
        model.setCircleId(info.getCircleId());
        model.setPublishUserId(info.getPublishUserId());
        model.setPublishUserName(info.getPublishUserName());
        model.setPublishUserImage(info.getPublishUserImage());
        model.setFromPosDesc(info.getPosDesc());
        model.setPublishTime(info.getPublishTime());
        model.setUpdateTime(info.getUpdateTime());
        model.setLikeCount(info.getLikeCount());
        model.setContentType(info.getContentType());
        model.setContent(info.getCircleContent());
        model.setCommentCount(info.getCommentCount());
        model.setLinkImage(info.getLinkImage());
        model.setLinkUrl(info.getLinkUrl());
        model.setLinkTitle(info.getLinkTitle());
        model.setIsLiked(info.isLiked());
        return model;
    }

    public static List<CircleImage> convertToCircleImageInfos(long circleId, int publishUserId, List<NetImageInfo> images) {
        if (circleId <= 0 || images == null || images.size() <= 0) return null;
        List<CircleImage> circleImages = new ArrayList<>(images.size());
        CircleImage imageModel = null;
        for (int i = 0; i < images.size(); i++) {
            imageModel = new CircleImage();
            imageModel.setCircleId(circleId);
            imageModel.setPostUserId(publishUserId);
            imageModel.setImageUrl(images.get(i).getUrl());
            imageModel.setImageHeight(images.get(i).getHeight());
            imageModel.setImageWidth(images.get(i).getWidth());
            circleImages.add(imageModel);
        }
        return circleImages;
    }

    public static List<CircleComment> convertToCircleCommentInfos(List<NetCircleCommentInfo> comments) {
        if (comments == null || comments.size() <= 0) return null;
        List<CircleComment> circleComments = new ArrayList<>(comments.size());
        for (int i = 0; i < comments.size(); i++) {
            circleComments.add(convertToCircleCommentInfo(comments.get(i)));
        }
        return circleComments;
    }

    public static CircleComment convertToCircleCommentInfo(NetCircleCommentInfo comment) {
        if (comment == null) return null;
        CircleComment commentModel = new CircleComment();
        commentModel.setCircleId(comment.getCircleId());
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

    public static List<CircleLike> convertToCircleLikeInfos(List<NetCircleLikeInfo> likes) {
        if (likes == null || likes.size() <= 0) return null;
        List<CircleLike> circleLikes = new ArrayList<>(likes.size());
        CircleLike likeModel = null;
        for (int i = 0; i < likes.size(); i++) {
            likeModel = new CircleLike();
            likeModel.setCircleId(likes.get(i).getCircleId());
            likeModel.setLikeUserId(likes.get(i).getLikeUserId());
            likeModel.setLikeUserImage(likes.get(i).getLikeUserImage());
            likeModel.setLikeUserName(likes.get(i).getLikeUserName());
            likeModel.setLikeTime(likes.get(i).getLikeTime());
            circleLikes.add(likeModel);
        }
        return circleLikes;
    }

    public static List<MultiImageView.ImageBean> convertToMultiImages(List<CircleImage> images) {
        if (images == null || images.size() <= 0) return null;
        List<MultiImageView.ImageBean> imageBeans = new ArrayList<>(images.size());
        MultiImageView.ImageBean imageModel = null;
        for (int i = 0; i < images.size(); i++) {
            imageBeans.add(new MultiImageView.ImageBean(images.get(i).getImageWidth(), images.get(i).getImageHeight(), images.get(i).getImageUrl()));
        }
        return imageBeans;
    }
}
