package com.hwl.beta.ui.chat.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatUserSetting;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.ui.chat.standard.ChatUserSettingStandard;
import com.hwl.beta.ui.common.DefaultCallback;

public class ChatUserSettingLogic implements ChatUserSettingStandard {
    static int pageSize = 10;
	
	@Override
    public Friend getUserInfo(long viewUserId,String viewUserName,String viewUserImage){
		Friend friend = DaoUtils.getFriendManagerInstance().get(viewUserId);
        if (friend == null) {
			friend = new Friend();
			friend.setName(viewUserName);
			friend.setHeadImage(viewUserImage);
        } 
		return friend;
	}
	
	@Override
    public ChatUserSetting getChatUserSetting(long viewUserId){
		ChatUserSetting userSetting = DaoUtils.getChatUserMessageManagerInstance().getChatUserSetting(viewUserId);
        if (userSetting == null) {
            userSetting = new ChatUserSetting();
            userSetting.setUserId(viewUserId);
            userSetting.setIsShield(false);
            DaoUtils.getChatUserMessageManagerInstance().setChatUserSetting(userSetting);
        }
		return userSetting;
	}
	
	@Override
	public void setShieldMessage(ChatUserSetting userSetting){
		DaoUtils.getChatUserMessageManagerInstance().setChatUserSetting(userSetting);
//                ChatRecordMessage recordMessage = DaoUtils.getChatRecordMessageManagerInstance().getUserRecord(myUserId, userSetting.getUserId());
//                if (recordMessage != null) {
//                    recordMessage.setIsShield(userSetting.getIsShield());
//                    EventBus.getDefault().post(new EventActionChatRecord(EventBusConstant.EB_TYPE_CHAT_RECORD_UPDATE_SHIELD, recordMessage));
//                }
	}
	
	@Override
	public void searchMessage(long viewUserId,String key,DefaultCallback<Boolean,String> callback){
		
	}
	
	@Override
	public void clearMessage(long viewUserId,DefaultCallback<Boolean,String> callback){		
//                                DaoUtils.getChatUserMessageManagerInstance().deleteUserMessages(myUserId, viewUserId);
//                                EventBus.getDefault().post(new EventClearUserMessages(EventBusConstant.EB_TYPE_ACTINO_CLEAR, myUserId, viewUserId));
	}
}
