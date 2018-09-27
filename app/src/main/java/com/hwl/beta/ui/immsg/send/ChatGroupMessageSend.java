package com.hwl.beta.ui.immsg.send;

import com.hwl.beta.sp.UserSP;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.AbstractMessageSendExecutor;
import com.hwl.im.improto.ImChatGroupMessageContent;
import com.hwl.im.improto.ImChatGroupMessageRequest;
import com.hwl.im.improto.ImMessageRequest;
import com.hwl.im.improto.ImMessageType;

public class ChatGroupMessageSend extends AbstractMessageSendExecutor {

    public final static int CHAT_MESSAGE_CONTENT_TYPE_WORD = 1;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_IMAGE = 2;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_SOUND = 3;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_VIDEO = 4;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_REJECT = 5;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_REJECT_COZY = 6;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_WELCOME_TIP = 7;

    ImChatGroupMessageContent messageContent;
    DefaultConsumer<Boolean> sendCallback;

    public ChatGroupMessageSend(String groupGuid,int contentType, String content,String previewUrl,int imageWidth,int imageHeight,int size,int playTime,DefaultConsumer<Boolean> sendCallback) {
        messageContent = ImChatGroupMessageContent.newBuilder().setFromUserId(UserSP.getUserId())
                .setFromUserName(UserSP.getUserShowName())
                .setFromUserImage(UserSP.getUserHeadImage())
                .setToGrouopGuid(groupGuid)
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

    public ChatGroupMessageSend(String groupGuid, String content,DefaultConsumer<Boolean> sendCallback) {
        this(groupGuid,CHAT_MESSAGE_CONTENT_TYPE_WORD,content,null,0,0,content.length,0,sendCallback);
    }

    public ChatGroupMessageSend(String groupGuid, String previewUrl,int imageWidth,int imageHeight,int size,DefaultConsumer<Boolean> sendCallback) {
        this(groupGuid,CHAT_MESSAGE_CONTENT_TYPE_IMAGE,"[图片]",previewUrl,imageWidth,imageHeight,size,0,sendCallback);
    }

    public ChatGroupMessageSend(String groupGuid, String previewUrl,int size,int playTime,DefaultConsumer<Boolean> sendCallback) {
        this(groupGuid,CHAT_MESSAGE_CONTENT_TYPE_SOUND,"[语音]",previewUrl,0,0,size,playTime,sendCallback);
    }

    public ChatGroupMessageSend(String groupGuid, String previewUrl,int imageWidth,int imageHeight,int size,int playTime,DefaultConsumer<Boolean> sendCallback) {
        this(groupGuid,CHAT_MESSAGE_CONTENT_TYPE_VIDEO,"[视频]",previewUrl,imageWidth,imageHeight,size,playTime,sendCallback);
    }

    @Override
    public ImMessageType getMessageType() {
        return ImMessageType.ChatGroup;
    }

    @Override
    public void setRequestBody(ImMessageRequest.Builder request) {
        request.setChatGroupMessageRequest(
                ImChatGroupMessageRequest.newBuilder().setChatGroupMessageContent(messageContent).build());
    }

	@Override
	public DefaultConsumer<Boolean> sendStatusCallback() {
		return sendCallback;
	}
}
