package com.hwl.beta.ui.user.standard;

import com.hwl.beta.db.entity.FriendRequest;

import java.util.List;

import io.reactivex.Observable;

public interface NewFriendStandard {
    List<FriendRequest> getFriendRequestInfos();
    Observable addFriend(FriendRequest friendRequest);
    void deleteFriendRequest(FriendRequest info);
}
