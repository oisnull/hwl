package com.hwl.beta.ui.user.standard;

import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.im.common.DefaultConsumer;

import java.util.List;

public interface FriendsStandard {

    void loadLocalFriends(DefaultConsumer<List<Friend>> succCallback, DefaultConsumer<Throwable>
            errorCallback);

    void loadServerFriends(List<Friend> friends,DefaultCallback<List<Friend>, String> callback);

}
