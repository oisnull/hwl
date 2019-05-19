package com.hwl.beta.ui.circle.action;

import com.hwl.beta.db.entity.CircleComment;

/**
 * Created by Administrator on 2018/4/15.
 */

public interface ICircleCommentItemListener {
    void onCommentUserClick(CircleComment comment);

    void onReplyUserClick(CircleComment comment);

    void onContentClick(CircleComment comment);
}
