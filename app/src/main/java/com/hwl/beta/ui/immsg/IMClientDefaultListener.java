package com.hwl.beta.ui.immsg;

import com.hwl.beta.AppConfig;
import com.hwl.beta.sp.UserSP;
import com.hwl.im.client.IMClientListener;

import java.util.logging.Logger;

public class IMClientDefaultListener implements IMClientListener {

    static Logger log = Logger.getLogger(AppConfig.IM_DEBUG_TAG);

    @Override
    public void onBuildConnectionSuccess(String serverAddress) {
        log.info("Client listen : connected to server " + serverAddress + " successfully.");
        IMClientEntry.stopCheckConnect();
        log.info("Client listen : send user validate message userid(" + UserSP.getUserId() + ") " +
                "usertoken(" + UserSP.getUserToken() + ")");
        IMClientEntry.sendUserValidateMessage();
    }

    @Override
    public void onBuildConnectionError(String serverAddress, String errorInfo) {
        log.info("Client listen : connected to server " + serverAddress + " failure. info :" +
                errorInfo);
        IMClientEntry.startCheckConnect();
    }

    @Override
    public void onClosed(String serverAddress) {
        log.info("Client listen : im server " + serverAddress + " closed");
        IMClientEntry.startCheckConnect();
    }

    @Override
    public void onDisconnected(String serverAddress) {
        log.info("Client listen : im server " + serverAddress + " disconnect");
        IMClientEntry.startCheckConnect();
    }

    @Override
    public void onError(String serverAddress, String errorInfo) {
        log.info("Client listen : an error occurred on the client . info : " + errorInfo);
        IMClientEntry.startCheckConnect();
    }
}
