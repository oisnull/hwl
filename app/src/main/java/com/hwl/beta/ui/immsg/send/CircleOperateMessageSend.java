package com.hwl.beta.ui.immsg.send;

import com.hwl.beta.sp.UserSP;
import com.hwl.beta.utils.StringUtils;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.AbstractMessageSendExecutor;
import com.hwl.imcore.improto.ImCircleOperateMessageContent;
import com.hwl.imcore.improto.ImCircleOperateMessageRequest;
import com.hwl.imcore.improto.ImCircleOperateType;
import com.hwl.imcore.improto.ImMessageRequest;
import com.hwl.imcore.improto.ImMessageType;
import com.hwl.imcore.improto.ImUserContent;

public class CircleOperateMessageSend extends AbstractMessageSendExecutor {

    ImCircleOperateMessageContent messageContent;
    DefaultConsumer<Boolean> sendCallback;

    public CircleOperateMessageSend(ImCircleOperateType operateType,
                                    long originUserId,
                                    long replyUserId,
                                    long CircleId,
                                    long commentId,
                                    String content,
                                    DefaultConsumer<Boolean> sendCallback) {

        ImUserContent originUser = ImUserContent.newBuilder()
                .setUserId(originUserId).build();

        ImUserContent postUser = ImUserContent.newBuilder()
                .setUserId(UserSP.getUserId())
                .setUserName(UserSP.getUserShowName())
                .setUserImage(UserSP.getUserHeadImage()).build();

        ImCircleOperateMessageContent.Builder builder =
                ImCircleOperateMessageContent.newBuilder()
                        .setOperateType(operateType)
                        .setPostUser(postUser)
                        .setOriginUser(originUser)
                        .setCircleId(CircleId)
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
        return ImMessageType.CircleOperate;
    }

    @Override
    public void setRequestBody(ImMessageRequest.Builder request) {
        request.setCircleOperateMessageRequest(
                ImCircleOperateMessageRequest.newBuilder().setCircleOperateMessageContent
                        (messageContent)
                        .build());
    }

    @Override
    public DefaultConsumer<Boolean> sendStatusCallback() {
        return sendCallback;
    }
}
