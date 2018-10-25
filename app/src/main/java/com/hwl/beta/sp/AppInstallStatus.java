package com.hwl.beta.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.hwl.beta.HWLApp;

public class AppInstallStatus {
    private static final String APPINSTALLSTATUSREFERENCE = "com.hwl.beta.install";
    private static final String ISFIRSTINSTALL = "isfirstinstall";
    private static final String SOFTINPUTHEIGHT = "softinputheight";


    private static SharedPreferences getSP() {
        return HWLApp.getContext().getSharedPreferences(APPINSTALLSTATUSREFERENCE, Context
                .MODE_PRIVATE);
    }

    public static void setFirst() {
        if (isFirst()) return;
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putBoolean(ISFIRSTINSTALL, false).apply();
    }

    public static boolean isFirst() {
        return getSP().getBoolean(ISFIRSTINSTALL, false);
    }

    public static int getSoftInputHeight() {
        return getSP().getInt(SOFTINPUTHEIGHT, 0);
    }

    public static void setSoftInputHeight(int softInputHeight) {
        int height = getSoftInputHeight();
        if (height > 0 && softInputHeight == height) return;
        if (softInputHeight <= 0) return;
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putInt(SOFTINPUTHEIGHT, softInputHeight).apply();
    }
}
