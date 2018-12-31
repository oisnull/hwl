package com.hwl.beta.ui.immsg.listen;

import com.hwl.im.imaction.AbstractMessageListenExecutor;
import com.hwl.imcore.improto.ImGroupOperateMessageContent;
import com.hwl.imcore.improto.ImGroupOperateMessageResponse;
import com.hwl.imcore.improto.ImMessageResponse;
import com.hwl.imcore.improto.ImMessageType;

public class GroupOperateMessageListen extends
        AbstractMessageListenExecutor<ImGroupOperateMessageResponse> {

    private ImGroupOperateMessageContent messageContent;

    @Override
    public void executeCore(ImMessageType messageType, ImGroupOperateMessageResponse response) {
        super.executeCore(messageType, response);
        messageContent = response.getGroupOperateMessageContent();

        switch (messageContent.getOperateType()) {
            case AddUser:
                break;
            case ExitGroup:
                break;
            case RemoveUser:
                break;
            case CreateGroup:
                break;
            case OperateNone:
                break;
            case DismissGroup:
                break;
            case UNRECOGNIZED:
                break;
        }
    }

    @Override
    public ImGroupOperateMessageResponse getResponse(ImMessageResponse response) {
        return response.getGroupOperateMessageResponse();
    }

}