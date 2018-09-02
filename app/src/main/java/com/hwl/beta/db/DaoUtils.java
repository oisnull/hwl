package com.hwl.beta.db;


import com.hwl.beta.HWLApp;
import com.hwl.beta.db.manage.ChatGroupMessageManager;
import com.hwl.beta.db.manage.ChatRecordMessageManager;
import com.hwl.beta.db.manage.ChatUserMessageManager;
import com.hwl.beta.db.manage.CircleManager;
import com.hwl.beta.db.manage.CircleMessageManager;
import com.hwl.beta.db.manage.FriendManager;
import com.hwl.beta.db.manage.FriendRequestManager;
import com.hwl.beta.db.manage.GroupInfoManager;
import com.hwl.beta.db.manage.GroupUserInfoManager;
import com.hwl.beta.db.manage.NearCircleManager;
import com.hwl.beta.db.manage.NearCircleMessageManager;

/**
 * Created by adminstrator on 2016/6/16.
 */
public class DaoUtils {
    private static FriendManager friendManager;
    private static FriendRequestManager friendRequestManager;
    private static ChatUserMessageManager chatUserMessageManager;
    private static ChatRecordMessageManager chatRecordMessageManager;
    private static ChatGroupMessageManager chatGroupMessageManager;
    private static GroupInfoManager groupInfoManager;
    private static GroupUserInfoManager groupUserInfoManager;
    private static NearCircleManager nearCircleManager;
    private static NearCircleMessageManager nearCircleMessageManager;
    private static CircleManager circleManager;
    private static CircleMessageManager circleMessageManager;

    public static void closeDB() {
        friendManager = null;
        friendRequestManager = null;
        chatUserMessageManager = null;
        chatRecordMessageManager = null;
        chatGroupMessageManager = null;
        groupInfoManager = null;
        groupUserInfoManager = null;
        nearCircleMessageManager = null;
        circleManager = null;
        circleMessageManager = null;
        nearCircleManager = null;
        DaoManager.getInstance().closeDataBase();
    }

    public static synchronized CircleMessageManager getCircleMessageManagerInstance() {
        if (circleMessageManager == null) {
            circleMessageManager = new CircleMessageManager(HWLApp.getContext());
        }
        return circleMessageManager;
    }

    public static synchronized CircleManager getCircleManagerInstance() {
        if (circleManager == null) {
            circleManager = new CircleManager(HWLApp.getContext());
        }
        return circleManager;
    }

    public static synchronized NearCircleMessageManager getNearCircleMessageManagerInstance() {
        if (nearCircleMessageManager == null) {
            nearCircleMessageManager = new NearCircleMessageManager(HWLApp.getContext());
        }
        return nearCircleMessageManager;
    }

    public static synchronized NearCircleManager getNearCircleManagerInstance() {
        if (nearCircleManager == null) {
            nearCircleManager = new NearCircleManager(HWLApp.getContext());
        }
        return nearCircleManager;
    }

    public static synchronized ChatGroupMessageManager getChatGroupMessageManagerInstance() {
        if (chatGroupMessageManager == null) {
            chatGroupMessageManager = new ChatGroupMessageManager(HWLApp.getContext());
        }
        return chatGroupMessageManager;
    }

    public static synchronized FriendManager getFriendManagerInstance() {
        if (friendManager == null) {
            friendManager = new FriendManager(HWLApp.getContext());
        }
        return friendManager;
    }

    public static synchronized FriendRequestManager getFriendRequestManagerInstance() {
        if (friendRequestManager == null) {
            friendRequestManager = new FriendRequestManager(HWLApp.getContext());
        }
        return friendRequestManager;
    }

    public static synchronized ChatUserMessageManager getChatUserMessageManagerInstance() {
        if (chatUserMessageManager == null) {
            chatUserMessageManager = new ChatUserMessageManager(HWLApp.getContext());
        }
        return chatUserMessageManager;
    }

    public static synchronized ChatRecordMessageManager getChatRecordMessageManagerInstance() {
        if (chatRecordMessageManager == null) {
            chatRecordMessageManager = new ChatRecordMessageManager(HWLApp.getContext());
        }
        return chatRecordMessageManager;
    }

    public static synchronized GroupInfoManager getGroupInfoManagerInstance() {
        if (groupInfoManager == null) {
            groupInfoManager = new GroupInfoManager(HWLApp.getContext());
        }
        return groupInfoManager;
    }

    public static synchronized GroupUserInfoManager getGroupUserInfoManagerInstance() {
        if (groupUserInfoManager == null) {
            groupUserInfoManager = new GroupUserInfoManager(HWLApp.getContext());
        }
        return groupUserInfoManager;
    }
}