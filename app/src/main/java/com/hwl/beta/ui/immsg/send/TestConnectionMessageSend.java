package com.hwl.beta.ui.immsg.send;

import com.hwl.beta.sp.UserSP;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.AbstractMessageSendExecutor;
import com.hwl.imcore.improto.ImMessageRequest;
import com.hwl.imcore.improto.ImMessageType;
import com.hwl.imcore.improto.ImTestConnectionMessageRequest;


public class TestConnectionMessageSend extends AbstractMessageSendExecutor {
    DefaultConsumer<Boolean> sendCallback;

    public TestConnectionMessageSend(DefaultConsumer<Boolean> sendCallback) {
        this.sendCallback = sendCallback;
    }

    @Override
    public DefaultConsumer<Boolean> sendStatusCallback() {
        return sendCallback;
    }

    @Override
    public void setRequestBody(ImMessageRequest.Builder request) {
        ImTestConnectionMessageRequest testConnectionMessageRequest =
                ImTestConnectionMessageRequest.newBuilder().setFromUserId(UserSP.getUserId())
                .build();
        request.setTestConnectionMessageRequest(testConnectionMessageRequest);
    }

    @Override
    public ImMessageType getMessageType() {
        return ImMessageType.TestConnection;
    }
}
