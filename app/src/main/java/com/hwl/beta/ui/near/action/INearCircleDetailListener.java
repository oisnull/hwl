package com.hwl.beta.ui.near.action;

import android.view.View;

/**
 * Created by Administrator on 2018/4/14.
 */

public interface INearCircleDetailListener extends INearCircleCommentItemListener,
        INearCircleLikeItemListener {

    void onItemViewClick(View view);

    void onUserHeadClick();

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
