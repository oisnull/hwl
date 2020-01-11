package com.hwl.beta.ui.immsg.listen;

import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.AbstractMessageListenExecutor;
import com.hwl.imcore.improto.ImMessageResponse;
import com.hwl.imcore.improto.ImUserValidateResponse;

public class UserValidateListen extends AbstractMessageListenExecutor<ImUserValidateResponse> {

    private DefaultConsumer<String> succCallback;
    private DefaultConsumer<String> failedCallback;

    public UserValidateListen(DefaultConsumer<String> succCallback) {
        this.succCallback = succCallback;
    }

    public UserValidateListen(DefaultConsumer<String> succCallback,
                              DefaultConsumer<String> failedCallback) {
        this.succCallback = succCallback;
        this.failedCallback = failedCallback;
    }

    @Override
    public void success(ImUserValidateResponse response) {
        super.success(response);
        if (response.getIsSuccess()) {
            if (this.succCallback != null)
                this.succCallback.accept(response.getSession());
        } else {
            if (this.failedCallback != null)
                this.failedCallback.accept(response.getMessage());
        }
    }

    @Override
    public void failed(int responseCode, String message) {
        if (this.failedCallback != null)
            this.failedCallback.accept(message);
    }

    @Override
    public void sessionInvalid() {
        EventBusUtil.sendTokenInvalidEvent();
    }

    @Override
    public ImUserValidateResponse getResponse(ImMessageResponse response) {
        return response.getUserValidateResponse();
    }

}