package com.hwl.beta.ui.group.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.group.GroupService;
import com.hwl.beta.net.group.body.AddGroupResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.FriendComparator;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.convert.DBGroupAction;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.ui.group.standard.GroupAddStandard;

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
    public void addUserToGroup(List<Friend> selectUsers, String groupGuid,
                               DefaultCallback<Boolean, String>
                                       callback) {
        if (selectUsers == null || selectUsers.size() <= 0) {
            callback.error("请选择加入的用户");
            return;
        }
        final GroupInfo groupInfo = DaoUtils.getGroupInfoManagerInstance().get(groupGuid);
        if (groupInfo == null) {
            callback.error("加入的群组信息错误");
            return;
        }

        //        GroupService.addGroupUsers(groupInfo.getGroupGuid(), userIds)
//                .subscribe(new NetDefaultObserver<AddGroupUsersResponse>() {
//                    @Override
//                    protected void onSuccess(AddGroupUsersResponse response) {
//                        LoadingDialog.hide();
//                        if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
//                            GroupActionMessageSend.sendAddGroupUsersMessage(groupInfo,
// finalMessageContent, userInfos).subscribe();
//                            finish();
//                        } else {
//                            onError("加入群组失败");
//                        }
//                        isRuning = false;
//                    }
//
//                    @Override
//                    protected void onError(String resultMessage) {
//                        super.onError(resultMessage);
//                        LoadingDialog.hide();
//                        isRuning = false;
//                    }
//
//                    @Override
//                    protected void onRelogin() {
//                        LoadingDialog.hide();
//                        isRuning = false;
//                        UITransfer.toReloginDialog(activity);
//                    }
//                });
    }

    @Override
    public void createGroup(List<Friend> selectUsers, final DefaultCallback<Boolean, String>
            callback) {
        if (isRunning) return;
        isRunning = true;

        if (selectUsers == null || selectUsers.size() <= 0) {
            callback.error("请选择群聊的用户");
            isRunning = false;
            return;
        }

        final GroupInfo groupInfo = DBGroupAction.convertToGroupInfo("", buildGroupName
                (selectUsers), "", UserSP.getUserId(), selectUsers.size(), getGroupUserImages
                (selectUsers), null, false);

        GroupService.addGroup(groupInfo.getGroupName(), getGroupUserIds(selectUsers))
                .subscribe(new NetDefaultObserver<AddGroupResponse>() {
                    @Override
                    protected void onSuccess(AddGroupResponse response) {
                        isRunning = false;
                        if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
                            callback.success(true);

                            groupInfo.setGroupGuid(response.getGroupGuid());
                            groupInfo.setBuildTime(response.getBuildTime());
                            DaoUtils.getGroupInfoManagerInstance().add(groupInfo);
                            EventBusUtil.sendGroupAddEvent(groupInfo);

                            //发送群创建的消息给群用户

//                            GroupActionMessageSend.sendCreateMessage(groupInfo.getGroupGuid(),
//                                    groupInfo.getGroupName(), groupInfo.getUserImages(), response
//                                            .getBuildTime(), groupInfo
//                                            .getGroupName() + " 加入聊天群", userInfos).subscribe();
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

    private String buildGroupName(List<Friend> selectUsers) {
        String groupName = UserSP.getUserName();
        for (int i = 0; i < selectUsers.size(); i++) {
            if (i <= 5) {
                groupName += "," + selectUsers.get(i).getName();
            } else {
                groupName += "...";
                break;
            }
        }

        return groupName;
    }

    private List<Long> getGroupUserIds(List<Friend> selectUsers) {
        List<Long> userIds = new ArrayList<>(selectUsers.size());
        userIds.add(UserSP.getUserId());
        for (int i = 0; i < selectUsers.size(); i++) {
            userIds.add(selectUsers.get(i).getId());
        }
        return userIds;
    }

    private List<String> getGroupUserImages(List<Friend> selectUsers) {
        List<String> images = new ArrayList<>(9);
        images.add(UserSP.getUserHeadImage());
        for (int i = 0; i < selectUsers.size(); i++) {
            if (i > 8) break;
            images.add(selectUsers.get(i).getHeadImage());
        }
        return images;
    }
}
