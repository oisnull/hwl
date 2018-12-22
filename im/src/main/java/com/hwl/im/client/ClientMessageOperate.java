package com.hwl.im.client;

import com.hwl.im.imaction.MessageListenExecutor;
import com.hwl.im.imaction.MessageSendExecutor;
import com.hwl.im.immode.MessageOperate;
import com.hwl.im.immode.MessageResponseHeadOperate;
import com.hwl.imcore.improto.ImMessageContext;
import com.hwl.imcore.improto.ImMessageType;

import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.reactivex.functions.Function;

public final class ClientMessageOperate {

    private void check() {
        if (this.serverChannel == null) {
            throw new NullPointerException("连接的服务器通道不存在,请尝试调用 IMClientLauncher 中的 connect() 后再试.");
        }
    }

    private Channel serverChannel = null;
    private ConcurrentHashMap<ImMessageType, MessageListenExecutor> listenExecutors = new
            ConcurrentHashMap<>();
    private Runnable disconnectCallback = null;
    private Function<String, MessageSendExecutor> clientAckExecutor = null;

    public void registerClientAckExecutor(Function<String, MessageSendExecutor> clientAckExecutor) {
        this.clientAckExecutor = clientAckExecutor;
    }

    public void registerChannel(Channel channel) {
        this.serverChannel = channel;
    }

    public void unregisterChannel() {
        this.serverChannel = null;
    }

    public void registerDisconnectCallback(Runnable disconnectCallback) {
        this.disconnectCallback = disconnectCallback;
    }

    public void registerListenExecutor(ImMessageType messageType, MessageListenExecutor executor) {
        if (this.listenExecutors.containsKey(messageType)) {
            this.listenExecutors.remove(messageType);
        }
        this.listenExecutors.put(messageType, executor);
    }

    public void unregisterListenExecutor(ImMessageType messageType) {
        if (this.listenExecutors.containsKey(messageType)) {
            this.listenExecutors.remove(messageType);
        }
    }

    public void send(MessageSendExecutor sendExecutor) {
        check();
        MessageOperate.clientSend(serverChannel, sendExecutor);
    }

    public void send(MessageSendExecutor sendExecutor, MessageListenExecutor listenExecutor) {
        if (sendExecutor == null)
            return;
        if (listenExecutor != null) {
            registerListenExecutor(sendExecutor.getMessageType(), listenExecutor);
        }
        this.send(sendExecutor);
    }

    private void sendAckMessage(ImMessageContext messageContext) {
        if (this.clientAckExecutor != null && MessageResponseHeadOperate.isAck(messageContext)) {
            try {
                MessageSendExecutor sendExecutor = this.clientAckExecutor.apply
                        (MessageResponseHeadOperate
                        .getMessageId(messageContext));
                this.send(sendExecutor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listen(ImMessageContext messageContext) {
        check();
        if (messageContext == null)
            return;

        this.sendAckMessage(messageContext);

        MessageListenExecutor listenExecutor = listenExecutors.get(messageContext.getType());
        if (listenExecutor == null)
            return;

        listenExecutor.execute(messageContext);
        if (listenExecutor.executedAndClose() && serverChannel != null) {
            serverChannel.close();
        }
    }

    public void disconnect() {
        if (this.disconnectCallback != null) {
            this.disconnectCallback.run();
        }
        unregisterChannel();
    }
}