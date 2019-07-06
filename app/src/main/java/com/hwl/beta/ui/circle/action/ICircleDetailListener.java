package com.hwl.beta.ui.circle.action;

import android.view.View;

/**
 * Created by Administrator on 2018/4/14.
 */

public interface ICircleDetailListener extends ICircleCommentItemListener, ICircleLikeItemListener {

//    void onItemViewClick(View view);
//
//    void onMyUserHeadClick();

    void onUserHeadClick();

//    void onLikeUserHeadClick(CircleLike likeInfo);

//    void onCommentUserClick(CircleComment comment);
//
//    void onReplyUserClick(CircleComment comment);
//
//    void onCommentContentClick(CircleComment comment);

    void onContentClick();

    void onMoreActionClick(View view);

    void onMoreCommentClick();

    void onDeleteClick();

    void onPublishClick();

    void onImageClick(int position);
}
