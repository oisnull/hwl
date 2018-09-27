package com.hwl.beta.ui.immsg;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.hwl.beta.AppConfig;
import com.hwl.beta.ui.immsg.listen.UserValidateListen;
import com.hwl.beta.ui.immsg.send.UserValidateSend;
import com.hwl.beta.utils.NetworkUtils;
import com.hwl.im.client.ClientMessageOperate;
import com.hwl.im.client.IMClientHeartbeatTimer;
import com.hwl.im.client.IMClientLauncher;
import com.hwl.beta.ui.immsg.listen.ChatGroupMessageListen;
import com.hwl.beta.ui.immsg.listen.ChatUserMessageListen;
import com.hwl.beta.ui.immsg.send.HeartBeatMessageSend;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.immode.MessageRequestHeadOperate;
import com.hwl.im.improto.ImMessageType;

public class IMClientEntry {

    static Logger log = Logger.getLogger(AppConfig.IM_DEBUG_TAG);
    static int CHECK_TIME_INTERNAL = 5 * 1000; // s

    final static ExecutorService executorService = Executors.newSingleThreadExecutor();
    static Timer checkConnectTimer;
    static boolean checkConnectTimerRuning = false;
    final static ClientMessageOperate messageOperate = new ClientMessageOperate();
    final static IMClientLauncher launcher = new IMClientLauncher(AppConfig.IM_HOST, AppConfig
            .IM_PORT);

    static {
        initListenExecutor();
        launcher.registerAction(messageOperate);
        launcher.registerClientListener(new IMClientDefaultListener());
    }

    static void initListenExecutor() {
        messageOperate.registerListenExecutor(ImMessageType.ChatUser, new ChatUserMessageListen());
        messageOperate.registerListenExecutor(ImMessageType.ChatGroup, new ChatGroupMessageListen
                ());
    }

    public static void startCheckConnect() {
        if (!NetworkUtils.isConnected() || checkConnectTimerRuning)
            return;
        checkConnectTimer = new Timer();
        checkConnectTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                connectServer();
                checkConnectTimerRuning = true;
            }
        }, CHECK_TIME_INTERNAL, CHECK_TIME_INTERNAL);
    }

    public static String getServerAddress() {
        return launcher.getServerAddress();
    }

    public static void stopCheckConnect() {
        checkConnectTimerRuning = false;
        if (checkConnectTimer != null) {
            checkConnectTimer.cancel();
            checkConnectTimer.purge();
            checkConnectTimer = null;
        }
    }

    public static void connectServer() {
        log.info("Client listen : start connect to server " + launcher.getServerAddress() + " ...");
        if (launcher.isConnected()) {
            stopCheckConnect();
            log.info("Client listen : connected to server " + launcher.getServerAddress() + " and" +
                    " stop auto check");
            return;
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                launcher.connect();
            }
        });
    }

    public static void disconnectServer() {
        launcher.stop();
    }

    static void commomExec(final IMDefaultSendOperateListener operateListener, final
    DefaultConsumer<DefaultConsumer<Boolean>> sendConsumer) {
        if (!launcher.isConnected()) {
            operateListener.unconnect();
            return;
        }
        DefaultConsumer<Boolean> sendCallback = new DefaultConsumer<Boolean>() {
            @Override
            public void accept(Boolean succ) {
                if (!succ) {
                    operateListener.notSendToServer();
                }
            }
        };

        sendConsumer.accept(sendCallback);
    }


    public static void sendUserValidateMessage() {
        sendUserValidateMessage(new IMDefaultSendOperateListener("UserValidateMessage"));
    }

    static void sendUserValidateMessage(final IMDefaultSendOperateListener operateListener) {
        commomExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                UserValidateSend request = new UserValidateSend(sendCallback);
                UserValidateListen response = new UserValidateListen(new DefaultConsumer<String>() {
                    @Override
                    public void accept(String sess) {
                        operateListener.success();
                        startHeartbeat(sess);
                    }
                }, new DefaultConsumer<String>() {
                    @Override
                    public void accept(String msg) {
                        operateListener.failed(msg);
                        operateListener.sessionidInvaild();
                        stopHeartbeat();
                    }
                });
                messageOperate.send(request, response);
            }
        });
    }

    static void sendHeartBeatMessage() {
        final IMDefaultSendOperateListener operateListener = new IMDefaultSendOperateListener
                ("HeartBeatMessage");
        commomExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                HeartBeatMessageSend request = new HeartBeatMessageSend(sendCallback);
                messageOperate.send(request);
            }
        });
    }

    static void startHeartbeat(String sessionId) {
        log.info("Client listen : start send heart package, sessionid : " + sessionId);
        MessageRequestHeadOperate.setSessionid(sessionId);
        IMClientHeartbeatTimer.getInstance().run(new TimerTask() {
            @Override
            public void run() {
                sendHeartBeatMessage();
            }
        });
    }

    static void stopHeartbeat() {
        log.info("Client listen : stop send heart package");
        IMClientHeartbeatTimer.getInstance().stop();
        launcher.stop();
    }
}