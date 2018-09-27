package com.hwl.beta.ui.immsg;

import com.hwl.beta.AppConfig;

import java.util.logging.Logger;

public class IMDefaultSendOperateListener {

    static Logger log = Logger.getLogger(AppConfig.IM_DEBUG_TAG);
    private String prefix;

    public IMDefaultSendOperateListener(){}

    public IMDefaultSendOperateListener(String prefix){
        this.prefix=prefix;
    }

    protected string getSendDesc(){
        return String.format("send %s message operate status",this.prefix);
    }

    public void unconnect() {
        log.info(getSendDesc()+ " : unconnect server ");
    }

    public void notSendToServer() {
        log.info(getSendDesc()+" : message not send to server ");
    }

    public void success() {

    }

    public void failed(String message) {
        log.info(getSendDesc()+" failed : " + message);
    }

    public void sessionidInvaild() {
        log.info(getSendDesc()+" : sessionid invalid");
        //提示用户退出，需要重新登录

    }
}
