package com.hwl.beta.ui.chat.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatGroupMessage;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.ui.chat.standard.ChatGroupStandard;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.utils.StringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChatGroupSettingLogic implements ChatGroupSettingStandard {
    static int pageSize = 10;

    @Override
    public GroupInfo getChatGroupInfo(String groupGuid) {
        return DaoUtils.getGroupInfoManagerInstance().get(groupGuid);
    }

    @Override
    public List<GroupUserInfo> getGroupUsers(String groupGuid){
		List<GroupUserInfo> users = DaoUtils.getGroupUserInfoManagerInstance().getUsers(groupGuid);
		
        if (group.getIsDismiss() || group.getGroupGuid().equals(UserPosSP.getGroupGuid())) return users;
        GroupUserInfo groupUserInfo = new GroupUserInfo();
        groupUserInfo.setId((long) -1);
        users.add(users.size(), groupUserInfo);
		
		return users;
	}

    @Override
    public void loadGroupUserFromServer(String groupGuid,DefaultCallback<Boolean,String> callback){
		
        GroupService.groupUsers(groupGuid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetDefaultObserver<GroupUsersResponse>() {
                    @Override
                    protected void onSuccess(GroupUsersResponse response) {
                        if (response.getGroupUserInfos() != null) {
                            //removeUserItem();
                            List<GroupUserInfo> userInfos = DBGroupAction.convertToGroupUserInfos(response.getGroupUserInfos());
                            DaoUtils.getGroupUserInfoManagerInstance().addListAsync(userInfos);
                            users.addAll(userInfos);
                            addUserItem();
                            userAdapter.notifyDataSetChanged();
                        }
                    }
                });
	}

    @Override
    public void setShieldMessage(GroupInfo group){
		DaoUtils.getGroupInfoManagerInstance().add(group);
		ChatRecordMessage recordMessage = DaoUtils.getChatRecordMessageManagerInstance().getGroupRecord(group.getGroupGuid());
		if (recordMessage != null) {
//                recordMessage.setIsShield(group.getIsShield());
//                EventBus.getDefault().post(new EventActionChatRecord(EventBusConstant.EB_TYPE_CHAT_RECORD_UPDATE_SHIELD, recordMessage));
		}
	}

    @Override
    public void searchMessage(String groupGuid,String key,DefaultCallback<Boolean,String> callback){
		
	}

    @Override
    public void clearMessage(String groupGuid,DefaultCallback<Boolean,String> callback){
		DaoUtils.getChatGroupMessageManagerInstance().deleteMessages(groupGuid);
//                            EventBus.getDefault().post(new EventActionGroup(EventBusConstant.EB_TYPE_ACTINO_CLEAR, group));
	}

    @Override
    public void exitGroup(String groupGuid,DefaultCallback<Boolean,String> callback){
		GroupService.deleteGroupUser(group.getGroupGuid())
					.subscribe(new NetDefaultObserver<DeleteGroupUserResponse>() {
						@Override
						protected void onSuccess(DeleteGroupUserResponse response) {
							LoadingDialog.hide();
							if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
//                                                GroupActionMessageSend.sendExitGroupUserMessage(group.getGroupGuid(), myUserId, group.getMyUserName() + " 退出群组").subscribe();
//
//                                                DaoUtils.getGroupInfoManagerInstance().deleteGroupInfo(group);
//                                                DaoUtils.getGroupUserInfoManagerInstance().deleteGroupUserInfo(group.getGroupGuid());
//                                                DaoUtils.getChatGroupMessageManagerInstance().deleteMessages(group.getGroupGuid());
//                                                ChatRecordMessage recordMessage = DaoUtils.getChatRecordMessageManagerInstance().deleteGroupRecord(group.getGroupGuid());
//
//                                                EventBus.getDefault().post(new EventActionGroup(EventBusConstant.EB_TYPE_ACTINO_EXIT, group));
//                                                EventBus.getDefault().post(new EventActionChatRecord(EventBusConstant.EB_TYPE_ACTINO_REMOVE, recordMessage));
								callback.success(true);
							} else {
								onError("退出失败");
							}
						}

						@Override
						protected void onError(String resultMessage) {
							super.onError(resultMessage);
							callback.error(resultMessage);
						}
					});
	}

    @Override
    public void dismissGroup(String groupGuid,DefaultCallback<Boolean,String> callback){
		GroupService.deleteGroup(groupGuid)
                    .subscribe(new NetDefaultObserver<DeleteGroupResponse>() {
                        @Override
                        protected void onSuccess(DeleteGroupResponse response) {
                            LoadingDialog.hide();
                            if (response.getStatus() == NetConstant.RESULT_SUCCESS) {

                                DaoUtils.getGroupInfoManagerInstance().deleteGroupInfo(group);
                                DaoUtils.getGroupUserInfoManagerInstance().deleteGroupUserInfo(group.getGroupGuid());
                                DaoUtils.getChatGroupMessageManagerInstance().deleteMessages(group.getGroupGuid());
                                ChatRecordMessage recordMessage = DaoUtils.getChatRecordMessageManagerInstance().deleteGroupRecord(group.getGroupGuid());
								callback.success(true);
//                                EventBus.getDefault().post(new EventActionGroup(EventBusConstant.EB_TYPE_ACTINO_EXIT, group));
//                                EventBus.getDefault().post(new EventActionChatRecord(EventBusConstant.EB_TYPE_ACTINO_REMOVE, recordMessage));
                            } else {
                                onError("解散失败");
                            }
                        }

                        @Override
                        protected void onError(String resultMessage) {
                            super.onError(resultMessage);
							callback.error(resultMessage);
                        }
                    });
	}
}
