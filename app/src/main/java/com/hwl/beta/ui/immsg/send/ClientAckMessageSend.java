package com.hwl.beta.ui.immsg.send;

import com.hwl.im.core.imaction.AbstractMessageSendExecutor;
import com.hwl.imcore.improto.ImAckMessageRequest;

import java.util.function.Consumer;

import com.hwl.imcore.improto.ImMessageRequest.Builder;
import com.hwl.imcore.improto.ImMessageType;

public class ClientAckMessageSend extends AbstractMessageSendExecutor {

    String messageid = "";

    public ClientAckMessageSend(String messageid) {
        this.messageid = messageid;
    }

    @Override
    public ImMessageType getMessageType() {
        return ImMessageType.ClientAckMessage;
    }

    @Override
    public void setRequestBody(Builder request) {
        request.setAckMessageRequest(ImAckMessageRequest.newBuilder()
                .setFromUserId(UserSP.getUserId())
                .setMessageid(messageid)
                .build());
    }

    @Override
    public Consumer<Boolean> sendStatusCallback() {
        return null;
    }
}
