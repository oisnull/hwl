package com.hwl.beta.ui.immsg;

import java.util.TimerTask;
import java.util.logging.Logger;

import com.hwl.beta.AppConfig;
import com.hwl.beta.ui.immsg.listen.AddFriendMessageListen;
import com.hwl.beta.ui.immsg.listen.UserValidateListen;
import com.hwl.beta.ui.immsg.send.AddFriendMessageSend;
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
import com.hwl.imcore.improto.ImMessageType;

public class IMClientEntry {

    static Logger log = Logger.getLogger(AppConfig.IM_DEBUG_TAG);
//    static int CHECK_TIME_INTERNAL = 5 * 1000; // s

    static Thread imThread = null;
    static boolean isIMThreadRuning = false;
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
        messageOperate.registerListenExecutor(ImMessageType.AddFriend, new AddFriendMessageListen
                ());
    }

    public static String getServerAddress() {
        return launcher.getServerAddress();
    }

    public static void connectServer() {
        if(!NetworkUtils.isConnected()){
            log.info("Client listen : network is disconnect");
            return;
        }

        if (launcher.isConnected()) {
            log.info("Client listen : im is connected");
            // log.info("Client listen : connected to server " + launcher.getServerAddress() + " and" +
            //         " stop auto check");
            return;
        }
        
        if (isIMThreadRuning) return;
        isIMThreadRuning = true;

        log.info("Client listen : start connect to server " + launcher.getServerAddress() + " ...");
        imThread = new Thread() {
            @Override
            public void run() {
                launcher.connect();
            }
        };
        imThread.start();
    }

    public static void disconnectServer() {
        isIMThreadRuning = false;
        imThread = null;
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
            public void accept(Boolean success) {
                if (success) {
                    operateListener.sendToServerSuccess();
                } else {
                    operateListener.sendToServerFaild();
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
                        operateListener.listenSucess();
                        startHeartbeat(sess);
                    }
                }, new DefaultConsumer<String>() {
                    @Override
                    public void accept(String msg) {
                        operateListener.listenFailed(msg);
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

    public static void stopHeartbeat() {
        log.info("Client listen : stop send heart package");
        IMClientHeartbeatTimer.getInstance().stop();
        disconnectServer();
    }

    public static void sendAddFriendMessage(final long toUserId, final String content, final
    IMDefaultSendOperateListener operateListener) {
        commomExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                AddFriendMessageSend request = new AddFriendMessageSend(toUserId, content,
                        sendCallback);
                messageOperate.send(request);
            }
        });
    }
}