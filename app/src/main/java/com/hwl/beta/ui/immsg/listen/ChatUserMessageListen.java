package com.hwl.beta.ui.immsg.listen;

import com.hwl.im.imaction.AbstractMessageListenExecutor;
import com.hwl.imcore.improto.ImChatUserMessageResponse;
import com.hwl.imcore.improto.ImMessageResponse;
import com.hwl.imcore.improto.ImMessageType;

public class ChatUserMessageListen extends AbstractMessageListenExecutor<ImChatUserMessageResponse> {

    @Override
    public void executeCore(ImMessageType messageType, ImChatUserMessageResponse response) {
        super.executeCore(messageType, response);
        System.out.println("ChatUserMessageReceive success : " + response.getChatUserMessageContent().toString());

        //  String fromUserName = model.getFromUserName();
        // Friend friend = DaoUtils.getFriendManagerInstance().get(model.getFromUserId());
        // if (friend != null) {
        //     fromUserName = friend.getShowName();
        //     if (DBFriendAction.updateFriendNameAndImage(friend, model.getFromUserName(), model.getFromUserHeadImage())) {
        //         EventBus.getDefault().post(friend);
        //     }
        // } else {
        //     if (UserSettingSP.getPrivacySettingRejectChat()) {
        //         UserMessageSend.sendRejectChatMessage(model.getFromUserId(), model.getMsgId()).subscribe();
        //         return;
        //     } else {
        //         AddSystemRejectCozyMessage(model);
        //     }
        // }

        // NetUserInfo user = UserSP.getUserInfo();
        // ChatRecordMessage record = new ChatRecordMessage();
        // record.setRecordType(MQConstant.CHAT_RECORD_TYPE_USER);
        // record.setRecordImage(model.getFromUserHeadImage());
        // record.setFromUserId(model.getFromUserId());
        // record.setFromUserName(fromUserName);
        // record.setFromUserHeadImage(model.getFromUserHeadImage());
        // record.setToUserId(user.getId());
        // record.setToUserName(user.getShowName());
        // record.setToUserHeadImage(user.getHeadImage());
        // record.setTitle(fromUserName);
        // record.setContentType(model.getContentType());
        // record.setContent(StringUtils.cutString(model.getContent(), 25));
        // //record.setUnreadCount(1);
        // record.setSendTime(model.getSendTime());
        // record = DaoUtils.getChatRecordMessageManagerInstance().addOrUpdate(record);

        // ChatUserMessage message = new ChatUserMessage();
        // message.setFromUserId(model.getFromUserId());
        // message.setFromUserName(fromUserName);
        // message.setFromUserHeadImage(model.getFromUserHeadImage());
        // message.setToUserId(model.getToUserId());
        // message.setContentType(model.getContentType());
        // message.setContent(model.getContent());
        // message.setLocalUrl("");
        // message.setPreviewUrl(model.getPreviewUrl());
        // message.setOriginalUrl(model.getOriginalUrl());
        // message.setImageWidth(model.getImageWidth());
        // message.setImageHeight(model.getImageHeight());
        // message.setPlayTime(model.getPlayTime());
        // message.setSendStatus(MQConstant.CHAT_SEND_SUCCESS);
        // message.setSendTime(model.getSendTime());
        // DaoUtils.getChatUserMessageManagerInstance().save(message);

        // EventBus.getDefault().post(message);
        // EventBus.getDefault().post(record);
        // MessageNotifyManage.play(DaoUtils.getChatUserMessageManagerInstance().getChatUserSettingIsShield(user.getId() == record.getFromUserId() ? record.getToUserId() : record.getFromUserId()));
    
    }

    @Override
    public ImChatUserMessageResponse getResponse(ImMessageResponse response) {
        return response.getChatUserMessageResponse();
    }

}