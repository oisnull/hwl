package com.hwl.beta.ui.chat.standard;

import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.ui.common.DefaultCallback;

import java.util.List;

public interface ChatGroupSettingStandard {
	GroupInfo getGroupInfo(String groupGuid);
	
	List<GroupUserInfo> getGroupUsers(String groupGuid, boolean isDismiss);
	
	void loadGroupUserFromServer(String groupGuid,DefaultCallback<Boolean,String> callback);
	
	void setShieldMessage(GroupInfo group);
	
	void searchMessage(String groupGuid,String key,DefaultCallback<Boolean,String> callback);
	
	void clearMessage(String groupGuid,DefaultCallback<Boolean,String> callback);
	
	void exitGroup(String groupGuid,DefaultCallback<Boolean,String> callback);
	
	void dismissGroup(String groupGuid,DefaultCallback<Boolean,String> callback);
}
