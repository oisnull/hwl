package com.hwl.beta.ui.immsg;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Logger;

import com.hwl.beta.ui.immsg.listen.UserValidateListen;
import com.hwl.beta.ui.immsg.send.UserValidateSend;
import com.hwl.im.client.ClientMessageOperate;
import com.hwl.im.client.IMClientHeartbeatTimer;
import com.hwl.im.client.IMClientLauncher;
import com.hwl.im.client.IMClientListener;
import com.hwl.beta.ui.immsg.listen.ChatGroupMessageListen;
import com.hwl.beta.ui.immsg.listen.ChatUserMessageListen;
import com.hwl.beta.ui.immsg.send.HeartBeatMessageSend;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.immode.MessageRequestHeadOperate;
import com.hwl.im.improto.ImMessageType;

public class IMClientEntry {

    static Logger log = Logger.getLogger(IMClientEntry.class.getName());
    static int CHECK_TIME_INTERNAL = 5; // s

    final static ExecutorService executorService = Executors.newSingleThreadExecutor();
    final static ScheduledExecutorService checkConnectExecutor = Executors
            .newSingleThreadScheduledExecutor();
    final static ClientMessageOperate messageOperate = new ClientMessageOperate();
    final static IMClientLauncher launcher = new IMClientLauncher("localhost", 8081);

    static {
        initListenExecutor();
        launcher.registerAction(messageOperate);
    }

    static void initListenExecutor() {
        messageOperate.registerListenExecutor(ImMessageType.ChatUser, new ChatUserMessageListen());
        messageOperate.registerListenExecutor(ImMessageType.ChatGroup, new ChatGroupMessageListen
                ());
    }

    public static void registerClientListener(IMClientListener clientListener) {
        if (clientListener != null)
            launcher.registerClientListener(clientListener);
    }

    public static void startCheckConnect() {
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

    static void commomExec(final DefaultSendOperateListener operateListener, final
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

    public static void sendUserValidateMessage(final Long userId, final String userToken,
                                               final DefaultSendOperateListener operateListener) {
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

    public static class DefaultSendOperateListener {

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
        }

    }

    public static void main(String[] args) {
        // IMClientEntry.connectServer();
        // IMClientEntry.sendUserValidateMessage(10000L, "123456", new
        // DefaultSendOperateListener() {
        // @Override
        // public void success() {

        // }
        // });
    }
}