package com.hwl.beta.ui.immsg.send;

import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.AbstractMessageSendExecutor;
import com.hwl.im.improto.ImMessageRequest;
import com.hwl.im.improto.ImMessageType;

public class AddFriendMessageSend extends AbstractMessageSendExecutor {
    @Override
    public DefaultConsumer<Boolean> sendStatusCallback() {
        return null;
    }

    @Override
    public void setRequestBody(ImMessageRequest.Builder request) {

    }

    @Override
    public ImMessageType getMessageType() {
        return null;
    }
}
