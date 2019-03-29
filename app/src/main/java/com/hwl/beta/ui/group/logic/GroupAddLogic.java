package com.hwl.beta.ui.group.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.group.GroupService;
import com.hwl.beta.net.group.body.AddGroupResponse;
import com.hwl.beta.net.group.body.AddGroupUsersResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.FriendComparator;
import com.hwl.beta.ui.common.rxext.RXDefaultObserver;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.beta.ui.convert.DBGroupAction;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.ui.group.standard.GroupAddStandard;
import com.hwl.beta.ui.immsg.IMClientEntry;
import com.hwl.beta.ui.immsg.IMDefaultSendOperateListener;
import com.hwl.imcore.improto.ImUserContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupAddLogic implements GroupAddStandard {
    private boolean isRunning = false;

    @Override
    public List<Friend> getLocalFriends() {
        List<Friend> users = DaoUtils.getFriendManagerInstance().getAllFriends();
        if (users == null) {
            users = new ArrayList<>();
        } else {
            FriendComparator pinyinComparator = new FriendComparator();
            Collections.sort(users, pinyinComparator);
        }

        return users;
    }

    @Override
    public void addUserToGroup(final List<Friend> selectUsers, final String groupGuid,
                               final DefaultCallback<Boolean, String> callback) {
        if (isRunning) return;
        isRunning = true;

        if (selectUsers == null || selectUsers.size() <= 0) {
            isRunning = false;
            callback.error("请选择加入的用户");
            return;
        }

        final GroupInfo groupInfo = DaoUtils.getGroupInfoManagerInstance().get(groupGuid);
        if (groupInfo == null) {
            isRunning = false;
            callback.error("加入的群组信息错误");
            return;
        }

        GroupService.addGroupUsers(groupInfo.getGroupGuid(), getGroupUserIds(selectUsers))
                .subscribe(new RXDefaultObserver<AddGroupUsersResponse>() {
                    @Override
                    protected void onSuccess(AddGroupUsersResponse response) {
                        isRunning = false;
                        if (response.getStatus() == NetConstant.RESULT_SUCCESS) {

                            IMClientEntry.sendGroupUserAddMessage(groupInfo.getGroupGuid(),
                                    groupInfo.getGroupName(),
                                    getImGroupUsers(selectUsers),
                                    new IMDefaultSendOperateListener("AddUserToGroupMessage"));

                            List<GroupUserInfo> userInfos = DBGroupAction
                                    .convertToGroupUserInfos(groupInfo.getGroupGuid(),
                                            selectUsers);
                            DaoUtils.getGroupUserInfoManagerInstance().addListAsync(userInfos);
                            EventBusUtil.sendGroupUsersAddEvent(userInfos);

                            callback.success(true);
                        } else {
                            onError("加入用户到群组失败");
                        }
                    }

                    @Override
                    protected void onError(String resultMessage) {
                        isRunning = false;
                        super.onError(resultMessage);
                        callback.error(resultMessage);
                    }
                });
    }

    @Override
    public void createGroup(final List<Friend> selectUsers, final DefaultCallback<Boolean, String>
            callback) {
        if (isRunning) return;
        isRunning = true;

        if (selectUsers == null || selectUsers.size() <= 0) {
            callback.error("请选择群聊的用户");
            isRunning = false;
            return;
        }

        this.addCurrentUser(selectUsers);

        final GroupInfo groupInfo = DBGroupAction.convertToGroupInfo("", buildGroupName
                (selectUsers), "", UserSP.getUserId(), selectUsers.size(), getGroupUserImages
                (selectUsers), null, false);

        GroupService.addGroup(groupInfo.getGroupName(), getGroupUserIds(selectUsers))
                .subscribe(new RXDefaultObserver<AddGroupResponse>() {
                    @Override
                    protected void onSuccess(AddGroupResponse response) {
                        isRunning = false;
                        if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
                            groupInfo.setGroupGuid(response.getGroupGuid());
                            groupInfo.setBuildTime(response.getBuildTime());

                            IMClientEntry.sendGroupCreateMessage(groupInfo.getGroupGuid(),
                                    groupInfo.getGroupName(),
                                    getImGroupUsers(selectUsers),
                                    new IMDefaultSendOperateListener("CreateGroupMessage"));

                            DaoUtils.getGroupInfoManagerInstance().add(groupInfo);
                            DaoUtils.getGroupUserInfoManagerInstance().addListAsync(DBGroupAction
                                    .convertToGroupUserInfos(groupInfo.getGroupGuid(),
                                            selectUsers));
                            EventBusUtil.sendGroupAddEvent(groupInfo);

                            callback.success(true);
                        } else {
                            onError("创建群组失败");
                        }
                    }

                    @Override
                    protected void onError(String resultMessage) {
                        super.onError(resultMessage);
                        isRunning = false;
                        callback.error(resultMessage);
                    }

                    @Override
                    protected void onRelogin() {
                        isRunning = false;
                        callback.relogin();
                    }
                });
    }

    private void addCurrentUser(List<Friend> selectUsers) {
        Friend friend = DBFriendAction.convertToFriendInfo(UserSP.getUserId(), UserSP.getUserName
                (), UserSP.getUserHeadImage());
        selectUsers.add(friend);
    }

    private List<ImUserContent> getImGroupUsers(List<Friend> selectUsers) {
        List<ImUserContent> users = new ArrayList<>(selectUsers.size());
        for (int i = 0; i < selectUsers.size(); i++) {
            users.add(ImUserContent.newBuilder().setUserId(selectUsers.get(i).getId())
                    .setUserName(selectUsers.get(i).getName())
                    .setUserImage(selectUsers.get(i).getHeadImage())
                    .build());
        }
        return users;
    }

    private String buildGroupName(List<Friend> selectUsers) {
        String groupName = "";
        for (int i = 0; i < selectUsers.size(); i++) {
            if (i <= 5) {
                groupName += selectUsers.get(i).getName() + ",";
            } else {
                groupName += "...";
                break;
            }
        }

        if (groupName.endsWith(",")) {
            groupName = groupName.substring(0, groupName.length() - 1);
        }

        return groupName;
    }

    private List<Long> getGroupUserIds(List<Friend> selectUsers) {
        List<Long> userIds = new ArrayList<>(selectUsers.size());
        for (int i = 0; i < selectUsers.size(); i++) {
            userIds.add(selectUsers.get(i).getId());
        }
        return userIds;
    }

    private List<String> getGroupUserImages(List<Friend> selectUsers) {
        List<String> images = new ArrayList<>(9);
        for (int i = 0; i < selectUsers.size(); i++) {
            if (i > 8) break;
            images.add(selectUsers.get(i).getHeadImage());
        }
        return images;
    }
}
