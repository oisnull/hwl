package com.hwl.beta.ui.user.standard;

import com.hwl.beta.net.user.UserSearchInfo;

import java.util.List;

public interface UserSearchStandard {
    Observable<List<UserSearchInfo>> searchUsers(String key);
}
