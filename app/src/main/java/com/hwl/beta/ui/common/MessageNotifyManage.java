package com.hwl.beta.ui.common;

import android.app.Service;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Vibrator;

import com.hwl.beta.HWLApp;
import com.hwl.beta.sp.UserSettingSP;

import java.util.Date;

public class MessageNotifyManage {
    public static final long MAX_PLAY_TIME = 10 * 1000;//10s内不要重新播放
    private static Vibrator vibrator;
    private static Ringtone ringtone;
    private static Date prevTime;

    static {
        if (vibrator == null)
            vibrator = (Vibrator) HWLApp.getContext().getSystemService(Service.VIBRATOR_SERVICE);
        if (ringtone == null)
            ringtone = RingtoneManager.getRingtone(HWLApp.getContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
    }

    public static void play(boolean isShield) {
        if (isShield) return;
        play();
    }

    public static void play() {
        if (!isPlay() || UserSettingSP.getMessageNotifySettingCloseAll()) return;
        // 震动 100ms
        if (vibrator != null && UserSettingSP.getMessageNotifySettingOpenShake()) {
            vibrator.vibrate(100);
        }
        if (ringtone != null && UserSettingSP.getMessageNotifySettingOpenSound()) {
            ringtone.play();
        }
        prevTime = new Date();
    }

    private static boolean isPlay() {
        if (prevTime == null) return true;
        long diff = new Date().getTime() - prevTime.getTime();
        if (diff >= MAX_PLAY_TIME) return true;
        return false;
    }

    public static void stop() {
        if (vibrator != null) {
            vibrator.cancel();
        }

        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
        }
    }
}