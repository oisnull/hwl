package com.hwl.beta.ui.immsg.listen;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.ChatUserMessage;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.ui.immsg.IMConstant;
import com.hwl.beta.utils.StringUtils;
import com.hwl.im.imaction.AbstractMessageListenExecutor;
import com.hwl.imcore.improto.ImChatUserMessageContent;
import com.hwl.imcore.improto.ImChatUserMessageResponse;
import com.hwl.imcore.improto.ImMessageResponse;
import com.hwl.imcore.improto.ImMessageType;

import java.util.Date;

public class ChatUserMessageListen extends
        AbstractMessageListenExecutor<ImChatUserMessageResponse> {

    private ImChatUserMessageContent messageContent;

    @Override
    public void executeCore(ImMessageType messageType, ImChatUserMessageResponse response) {
        super.executeCore(messageType, response);
        System.out.println("ChatUserMessageReceive success : " + response
                .getChatUserMessageContent().toString());
        messageContent = response.getChatUserMessageContent();

        Friend friend = this.getFriendInfo();
        Date buildDate = new Date(response.getBuildTime());

        ChatUserMessage message = new ChatUserMessage();
        message.setFromUserId(messageContent.getFromUserId());
        message.setFromUserName(friend.getShowName());
        message.setFromUserHeadImage(friend.getHeadImage());
        message.setToUserId(messageContent.getToUserId());
        message.setContentType(messageContent.getContentType());
        message.setContent(messageContent.getContent());
        message.setPreviewUrl(messageContent.getPreviewUrl());
//        message.setOriginalUrl(messageContent.getPreviewUrl());
        message.setImageWidth(messageContent.getImageWidth());
        message.setImageHeight(messageContent.getImageHeight());
        message.setPlayTime(messageContent.getPlayTime());
        message.setSendStatus(IMConstant.CHAT_SEND_SUCCESS);
        message.setSendTime(buildDate);

        ChatRecordMessage record = new ChatRecordMessage();
        record.setRecordType(IMConstant.CHAT_RECORD_TYPE_USER);
        record.setFromUserId(messageContent.getFromUserId());
        record.setFromUserName(friend.getShowName());
        record.setFromUserHeadImage(friend.getHeadImage());
        record.setToUserId(messageContent.getToUserId());
        record.setToUserName(messageContent.getFromUserName());
        record.setToUserHeadImage(messageContent.getFromUserImage());
        record.setTitle(friend.getShowName());
        record.setContentType(messageContent.getContentType());
        record.setContent(StringUtils.cutString(messageContent.getContent(), 25));
        //record.setUnreadCount(1);
        record.setSendTime(buildDate);

        DaoUtils.getChatUserMessageManagerInstance().save(message);
        record = DaoUtils.getChatRecordMessageManagerInstance().save(record);

        EventBusUtil.sendChatUserMessageEvent(message);
        EventBusUtil.sendChatRecordMessageEvent(record);

        //  String fromUserName = model.getFromUserName();
        // Friend friend = DaoUtils.getFriendManagerInstance().get(model.getFromUserId());
        // if (friend != null) {
        //     fromUserName = friend.getShowName();
        //     if (DBFriendAction.updateFriendNameAndImage(friend, model.getFromUserName(), model
        // .getFromUserHeadImage())) {
        //         EventBus.getDefault().post(friend);
        //     }
        // } else {
        //     if (UserSettingSP.getPrivacySettingRejectChat()) {
        //         UserMessageSend.sendRejectChatMessage(model.getFromUserId(), model.getMsgId())
        // .subscribe();
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
        // MessageNotifyManage.play(DaoUtils.getChatUserMessageManagerInstance()
        // .getChatUserSettingIsShield(user.getId() == record.getFromUserId() ? record
        // .getToUserId() : record.getFromUserId()));

    }

    private Friend getFriendInfo() {
        Friend friend = DaoUtils.getFriendManagerInstance().get(messageContent.getFromUserId());
        if (friend == null) {
            friend = DBFriendAction.convertToFriendInfo(messageContent.getFromUserId(),
                    messageContent.getFromUserName(), messageContent.getFromUserImage(),
                    messageContent.getIsFriend());

            DaoUtils.getFriendManagerInstance().save(friend);
            // if (messageContent.getIsFriend()) {
            //     DaoUtils.getFriendManagerInstance().save(friend);
            // }
        }

        return friend;
    }

    @Override
    public ImChatUserMessageResponse getResponse(ImMessageResponse response) {
        return response.getChatUserMessageResponse();
    }

}