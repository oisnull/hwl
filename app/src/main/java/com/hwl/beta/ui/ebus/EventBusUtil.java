package com.hwl.beta.ui.ebus;

import com.hwl.beta.db.entity.ChatGroupMessage;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.ChatUserMessage;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.ui.ebus.bean.EventChatGroupSetting;
import com.hwl.beta.ui.ebus.bean.EventDeleteFriend;
import com.hwl.beta.ui.ebus.bean.EventUpdateFriendRemark;
import com.hwl.beta.ui.ebus.bean.EventUserEditModel;
import com.hwl.beta.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class EventBusUtil {
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void unRegister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void sendEvent(EventMessageModel messageModel) {
        EventBus.getDefault().post(messageModel);
    }

    public static void sendStickyEvent(EventMessageModel messageModel) {
        EventBus.getDefault().postSticky(messageModel);
    }

    public static void sendNetworkConnectEvent() {
        sendEvent(new EventMessageModel(EventBusConstant.EB_TYPE_NETWORK_CONNECT_UPDATE));
    }

    public static void sendNetworkBreakEvent() {
        sendEvent(new EventMessageModel(EventBusConstant.EB_TYPE_NETWORK_BREAK_UPDATE));
    }

    public static void sendTokenInvalidEvent() {
        sendStickyEvent(new EventMessageModel(EventBusConstant.EB_TYPE_TOKEN_INVALID_RELOGIN));
    }

    public static void sendUserHeadImageEditEvent(String userHeadImageUrl) {
        if (StringUtils.isBlank(userHeadImageUrl)) return;
        EventUserEditModel messageModel = new EventUserEditModel();
        messageModel.setUserHeadImageUrl(userHeadImageUrl);
        sendEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_USER_HEAD_UPDATE, messageModel));
    }

    public static void sendUserSymbolEditEvent(String userSymbol) {
        if (StringUtils.isBlank(userSymbol)) return;
        EventUserEditModel messageModel = new EventUserEditModel();
        messageModel.setUserSymbol(userSymbol);
        sendEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_USER_SYMBOL_UPDATE, messageModel));
    }

    public static void sendUserNameEditEvent(String userName) {
        if (StringUtils.isBlank(userName)) return;
        EventUserEditModel messageModel = new EventUserEditModel();
        messageModel.setUserName(userName);
        sendEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_USER_NAME_UPDATE, messageModel));
    }

    public static void sendUserSexEditEvent(int userSex) {
        EventUserEditModel messageModel = new EventUserEditModel();
        messageModel.setUserSex(userSex);
        sendEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_USER_SEX_UPDATE, messageModel));
    }

    public static void sendUserLifeNotesEditEvent(String userLifeNotes) {
        if (StringUtils.isBlank(userLifeNotes)) return;
        EventUserEditModel messageModel = new EventUserEditModel();
        messageModel.setUserLifeNotes(userLifeNotes);
        sendEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_USER_LIFENOTES_UPDATE, messageModel));
    }

    public static void sendFriendRequestEvent() {
        sendEvent(new EventMessageModel(EventBusConstant.EB_TYPE_FRIEND_REQUEST_UPDATE));
    }

    public static void sendFriendAddEvent(Friend friend) {
        if (friend == null) return;
        sendEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_FRIEND_ADD, friend));
    }

    public static void sendFriendDeleteEvent(long friendId) {
        if (friendId <= 0) return;
        sendEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_FRIEND_DELETE, new EventDeleteFriend(friendId)));
    }

    public static void sendFriendUpdateRemarkEvent(long friendId, String remark) {
        if (friendId <= 0) return;
        EventUpdateFriendRemark messageModel = new EventUpdateFriendRemark(friendId, remark);
        sendEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_FRIEND_UPDATE_REMARK, messageModel));
    }

    public static void sendChatUserMessageEvent(ChatUserMessage userMessage) {
        sendEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_CHAT_USER_MESSAGE_UPDATE, userMessage));
    }

    public static void sendChatGroupMessageEvent(ChatGroupMessage groupMessage) {
        sendEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_CHAT_GROUP_MESSAGE_UPDATE, groupMessage));
    }

    public static void sendChatGroupNoteSettingEvent(String groupGuid, String groupNote) {
        EventChatGroupSetting groupSetting = new EventChatGroupSetting();
        groupSetting.setGroupGuid(groupGuid);
        groupSetting.setGroupNote(groupNote);
        sendEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_CHAT_GROUP_NOTE_SETTING, groupSetting));
    }

    public static void sendChatGroupNameSettingEvent(String groupGuid, String groupName) {
        EventChatGroupSetting groupSetting = new EventChatGroupSetting();
        groupSetting.setGroupGuid(groupGuid);
        groupSetting.setGroupName(groupName);
        sendEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_CHAT_GROUP_NAME_SETTING, groupSetting));
    }

    public static void sendChatGroupUserRemarkSettingEvent(String groupGuid, String
            groupUserRemark) {
        EventChatGroupSetting groupSetting = new EventChatGroupSetting();
        groupSetting.setGroupGuid(groupGuid);
        groupSetting.setGroupUserRemark(groupUserRemark);
        sendEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_CHAT_GROUP_USER_REMARK_SETTING, groupSetting));
    }

    public static void sendChatRecordMessageSortEvent(ChatRecordMessage recordMessage) {
        sendEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_CHAT_RECORD_MESSAGE_UPDATE_SORT, recordMessage));
    }

    public static void sendChatRecordMessageNoSortEvent(ChatRecordMessage recordMessage) {
        sendEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_CHAT_RECORD_MESSAGE_UPDATE_SORT, recordMessage));
    }

    public static void sendChatRecordMessageClearEvent(long recordId) {
        sendEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_CHAT_RECORD_MESSAGE_CLEAR, recordId));
    }

    public static void sendGroupAddEvent(GroupInfo groupInfo) {
        sendEvent(new EventMessageModel(EventBusConstant.EB_TYPE_GROUP_ACTION_ADD, groupInfo));
    }

    public static void sendGroupDismissEvent(String groupGuid) {
        sendEvent(new EventMessageModel(EventBusConstant.EB_TYPE_GROUP_ACTION_DISMISS, groupGuid));
    }

    public static void sendGroupExitEvent(String groupGuid) {
        sendEvent(new EventMessageModel(EventBusConstant.EB_TYPE_GROUP_ACTION_DELETE, groupGuid));
    }

    public static void sendGroupUsersAddEvent(List<GroupUserInfo> users) {
        sendEvent(new EventMessageModel(EventBusConstant.EB_TYPE_GROUP_USERS_ADD, users));
    }

    public static void sendNearMessageUpdateEvent() {
        sendEvent(new EventMessageModel(EventBusConstant.EB_TYPE_NEAR_CIRCLE_MESSAGE_UPDATE));
    }

    public static void sendCircleMessageUpdateEvent() {
        sendEvent(new EventMessageModel(EventBusConstant.EB_TYPE_CIRCLE_MESSAGE_UPDATE));
    }
}
