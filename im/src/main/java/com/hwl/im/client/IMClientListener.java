package com.hwl.im.client;

public interface IMClientListener {

    void onBuildConnectionSuccess(String serverAddress);

    void onBuildConnectionError(String serverAddress, String errorInfo);

    void onClosed(String serverAddress);

    void onDisconnected(String serverAddress);

    void onError(String serverAddress, String errorInfo);

}