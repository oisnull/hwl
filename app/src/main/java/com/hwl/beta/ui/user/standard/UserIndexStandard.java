package com.hwl.beta.ui.user.standard;

import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.user.bean.UserIndexBean;

public interface UserIndexStandard {

    UserIndexBean getUserInfo(long userId, String userName, String userImage);

    void loadServerUserInfo(long userId,String updateTime, DefaultCallback<UserIndexBean, String> callback);

    void deleteFriend(long userId, DefaultCallback callback);
}
