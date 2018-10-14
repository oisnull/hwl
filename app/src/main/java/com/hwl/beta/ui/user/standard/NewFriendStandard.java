package com.hwl.beta.ui.user.standard;

import com.hwl.beta.db.entity.FriendRequest;
import com.hwl.beta.ui.common.DefaultCallback;

import java.util.List;

public interface NewFriendStandard {
    List<FriendRequest> getFriendRequestInfos();
    void addFriend(FriendRequest friendRequest,DefaultCallback<Boolean,String> callback);
    void deleteFriendRequest(FriendRequest info);
}
