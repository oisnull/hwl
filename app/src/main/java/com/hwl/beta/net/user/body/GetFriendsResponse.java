package com.hwl.beta.net.user.body;

import com.hwl.beta.net.user.NetUserFriendInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/2/15.
 */
public class GetFriendsResponse {

    private List<NetUserFriendInfo> UserFriendInfos;

    public List<NetUserFriendInfo> getUserFriendInfos() {
        return UserFriendInfos;
    }
}
