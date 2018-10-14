package com.hwl.beta.ui.immsg.send;

import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.AbstractMessageSendExecutor;
import com.hwl.imcore.improto.ImHeartBeatMessageRequest;
import com.hwl.imcore.improto.ImMessageRequest;
import com.hwl.imcore.improto.ImMessageType;

public class HeartBeatMessageSend extends AbstractMessageSendExecutor {

    DefaultConsumer<Boolean> sendCallback;

    public HeartBeatMessageSend(DefaultConsumer<Boolean> sendCallback) {
        this.sendCallback = sendCallback;
    }

    @Override
    public ImMessageType getMessageType() {
        return ImMessageType.HeartBeat;
    }

    @Override
    public void setRequestBody(ImMessageRequest.Builder request) {
        request.setHeartBeatMessageRequest(
                ImHeartBeatMessageRequest.newBuilder().setCurrentTime(System.currentTimeMillis()).build());
    }

	@Override
	public DefaultConsumer<Boolean> sendStatusCallback() {
		return sendCallback;
	}
}
