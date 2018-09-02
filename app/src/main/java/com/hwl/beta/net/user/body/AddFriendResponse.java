package com.hwl.beta.net.user.body;

import com.hwl.beta.net.user.NetUserFriendInfo;

/**
 * Created by Administrator on 2018/1/27.
 */

public class AddFriendResponse {
    private int Status;
    private NetUserFriendInfo FriendInfo;

    public int getStatus() {
        return Status;
    }

    public NetUserFriendInfo getFriendInfo() {
        return FriendInfo;
    }
}