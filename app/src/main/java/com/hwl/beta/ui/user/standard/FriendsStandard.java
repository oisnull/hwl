package com.hwl.beta.ui.user.standard;

import com.hwl.beta.db.entity.Friend;
import com.hwl.im.common.DefaultConsumer;

import java.util.List;

import io.reactivex.Observable;

public interface FriendsStandard {

    void loadLocalFriends(DefaultConsumer<List<Friend>> succCallback, DefaultConsumer<Throwable>
            errorCallback);

    Observable<List<Friend>> loadServerFriends(List<Friend> friends);

}
