package com.hwl.beta.ui.user.action;

import android.view.View;

import com.hwl.beta.db.entity.FriendRequest;

/**
 * Created by Administrator on 2018/4/1.
 */

public interface INewFriendItemListener {
    void onHeadImageClick(FriendRequest friendRequest);

    void onRemarkClick(String remark);

    void onCancelClick(View view, FriendRequest friendRequest);

    void onAddClick(View view, FriendRequest friendRequest);
}
