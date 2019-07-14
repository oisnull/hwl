package com.hwl.beta.ui.immsg.listen;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatGroupMessage;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.MessageNotifyManage;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.beta.ui.convert.DBGroupAction;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.ui.group.logic.GroupAction;
import com.hwl.beta.ui.immsg.IMConstant;
import com.hwl.beta.utils.StringUtils;
import com.hwl.im.imaction.AbstractMessageListenExecutor;
import com.hwl.imcore.improto.ImGroupOperateMessageContent;
import com.hwl.imcore.improto.ImGroupOperateMessageResponse;
import com.hwl.imcore.improto.ImGroupOperateType;
import com.hwl.imcore.improto.ImMessageResponse;
import com.hwl.imcore.improto.ImMessageType;
import com.hwl.imcore.improto.ImUserContent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupOperateMessageListen extends
        AbstractMessageListenExecutor<ImGroupOperateMessageResponse> {

    private ImGroupOperateMessageResponse response;
    private ImGroupOperateMessageContent messageContent;
    private ImUserContent operateUser;
    private GroupInfo groupInfo;

    @Override
    public void executeCore(ImMessageType messageType, ImGroupOperateMessageResponse response) {
        super.executeCore(messageType, response);
        this.response = response;
        messageContent = response.getGroupOperateMessageContent();
        operateUser = messageContent.getOperateUser();

        if (messageContent.getOperateType() == ImGroupOperateType.CreateGroup) {
            this.createGroup();
            return;
        } else if (messageContent.getOperateType() == ImGroupOperateType.AddUser) {
            this.createGroup();
            GroupAction.loadServerGroupInfo(messageContent.getGroupGuid());
            this.buildChatMessage(this.getGroupUserNames());
            return;
        }

        groupInfo = DaoUtils.getGroupInfoManagerInstance().get(messageContent.getGroupGuid());
        if (groupInfo == null) return;

        switch (messageContent.getOperateType()) {
            case ExitGroup:
                DaoUtils.getGroupUserInfoManagerInstance().deleteGroupUserInfo(groupInfo
                        .getGroupGuid(), messageContent.getOperateUser().getUserId());
                this.buildChatMessage(operateUser.getUserName() + " 退出了群组.");
                break;
            case RemoveUser:
                for (int i = 0; i < messageContent.getGroupUsersCount(); i++) {
                    DaoUtils.getGroupUserInfoManagerInstance().deleteGroupUserInfo(groupInfo
                            .getGroupGuid(), messageContent.getGroupUsers(i).getUserId());
                }
                break;
//            case AddUser:
//                break;
//            case CreateGroup:
//                break;
//            case OperateNone:
//                break;
            case DismissGroup:
                this.dismissGroup();
                break;
        }
    }

    private void dismissGroup() {
        //检测是否存在chat record
        ChatRecordMessage recordMessage = DaoUtils.getChatRecordMessageManagerInstance()
                .getGroupRecord(groupInfo.getGroupGuid());
        if (recordMessage != null) {
            //如果存在记录则标识存在已经被解散
            groupInfo.setIsDismiss(true);
            DaoUtils.getGroupInfoManagerInstance().add(groupInfo);
            EventBusUtil.sendGroupDismissEvent(groupInfo.getGroupGuid());
        } else {
            //如果不存在记录，则直接删除群组相关数据
            DaoUtils.getGroupInfoManagerInstance().deleteGroupInfo(groupInfo);
            DaoUtils.getGroupUserInfoManagerInstance().deleteGroupUserInfo(groupInfo
                    .getGroupGuid());
        }
    }

    private void createGroup() {
        groupInfo = new GroupInfo();
        groupInfo.setGroupGuid(messageContent.getGroupGuid());
        groupInfo.setGroupName(messageContent.getGroupName());
        groupInfo.setMyUserName(UserSP.getUserShowName());
        groupInfo.setGroupImages(this.getUserImages());
        groupInfo.setBuildTime(new Date(response.getBuildTime()));
        groupInfo.setIsSystem(false);
        groupInfo.setGroupUserCount(messageContent.getGroupUsersCount());
        groupInfo.setBuildUserId(messageContent.getOperateUser().getUserId());

        DaoUtils.getGroupInfoManagerInstance().add(groupInfo);
        DaoUtils.getGroupUserInfoManagerInstance().addListAsync(this.getGroupUsers());
        DaoUtils.getFriendManagerInstance().addListAsync(this.getUsers());
    }

    private List<String> getUserImages() {
        if (messageContent.getGroupUsersCount() <= 0) return null;

        List<String> images = new ArrayList<>(messageContent.getGroupUsersCount());
        for (int i = 0; i < messageContent.getGroupUsersCount(); i++) {
            if (i > 8) break;
            images.add(messageContent.getGroupUsers(i).getUserImage());
        }
        return images;
    }

    private List<GroupUserInfo> getGroupUsers() {
        if (messageContent.getGroupUsersCount() <= 0) return null;

        List<GroupUserInfo> users = new ArrayList<>(messageContent.getGroupUsersCount());
        for (int i = 0; i < messageContent.getGroupUsersCount(); i++) {
            users.add(DBGroupAction.convertToGroupUserInfo(messageContent.getGroupGuid(),
                    messageContent.getGroupUsers(i).getUserId()));
        }
        return users;
    }

    private String getGroupUserNames() {
        if (messageContent.getGroupUsersCount() <= 0) return null;

        String contents = messageContent.getOperateUser().getUserName() + " 邀请 ";
        for (int i = 0; i < messageContent.getGroupUsersCount(); i++) {
            contents += messageContent.getGroupUsers(i).getUserName() + ",";
        }

        return contents + "加入群组";
    }

    private List<Friend> getUsers() {
        if (messageContent.getGroupUsersCount() <= 0) return null;

        List<Friend> users = new ArrayList<>(messageContent.getGroupUsersCount());
        for (int i = 0; i < messageContent.getGroupUsersCount(); i++) {
            users.add(DBFriendAction.convertToFriendInfo(
                    messageContent.getGroupUsers(i).getUserId(),
                    messageContent.getGroupUsers(i).getUserName(),
                    messageContent.getGroupUsers(i).getUserImage(),
                    messageContent.getGroupUsers(i).getGroupUserRemark()));
        }
        return users;
    }

    private void buildChatMessage(String content) {
        ChatGroupMessage message = new ChatGroupMessage();
        message.setGroupGuid(messageContent.getGroupGuid());
        message.setGroupName(groupInfo.getGroupName());
        message.setFromUserId(operateUser.getUserId());
        message.setFromUserName(operateUser.getUserName());
        message.setFromUserHeadImage(operateUser.getUserImage());
        message.setContentType(IMConstant.CHAT_MESSAGE_CONTENT_TYPE_SYSTEM);
        message.setContent(content);
        message.setSendTime(new Date(response.getBuildTime()));

        ChatRecordMessage record = new ChatRecordMessage();
        record.setRecordType(IMConstant.CHAT_RECORD_TYPE_GROUP);
        record.setGroupGuid(groupInfo.getGroupGuid());
        record.setGroupName(groupInfo.getGroupName());
        record.setGroupUserImages(groupInfo.getGroupImages());
        record.setFromUserId(message.getFromUserId());
        record.setFromUserName(message.getFromUserName());
        record.setFromUserHeadImage(message.getFromUserHeadImage());
        record.setTitle(groupInfo.getGroupName());
        record.setContentType(message.getContentType());
        record.setContent(StringUtils.cutString(message.getContent(), 25));
        record.setSendTime(message.getSendTime());

        DaoUtils.getChatGroupMessageManagerInstance().save(message);
        record = DaoUtils.getChatRecordMessageManagerInstance().save(record);

        EventBusUtil.sendChatGroupMessageEvent(message);
        EventBusUtil.sendChatRecordMessageSortEvent(record);
        MessageNotifyManage.play(DaoUtils.getGroupInfoManagerInstance().getGroupSettingIsShield
                (record.getGroupGuid()));
    }

    @Override
    public ImGroupOperateMessageResponse getResponse(ImMessageResponse response) {
        return response.getGroupOperateMessageResponse();
    }
}