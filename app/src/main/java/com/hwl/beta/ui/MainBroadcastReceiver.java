package com.hwl.beta.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2018/4/1.
 */

public class MainBroadcastReceiver extends BroadcastReceiver {

    public static final String BD_MAIN_NAME = "com.hwl.beta.broadcast.main";
    public static final String BD_MAIN_TYPE_NAME = "bd_type";

    public static final int BD_TYPE_MAX_MESSAGECOUNT = 1;
    public static final int BD_TYPE_UPDATE_FRIENDREQUESTCOUNT = 2;
    public static final int BD_TYPE_FRIEND_REQUEST = 3;
    public static final int BD_TYPE_CHAT_FRIEND_REQUEST = 4;
    public static final int BD_TYPE_CHAT_USER_MESSAGE = 5;
    public static final int BD_TYPE_CHAT_GROUP_MESSAGE = 6;
    public static final int BD_TYPE_DELETE_FRIEND = 7;
    public static final int BD_TYPE_ADD_NEARINFO = 8;
    public static final int BD_TYPE_NEARINFO_REFRESH = 9;
    protected Context context;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (!intent.hasExtra(BD_MAIN_TYPE_NAME)) {
            return;
        }
        switch (intent.getIntExtra(BD_MAIN_TYPE_NAME, 0)) {
            case BD_TYPE_FRIEND_REQUEST:
                break;
        }
    }
}
