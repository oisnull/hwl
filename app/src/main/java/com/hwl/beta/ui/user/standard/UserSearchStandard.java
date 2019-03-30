package com.hwl.beta.ui.user.standard;

import com.hwl.beta.net.user.UserSearchInfo;

import java.util.List;

import io.reactivex.Observable;

public interface UserSearchStandard {
    Observable<List<UserSearchInfo>> searchUsers(String key);
}
