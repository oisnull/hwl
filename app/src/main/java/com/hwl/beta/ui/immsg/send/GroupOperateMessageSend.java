package com.hwl.beta.ui.immsg.send;

import com.hwl.beta.sp.UserSP;
import com.hwl.beta.utils.StringUtils;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.AbstractMessageSendExecutor;
import com.hwl.imcore.improto.ImGroupOperateMessageContent;
import com.hwl.imcore.improto.ImGroupOperateMessageRequest;
import com.hwl.imcore.improto.ImGroupOperateType;
import com.hwl.imcore.improto.ImMessageRequest;
import com.hwl.imcore.improto.ImMessageType;
import com.hwl.imcore.improto.ImUserContent;

import java.util.List;

public class GroupOperateMessageSend extends AbstractMessageSendExecutor {

    ImGroupOperateMessageContent messageContent;
    DefaultConsumer<Boolean> sendCallback;

    public GroupOperateMessageSend(ImGroupOperateType operateType,
                                   String groupGuid,
                                   String groupName,
                                   DefaultConsumer<Boolean> sendCallback) {
        this(operateType, groupGuid, groupName, null, sendCallback);
    }

    public GroupOperateMessageSend(ImGroupOperateType operateType,
                                   String groupGuid,
                                   String groupName,
                                   List<ImUserContent> groupUsers,
                                   DefaultConsumer<Boolean> sendCallback) {

        ImUserContent operateUser = ImUserContent.newBuilder()
                .setUserId(UserSP.getUserId())
                .setUserName(UserSP.getUserShowName())
                .setUserImage(UserSP.getUserHeadImage()).build();

        ImGroupOperateMessageContent.Builder builder = ImGroupOperateMessageContent.newBuilder()
                .setOperateType(operateType)
                .setOperateUser(operateUser)
                .setGroupGuid(StringUtils.nullStrToEmpty(groupGuid))
                .setGroupName(StringUtils.nullStrToEmpty(groupName));

        if (groupUsers != null && groupUsers.size() > 0) {
            for (int i = 0; i < groupUsers.size(); i++) {
                builder.setGroupUsers(i, groupUsers.get(i));
            }
        }

        messageContent = builder.build();

        this.sendCallback = sendCallback;
    }

    @Override
    public ImMessageType getMessageType() {
        return ImMessageType.GroupOperate;
    }

    @Override
    public void setRequestBody(ImMessageRequest.Builder request) {
        request.setGroupOperateMessageRequest(
                ImGroupOperateMessageRequest.newBuilder().setGroupOperateMessageContent
                        (messageContent)
                        .build());
    }

    @Override
    public DefaultConsumer<Boolean> sendStatusCallback() {
        return sendCallback;
    }
}
