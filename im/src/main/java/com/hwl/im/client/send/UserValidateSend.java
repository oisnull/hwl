package com.hwl.im.client.send;

import com.hwl.im.imaction.AbstractMessageSendExecutor;
import com.hwl.im.improto.ImMessageRequest;
import com.hwl.im.improto.ImMessageType;
import com.hwl.im.improto.ImUserValidateRequest;

import java.util.function.Consumer;

public class UserValidateSend extends AbstractMessageSendExecutor {

//    static Logger log = LogManager.getLogger(UserValidateSend.class.getName());

    Long userId = 0L;
    String token = "";
    Consumer<Boolean> sendCallback;

    public UserValidateSend(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public UserValidateSend(Long userId, String token, Consumer<Boolean> sendCallback) {
        this(userId, token);
        this.sendCallback = sendCallback;
    }

    @Override
    public ImMessageType getMessageType() {
        return ImMessageType.UserValidate;
    }

    @Override
    public void setRequestBody(ImMessageRequest.Builder request) {
        ImUserValidateRequest userValidateRequest = ImUserValidateRequest.newBuilder().setUserId(userId).setToken(token)
                .build();
        request.setUserValidateRequest(userValidateRequest);
    }

    @Override
    public Consumer<Boolean> sendStatusCallback() {
        return this.sendCallback;
    }
}