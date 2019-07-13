package com.hwl.beta.ui.circle.action;

import android.view.View;

import com.hwl.beta.db.entity.CircleComment;

/**
 * Created by Administrator on 2018/4/15.
 */

public interface ICircleCommentItemListener {
    void onCommentUserClick(CircleComment comment);

    void onReplyUserClick(CircleComment comment);

    void onCommentContentClick(CircleComment comment);

    boolean onCommentLongClick(View view, CircleComment comment);
}
