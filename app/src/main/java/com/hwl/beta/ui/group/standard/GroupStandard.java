package com.hwl.beta.ui.group.standard;

import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.ui.common.DefaultCallback;

import java.util.List;

public interface GroupStandard {
    List<GroupInfo> getLocalGroups();

    void loadServerGroups(List<GroupInfo> localGroups, DefaultCallback<List<GroupInfo>, String>
            callback);
}
