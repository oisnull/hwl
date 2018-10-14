package com.hwl.beta.ui.immsg;

import android.os.Handler;
import android.os.Looper;

import com.hwl.beta.AppConfig;
import com.hwl.beta.ui.ebus.EventBusUtil;

import java.util.logging.Logger;

public class IMDefaultSendOperateListener {

    static Logger log = Logger.getLogger(AppConfig.IM_DEBUG_TAG);
    private String prefix;
    private boolean isRunMainThread = false;
    private Handler mainHandler;

    public IMDefaultSendOperateListener() {
        this(false);
    }

    public IMDefaultSendOperateListener(boolean isRunMainThread) {
        this.isRunMainThread = isRunMainThread;
        if (isRunMainThread)
            mainHandler = new Handler(Looper.getMainLooper());
    }

    public IMDefaultSendOperateListener(String prefix) {
        this(false);
        this.prefix = prefix;
    }

    public IMDefaultSendOperateListener(String prefix, boolean isRunMainThread) {
        this(isRunMainThread);
        this.prefix = prefix;
    }

    protected String getSendDesc() {
        return String.format("send %s message operate status", this.prefix);
    }

    public void unconnect() {
        log.info(getSendDesc() + " : unconnect server ");
    }

    public final void sendToServerSuccess() {
        log.info(getSendDesc() + " : message send to server success");
        if (mainHandler != null)
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    success1();
                }
            });
    }

    public final void sendToServerFaild() {
        log.info(getSendDesc() + " : message send to server failed");
        if (mainHandler != null)
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    failed1();
                }
            });
    }

    public final void sessionidInvaild() {
        log.info(getSendDesc() + " : sessionid invalid");
        EventBusUtil.sendTokenInvalidEvent();
    }

    public final void listenSucess() {
        log.info(getSendDesc() + " listen success");
        if (mainHandler != null)
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    success2();
                }
            });
    }

    public final void listenFailed(final String message) {
        log.info(getSendDesc() + " listen failed : " + message);
        if (mainHandler != null)
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    failed2(message);
                }
            });
    }


    public void success1() {
    }

    public void failed1() {
    }

    public void success2() {
    }

    public void failed2(String message) {
    }

}
