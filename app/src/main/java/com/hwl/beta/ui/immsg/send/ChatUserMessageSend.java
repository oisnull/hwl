package com.hwl.beta.ui.immsg.send;

import com.hwl.beta.sp.UserSP;
import com.hwl.beta.utils.StringUtils;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.AbstractMessageSendExecutor;
import com.hwl.imcore.improto.ImChatUserMessageContent;
import com.hwl.imcore.improto.ImChatUserMessageRequest;
import com.hwl.imcore.improto.ImMessageRequest;
import com.hwl.imcore.improto.ImMessageType;

import java.security.InvalidParameterException;

public class ChatUserMessageSend extends AbstractMessageSendExecutor {

    ImChatUserMessageContent messageContent;
    DefaultConsumer<Boolean> sendCallback;
    
    public ChatUserMessageSend(Long toUserId,int contentType, String content,String previewUrl,int imageWidth,int imageHeight,int size,int playTime,boolean isFriend,DefaultConsumer<Boolean> sendCallback) {
        messageContent = ImChatUserMessageContent.newBuilder().setFromUserId(UserSP.getUserId())
                .setIsFriend(isFriend)
                .setFromUserName(UserSP.getUserShowName())
                .setFromUserImage(UserSP.getUserHeadImage())
                .setToUserId(toUserId)
                .setContentType(contentType)
                .setContent(StringUtils.nullStrToEmpty(content))
                .setPreviewUrl(StringUtils.nullStrToEmpty(previewUrl))
                .setImageWidth(imageWidth)
                .setImageHeight(imageHeight)
                .setSize(size)
                .setPlayTime(playTime)
                .build();
        this.sendCallback=sendCallback;
    }

    // public ChatUserMessageSend(Long toUserId, String content,DefaultConsumer<Boolean> sendCallback) {
    //     this(toUserId,IMConstant.CHAT_MESSAGE_CONTENT_TYPE_WORD,content,null,0,0,content.length(),0,sendCallback);
    // }

    // public ChatUserMessageSend(Long toUserId, String previewUrl,int imageWidth,int imageHeight,int size,DefaultConsumer<Boolean> sendCallback) {
    //     this(toUserId,IMConstant.CHAT_MESSAGE_CONTENT_TYPE_IMAGE,"[图片]",previewUrl,imageWidth,imageHeight,size,0,sendCallback);
    // }

    // public ChatUserMessageSend(Long toUserId, String previewUrl,int size,int playTime,DefaultConsumer<Boolean> sendCallback) {
    //     this(toUserId,IMConstant.CHAT_MESSAGE_CONTENT_TYPE_SOUND,"[语音]",previewUrl,0,0,size,playTime,sendCallback);
    // }

    // public ChatUserMessageSend(Long toUserId, String previewUrl,int imageWidth,int imageHeight,int size,int playTime,DefaultConsumer<Boolean> sendCallback) {
    //     this(toUserId,IMConstant.CHAT_MESSAGE_CONTENT_TYPE_VIDEO,"[视频]",previewUrl,imageWidth,imageHeight,size,playTime,sendCallback);
    // }

    @Override
    public ImMessageType getMessageType() {
        return ImMessageType.ChatUser;
    }

    @Override
    public void setRequestBody(ImMessageRequest.Builder request) {
        request.setChatUserMessageRequest(
                ImChatUserMessageRequest.newBuilder().setChatUserMessageContent(messageContent).build());
    }

	@Override
	public DefaultConsumer<Boolean> sendStatusCallback() {
		return sendCallback;
	}

}