package com.hwl.beta.ui.chat.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.ChatUserSetting;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.chat.standard.ChatUserSettingStandard;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.ebus.EventBusUtil;

public class ChatUserSettingLogic implements ChatUserSettingStandard {
    @Override
    public Friend getUserInfo(long viewUserId, String viewUserName, String viewUserImage) {
        Friend friend = DaoUtils.getFriendManagerInstance().get(viewUserId);
        if (friend == null) {
            friend = new Friend();
            friend.setId(viewUserId);
            friend.setName(viewUserName);
            friend.setHeadImage(viewUserImage);
        }
        return friend;
    }

    @Override
    public ChatUserSetting getChatUserSetting(long viewUserId) {
        ChatUserSetting userSetting = DaoUtils.getChatUserMessageManagerInstance()
                .getChatUserSetting(viewUserId);
        if (userSetting == null) {
            userSetting = new ChatUserSetting();
            userSetting.setUserId(viewUserId);
            userSetting.setIsShield(false);
            DaoUtils.getChatUserMessageManagerInstance().setChatUserSetting(userSetting);
        }
        return userSetting;
    }

    @Override
    public void setShieldMessage(ChatUserSetting userSetting) {
        DaoUtils.getChatUserMessageManagerInstance().setChatUserSetting(userSetting);
        ChatRecordMessage recordMessage = DaoUtils.getChatRecordMessageManagerInstance
                ().getUserRecord(UserSP.getUserId(), userSetting.getUserId());
        if (recordMessage != null) {
            recordMessage.setShield(userSetting.getIsShield());
            EventBusUtil.sendChatRecordMessageNoSortEvent(recordMessage);
        }
    }

    @Override
    public void searchMessage(long viewUserId, String key, DefaultCallback<Boolean, String>
            callback) {

    }

    @Override
    public void clearMessage(long viewUserId) {
        long myUserId = UserSP.getUserId();
        DaoUtils.getChatUserMessageManagerInstance().deleteUserMessages(myUserId, viewUserId);
        ChatRecordMessage recordMessage = DaoUtils.getChatRecordMessageManagerInstance()
                .deleteUserRecords(myUserId, viewUserId);
        if (recordMessage == null) return;

        EventBusUtil.sendChatRecordMessageClearEvent(recordMessage.getRecordId());
    }
}
