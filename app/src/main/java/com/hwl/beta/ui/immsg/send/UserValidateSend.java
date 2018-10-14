package com.hwl.beta.ui.immsg.send;

import com.hwl.beta.sp.UserSP;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.AbstractMessageSendExecutor;
import com.hwl.imcore.improto.ImMessageRequest;
import com.hwl.imcore.improto.ImMessageType;
import com.hwl.imcore.improto.ImUserValidateRequest;

public class UserValidateSend extends AbstractMessageSendExecutor {

    DefaultConsumer<Boolean> sendCallback;

    public UserValidateSend(DefaultConsumer<Boolean> sendCallback) {
        this.sendCallback = sendCallback;
    }

    @Override
    public ImMessageType getMessageType() {
        return ImMessageType.UserValidate;
    }

    @Override
    public void setRequestBody(ImMessageRequest.Builder request) {
        ImUserValidateRequest userValidateRequest = ImUserValidateRequest.newBuilder().setUserId(UserSP.getUserId()).setToken(UserSP.getUserToken())
                .build();
        request.setUserValidateRequest(userValidateRequest);
    }

    @Override
    public DefaultConsumer<Boolean> sendStatusCallback() {
        return this.sendCallback;
    }
}