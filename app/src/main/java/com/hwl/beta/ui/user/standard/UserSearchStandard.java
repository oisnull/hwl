package com.hwl.beta.ui.user.standard;

import com.hwl.beta.net.user.UserSearchInfo;
import com.hwl.im.common.DefaultConsumer;

import java.util.List;

public interface UserSearchStandard {

    void searchUsers(String key, DefaultConsumer<List<UserSearchInfo>> succCallback, DefaultConsumer<String>
            errorCallback);

}
