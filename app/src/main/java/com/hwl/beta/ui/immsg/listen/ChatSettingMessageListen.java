package com.hwl.beta.ui.immsg.listen;

import com.hwl.im.imaction.AbstractMessageListenExecutor;
import com.hwl.imcore.improto.ImChatSettingMessageContent;
import com.hwl.imcore.improto.ImChatSettingMessageResponse;
import com.hwl.imcore.improto.ImMessageResponse;
import com.hwl.imcore.improto.ImMessageType;

public class ChatSettingMessageListen extends
        AbstractMessageListenExecutor<ImChatSettingMessageResponse> {

    private ImChatSettingMessageContent messageContent;

    @Override
    public void executeCore(ImMessageType messageType, ImChatSettingMessageResponse response) {
        super.executeCore(messageType, response);
        messageContent = response.getChatSettingMessageContent();
//        switch (messageContent.getSettingType()) {
//            case GroupName:
//                break;
//            case GroupNote:
//                break;
//            case UserRemark:
//                break;
//            case SettingNone:
//                break;
//            case UNRECOGNIZED:
//                break;
//        }
    }

    @Override
    public ImChatSettingMessageResponse getResponse(ImMessageResponse response) {
        return response.getChatSettingMessageResponse();
    }

}