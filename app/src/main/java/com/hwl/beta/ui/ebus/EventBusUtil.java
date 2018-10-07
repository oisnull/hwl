package com.hwl.beta.ui.ebus;

import com.hwl.beta.ui.ebus.bean.EventUserEditModel;
import com.hwl.beta.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

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

    public static void sendTokenInvalidEvent() {
        sendStickyEvent(new EventMessageModel(EventBusConstant.EB_TYPE_TOKEN_INVALID_RELOGIN));
    }

    public static void sendUserHeadImageEditEvent(String userHeadImageUrl) {
        if (StringUtils.isBlank(userHeadImageUrl)) return;
        EventUserEditModel messageModel = new EventUserEditModel();
        messageModel.setUserHeadImageUrl(userHeadImageUrl);
        sendStickyEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_USER_HEAD_UPDATE, messageModel));
    }

    public static void sendUserSymbolEditEvent(String userSymbol) {
        if (StringUtils.isBlank(userSymbol)) return;
        EventUserEditModel messageModel = new EventUserEditModel();
        messageModel.setUserSymbol(userSymbol);
        sendStickyEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_USER_SYMBOL_UPDATE, messageModel));
    }

    public static void sendUserNameEditEvent(String userName) {
        if (StringUtils.isBlank(userName)) return;
        EventUserEditModel messageModel = new EventUserEditModel();
        messageModel.setUserName(userName);
        sendStickyEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_USER_NAME_UPDATE, messageModel));
    }

    public static void sendUserSexEditEvent(int userSex) {
        EventUserEditModel messageModel = new EventUserEditModel();
        messageModel.setUserSex(userSex);
        sendStickyEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_USER_SEX_UPDATE, messageModel));
    }

    public static void sendUserLifeNotesEditEvent(String userLifeNotes) {
        if (StringUtils.isBlank(userLifeNotes)) return;
        EventUserEditModel messageModel = new EventUserEditModel();
        messageModel.setUserLifeNotes(userLifeNotes);
        sendStickyEvent(new EventMessageModel
                (EventBusConstant.EB_TYPE_USER_LIFENOTES_UPDATE, messageModel));
    }
}
