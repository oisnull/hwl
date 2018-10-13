package com.hwl.beta.ui.user.logic;

import com.hwl.beta.net.user.UserSearchInfo;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.SearchUserResponse;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.user.standard.UserSearchStandard;
import com.hwl.beta.utils.StringUtils;

import java.util.List;

public class UserSearchLogic implements UserSearchStandard {

    @Override
    public void searchUsers(String key, final DefaultCallback<List<UserSearchInfo>, String>
            callback) {
        if (StringUtils.isBlank(key)) {
            callback.error("Search key is empty");
            return;
        }
        UserService.searchUser(key)
                .subscribe(new NetDefaultObserver<SearchUserResponse>() {
                    @Override
                    protected void onSuccess(SearchUserResponse response) {
                        callback.success(response.getUserInfos());
                    }

                    @Override
                    protected void onError(String resultMessage) {
                        super.onError(resultMessage);
                        callback.error(resultMessage);
                    }

                    @Override
                    protected void onRelogin() {
                        callback.relogin();
                    }
                });
    }
}
