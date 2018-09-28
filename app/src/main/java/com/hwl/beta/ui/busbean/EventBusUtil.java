package com.hwl.beta.ui.busbean;

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
}
