package com.hwl.beta.ui.chat.standard;

import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.ui.common.DefaultCallback;

import java.util.List;

import io.reactivex.Observable;

public interface ChatGroupSettingStandard {
    GroupInfo getGroupInfo(String groupGuid);

    List<GroupUserInfo> getGroupUsers(GroupInfo group);

    Observable<List<GroupUserInfo>> loadGroupUsersFromServer(String groupGuid);

    void setShieldMessage(GroupInfo group);

    void searchMessage(String groupGuid, String key, DefaultCallback<Boolean, String> callback);

    void clearMessage(String groupGuid);

    void exitGroup(String groupGuid, DefaultCallback<Boolean, String> callback);

    void dismissGroup(String groupGuid, DefaultCallback<Boolean, String> callback);
}
