package com.hwl.beta.ui.user.standard;

import com.hwl.beta.net.user.UserSearchInfo;
import com.hwl.beta.ui.common.DefaultCallback;

import java.util.List;

public interface UserSearchStandard {
    void searchUsers(String key, DefaultCallback<List<UserSearchInfo>, String> callback);
}
