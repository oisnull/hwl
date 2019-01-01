package com.hwl.beta.ui.chat.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.group.GroupService;
import com.hwl.beta.net.group.body.DeleteGroupResponse;
import com.hwl.beta.net.group.body.DeleteGroupUserResponse;
import com.hwl.beta.net.group.body.GroupUsersResponse;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.ui.chat.standard.ChatGroupSettingStandard;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.ui.immsg.IMClientEntry;
import com.hwl.beta.ui.immsg.IMDefaultSendOperateListener;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class ChatGroupSettingLogic implements ChatGroupSettingStandard {

    @Override
    public GroupInfo getGroupInfo(String groupGuid) {
        return DaoUtils.getGroupInfoManagerInstance().get(groupGuid);
    }

    @Override
    public List<GroupUserInfo> getGroupUsers(String groupGuid, boolean isDismiss) {
        List<GroupUserInfo> users = DaoUtils.getGroupUserInfoManagerInstance().getUsers(groupGuid);

        if (isDismiss || groupGuid.equals(UserPosSP.getGroupGuid())) return users;
        GroupUserInfo groupUserInfo = new GroupUserInfo();
        groupUserInfo.setId((long) -1);
        users.add(users.size(), groupUserInfo);

        return users;
    }

    @Override
    public void loadGroupUserFromServer(String groupGuid, DefaultCallback<Boolean, String>
            callback) {

        GroupService.groupUsers(groupGuid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetDefaultObserver<GroupUsersResponse>() {
                    @Override
                    protected void onSuccess(GroupUsersResponse response) {
                        if (response.getGroupUserInfos() != null) {
                            //removeUserItem();
//                            List<GroupUserInfo> userInfos = DBGroupAction
// .convertToGroupUserInfos(response.getGroupUserInfos());
//                            DaoUtils.getGroupUserInfoManagerInstance().addListAsync(userInfos);
//                            users.addAll(userInfos);
//                            addUserItem();
//                            userAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void setShieldMessage(GroupInfo group) {
        DaoUtils.getGroupInfoManagerInstance().add(group);
        ChatRecordMessage recordMessage = DaoUtils.getChatRecordMessageManagerInstance()
                .getGroupRecord(group.getGroupGuid());
        if (recordMessage != null) {
            recordMessage.setShield(group.getIsShield());
            EventBusUtil.sendChatRecordMessageNoSortEvent(recordMessage);
        }
    }

    @Override
    public void searchMessage(String groupGuid, String key, DefaultCallback<Boolean, String>
            callback) {

    }

    @Override
    public void clearMessage(String groupGuid) {
        DaoUtils.getChatGroupMessageManagerInstance().deleteMessages(groupGuid);
        ChatRecordMessage recordMessage = DaoUtils.getChatRecordMessageManagerInstance()
                .deleteGroupRecord(groupGuid);
        if (recordMessage == null) return;

        EventBusUtil.sendChatRecordMessageClearEvent(recordMessage.getRecordId());
    }

    @Override
    public void exitGroup(final String groupGuid, final DefaultCallback<Boolean, String> callback) {
        GroupService.deleteGroupUser(groupGuid)
                .subscribe(new NetDefaultObserver<DeleteGroupUserResponse>() {
                    @Override
                    protected void onSuccess(DeleteGroupUserResponse response) {
                        if (response.getStatus() == NetConstant.RESULT_SUCCESS) {

                            IMClientEntry.sendGroupExitMessage(groupGuid, new
                                    IMDefaultSendOperateListener("ExitGroupMessage"));

                            DaoUtils.getGroupInfoManagerInstance().deleteGroupInfo(groupGuid);
                            DaoUtils.getGroupUserInfoManagerInstance().deleteGroupUserInfo
                                    (groupGuid);
                            DaoUtils.getChatGroupMessageManagerInstance().deleteMessages(groupGuid);
                            DaoUtils.getChatRecordMessageManagerInstance().deleteGroupRecord
                                    (groupGuid);

                            EventBusUtil.sendGroupExitEvent(groupGuid);
                            callback.success(true);
                        } else {
                            onError("退出失败");
                        }
                    }

                    @Override
                    protected void onError(String resultMessage) {
                        super.onError(resultMessage);
                        callback.error(resultMessage);
                    }
                });
    }

    @Override
    public void dismissGroup(final String groupGuid, final DefaultCallback<Boolean, String>
            callback) {
        IMClientEntry.sendGroupDismissMessage(groupGuid, new
                IMDefaultSendOperateListener("DismissGroupMessage") {
                    @Override
                    public void success1() {

                        GroupService.deleteGroup(groupGuid)
                                .subscribe(new NetDefaultObserver<DeleteGroupResponse>() {
                                    @Override
                                    protected void onSuccess(DeleteGroupResponse response) {
                                        if (response.getStatus() == NetConstant.RESULT_SUCCESS) {

                                            DaoUtils.getGroupInfoManagerInstance()
                                                    .deleteGroupInfo(groupGuid);
                                            DaoUtils.getGroupUserInfoManagerInstance()
                                                    .deleteGroupUserInfo
                                                    (groupGuid);
                                            EventBusUtil.sendGroupDismissEvent(groupGuid);
                                            callback.success(true);
                                        } else {
                                            onError("解散失败");
                                        }
                                    }

                                    @Override
                                    protected void onError(String resultMessage) {
                                        super.onError(resultMessage);
                                        callback.error(resultMessage);
                                    }
                                });
                    }

                    @Override
                    public void failed1() {
                        callback.error("解散群组失败");
                    }
                });
    }
}
