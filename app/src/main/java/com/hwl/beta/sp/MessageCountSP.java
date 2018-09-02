package com.hwl.beta.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.hwl.beta.HWLApp;

/**
 * Created by Administrator on 2018/2/5.
 */

public class MessageCountSP {
    private static final String MESSAGECOUNTPREFERENCE = "com.hwl.beta.message.count";
    private static final String FRIENDREQUESTCOUNT = "friendrequestcount";
    private static final String CHATMESSAGECOUNT = "chatmessagecount";
    private static final String NEARCIRCLEMESSAGECOUNT = "nearcirclemessagecount";
    private static final String CIRCLEMESSAGECOUNT = "circlemessagecount";

    private static SharedPreferences getSP() {
        return HWLApp.getContext().getSharedPreferences(MESSAGECOUNTPREFERENCE, Context.MODE_PRIVATE);
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
