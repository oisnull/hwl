package com.hwl.beta.ui.user.logic;

import com.hwl.beta.net.user.UserSearchInfo;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.SearchUserResponse;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.user.standard.UserSearchStandard;
import com.hwl.beta.utils.StringUtils;
import com.hwl.im.common.DefaultConsumer;

import java.util.List;

public class UserSearchLogic implements UserSearchStandard {
    @Override
    public void searchUsers(String key, final DefaultConsumer<List<UserSearchInfo>> succCallback,
                            final DefaultConsumer<String> errorCallback) {
        if (StringUtils.isBlank(key)) {
            errorCallback.accept("Search key is empty");
            return;
        }
        UserService.searchUser(key)
                .subscribe(new NetDefaultObserver<SearchUserResponse>() {
                    @Override
                    protected void onSuccess(SearchUserResponse response) {
                        succCallback.accept(response.getUserInfos());
                    }

                    @Override
                    protected void onError(String resultMessage) {
                        super.onError(resultMessage);
                        errorCallback.accept(resultMessage);
                    }
                });
    }
}
