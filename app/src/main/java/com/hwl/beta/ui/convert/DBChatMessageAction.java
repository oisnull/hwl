package com.hwl.beta.ui.convert;

import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.ChatUserMessage;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.immsg.IMConstant;

import java.util.Date;

/**
 * Created by Administrator on 2018/4/1.
 */

public class DBChatMessageAction {


    public static ChatUserMessage convertToTextMessage(Friend friend, String content) {
        ChatUserMessage message = new ChatUserMessage();
        message.setFromUserId(UserSP.getUserId());
        message.setFromUserName(UserSP.getUserShowName());
        message.setFromUserHeadImage(UserSP.getUserHeadImage());
        message.setToUserId(friend.getId());
        message.setContentType(IMConstant.CHAT_MESSAGE_CONTENT_TYPE_TEXT);
        message.setContent(content);
        message.setSendStatus(IMConstant.CHAT_SEND_SUCCESS);
        message.setSendTime(new Date());
        return message;
    }

    public static ChatRecordMessage convertToRecordMessage(Friend friend, String content) {
        ChatRecordMessage record = new ChatRecordMessage();
        record.setRecordType(IMConstant.CHAT_RECORD_TYPE_USER);
        record.setToUserId(friend.getId());
        record.setToUserName(friend.getName());
        record.setToUserHeadImage(friend.getHeadImage());
        record.setFromUserId(UserSP.getUserId());
        record.setFromUserName(UserSP.getUserShowName());
        record.setFromUserHeadImage(UserSP.getUserHeadImage());
//        record.setRecordImage(friend.getHeadImage());
        record.setTitle(friend.getName());
        record.setContentType(IMConstant.CHAT_MESSAGE_CONTENT_TYPE_TEXT);
        record.setContent(content);
        // record.setUnreadCount(0);
        record.setSendTime(new Date());
        return record;
    }
}
