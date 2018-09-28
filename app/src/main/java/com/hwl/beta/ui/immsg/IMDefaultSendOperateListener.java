package com.hwl.beta.ui.immsg;

import com.hwl.beta.AppConfig;
import com.hwl.beta.ui.busbean.EventBusUtil;

import java.util.logging.Logger;

public class IMDefaultSendOperateListener {

    static Logger log = Logger.getLogger(AppConfig.IM_DEBUG_TAG);
    private String prefix;

    public IMDefaultSendOperateListener() {
    }

    public IMDefaultSendOperateListener(String prefix) {
        this.prefix = prefix;
    }

    protected String getSendDesc() {
        return String.format("send %s message operate status", this.prefix);
    }

    public void unconnect() {
        log.info(getSendDesc() + " : unconnect server ");
    }

    public void sendToServerSuccess() {
        log.info(getSendDesc() + " : message not send to server ");
    }

    public void sendToServerFaild() {
        log.info(getSendDesc() + " : message send to server success");
    }

    public void success() {

    }

    public void failed(String message) {
        log.info(getSendDesc() + " failed : " + message);
    }

    public void sessionidInvaild() {
        log.info(getSendDesc() + " : sessionid invalid");
        EventBusUtil.sendTokenInvalidEvent();
    }
}
