package com.hwl.im.immode;

import com.hwl.im.ThreadPoolUtil;
import com.hwl.im.imaction.MessageSendExecutor;
import com.hwl.im.improto.ImMessageContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class MessageOperate {

    final static ExecutorService executorService = Executors.newFixedThreadPool(ThreadPoolUtil.ioIntesivePoolSize());

    public static void send(Channel channel, ImMessageContext messageContext,final Function<Boolean, Void> callback) {
        if (channel == null || messageContext == null) {
            callback.apply(false);
            return;
        }
        ChannelFuture channelFuture = channel.writeAndFlush(messageContext);
        channelFuture.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (callback != null)
                    callback.apply(future.isSuccess());
            }
        });
    }

    public static void clientSend(final Channel channel,final MessageSendExecutor sendExecutor) {
        if (sendExecutor == null || channel == null)
            return;
        ImMessageContext messageContext = sendExecutor.getMessageContext();
        if (messageContext == null) {
            sendExecutor.sendResultCallback(false);
            return;
        }
        ChannelFuture channelFuture = channel.writeAndFlush(messageContext);
        channelFuture.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (sendExecutor.isSendFailedAndClose() && !future.isSuccess()) {
                    channel.close();
                }
                sendExecutor.sendResultCallback(future.isSuccess());
            }
        });
    }


}