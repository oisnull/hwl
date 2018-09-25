package com.hwl.beta.ui.immsg;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
    static int CHECK_TIME_INTERNAL = 5; // s

    final static ExecutorService executorService = Executors.newSingleThreadExecutor();
    final static ScheduledExecutorService checkConnectExecutor = Executors
            .newSingleThreadScheduledExecutor();
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
        if (!NetworkUtils.isConnected())
            return;

        if (checkConnectExecutor.isShutdown()) {
            checkConnectExecutor.schedule(new Runnable() {
                @Override
                public void run() {
                    connectServer();
                }
            }, CHECK_TIME_INTERNAL, TimeUnit.SECONDS);
        }
    }

    public static String getServerAddress() {
        return launcher.getServerAddress();
    }

    public static void stopCheckConnect() {
        if (checkConnectExecutor.isShutdown())
            return;
        checkConnectExecutor.shutdown();
        // stopHeartbeat();
    }

    public static void connectServer() {
        if (launcher.isConnected()) {
            stopCheckConnect();
            return;
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                launcher.connect();
            }
        });
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

    public static void sendUserValidateMessage(final Long userId, final String userToken) {
        final IMDefaultSendOperateListener operateListener = new IMDefaultSendOperateListener();
        commomExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {

                UserValidateSend request = new UserValidateSend(userId, userToken, sendCallback);
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

    private static void startHeartbeat(String sessionId) {
        MessageRequestHeadOperate.setSessionid(sessionId);
        IMClientHeartbeatTimer.getInstance().run(new TimerTask() {
            @Override
            public void run() {
                messageOperate.send(new HeartBeatMessageSend());
            }
        });
    }

    private static void stopHeartbeat() {
        IMClientHeartbeatTimer.getInstance().stop();
        launcher.stop();
    }
}