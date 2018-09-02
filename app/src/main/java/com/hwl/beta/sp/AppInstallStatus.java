package com.hwl.beta.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.hwl.beta.HWLApp;

public class AppInstallStatus {
    private static final String APPINSTALLSTATUSREFERENCE = "com.hwl.beta.install";
    private static final String ISFIRSTINSTALL = "isfirstinstall";


    private static SharedPreferences getSP() {
        return HWLApp.getContext().getSharedPreferences(APPINSTALLSTATUSREFERENCE, Context.MODE_PRIVATE);
    }

    public static void setFrist() {
        if (isFrist()) return;
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putBoolean(ISFIRSTINSTALL, false).apply();
    }

    public static boolean isFrist() {
        return getSP().getBoolean(ISFIRSTINSTALL, false);
    }
}
