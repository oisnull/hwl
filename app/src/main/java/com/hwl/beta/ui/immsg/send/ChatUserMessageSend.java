package com.hwl.beta.ui.immsg.send;

import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.AbstractMessageSendExecutor;
import com.hwl.im.improto.ImChatUserMessageContent;
import com.hwl.im.improto.ImChatUserMessageRequest;
import com.hwl.im.improto.ImMessageRequest;
import com.hwl.im.improto.ImMessageType;

import java.security.InvalidParameterException;

public class ChatUserMessageSend extends AbstractMessageSendExecutor {

//    static Logger log = LogManager.getLogger(ChatUserMessageSend.class.getName());

    Long fromUserId, toUserId = 0L;
    String content = "";

    public ChatUserMessageSend(Long fromUserId, Long toUserId, String content) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.content = content;
    }

    @Override
    public ImMessageType getMessageType() {
        return ImMessageType.ChatUser;
    }

    @Override
    public void setRequestBody(ImMessageRequest.Builder request) {

        this.checkParams();

        ImChatUserMessageContent messageContent = ImChatUserMessageContent.newBuilder().setFromUserId(fromUserId)
                .setToUserId(toUserId).setContent(content).build();
        request.setChatUserMessageRequest(
                ImChatUserMessageRequest.newBuilder().setChatUserMessageContent(messageContent).build());
    }

    private void checkParams() {
        if (fromUserId <= 0 || toUserId <= 0 || fromUserId == toUserId) {
            throw new InvalidParameterException("FromUserId cannot be the same as toUserId or is zero");
        }
        if (content == null || content.isEmpty()) {
            throw new NullPointerException("Send chat user message content cannot be null or empty");
        }
    }

	@Override
	public DefaultConsumer<Boolean> sendStatusCallback() {
		return null;
	}

}