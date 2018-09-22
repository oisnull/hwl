package com.hwl.beta.ui.immsg.send;

import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.AbstractMessageSendExecutor;
import com.hwl.im.improto.ImChatGroupMessageContent;
import com.hwl.im.improto.ImChatGroupMessageRequest;
import com.hwl.im.improto.ImMessageRequest;
import com.hwl.im.improto.ImMessageType;

public class ChatGroupMessageSend extends AbstractMessageSendExecutor {

//    static Logger log = LogManager.getLogger(ChatUserMessageSend.class.getName());

    Long fromUserId;
    String groupGuid;
    String content;

    public ChatGroupMessageSend(Long fromUserId, String groupGuid, String content) {
        this.fromUserId = fromUserId;
        this.groupGuid = groupGuid;
        this.content = content;
    }

    @Override
    public ImMessageType getMessageType() {
        return ImMessageType.ChatGroup;
    }

    // @Override
    // public void sendResultCallback(boolean isSuccess) {
    //     log.debug("Client send chat user message to im server {}", isSuccess ? "success" : "failed");
    // }

    @Override
    public void setRequestBody(ImMessageRequest.Builder request) {
        ImChatGroupMessageContent messageContent = ImChatGroupMessageContent.newBuilder().setFromUserId(fromUserId)
                .setToGrouopGuid(groupGuid).setContent(content).build();
        request.setChatGroupMessageRequest(
                ImChatGroupMessageRequest.newBuilder().setChatGroupMessageContent(messageContent).build());
    }

	@Override
	public DefaultConsumer<Boolean> sendStatusCallback() {
		return null;
	}
}
