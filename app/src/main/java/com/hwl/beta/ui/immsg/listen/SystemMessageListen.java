package com.hwl.beta.ui.immsg.listen;

import android.util.Log;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatGroupMessage;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.MessageNotifyManage;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.ui.immsg.IMConstant;
import com.hwl.beta.utils.StringUtils;
import com.hwl.im.imaction.AbstractMessageListenExecutor;
import com.hwl.imcore.improto.ImMessageResponse;
import com.hwl.imcore.improto.ImMessageType;
import com.hwl.imcore.improto.ImSystemMessageContent;
import com.hwl.imcore.improto.ImSystemMessageResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SystemMessageListen extends
        AbstractMessageListenExecutor<ImSystemMessageResponse> {

    private ImSystemMessageResponse response;
    private ImSystemMessageContent messageContent;

    @Override
    public void executeCore(ImMessageType messageType, ImSystemMessageResponse response) {
        super.executeCore(messageType, response);
        this.response = response;
        this.messageContent = response.getSystemMessageContent();
        Log.d("SystemMessageListen", messageContent.toString());

        switch (messageContent.getSystemMessageType()) {
            case AddNearGroup:
                this.createChatGroupMessage();
                break;
        }
    }

    private void createChatGroupMessage() {
        GroupInfo groupInfo = this.getGroupInfo();

        ChatGroupMessage message = new ChatGroupMessage();
        message.setGroupGuid(response.getToGroupGuid());
        message.setGroupName(groupInfo.getGroupName());
        //        message.setGroupImage(model.getGroupImage());
        message.setFromUserId(0);
        message.setFromUserName("系统消息");
        message.setFromUserHeadImage(null);
        message.setContentType(IMConstant.CHAT_MESSAGE_CONTENT_TYPE_SYSTEM);
        if (response.getToUser().getUserId() == UserSP.getUserId()) {
            message.setContent(messageContent.getToUserDesc());
        } else {
            message.setContent(messageContent.getAddGroupDesc());
        }
        message.setSendTime(new Date(response.getBuildTime()));

        ChatRecordMessage record = new ChatRecordMessage();
        record.setRecordType(IMConstant.CHAT_RECORD_TYPE_GROUP);
        record.setGroupGuid(message.getGroupGuid());
        record.setGroupName(groupInfo.getGroupName());
        record.setGroupUserImages(groupInfo.getGroupImages());
        //        record.setRecordImage(model.getGroupImage());
        record.setFromUserId(message.getFromUserId());
        record.setFromUserName(message.getFromUserName());
        record.setFromUserHeadImage(message.getFromUserHeadImage());
        record.setTitle(groupInfo.getGroupName());
        record.setContentType(message.getContentType());
        record.setContent(message.getFromUserName() + " : " + StringUtils.cutString(message.getContent(), 25));
        record.setSendTime(message.getSendTime());

        DaoUtils.getChatGroupMessageManagerInstance().save(message);
        record = DaoUtils.getChatRecordMessageManagerInstance().save(record);

        EventBusUtil.sendChatGroupMessageEvent(message);
        EventBusUtil.sendChatRecordMessageSortEvent(record);
        MessageNotifyManage.play(DaoUtils.getGroupInfoManagerInstance().getGroupSettingIsShield
                (record.getGroupGuid()));
    }

    private GroupInfo getGroupInfo() {
        GroupInfo groupInfo = DaoUtils.getGroupInfoManagerInstance().get(response.getToGroupGuid());
        if (groupInfo == null) {
            groupInfo = new GroupInfo();
            groupInfo.setGroupGuid(response.getToGroupGuid());
            groupInfo.setGroupName(response.getToGroupName());
            List<String> userImages = new ArrayList<>(1);
            userImages.add(response.getToUser().getUserImage());
            userImages.add(UserSP.getUserHeadImage());
            groupInfo.setGroupImages(userImages);
        }
        return groupInfo;
    }

    @Override
    public ImSystemMessageResponse getResponse(ImMessageResponse response) {
        return response.getSystemMessageResponse();
    }

}