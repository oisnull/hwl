package com.hwl.beta.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.hwl.beta.HWLApp;

public class UserSettingSP {
    private static final String USERSETTINGPREFERENCE = "com.hwl.beta.usersetting";
    //消息提示设置
    private static final String MESSAGENOTIFYSETTING_CLOSEALL = "messagenotifysetting_closeall";
    private static final String MESSAGENOTIFYSETTING_OPENSOUND = "messagenotifysetting_opensound";
    private static final String MESSAGENOTIFYSETTING_OPENSHAKE = "messagenotifysetting_openshake";
    //用户隐私设置
//    private static final String PRAVICYSETTING_HIDEUSERHEADIMAGE = "pravicysetting_hideuserheadimage";
    private static final String PRAVICYSETTING_REJECTCHAT = "pravicysetting_rejectchat";

    private static SharedPreferences getSP() {
        return HWLApp.getContext().getSharedPreferences(USERSETTINGPREFERENCE, Context.MODE_PRIVATE);
    }

    public static boolean getPrivacySettingRejectChat() {
        return getSP().getBoolean(PRAVICYSETTING_REJECTCHAT, false);
    }

    public static void setPrivaySettingRejectChat(boolean isReject) {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putBoolean(PRAVICYSETTING_REJECTCHAT, isReject);
        editor.commit();
    }

//    public static boolean getPrivacySettingHideUserheadimage() {
//        return getSP().getBoolean(PRAVICYSETTING_HIDEUSERHEADIMAGE, false);
//    }
//
//    public static void setPrivaySettingHideUserheadimage(boolean isHide) {
//        final SharedPreferences.Editor editor = getSP().edit();
//        editor.putBoolean(PRAVICYSETTING_HIDEUSERHEADIMAGE, isHide);
//        editor.commit();
//    }

    public static boolean getMessageNotifySettingOpenShake() {
        return getSP().getBoolean(MESSAGENOTIFYSETTING_OPENSHAKE, true);
    }

    public static void setMessageNotifySettingOpenShake(boolean isClose) {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putBoolean(MESSAGENOTIFYSETTING_OPENSHAKE, isClose);
        editor.commit();
    }

    public static boolean getMessageNotifySettingOpenSound() {
        return getSP().getBoolean(MESSAGENOTIFYSETTING_OPENSOUND, true);
    }

    public static void setMessageNotifySettingOpenSound(boolean isClose) {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putBoolean(MESSAGENOTIFYSETTING_OPENSOUND, isClose);
        editor.commit();
    }

    public static boolean getMessageNotifySettingCloseAll() {
        return getSP().getBoolean(MESSAGENOTIFYSETTING_CLOSEALL, false);
    }

    public static void setMessageNotifySettingCloseAll(boolean isClose) {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putBoolean(MESSAGENOTIFYSETTING_CLOSEALL, isClose);
        editor.commit();
    }
}
