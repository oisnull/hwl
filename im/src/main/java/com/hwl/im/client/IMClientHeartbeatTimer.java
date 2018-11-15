package com.hwl.im.client;

import com.hwl.im.ImCoreConfig;

import java.util.Timer;
import java.util.TimerTask;

public class IMClientHeartbeatTimer {
    private static Timer timer = null;
    private static IMClientHeartbeatTimer instance = null;

    private IMClientHeartbeatTimer() {
    }

    public static IMClientHeartbeatTimer getInstance() {
        if (instance == null)
            instance = new IMClientHeartbeatTimer();

        return instance;
    }

    public void run(TimerTask task) {
        timer = new Timer();
        long internal = (ImCoreConfig.IDLE_TIMEOUT_SECONDS - 2) * 1000;
        timer.schedule(task, internal, internal);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
