package com.hwl.beta.ui.near.action;

import android.view.View;

import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleImage;
import com.hwl.beta.db.entity.NearCircleLike;

import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public interface INearCircleItemListener extends INearLikeItemListener,INearCircleCommentItemListener {

    //void onItemViewClick(View view);

    void onUserHeadClick(NearCircle info);

    void onContentClick(NearCircle info);

    void onMoreActionClick(View view, int position,NearCircle info);

    void onMoreCommentClick(NearCircle info);

    void onDeleteClick(int position, NearCircle info);

    void onPublishClick();

    void onImageClick(int position, List<NearCircleImage> images);

//    void onVideoClick(int position);
//
//    void onLinkClick(int position);

}
