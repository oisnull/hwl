package com.hwl.beta.ui.immsg.send;

import com.hwl.beta.sp.UserSP;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.AbstractMessageSendExecutor;
import com.hwl.im.improto.ImChatUserMessageContent;
import com.hwl.im.improto.ImChatUserMessageRequest;
import com.hwl.im.improto.ImMessageRequest;
import com.hwl.im.improto.ImMessageType;

import java.security.InvalidParameterException;

public class ChatUserMessageSend extends AbstractMessageSendExecutor {
    
    public final static int CHAT_MESSAGE_CONTENT_TYPE_WORD = 1;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_IMAGE = 2;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_SOUND = 3;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_VIDEO = 4;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_REJECT = 5;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_REJECT_COZY = 6;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_WELCOME_TIP = 7;

    ImChatUserMessageContent messageContent;
    DefaultConsumer<Boolean> sendCallback;
    
    public ChatUserMessageSend(Long toUserId,int contentType, String content,String previewUrl,int imageWidth,int imageHeight,int size,int playTime,DefaultConsumer<Boolean> sendCallback) {
        messageContent = ImChatUserMessageContent.newBuilder().setFromUserId(UserSP.getUserId())
                .setFromUserName(UserSP.getUserShowName())
                .setFromUserImage(UserSP.getUserHeadImage())
                .setToUserId(toUserId)
                .setContentType(contentType)
                .setContent(content)
                .setPreviewUrl(previewUrl)
                .setImageWidth(imageWidth)
                .setImageHeight(imageHeight)
                .setSize(size)
                .setPlayTime(playTime)
                .build();
        this.sendCallback=sendCallback;
    }

    public ChatUserMessageSend(Long toUserId, String content,DefaultConsumer<Boolean> sendCallback) {
        this(toUserId,CHAT_MESSAGE_CONTENT_TYPE_WORD,content,null,0,0,content.length(),0,sendCallback);
    }

    public ChatUserMessageSend(Long toUserId, String previewUrl,int imageWidth,int imageHeight,int size,DefaultConsumer<Boolean> sendCallback) {
        this(toUserId,CHAT_MESSAGE_CONTENT_TYPE_IMAGE,"[图片]",previewUrl,imageWidth,imageHeight,size,0,sendCallback);
    }

    public ChatUserMessageSend(Long toUserId, String previewUrl,int size,int playTime,DefaultConsumer<Boolean> sendCallback) {
        this(toUserId,CHAT_MESSAGE_CONTENT_TYPE_SOUND,"[语音]",previewUrl,0,0,size,playTime,sendCallback);
    }

    public ChatUserMessageSend(Long toUserId, String previewUrl,int imageWidth,int imageHeight,int size,int playTime,DefaultConsumer<Boolean> sendCallback) {
        this(toUserId,CHAT_MESSAGE_CONTENT_TYPE_VIDEO,"[视频]",previewUrl,imageWidth,imageHeight,size,playTime,sendCallback);
    }

    @Override
    public ImMessageType getMessageType() {
        return ImMessageType.ChatUser;
    }

    @Override
    public void setRequestBody(ImMessageRequest.Builder request) {

//        this.checkParams();

        request.setChatUserMessageRequest(
                ImChatUserMessageRequest.newBuilder().setChatUserMessageContent(messageContent).build());
    }

//    private void checkParams() {
//        if (fromUserId <= 0 || toUserId <= 0 || fromUserId == toUserId) {
//            throw new InvalidParameterException("FromUserId cannot be the same as toUserId or is zero");
//        }
//        if (content == null || content.isEmpty()) {
//            throw new NullPointerException("Send chat user message content cannot be null or empty");
//        }
//    }

	@Override
	public DefaultConsumer<Boolean> sendStatusCallback() {
		return sendCallback;
	}

}