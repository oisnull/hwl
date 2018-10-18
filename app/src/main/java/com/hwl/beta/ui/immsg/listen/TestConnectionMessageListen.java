package com.hwl.beta.ui.immsg.listen;

import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.AbstractMessageListenExecutor;
import com.hwl.imcore.improto.ImMessageResponse;
import com.hwl.imcore.improto.ImTestConnectionMessageResponse;

public class TestConnectionMessageListen extends
        AbstractMessageListenExecutor<ImTestConnectionMessageResponse> {
    private DefaultConsumer<ImTestConnectionMessageResponse> succCallback;
    private DefaultConsumer<String> failedCallback;

    public TestConnectionMessageListen(DefaultConsumer<ImTestConnectionMessageResponse> succCallback, DefaultConsumer<String> failedCallback) {
        this.succCallback = succCallback;
        this.failedCallback = failedCallback;
    }

    @Override
    public ImTestConnectionMessageResponse getResponse(ImMessageResponse response) {
        return response.getTestConnectionMessageResponse();
    }

    @Override
    public void success(ImTestConnectionMessageResponse response) {
        super.success(response);
        if (this.succCallback != null)
            this.succCallback.accept(response);
    }

    @Override
    public void failed(int responseCode, String message) {
        if (this.failedCallback != null)
            this.failedCallback.accept(message);
    }
}
