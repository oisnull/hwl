package com.hwl.beta.ui.near.action;

import android.view.View;

import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleLike;

import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public interface INearCircleDetailListener {

    void onItemViewClick(View view);

    void onUserHeadClick();

    void onLikeUserHeadClick(NearCircleLike likeInfo);

    void onCommentUserClick(NearCircleComment comment);

    void onReplyUserClick(NearCircleComment comment);

    void onCommentContentClick(NearCircleComment comment);

    void onContentClick();

    void onMoreActionClick(View view);

    void onMoreCommentClick();

    void onDeleteClick();

    void onPublishClick();

    void onImageClick(int position);

//    void onVideoClick(int position);
//
//    void onLinkClick(int position);

}
