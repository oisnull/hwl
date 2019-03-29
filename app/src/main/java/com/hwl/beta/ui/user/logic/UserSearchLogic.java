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
    public Observable<List<UserSearchInfo>> searchUsers(String key) {
//        if (StringUtils.isBlank(key)) {
//            callback.error("Search key is empty");
//            return;
//        }
      return UserService.searchUser(key)
				.map(new Function<SearchUserResponse,List<UserSearchInfo>>(){
					@Override
                    public List<UserSearchInfo> apply(SearchUserResponse response) throws Exception {
						return response.getUserInfos();
                    }
				});
    }
}
