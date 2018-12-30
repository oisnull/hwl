package com.hwl.beta.ui.chat.standard;

import com.hwl.beta.db.entity.ChatUserSetting;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.ui.common.DefaultCallback;

public interface ChatUserSettingStandard {
	
	Friend getUserInfo(long viewUserId, String viewUserName, String viewUserImage);

	ChatUserSetting getChatUserSetting(long viewUserId);
	
	void setShieldMessage(ChatUserSetting userSetting);
	
	void searchMessage(long viewUserId,String key,DefaultCallback<Boolean,String> callback);
	
	void clearMessage(long viewUserId);
}
