package com.hwl.beta.ui.circle.action;

import android.view.View;

import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleLike;

import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public interface ICircleDetailListener {

    void onItemViewClick(View view);

    void onMyUserHeadClick();

    void onUserHeadClick();

    void onLikeUserHeadClick(CircleLike likeInfo);

    void onCommentUserClick(CircleComment comment);

    void onReplyUserClick(CircleComment comment);

    void onCommentContentClick(CircleComment comment);

    void onContentClick();

    void onMoreActionClick(View view);

    void onMoreCommentClick();

    void onDeleteClick(Circle info);

    void onPublishClick();

    void onImageClick(int position);
}
