package com.hwl.beta.ui.immsg;

import java.util.List;
import java.util.TimerTask;
import java.util.logging.Logger;

import com.hwl.beta.AppConfig;
import com.hwl.beta.ui.immsg.listen.AddFriendMessageListen;
import com.hwl.beta.ui.immsg.listen.AppVersionListen;
import com.hwl.beta.ui.immsg.listen.ChatSettingMessageListen;
import com.hwl.beta.ui.immsg.listen.CircleOperateMessageListen;
import com.hwl.beta.ui.immsg.listen.GroupOperateMessageListen;
import com.hwl.beta.ui.immsg.listen.NearCircleOperateMessageListen;
import com.hwl.beta.ui.immsg.listen.SystemMessageListen;
import com.hwl.beta.ui.immsg.listen.TestConnectionMessageListen;
import com.hwl.beta.ui.immsg.listen.UserValidateListen;
import com.hwl.beta.ui.immsg.send.AddFriendMessageSend;
import com.hwl.beta.ui.immsg.send.ChatGroupMessageSend;
import com.hwl.beta.ui.immsg.send.ChatSettingMessageSend;
import com.hwl.beta.ui.immsg.send.ChatUserMessageSend;
import com.hwl.beta.ui.immsg.send.CircleOperateMessageSend;
import com.hwl.beta.ui.immsg.send.ClientAckMessageSend;
import com.hwl.beta.ui.immsg.send.GroupOperateMessageSend;
import com.hwl.beta.ui.immsg.send.NearCircleOperateMessageSend;
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
import com.hwl.imcore.improto.ImChatSettingType;
import com.hwl.imcore.improto.ImCircleOperateType;
import com.hwl.imcore.improto.ImGroupOperateType;
import com.hwl.imcore.improto.ImMessageType;
import com.hwl.imcore.improto.ImTestConnectionMessageResponse;
import com.hwl.imcore.improto.ImUserContent;

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
        messageOperate.registerListenExecutor(ImMessageType.ChatSetting, new
                ChatSettingMessageListen());
        messageOperate.registerListenExecutor(ImMessageType.GroupOperate, new
                GroupOperateMessageListen());
        messageOperate.registerListenExecutor(ImMessageType.NearCircleOperate, new
                NearCircleOperateMessageListen());
        messageOperate.registerListenExecutor(ImMessageType.CircleOperate, new
                CircleOperateMessageListen());
        messageOperate.registerListenExecutor(ImMessageType.SystemMessage,
                new SystemMessageListen());
        messageOperate.registerListenExecutor(ImMessageType.AppVersion, new AppVersionListen());
    }

    public static void connectServer() {
        if (!NetworkUtils.isConnected()) {
            log.info("Client listen : network is disconnect");
            return;
        }

        if (launcher.isConnected()) {
            log.info("Client listen : im is connected");
            // log.info("Client listen : connected to server " + launcher.getServerAddress() + "
            // and" + " stop auto check");
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
                        //operateListener.sessionInvalid();
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
        MessageRequestHeadOperate.setSession(sessionId);
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
                TestConnectionMessageListen response =
                        new TestConnectionMessageListen(new DefaultConsumer<ImTestConnectionMessageResponse>() {
                            @Override
                            public void accept(ImTestConnectionMessageResponse content) {
                                operateListener.listenSuccess(content);
                            }
                        }, new DefaultConsumer<String>() {
                            @Override
                            public void accept(String msg) {
                                operateListener.listenFailed(msg);
                                //operateListener.sessionInvalid();
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

    public static void sendChatUserTextMessage(Long toUserId, String content, boolean isFriend,
                                               IMDefaultSendOperateListener operateListener) {
        sendChatUserMessage(toUserId, IMConstant.CHAT_MESSAGE_CONTENT_TYPE_TEXT, content, null,
                null,
                0, 0, content.length(), 0, isFriend, operateListener);
    }

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

    public static void sendChatGroupNoteSettingMessage(final String groupGuid,
                                                       final String groupNote,
                                                       final IMDefaultSendOperateListener
                                                               operateListener) {
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                ChatSettingMessageSend request = new ChatSettingMessageSend(ImChatSettingType
                        .GroupNote, groupGuid, null, groupNote, null, sendCallback);
                messageOperate.send(request);
            }
        });
    }

    public static void sendChatGroupNameSettingMessage(final String groupGuid,
                                                       final String groupName,
                                                       final IMDefaultSendOperateListener
                                                               operateListener) {
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                ChatSettingMessageSend request = new ChatSettingMessageSend(ImChatSettingType
                        .GroupName, groupGuid, groupName, null, null, sendCallback);
                messageOperate.send(request);
            }
        });
    }

    public static void sendChatGroupUserRemarkSettingMessage(final String groupGuid,
                                                             final String groupUserRemark,
                                                             final IMDefaultSendOperateListener
                                                                     operateListener) {
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                ChatSettingMessageSend request = new ChatSettingMessageSend(ImChatSettingType
                        .UserRemark, groupGuid, null, null, groupUserRemark, sendCallback);
                messageOperate.send(request);
            }
        });
    }

    public static void sendGroupCreateMessage(final String groupGuid,
                                              final String groupName,
                                              final List<ImUserContent> groupUsers,
                                              final IMDefaultSendOperateListener operateListener) {
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                GroupOperateMessageSend request = new GroupOperateMessageSend(ImGroupOperateType
                        .CreateGroup, groupGuid, groupName, groupUsers, sendCallback);
                messageOperate.send(request);
            }
        });
    }

    public static void sendGroupExitMessage(final String groupGuid,
                                            final IMDefaultSendOperateListener operateListener) {
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                GroupOperateMessageSend request = new GroupOperateMessageSend(ImGroupOperateType
                        .ExitGroup, groupGuid, null, null, sendCallback);
                messageOperate.send(request);
            }
        });
    }

    public static void sendGroupDismissMessage(final String groupGuid,
                                               final IMDefaultSendOperateListener operateListener) {
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                GroupOperateMessageSend request = new GroupOperateMessageSend(ImGroupOperateType
                        .DismissGroup, groupGuid, null, sendCallback);
                messageOperate.send(request);
            }
        });
    }

    public static void sendGroupUserAddMessage(final String groupGuid,
                                               final String groupName,
                                               final List<ImUserContent> groupUsers,
                                               final IMDefaultSendOperateListener operateListener) {
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                GroupOperateMessageSend request = new GroupOperateMessageSend(ImGroupOperateType
                        .AddUser, groupGuid, groupName, groupUsers, sendCallback);
                messageOperate.send(request);
            }
        });
    }

    public static void sendGroupUserRemoveMessage(final String groupGuid,
                                                  final String groupName,
                                                  final List<ImUserContent> groupUsers,
                                                  final IMDefaultSendOperateListener
                                                          operateListener) {
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                GroupOperateMessageSend request = new GroupOperateMessageSend(ImGroupOperateType
                        .RemoveUser, groupGuid, groupName, groupUsers, sendCallback);
                messageOperate.send(request);
            }
        });
    }

    public static void sendNearCircleLikeMessage(final long nearCircleId,
                                                 final boolean isLike,
                                                 final long originUserId,
                                                 final IMDefaultSendOperateListener operateListener) {
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                NearCircleOperateMessageSend request =
                        new NearCircleOperateMessageSend(isLike ? ImCircleOperateType
                                .AddLike : ImCircleOperateType.CancelLike, originUserId, 0,
                                nearCircleId, 0, null,
                                sendCallback);
                messageOperate.send(request);
            }
        });
    }

    public static void sendNearCircleCommentMessage(final long nearCircleId,
                                                    final long commentId,
                                                    final String content,
                                                    final long originUserId,
                                                    final long replyUserId,
                                                    final IMDefaultSendOperateListener operateListener) {
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                NearCircleOperateMessageSend request =
                        new NearCircleOperateMessageSend(ImCircleOperateType
                                .PostComment, originUserId, replyUserId,
                                nearCircleId, commentId, content,
                                sendCallback);
                messageOperate.send(request);
            }
        });
    }

    public static void sendNearCircleCancelCommentMessage(final long nearCircleId,
                                                          final long commentId,
                                                          final long originUserId,
                                                          final long replyUserId,
                                                          final IMDefaultSendOperateListener operateListener) {
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                NearCircleOperateMessageSend request =
                        new NearCircleOperateMessageSend(ImCircleOperateType
                                .CancelComment, originUserId, replyUserId,
                                nearCircleId, commentId, null,
                                sendCallback);
                messageOperate.send(request);
            }
        });
    }

    public static void sendCircleLikeMessage(final long circleId,
                                             final boolean isLike,
                                             final long originUserId,
                                             final IMDefaultSendOperateListener operateListener) {
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                CircleOperateMessageSend request =
                        new CircleOperateMessageSend(isLike ? ImCircleOperateType
                                .AddLike : ImCircleOperateType.CancelLike, originUserId, 0,
                                circleId, 0, null,
                                sendCallback);
                messageOperate.send(request);
            }
        });
    }

    public static void sendCircleCommentMessage(final long circleId,
                                                final long commentId,
                                                final String content,
                                                final long originUserId,
                                                final long replyUserId,
                                                final IMDefaultSendOperateListener operateListener) {
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                CircleOperateMessageSend request =
                        new CircleOperateMessageSend(ImCircleOperateType
                                .PostComment, originUserId, replyUserId,
                                circleId, commentId, content,
                                sendCallback);
                messageOperate.send(request);
            }
        });
    }

    public static void sendCircleCancelCommentMessage(final long circleId,
                                                      final long commentId,
                                                      final long originUserId,
                                                      final long replyUserId,
                                                      final IMDefaultSendOperateListener operateListener) {
        commonExec(operateListener, new DefaultConsumer<DefaultConsumer<Boolean>>() {
            @Override
            public void accept(DefaultConsumer<Boolean> sendCallback) {
                CircleOperateMessageSend request =
                        new CircleOperateMessageSend(ImCircleOperateType
                                .CancelComment, originUserId, replyUserId,
                                circleId, commentId, null,
                                sendCallback);
                messageOperate.send(request);
            }
        });
    }
}