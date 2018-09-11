package com.hwl.beta.ui.user.action;

import android.view.View;

import com.hwl.beta.net.user.UserSearchInfo;

/**
 * Created by Administrator on 2018/3/31.
 */

public interface IUserSearchItemListener {
    void onHeadImageClick(UserSearchInfo user);

    void onAddClick(View view, UserSearchInfo user);
}
