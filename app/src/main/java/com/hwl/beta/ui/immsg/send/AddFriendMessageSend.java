package com.hwl.beta.ui.immsg.send;

import com.hwl.beta.sp.UserSP;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.AbstractMessageSendExecutor;
import com.hwl.imcore.improto.ImAddFriendMessageContent;
import com.hwl.imcore.improto.ImAddFriendMessageRequest;
import com.hwl.imcore.improto.ImMessageRequest;
import com.hwl.imcore.improto.ImMessageType;

public class AddFriendMessageSend extends AbstractMessageSendExecutor {

    ImAddFriendMessageContent messageContent;
    DefaultConsumer<Boolean> sendCallback;

    public AddFriendMessageSend(Long toUserId, String content, DefaultConsumer<Boolean>
            sendCallback) {
        messageContent = ImAddFriendMessageContent.newBuilder()
                .setFromUserId(UserSP.getUserId())
                .setFromUserName(UserSP.getUserShowName())
                .setFromUserHeadImage(UserSP.getUserHeadImage())
                .setToUserId(toUserId)
                .setContent(content)
                .build();
        this.sendCallback = sendCallback;
    }

    @Override
    public DefaultConsumer<Boolean> sendStatusCallback() {
        return sendCallback;
    }

    @Override
    public void setRequestBody(ImMessageRequest.Builder request) {
        request.setAddFriendMessageRequest(
                ImAddFriendMessageRequest.newBuilder().setAddFriendMessageContent(messageContent)
                        .build());
    }

    @Override
    public ImMessageType getMessageType() {
        return ImMessageType.AddFriend;
    }
}
