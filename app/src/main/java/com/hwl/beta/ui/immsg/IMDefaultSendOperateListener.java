package com.hwl.beta.ui.immsg;

import android.os.Handler;
import android.os.Looper;

import com.hwl.beta.AppConfig;
import com.hwl.beta.ui.ebus.EventBusUtil;

import java.util.logging.Logger;

public class IMDefaultSendOperateListener<T> {

    private static Logger log = Logger.getLogger(AppConfig.IM_DEBUG_TAG);
    private String prefix;
    private boolean isRunMainThread = false;
    private Handler mainHandler;

    public IMDefaultSendOperateListener() {
        this("",false);
    }

    public IMDefaultSendOperateListener(boolean isRunMainThread) {
        this("",isRunMainThread);
    }

    public IMDefaultSendOperateListener(String prefix) {
        this(prefix,false);
    }

    public IMDefaultSendOperateListener(String prefix, boolean isRunMainThread) {
        this.prefix = prefix;
        this.isRunMainThread = isRunMainThread;
        if (isRunMainThread)
            mainHandler = new Handler(Looper.getMainLooper());
    }

    protected String getSendDesc() {
        return String.format("send %s message operate status", (this.prefix==null?"":this.prefix));
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

    public final void sessionInvalid() {
        log.info(getSendDesc() + " : session invalid");
        EventBusUtil.sendTokenInvalidEvent();
    }

    public final void listenSuccess(final T t) {
        log.info(getSendDesc() + " listen success");
        if (mainHandler != null)
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    success2(t);
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

    public void success2(T t) {
    }

    public void failed2(String message) {
    }

}
