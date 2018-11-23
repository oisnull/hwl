package com.hwl.beta.ui.immsg.send;

import com.hwl.beta.sp.UserSP;
import com.hwl.beta.utils.StringUtils;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.AbstractMessageSendExecutor;
import com.hwl.imcore.improto.ImChatGroupMessageContent;
import com.hwl.imcore.improto.ImChatGroupMessageRequest;
import com.hwl.imcore.improto.ImMessageRequest;
import com.hwl.imcore.improto.ImMessageType;

public class ChatGroupMessageSend extends AbstractMessageSendExecutor {

    ImChatGroupMessageContent messageContent;
    DefaultConsumer<Boolean> sendCallback;

    public ChatGroupMessageSend(String groupGuid,int contentType, String content,String previewUrl,int imageWidth,int imageHeight,int size,int playTime,DefaultConsumer<Boolean> sendCallback) {
        messageContent = ImChatGroupMessageContent.newBuilder().setFromUserId(UserSP.getUserId())
                .setFromUserName(UserSP.getUserShowName())
                .setFromUserImage(UserSP.getUserHeadImage())
                .setToGrouopGuid(groupGuid)
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

    // public ChatGroupMessageSend(String groupGuid, String content,DefaultConsumer<Boolean> sendCallback) {
    //     this(groupGuid,IMConstant.CHAT_MESSAGE_CONTENT_TYPE_WORD,content,null,0,0,content.length(),0,sendCallback);
    // }

    // public ChatGroupMessageSend(String groupGuid, String previewUrl,int imageWidth,int imageHeight,int size,DefaultConsumer<Boolean> sendCallback) {
    //     this(groupGuid,IMConstant.CHAT_MESSAGE_CONTENT_TYPE_IMAGE,"[图片]",previewUrl,imageWidth,imageHeight,size,0,sendCallback);
    // }

    // public ChatGroupMessageSend(String groupGuid, String previewUrl,int size,int playTime,DefaultConsumer<Boolean> sendCallback) {
    //     this(groupGuid,IMConstant.CHAT_MESSAGE_CONTENT_TYPE_SOUND,"[语音]",previewUrl,0,0,size,playTime,sendCallback);
    // }

    // public ChatGroupMessageSend(String groupGuid, String previewUrl,int imageWidth,int imageHeight,int size,int playTime,DefaultConsumer<Boolean> sendCallback) {
    //     this(groupGuid,IMConstant.CHAT_MESSAGE_CONTENT_TYPE_VIDEO,"[视频]",previewUrl,imageWidth,imageHeight,size,playTime,sendCallback);
    // }

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
