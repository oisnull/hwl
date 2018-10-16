package com.hwl.beta.ui.convert;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.net.user.NetUserFriendInfo;
import com.hwl.beta.utils.StringUtils;

import java.util.Date;

/**
 * Created by Administrator on 2018/4/1.
 */

public class DBChatMessageAction {


    public static ChatUserMessage convertToTextMessage(Friend friend,String content){
        NetUserInfo user = UserSP.getUserInfo();
        ChatUserMessage message = new ChatUserMessage();
        message.setFromUserId(user.getId());
        message.setFromUserName(user.getShowName());
        message.setFromUserHeadImage(user.getHeadImage());
        message.setToUserId(friend.getId());
        message.setContentType(IMConstant.CHAT_MESSAGE_CONTENT_TYPE_WORD);
        message.setContent(content);
        message.setSendStatus(IMConstant.CHAT_SEND_SUCCESS);
        message.setSendTime(new Date());
        return message;
    }

    public static ChatRecordMessage convertToRecordMessage(Friend friend,String content){
        NetUserInfo user = UserSP.getUserInfo();
        ChatRecordMessage record = new ChatRecordMessage();
        record.setRecordType(MQConstant.CHAT_RECORD_TYPE_USER);
        record.setToUserId(friend.getId());
        record.setToUserName(friend.getName());
        record.setToUserHeadImage(friend.getHeadImage());
        record.setFromUserId(user.getId());
        record.setFromUserName(user.getShowName());
        record.setFromUserHeadImage(user.getHeadImage());
        record.setRecordImage(friend.getHeadImage());
        record.setTitle(friend.getName());
        record.setContentType(MQConstant.CHAT_MESSAGE_CONTENT_TYPE_WORD);
        record.setContent(content);
    //                    record.setUnreadCount(0);
        record.setSendTime(new Date());
        return record;
    }
}
