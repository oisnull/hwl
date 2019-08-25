package com.hwl.beta.ui.immsg.listen;

import android.util.Log;

import com.hwl.im.imaction.AbstractMessageListenExecutor;
import com.hwl.imcore.improto.ImMessageResponse;
import com.hwl.imcore.improto.ImMessageType;
import com.hwl.imcore.improto.ImSystemMessageContent;
import com.hwl.imcore.improto.ImSystemMessageResponse;

public class SystemMessageListen extends
        AbstractMessageListenExecutor<ImSystemMessageResponse> {

    private ImSystemMessageContent messageContent;

    @Override
    public void executeCore(ImMessageType messageType, ImSystemMessageResponse response) {
        super.executeCore(messageType, response);
        messageContent = response.getSystemMessageContent();

        Log.d("SystemMessageListen", messageContent.toString());
    }

    @Override
    public ImSystemMessageResponse getResponse(ImMessageResponse response) {
        return response.getSystemMessageResponse();
    }

}