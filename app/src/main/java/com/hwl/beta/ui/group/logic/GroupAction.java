package com.hwl.beta.ui.group.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.net.group.GroupService;
import com.hwl.beta.net.group.body.GetGroupAndUsersResponse;
import com.hwl.beta.ui.common.rxext.RXDefaultObserver;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.beta.ui.convert.DBGroupAction;
import com.hwl.beta.utils.StringUtils;

import java.util.Hashtable;
import java.util.List;

public class GroupAction {
    private static Hashtable<String, Boolean> statusContainer = null;

    private static void setStatus(String groupGuid, boolean isRunning) {
        if (StringUtils.isEmpty(groupGuid)) return;

        if (statusContainer == null) {
            statusContainer = new Hashtable<>();
        }

        if (isRunning) {
            statusContainer.put(groupGuid, isRunning);
        } else {
            statusContainer.remove(groupGuid);
        }

        if (statusContainer.size() <= 0) {
            statusContainer = null;
        }
    }

    private static boolean isRunning(String groupGuid) {
        if (StringUtils.isEmpty(groupGuid) || statusContainer == null) return false;

        if (statusContainer.contains(groupGuid)) {
            return statusContainer.get(groupGuid);
        }

        return false;
    }

    public static void loadServerGroupInfo(final String groupGuid) {
        if (isRunning(groupGuid) || StringUtils.isEmpty(groupGuid)) return;
        setStatus(groupGuid, true);

        GroupService.getGroupAndUsers(groupGuid)
                .subscribe(new RXDefaultObserver<GetGroupAndUsersResponse>() {

                    @Override
                    protected void onSuccess(GetGroupAndUsersResponse response) {
                        setStatus(groupGuid, false);
                        if (response.getGroupInfo() != null) {
                            GroupInfo groupInfo = DBGroupAction.convertToGroupInfo(
                                    response.getGroupInfo().getGroupGuid(),
                                    response.getGroupInfo().getGroupName(),
                                    response.getGroupInfo().getGroupNote(),
                                    response.getGroupInfo().getBuildUserId(),
                                    response.getGroupInfo().getGroupUserCount(),
                                    response.getGroupInfo().getGroupUserImages(),
                                    response.getGroupInfo().getBuildDate(),
                                    false,
                                    response.getGroupInfo().getGroupUsers() != null && response.getGroupInfo().getGroupUsers().size() > 0
                            );
                            List<GroupUserInfo> userInfos = DBGroupAction.convertToGroupUserInfos
                                    (response.getGroupInfo().getGroupUsers());
                            List<Friend> users = DBFriendAction
                                    .convertGroupUserToFriendInfos(response.getGroupInfo()
                                            .getGroupUsers());
                            DaoUtils.getGroupInfoManagerInstance().add(groupInfo);
                            DaoUtils.getGroupUserInfoManagerInstance().addListAsync(userInfos);
                            DaoUtils.getFriendManagerInstance().addListAsync(users);
                        }
                    }

                    @Override
                    protected void onError(String resultMessage) {
                        super.onError(resultMessage);
                        setStatus(groupGuid, false);
                    }
                });

    }
}
