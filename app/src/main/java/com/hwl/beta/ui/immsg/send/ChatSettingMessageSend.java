package com.hwl.beta.ui.immsg.send;

import com.hwl.beta.sp.UserSP;
import com.hwl.beta.utils.StringUtils;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.AbstractMessageSendExecutor;
import com.hwl.imcore.improto.ImChatSettingMessageContent;
import com.hwl.imcore.improto.ImChatSettingMessageRequest;
import com.hwl.imcore.improto.ImChatSettingType;
import com.hwl.imcore.improto.ImMessageRequest;
import com.hwl.imcore.improto.ImMessageType;
import com.hwl.imcore.improto.ImUserContent;

public class ChatSettingMessageSend extends AbstractMessageSendExecutor {

    ImChatSettingMessageContent messageContent;
    DefaultConsumer<Boolean> sendCallback;

    public ChatSettingMessageSend(ImChatSettingType settingType,
                                  String groupGuid,
                                  String groupName,
                                  String groupNote,
                                  String groupUserRemark,
                                  DefaultConsumer<Boolean> sendCallback) {

        ImUserContent user = ImUserContent.newBuilder()
                .setUserId(UserSP.getUserId())
                .setUserName(UserSP.getUserShowName())
                .setUserImage(UserSP.getUserHeadImage())
                .setGroupUserRemark(StringUtils.nullStrToEmpty(groupUserRemark))
                .build();

        messageContent = ImChatSettingMessageContent.newBuilder().setSettingType(settingType)
                .setSettingUser(user)
                .setGroupGuid(StringUtils.nullStrToEmpty(groupGuid))
                .setGroupName(StringUtils.nullStrToEmpty(groupName))
                .setGroupNote(StringUtils.nullStrToEmpty(groupNote))
                .build();

        this.sendCallback = sendCallback;
    }

    @Override
    public ImMessageType getMessageType() {
        return ImMessageType.ChatSetting;
    }

    @Override
    public void setRequestBody(ImMessageRequest.Builder request) {
        request.setChatSettingMessageRequest(
                ImChatSettingMessageRequest.newBuilder().setChatSettingMessageContent
                        (messageContent)
                        .build());
    }

    @Override
    public DefaultConsumer<Boolean> sendStatusCallback() {
        return sendCallback;
    }
}
