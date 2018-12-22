package com.hwl.beta.ui.group.standard;

import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.ui.common.DefaultCallback;

import java.util.List;

public interface GroupAddStandard {

    List<Friend> getLocalFriends();

    void addUserToGroup(List<Friend> selectUsers,String groupGuid, DefaultCallback<Boolean, String>
            callback);

    void createGroup(List<Friend> selectUsers, DefaultCallback<Boolean, String>
            callback);
}
