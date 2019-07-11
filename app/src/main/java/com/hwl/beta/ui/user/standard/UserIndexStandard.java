package com.hwl.beta.ui.user.standard;

import com.hwl.beta.ui.user.bean.UserIndexBean;

public interface UserIndexStandard {

    UserIndexBean getUserInfo(long userId, String userName, String userImage);

    Observable<Friend> loadServerUserInfo(long userId,String updateTime);

    Observable deleteFriend(long userId);

	Observable<List<Circle>> loadLocalCircleInfos(long userId);

	Observable<List<Circle>> loadServerCircleInfos(long userId,final List<Circle> localInfos);
}
