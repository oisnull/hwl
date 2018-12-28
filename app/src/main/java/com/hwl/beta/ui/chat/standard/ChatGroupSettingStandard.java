package com.hwl.beta.ui.chat.standard;

public interface ChatGroupStandard {
	GroupInfo getGroupInfo(String groupGuid);
	
	List<GroupUserInfo> getGroupUsers(String groupGuid);
	
	void loadGroupUserFromServer(String groupGuid,DefaultCallback<Boolean,String> callback);
	
	void setShieldMessage(String groupGuid);
	
	void searchMessage(String groupGuid,String key,DefaultCallback<Boolean,String> callback);
	
	void clearMessage(String groupGuid,DefaultCallback<Boolean,String> callback);
	
	void exitGroup(String groupGuid,DefaultCallback<Boolean,String> callback);
	
	void dismissGroup(String groupGuid,DefaultCallback<Boolean,String> callback);
}
