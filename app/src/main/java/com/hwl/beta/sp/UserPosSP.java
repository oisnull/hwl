package com.hwl.beta.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.hwl.beta.HWLApp;
import com.hwl.beta.utils.StringUtils;

/**
 * Created by Administrator on 2018/1/20.
 */

public class UserPosSP {

    private static final String USERPOSPREFERENCE = "com.hwl.beta.userinfo.pos";
    private static final String USERPOSID = "userposid";
    private static final String GROUPGUID = "groupguid";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String COUNTRY = "country";
    private static final String PROVINCE = "province";
    private static final String CITY = "city";
    private static final String DISTRICT = "district";
    private static final String STREET = "street";
    private static final String ADDR = "addr";
    private static final String LASTNEARCIRCLEID = "lastNearCircleId";

    private static SharedPreferences getSP() {
        return HWLApp.getContext().getSharedPreferences(USERPOSPREFERENCE, Context.MODE_PRIVATE);
    }

    public static boolean isExistsPosInfo() {
        if (getLatitude() < 0 || getLongitude() < 0) {
            return false;
        }
        return true;
    }

    public static void setUserPos(float latitude, float longitude, String country, String
            province, String city, String district, String street, String addr) {
        setUserPos(0, "", latitude, longitude, country, province, city, district, street, addr);
    }

    public static void setUserPos(int userPosId, String groupGuid) {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putInt(USERPOSID, userPosId);
        editor.putString(GROUPGUID, groupGuid);
        editor.commit();
    }

    public static void setUserPos(int userPosId,
                                  String groupGuid,
                                  double latitude,
                                  double longitude,
                                  String country,
                                  String province,
                                  String city,
                                  String district,
                                  String street,
                                  String addr) {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putInt(USERPOSID, userPosId);
        editor.putString(GROUPGUID, groupGuid);

        editor.putFloat(LATITUDE, (float) latitude);
        editor.putFloat(LONGITUDE, (float) longitude);

        editor.putString(COUNTRY, country);
        editor.putString(PROVINCE, province);
        editor.putString(CITY, city);
        editor.putString(DISTRICT, district);
        editor.putString(STREET, street);
        editor.putString(ADDR, addr);
        editor.commit();
    }

    public static void setLastNearCircleId(long lastNearCircleId) {
        if (getLastNearCircleId() >= lastNearCircleId) {
            return;
        }
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putLong(LASTNEARCIRCLEID, lastNearCircleId);
        editor.commit();
    }

    public static long getLastNearCircleId() {
        return getSP().getLong(LASTNEARCIRCLEID, 0);
    }

    public static int getUserPosId() {
        return getSP().getInt(USERPOSID, 0);
    }

    public static String getGroupGuid() {
        return getSP().getString(GROUPGUID, null);
    }

    public static float getLatitude() {
        return getSP().getFloat(LATITUDE, -1);
    }

    public static float getLongitude() {
        return getSP().getFloat(LONGITUDE, -1);
    }

    public static String getPosDesc() {
        String addr = getSP().getString(ADDR, null);
        if (StringUtils.isBlank(addr)) {
            addr = "我的附近";
        }
        return addr;
    }

    public static String getPublishDesc() {
        final SharedPreferences prefs = getSP();
        String street = prefs.getString(STREET, null);
        if (StringUtils.isBlank(street)) {
            street = prefs.getString(DISTRICT, null);
        }
        return street + "附近";
    }

    public static String getNearDesc() {
        final SharedPreferences prefs = getSP();
        String street = prefs.getString(STREET, null);
        if (StringUtils.isBlank(street)) {
            street = prefs.getString(DISTRICT, null);
        }
        if (StringUtils.isBlank(street)) {
            street = "我的";
        }
        return street + "附近";
    }

    public static void clearPosInfo() {
        clearPosInfo(false);
    }

    public static void clearPosInfo(boolean keepGroup) {
        SharedPreferences.Editor editor = getSP().edit();
        if (keepGroup) {
            editor.putInt(USERPOSID, 0);
//            editor.putString(GROUPGUID, groupGuid);
            editor.putFloat(LATITUDE, 0);
            editor.putFloat(LONGITUDE, 0);

            editor.putString(COUNTRY, null);
            editor.putString(PROVINCE, null);
            editor.putString(CITY, null);
            editor.putString(DISTRICT, null);
            editor.putString(STREET, null);
            editor.putString(ADDR, null);
        } else {
            editor.clear();
        }
        editor.commit();
    }
}
