package com.hwl.beta.ui.user.action;

import com.hwl.beta.db.entity.Friend;

/**
 * Created by Administrator on 2018/4/1.
 */

public interface IFriendItemListener {
    void onHeadImageClick(Friend friend);

    void onNameClick(Friend friend);
}
