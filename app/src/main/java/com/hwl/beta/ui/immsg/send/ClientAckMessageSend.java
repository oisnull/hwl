package com.hwl.beta.ui.immsg.send;

import com.hwl.beta.sp.UserSP;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.AbstractMessageSendExecutor;
import com.hwl.imcore.improto.ImAckMessageRequest;

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
    public DefaultConsumer<Boolean> sendStatusCallback() {
        return null;
    }
}
