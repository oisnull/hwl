package com.hwl.beta.net.user.body;

import com.hwl.beta.net.user.UserSearchInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/1/27.
 */

public class SearchUserResponse {
    private List<UserSearchInfo> UserInfos;

    public List<UserSearchInfo> getUserInfos() {
        return UserInfos;
    }
}
