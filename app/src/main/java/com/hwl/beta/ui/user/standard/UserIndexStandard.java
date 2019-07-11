package com.hwl.beta.ui.user.standard;

import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.ui.user.bean.UserIndexBean;

import java.util.List;

import io.reactivex.Observable;

public interface UserIndexStandard {

    UserIndexBean getUserInfo(long userId, String userName, String userImage);

    Observable<Friend> loadServerUserInfo(long userId, String updateTime);

    Observable deleteFriend(long userId);

    Observable<List<Circle>> loadLocalCircleInfos(long userId);

    Observable<List<Circle>> loadServerCircleInfos(long userId, final List<Circle> localInfos);
}
