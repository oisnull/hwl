package com.hwl.beta.ui.user.logic;

import com.hwl.beta.net.user.UserSearchInfo;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.SearchUserResponse;
import com.hwl.beta.ui.user.standard.UserSearchStandard;
import com.hwl.beta.utils.StringUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class UserSearchLogic implements UserSearchStandard {

    @Override
    public Observable<List<UserSearchInfo>> searchUsers(String key) {
        if (StringUtils.isBlank(key)) {
            Observable.error(new Throwable("Search key is empty"));
        }
        return UserService.searchUser(key)
                .map(new Function<SearchUserResponse, List<UserSearchInfo>>() {
                    @Override
                    public List<UserSearchInfo> apply(SearchUserResponse response) {
                        return response.getUserInfos();
                    }
                });
    }
}
