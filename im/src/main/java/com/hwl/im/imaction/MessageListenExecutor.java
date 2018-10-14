package com.hwl.im.imaction;


import com.hwl.imcore.improto.ImMessageContext;

public interface MessageListenExecutor {
    void execute(ImMessageContext messageContext);

    boolean executedAndClose();

    // ImMessageType getMessageType();
}