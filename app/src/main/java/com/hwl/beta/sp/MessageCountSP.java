package com.hwl.beta.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.hwl.beta.HWLApp;
import com.hwl.beta.net.general.NetAppVersionInfo;
import com.hwl.beta.utils.StringUtils;

/**
 * Created by Administrator on 2018/2/5.
 */

public class MessageCountSP {
    private static final String MESSAGECOUNTPREFERENCE = "com.hwl.beta.message.count";
    private static final String FRIENDREQUESTCOUNT = "friendrequestcount";
    private static final String CHATMESSAGECOUNT = "chatmessagecount";
    private static final String NEARCIRCLEMESSAGECOUNT = "nearcirclemessagecount";
    private static final String CIRCLEMESSAGECOUNT = "circlemessagecount";
    private static final String APP_VERSION_COUNT = "app_version_count";
    private static final String APP_VERSION_INFO = "app_version_info";

    private static SharedPreferences getSP() {
        return HWLApp.getContext().getSharedPreferences(MESSAGECOUNTPREFERENCE, Context
                .MODE_PRIVATE);
    }

    public static int getAppVersionCount() {
        return getSP().getInt(APP_VERSION_COUNT, 0);
    }

    public static NetAppVersionInfo getAppVersionInfo() {
        String jsonString = getSP().getString(APP_VERSION_INFO, null);
        if (StringUtils.isBlank(jsonString)) {
            return null;
        } else {
            Gson gson = new Gson();
            return gson.fromJson(jsonString, NetAppVersionInfo.class);
        }
    }

    public static void setAppVersionInfo(NetAppVersionInfo appVersion) {
        final SharedPreferences.Editor editor = getSP().edit();
        if (appVersion != null) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(appVersion);
            editor.putString(APP_VERSION_INFO, jsonString);
            editor.putInt(APP_VERSION_COUNT, 1);
        } else {
            editor.putInt(APP_VERSION_COUNT, 0);
            editor.putString(APP_VERSION_INFO, "");
        }
        editor.commit();
    }

    public static int getFriendRequestCount() {
        return getSP().getInt(FRIENDREQUESTCOUNT, 0);
    }

    public static String getFriendRequestCountDesc() {
        int count = getFriendRequestCount();
        if (count <= 0) return "0";
        return count > 99 ? "99+" : count + "";
    }

    public static void setFriendRequestCount(int count) {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putInt(FRIENDREQUESTCOUNT, count);
        editor.commit();
    }

    public static void setFriendRequestIncreaseCount() {
        int count = getFriendRequestCount();
        setFriendRequestCount(count + 1);
    }

    public static void setFriendRequestReductionCount() {
        int count = getFriendRequestCount();
        if (count <= 0) return;
        setFriendRequestCount(count - 1);
    }

    public static String getChatMessageCountDesc() {
        int count = getChatMessageCount();
        if (count <= 0) return "0";
        return count > 99 ? "99+" : count + "";
    }

    public static int getChatMessageCount() {
        return getSP().getInt(CHATMESSAGECOUNT, 0);
    }

    public static void setChatMessageCount(int count) {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putInt(CHATMESSAGECOUNT, count);
        editor.commit();
    }

    public static void setNearCircleMessageCount(int count) {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putInt(NEARCIRCLEMESSAGECOUNT, count);
        editor.commit();
    }

    public static void setNearCircleMessageCountIncrease() {
        int count = getNearCircleMessageCount();
        count++;
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putInt(NEARCIRCLEMESSAGECOUNT, count);
        editor.commit();
    }

    public static void setNearCircleMessageCountReduce() {
        int count = getNearCircleMessageCount();
        count--;
        if (count <= 0) count = 0;
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putInt(NEARCIRCLEMESSAGECOUNT, count);
        editor.commit();
    }

    public static int getNearCircleMessageCount() {
        return getSP().getInt(NEARCIRCLEMESSAGECOUNT, 0);
    }

    public static void setCircleMessageCount(int count) {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putInt(CIRCLEMESSAGECOUNT, count);
        editor.commit();
    }

    public static void setCircleMessageCountIncrease() {
        int count = getCircleMessageCount();
        count++;
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putInt(CIRCLEMESSAGECOUNT, count);
        editor.commit();
    }

    public static void setCircleMessageCountReduce() {
        int count = getCircleMessageCount();
        count--;
        if (count <= 0) count = 0;
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putInt(CIRCLEMESSAGECOUNT, count);
        editor.commit();
    }

    public static int getCircleMessageCount() {
        return getSP().getInt(CIRCLEMESSAGECOUNT, 0);
    }

    public static String getCircleMessageCountDesc() {
        int count = getCircleMessageCount();
        if (count <= 0) return "0";
        return count > 99 ? "99+" : count + "";
    }
}
