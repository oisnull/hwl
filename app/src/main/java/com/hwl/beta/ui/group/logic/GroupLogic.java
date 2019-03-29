package com.hwl.beta.ui.group.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.net.group.GroupService;
import com.hwl.beta.net.group.body.GetGroupsResponse;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.common.rxext.RXDefaultObserver;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.beta.ui.convert.DBGroupAction;
import com.hwl.beta.ui.group.standard.GroupStandard;

import java.util.ArrayList;
import java.util.List;

public class GroupLogic implements GroupStandard {
    @Override
    public List<GroupInfo> getLocalGroups() {
        List<GroupInfo> groupInfos = DaoUtils.getGroupInfoManagerInstance().getAll();
        if (groupInfos == null) {
            groupInfos = new ArrayList<>();
        }
        groupInfos.add(0, new GroupInfo());

        return groupInfos;
    }

    //系统组只存在于本地，服务器上不进行存储
    @Override
    public void loadServerGroups(final List<GroupInfo> localGroups, final
    DefaultCallback<List<GroupInfo>,
            String> callback) {

        GroupService.getGroups()
                .subscribe(new RXDefaultObserver<GetGroupsResponse>(false) {
                    @Override
                    protected void onSuccess(GetGroupsResponse response) {
                        List<GroupInfo> groups = null;
                        if (response.getGroupInfos() != null && response.getGroupInfos().size() >
                                0) {
                            groups = DBGroupAction.convertToGroupInfos(response
                                    .getGroupInfos());
                            groups.removeAll(localGroups);//取差集
                            DaoUtils.getGroupInfoManagerInstance().addList(groups);
                        }

                        addLocalUserInfos(response);

                        callback.success(groups);
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

    private void addLocalUserInfos(GetGroupsResponse response) {
        if (response.getGroupInfos() != null && response.getGroupInfos().size() > 0) {

            //set group users in local
            //set users in local
            List<GroupUserInfo> groupUserInfos = new ArrayList<>();
            List<Friend> users = new ArrayList<>();
            for (int i = 0; i < response.getGroupInfos().size(); i++) {
                groupUserInfos.addAll(DBGroupAction.convertToGroupUserInfos(response
                        .getGroupInfos().get(i)
                        .getGroupUsers()));
                users.addAll(DBFriendAction
                        .convertGroupUserToFriendInfos(response.getGroupInfos()
                                .get(i)
                                .getGroupUsers()));
            }

            DaoUtils.getGroupUserInfoManagerInstance().addListAsync(groupUserInfos);
            DaoUtils.getFriendManagerInstance().addListAsync(users);
        }
    }
}
