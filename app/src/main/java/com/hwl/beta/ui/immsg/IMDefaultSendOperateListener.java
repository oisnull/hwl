package com.hwl.beta.ui.immsg;

import com.hwl.beta.AppConfig;

import java.util.logging.Logger;

public class IMDefaultSendOperateListener {

    static Logger log = Logger.getLogger(AppConfig.IM_DEBUG_TAG);

    public void unconnect() {
        log.info("Android client send operate : unconnect server ");
    }

    public void notSendToServer() {
        log.info("Android client send operate : message not send to server ");
    }

    public void success() {

    }

    public void failed(String message) {
        log.info("Android client send operate failed : " + message);
    }

    public void sessionidInvaild() {
        log.info("Android client send operate : sessionid invalid");
        //提示用户退出，需要重新登录

    }
}
