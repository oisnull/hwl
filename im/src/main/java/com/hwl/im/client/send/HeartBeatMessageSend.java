package com.hwl.im.client.send;

import com.hwl.im.imaction.AbstractMessageSendExecutor;
import com.hwl.im.improto.ImHeartBeatMessageRequest;
import com.hwl.im.improto.ImMessageRequest;
import com.hwl.im.improto.ImMessageType;

import java.util.function.Consumer;

public class HeartBeatMessageSend extends AbstractMessageSendExecutor {

//    static Logger log = LogManager.getLogger(HeartBeatMessageSend.class.getName());

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
	public Consumer<Boolean> sendStatusCallback() {
		return null;
	}
}
