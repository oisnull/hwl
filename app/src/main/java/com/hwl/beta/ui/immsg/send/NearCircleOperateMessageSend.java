package com.hwl.beta.ui.immsg.send;

import com.hwl.beta.sp.UserSP;
import com.hwl.beta.utils.StringUtils;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.AbstractMessageSendExecutor;
import com.hwl.imcore.improto.ImMessageRequest;
import com.hwl.imcore.improto.ImMessageType;
import com.hwl.imcore.improto.ImNearCircleOperateMessageContent;
import com.hwl.imcore.improto.ImNearCircleOperateMessageRequest;
import com.hwl.imcore.improto.ImNearCircleOperateType;
import com.hwl.imcore.improto.ImUserContent;

public class NearCircleOperateMessageSend extends AbstractMessageSendExecutor {

    ImNearCircleOperateMessageContent messageContent;
    DefaultConsumer<Boolean> sendCallback;

    public NearCircleOperateMessageSend(ImNearCircleOperateType operateType,
                                        long originUserId,
                                        long replyUserId,
                                        long nearCircleId,
                                        boolean isLike,
                                        long commentId,
                                        String content,
                                        DefaultConsumer<Boolean> sendCallback) {

        ImUserContent originUser = ImUserContent.newBuilder()
                .setUserId(originUserId).build();

        ImUserContent postUser = ImUserContent.newBuilder()
                .setUserId(UserSP.getUserId())
                .setUserName(UserSP.getUserShowName())
                .setUserImage(UserSP.getUserHeadImage()).build();

        ImNearCircleOperateMessageContent.Builder builder =
                ImNearCircleOperateMessageContent.newBuilder()
                        .setOperateType(operateType)
                        .setPostUser(postUser)
                        .setOriginUser(originUser)
                        .setNearCircleId(nearCircleId)
                        .setIsLike(isLike)
                        .setCommentId(commentId)
                        .setCommentCont(StringUtils.nullStrToEmpty(content));

        if (replyUserId > 0) {
            ImUserContent replyUser = ImUserContent.newBuilder()
                    .setUserId(replyUserId).build();
            builder.setReplyUser(replyUser);
        }

        messageContent = builder.build();

        this.sendCallback = sendCallback;
    }

    @Override
    public ImMessageType getMessageType() {
        return ImMessageType.NearCircleOperate;
    }

    @Override
    public void setRequestBody(ImMessageRequest.Builder request) {
        request.setNearCircleOperateMessageRequest(
                ImNearCircleOperateMessageRequest.newBuilder().setNearCircleOperateMessageContent
                        (messageContent)
                        .build());
    }

    @Override
    public DefaultConsumer<Boolean> sendStatusCallback() {
        return sendCallback;
    }
}
