package com.hwl.beta.ui.immsg;

import java.util.TimerTask;
import java.util.logging.Logger;

import com.hwl.beta.AppConfig;
import com.hwl.beta.ui.immsg.listen.AddFriendMessageListen;
import com.hwl.beta.ui.immsg.listen.TestConnectionMessageListen;
import com.hwl.beta.ui.immsg.listen.UserValidateListen;
import com.hwl.beta.ui.immsg.send.AddFriendMessageSend;
import com.hwl.beta.ui.immsg.send.ChatGroupMessageSend;
import com.hwl.beta.ui.immsg.send.ChatUserMessageSend;
import com.hwl.beta.ui.immsg.send.ClientAckMessageSend;
import com.hwl.beta.ui.immsg.send.TestConnectionMessageSend;
import com.hwl.beta.ui.immsg.send.UserValidateSend;
import com.hwl.beta.utils.NetworkUtils;
import com.hwl.im.client.ClientMessageOperate;
import com.hwl.im.client.IMClientHeartbeatTimer;
import com.hwl.im.client.IMClientLauncher;
import com.hwl.beta.ui.immsg.listen.ChatGroupMessageListen;
import com.hwl.beta.ui.immsg.listen.ChatUserMessageListen;
import com.hwl.beta.ui.immsg.send.HeartBeatMessageSend;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.MessageSendExecutor;
import com.hwl.im.immode.MessageRequestHeadOperate;
import com.hwl.imcore.improto.ImMessageType;
import com.hwl.imcore.improto.ImTestConnectionMessageResponse;

import io.reactivex.functions.Function;

public class IMClientEntry {

    private static Logger log = Logger.getLogger(AppConfig.IM_DEBUG_TAG);
//    static int CHECK_TIME_INTERNAL = 5 * 1000; // s

    private static Thread imThread = null;
    private static boolean isIMThreadRunning = false;
    private final static ClientMessageOperate messageOperate = new ClientMessageOperate();
    private final static IMClientLauncher launcher = new IMClientLauncher(AppConfig.IM_HOST,
            AppConfig.IM_PORT);

    static {
        initListenExecutor();
        launcher.registerAction(messageOperate);
        launcher.registerClientListener(new IMClientDefaultListener());
    }

    private static void initListenExecutor() {
        messageOperate.registerClientAckExecutor(new Function<String, MessageSendExecutor>() {
            @Override
            public MessageSendExecutor apply(String messageId) {
                return new ClientAckMessageSend(messageId);
            }
        });

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
        if (!NetworkUtils.isConnected()) {
            log.info("Client listen : network is disconnect");
            return;
        }

        if (launcher.isConnected()) {
            log.info("Client listen : im is connected");
            // log.info("Client listen : connected to server " + launcher.getServerAddress() + "
            // and" +
            //         " stop auto check");
            return;
        }

        if (isIMThreadRunning) return;
        isIMThreadRunning = true;

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
        isIMThreadRunning = false;
        imThread = null;
        launcher.stop();
    }

    private static void commonExec(final IMDefaultSendOperateListener operateListener, final
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

    private static void sendUserValidateMessage(final IMDefaultSendOperateListener
                                                        operateListener) {
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                UserValidateSend request = new UserValidateSend(sendCallback);
                UserValidateListen response = new UserValidateListen(new DefaultConsumer<String>() {
                    @Override
                    public void accept(String sess) {
                        operateListener.listenSuccess(null);
                        startHeartbeat(sess);
                    }
                }, new DefaultConsumer<String>() {
                    @Override
                    public void accept(String msg) {
                        operateListener.listenFailed(msg);
                        operateListener.sessionInvalid();
                        stopHeartbeat();
                    }
                });
                messageOperate.send(request, response);
            }
        });
    }

    private static void sendHeartBeatMessage() {
        final IMDefaultSendOperateListener operateListener = new IMDefaultSendOperateListener
                ("HeartBeatMessage");
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                HeartBeatMessageSend request = new HeartBeatMessageSend(sendCallback);
                messageOperate.send(request);
            }
        });
    }

    private static void startHeartbeat(String sessionId) {
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

    public static void sendTestConnectMessage(final
                                              IMDefaultSendOperateListener<ImTestConnectionMessageResponse>
                                                      operateListener) {
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                TestConnectionMessageSend request = new TestConnectionMessageSend(sendCallback);
                TestConnectionMessageListen response = new TestConnectionMessageListen(new DefaultConsumer<ImTestConnectionMessageResponse>() {
                    @Override
                    public void accept(ImTestConnectionMessageResponse content) {
                        operateListener.listenSuccess(content);
                    }
                }, new DefaultConsumer<String>() {
                    @Override
                    public void accept(String msg) {
                        operateListener.listenFailed(msg);
                        operateListener.sessionInvalid();
                    }
                });
                messageOperate.send(request, response);
            }
        });
    }

    public static void sendAddFriendMessage(final long toUserId, final String content, final
    IMDefaultSendOperateListener operateListener) {
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                AddFriendMessageSend request = new AddFriendMessageSend(toUserId, content,
                        sendCallback);
                messageOperate.send(request);
            }
        });
    }

//    public static void sendChatUserTextMessage(Long toUserId, String content,
//                                               IMDefaultSendOperateListener operateListener) {
//        sendChatUserTextMessage(toUserId, content, false, operateListener);
//    }

    public static void sendChatUserTextMessage(Long toUserId, String content, boolean isFriend,
                                               IMDefaultSendOperateListener operateListener) {
        sendChatUserMessage(toUserId, IMConstant.CHAT_MESSAGE_CONTENT_TYPE_TEXT, content, null,
                null,
                0, 0, content.length(), 0, isFriend, operateListener);
    }

//    public static void sendChatUserImageMessage(Long toUserId, String previewUrl, int imageWidth,
//                                                int imageHeight, int size,
//                                                IMDefaultSendOperateListener operateListener) {
//        sendChatUserMessage(toUserId, IMConstant.CHAT_MESSAGE_CONTENT_TYPE_IMAGE, "[图片]",
//                previewUrl, imageWidth, imageHeight, size, 0, false, operateListener);
//    }
//
//    public static void sendChatUserVoiceMessage(Long toUserId, String previewUrl, int size, int
//            playTime, IMDefaultSendOperateListener operateListener) {
//        sendChatUserMessage(toUserId, IMConstant.CHAT_MESSAGE_CONTENT_TYPE_VOICE, "[语音]",
//                previewUrl, 0, 0, size, playTime, false, operateListener);
//    }
//
//    public static void sendChatUserVideoMessage(Long toUserId, String previewUrl, int imageWidth,
//                                                int imageHeight, int size, int playTime,
//                                                IMDefaultSendOperateListener operateListener) {
//        sendChatUserMessage(toUserId, IMConstant.CHAT_MESSAGE_CONTENT_TYPE_VIDEO, "[视频]",
//                previewUrl, imageWidth, imageHeight, size, playTime, false, operateListener);
//    }

    public static void sendChatUserMessage(final Long toUserId, final int contentType, final
    String content, final String originalUrl, final String previewUrl, final int imageWidth,
                                           final int imageHeight, final
                                           int size, final int playTime,
                                           IMDefaultSendOperateListener operateListener) {
        sendChatUserMessage(toUserId, contentType, content, originalUrl, previewUrl, imageWidth,
                imageHeight, size, playTime, false, operateListener);
    }

    public static void sendChatUserMessage(final Long toUserId, final int contentType, final
    String content, final String originalUrl, final String previewUrl, final int imageWidth,
                                           final int imageHeight, final
                                           int size, final int playTime, final boolean isFriend,
                                           final
                                           IMDefaultSendOperateListener operateListener) {
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                ChatUserMessageSend request = new ChatUserMessageSend(toUserId, contentType,
                        content, originalUrl, previewUrl, imageWidth, imageHeight, size,
                        playTime, isFriend,
                        sendCallback);
                messageOperate.send(request);
            }
        });
    }

//    public static void sendChatGroupTextMessage(String groupGuid, String content,
//                                                IMDefaultSendOperateListener operateListener) {
//        sendChatGroupMessage(groupGuid, IMConstant.CHAT_MESSAGE_CONTENT_TYPE_TEXT, content, null,
//                0, 0, content.length(), 0, operateListener);
//    }
//
//    public static void sendChatGroupImageMessage(String groupGuid, String previewUrl, int
//            imageWidth, int imageHeight, int size, IMDefaultSendOperateListener operateListener) {
//        sendChatGroupMessage(groupGuid, IMConstant.CHAT_MESSAGE_CONTENT_TYPE_IMAGE, "[图片]",
//                previewUrl, imageWidth, imageHeight, size, 0, operateListener);
//    }
//
//    public static void sendChatGroupVoiceMessage(String groupGuid, String previewUrl, int size,
//                                                 int playTime, IMDefaultSendOperateListener
//                                                         operateListener) {
//        sendChatGroupMessage(groupGuid, IMConstant.CHAT_MESSAGE_CONTENT_TYPE_VOICE, "[语音]",
//                previewUrl, 0, 0, size, playTime, operateListener);
//    }
//
//    public static void sendChatGroupVideoMessage(String groupGuid, String previewUrl, int
//            imageWidth, int imageHeight, int size, int playTime, IMDefaultSendOperateListener
//                                                         operateListener) {
//        sendChatGroupMessage(groupGuid, IMConstant.CHAT_MESSAGE_CONTENT_TYPE_VIDEO, "[视频]",
//                previewUrl, imageWidth, imageHeight, size, playTime, operateListener);
//    }

    public static void sendChatGroupMessage(final String groupGuid, final int contentType, final
    String content, final String originalUrl, final String previewUrl, final int imageWidth,
                                            final int imageHeight, final
                                            int size, final int
                                                    playTime, final IMDefaultSendOperateListener
                                                    operateListener) {
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                ChatGroupMessageSend request = new ChatGroupMessageSend(groupGuid, contentType,
                        content, originalUrl, previewUrl, imageWidth, imageHeight, size,
                        playTime, sendCallback);
                messageOperate.send(request);
            }
        });
    }
}