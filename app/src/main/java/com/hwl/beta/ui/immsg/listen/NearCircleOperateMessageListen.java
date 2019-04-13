package com.hwl.beta.ui.immsg.listen;

import com.hwl.im.imaction.AbstractMessageListenExecutor;
import com.hwl.imcore.improto.ImMessageResponse;
import com.hwl.imcore.improto.ImMessageType;
import com.hwl.imcore.improto.ImNearCircleOperateMessageContent;
import com.hwl.imcore.improto.ImNearCircleOperateMessageResponse;

public class NearCircleOperateMessageListen extends
        AbstractMessageListenExecutor<ImNearCircleOperateMessageResponse> {

    private ImNearCircleOperateMessageResponse response;
    private ImNearCircleOperateMessageContent messageContent;

    @Override
    public void executeCore(ImMessageType messageType,
                            ImNearCircleOperateMessageResponse response) {
        super.executeCore(messageType, response);
        this.response = response;
        messageContent = response.getNearCircleOperateMessageContent();

    }

    @Override
    public ImNearCircleOperateMessageResponse getResponse(ImMessageResponse response) {
        return response.getNearCircleOperateMessageResponse();
    }
}