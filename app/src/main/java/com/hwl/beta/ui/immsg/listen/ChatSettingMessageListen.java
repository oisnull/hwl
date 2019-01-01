package com.hwl.beta.ui.immsg.listen;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatGroupMessage;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.ui.common.MessageNotifyManage;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.ui.immsg.IMConstant;
import com.hwl.beta.utils.StringUtils;
import com.hwl.im.imaction.AbstractMessageListenExecutor;
import com.hwl.imcore.improto.ImChatSettingMessageContent;
import com.hwl.imcore.improto.ImChatSettingMessageResponse;
import com.hwl.imcore.improto.ImMessageResponse;
import com.hwl.imcore.improto.ImMessageType;
import com.hwl.imcore.improto.ImUserContent;

import java.util.Date;

public class ChatSettingMessageListen extends
        AbstractMessageListenExecutor<ImChatSettingMessageResponse> {

    private ImChatSettingMessageContent messageContent;
    private GroupInfo groupInfo;

    @Override
    public void executeCore(ImMessageType messageType, ImChatSettingMessageResponse response) {
        super.executeCore(messageType, response);
        messageContent = response.getChatSettingMessageContent();

        groupInfo = DaoUtils.getGroupInfoManagerInstance().get(messageContent.getGroupGuid());
        if (groupInfo == null) return;

        this.setGroupInfo();

        if (!this.isCreateChatContent()) return;

        ChatGroupMessage message = new ChatGroupMessage();
        message.setGroupGuid(messageContent.getGroupGuid());
        message.setGroupName(groupInfo.getGroupName());
        message.setFromUserId(messageContent.getSettingUser().getUserId());
        message.setFromUserName(messageContent.getSettingUser().getUserName());
        message.setFromUserHeadImage(messageContent.getSettingUser().getUserImage());
        message.setContentType(IMConstant.CHAT_MESSAGE_CONTENT_TYPE_SYSTEM);
        message.setContent(this.getChatContent());
        message.setLocalUrl("");
        message.setSendTime(new Date(response.getBuildTime()));

        ChatRecordMessage record = new ChatRecordMessage();
        record.setRecordType(IMConstant.CHAT_RECORD_TYPE_GROUP);
        record.setGroupGuid(groupInfo.getGroupGuid());
        record.setGroupName(groupInfo.getGroupName());
        record.setGroupUserImages(groupInfo.getUserImages());
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

    private boolean isCreateChatContent() {
        switch (messageContent.getSettingType()) {
            case GroupName:
            case GroupNote:
                return true;
        }
        return false;
    }

    private String getChatContent() {
        String content = "";
        switch (messageContent.getSettingType()) {
            case GroupName:
                content = messageContent.getSettingUser().getUserName() + " 修改群组名称为: " +
                        messageContent.getGroupName();
                break;
            case GroupNote:
                content = messageContent.getSettingUser().getUserName() + " 修改群组公告为: " +
                        messageContent.getGroupNote();
                break;
        }
        return content;
    }

    private void setGroupInfo() {
        switch (messageContent.getSettingType()) {
            case GroupName:
                groupInfo.setGroupName(messageContent.getGroupName());
                DaoUtils.getGroupInfoManagerInstance().add(groupInfo);
                EventBusUtil.sendChatGroupNoteSettingEvent(groupInfo.getGroupGuid(), groupInfo
                        .getGroupName());
                break;
            case GroupNote:
                groupInfo.setGroupNote(messageContent.getGroupNote());
                DaoUtils.getGroupInfoManagerInstance().add(groupInfo);
                EventBusUtil.sendChatGroupNoteSettingEvent(groupInfo.getGroupGuid(), groupInfo
                        .getGroupNote());
                break;
            case UserRemark:
                this.setGroupUserRemark();
                break;
        }
    }

    private void setGroupUserRemark() {
        ImUserContent userContent = messageContent.getSettingUser();
        Friend friend = DaoUtils.getFriendManagerInstance().get(userContent.getUserId());
        if (friend == null) {
            friend = DBFriendAction.convertToFriendInfo(userContent.getUserId(),
                    userContent.getUserName(),
                    userContent.getUserImage(),
                    userContent.getGroupUserRemark(),
                    false);
        } else {
            friend.setGroupRemark(userContent.getGroupUserRemark());
        }

        DaoUtils.getFriendManagerInstance().save(friend);
    }

    @Override
    public ImChatSettingMessageResponse getResponse(ImMessageResponse response) {
        return response.getChatSettingMessageResponse();
    }

}