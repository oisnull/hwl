package com.hwl.beta.ui.immsg;

import com.hwl.beta.AppConfig;

import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;

public class IMClientMonitor {

    static Logger log = Logger.getLogger(AppConfig.IM_DEBUG_TAG);
    static int MONITOR_TIME_INTERNAL = 5 * 1000; // s

    private Timer monTimer = null;
    private Boolean isRunning = false;

    private IMClientMonitor() {
    }

    private static class SingletonHolder {
        private final static IMClientMonitor instance = new IMClientMonitor();
    }

    public static IMClientMonitor getInstance() {
        return SingletonHolder.instance;
    }

    public Boolean getStatus() {
        return isRunning;
    }

    public void run() {
        if (isRunning) return;
        isRunning = true;
        monTimer = new Timer();
        monTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.info("im client monitor is running ...");
                IMClientEntry.connectServer();
            }
        }, MONITOR_TIME_INTERNAL, MONITOR_TIME_INTERNAL);
    }

    public void stop() {
        log.info("im client monitor stop ...");
        if (monTimer != null) {
            monTimer.cancel();
            monTimer = null;
        }
        isRunning = false;
    }

}