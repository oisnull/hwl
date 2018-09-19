package com.hwl.im.imaction;

import com.hwl.im.immode.MessageRequestHeadOperate;
import com.hwl.im.improto.ImMessageContext;
import com.hwl.im.improto.ImMessageRequest;
import com.hwl.im.improto.ImMessageRequestHead;

import java.util.function.Consumer;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public abstract class AbstractMessageSendExecutor implements MessageSendExecutor {

//    static Logger log = LogManager.getLogger(AbstractMessageSendExecutor.class.getName());

    @Override
    public final ImMessageContext getMessageContext() {
        // if (!checkBody())
        // return null;
        ImMessageRequest.Builder request = ImMessageRequest.newBuilder();
        request.setRequestHead(getMessageHead());

        try {
            this.setRequestBody(request);
        } catch (Exception e) {
//            log.debug("Client build message content error, info : {}", e.getMessage());
            return null;
        }

        ImMessageContext.Builder context = ImMessageContext.newBuilder();
        context.setRequest(request);
        context.setType(getMessageType());

        return context.build();
    }

    @Override
    public final void sendResultCallback(boolean isSuccess) {
//        log.debug("Client send request {} , message context : {}", isSuccess ? "success" : "failed",
//                getMessageContext());
        Consumer<Boolean> callback = sendStatusCallback();
        if (callback != null) {
            callback.accept(isSuccess);
        }
    }

    @Override
    public boolean isSendFailedAndClose() {
        return false;
    }

    public ImMessageRequestHead getMessageHead() {
        return MessageRequestHeadOperate.buildRequestHead();
    }

    // public abstract boolean checkBody();

    public abstract Consumer<Boolean> sendStatusCallback();

    public abstract void setRequestBody(final ImMessageRequest.Builder request);

}