package com.hwl.beta.ui.chat.standard;

public interface ChatUserSettingStandard {
	
	Friend getUserInfo(long viewUserId,String viewUserName,String viewUserImage);

	ChatUserSetting getChatUserSetting(long viewUserId);
	
	void setShieldMessage(long viewUserId);
	
	void searchMessage(long viewUserId,String key,DefaultCallback<Boolean,String> callback);
	
	void clearMessage(long viewUserId,DefaultCallback<Boolean,String> callback);
}
