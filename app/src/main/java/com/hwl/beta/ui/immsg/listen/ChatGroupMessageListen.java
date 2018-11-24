package com.hwl.beta.ui.immsg.listen;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatGroupMessage;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.ui.common.MessageNotifyManage;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.beta.ui.convert.DBGroupAction;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.ui.immsg.IMConstant;
import com.hwl.beta.utils.StringUtils;
import com.hwl.im.imaction.AbstractMessageListenExecutor;
import com.hwl.imcore.improto.ImChatGroupMessageContent;
import com.hwl.imcore.improto.ImChatGroupMessageResponse;
import com.hwl.imcore.improto.ImMessageResponse;
import com.hwl.imcore.improto.ImMessageType;

import java.util.Date;

public class ChatGroupMessageListen extends
        AbstractMessageListenExecutor<ImChatGroupMessageResponse> {

    private ImChatGroupMessageContent messageContent;

    @Override
    public void executeCore(ImMessageType messageType, ImChatGroupMessageResponse response) {
        super.executeCore(messageType, response);
        messageContent = response.getChatGroupMessageContent();

        GroupInfo groupInfo = DaoUtils.getGroupInfoManagerInstance().get(messageContent
                .getToGroupGuid());
        if (groupInfo == null) return;

        Friend fromUser = this.getFriendInfo();

        this.addGroupUser();

        ChatGroupMessage message = new ChatGroupMessage();
        message.setGroupGuid(messageContent.getToGroupGuid());
        message.setGroupName(groupInfo.getGroupName());
        //        message.setGroupImage(model.getGroupImage());
        message.setFromUserId(messageContent.getFromUserId());
        message.setFromUserName(fromUser.getShowName());
        message.setFromUserHeadImage(fromUser.getHeadImage());
        message.setContentType(messageContent.getContentType());
        message.setContent(messageContent.getContent());
        message.setLocalUrl("");
        message.setOriginalUrl(messageContent.getOriginalUrl());
        message.setPreviewUrl(messageContent.getPreviewUrl());
        message.setImageWidth(messageContent.getImageWidth());
        message.setImageHeight(messageContent.getImageHeight());
        message.setPlayTime(messageContent.getPlayTime());
        message.setSendTime(new Date(response.getBuildTime()));

        ChatRecordMessage record = new ChatRecordMessage();
        record.setRecordType(IMConstant.CHAT_RECORD_TYPE_GROUP);
        record.setGroupGuid(message.getGroupGuid());
        record.setGroupName(groupInfo.getGroupName());
        record.setGroupUserImages(groupInfo.getUserImages());
        //        record.setRecordImage(model.getGroupImage());
        record.setFromUserId(message.getFromUserId());
        record.setFromUserName(fromUser.getShowName());
        record.setFromUserHeadImage(fromUser.getHeadImage());
        record.setTitle(groupInfo.getGroupName());
        record.setContentType(message.getContentType());
        record.setContent(fromUser.getShowName() + " : " + StringUtils.cutString(messageContent
                .getContent(), 25));
        record.setSendTime(message.getSendTime());

        DaoUtils.getChatGroupMessageManagerInstance().save(message);
        record = DaoUtils.getChatRecordMessageManagerInstance().save(record);

        EventBusUtil.sendChatGroupMessageEvent(message);
        EventBusUtil.sendChatRecordMessageSortEvent(record);
        MessageNotifyManage.play(DaoUtils.getGroupInfoManagerInstance().getGroupSettingIsShield
                (record.getGroupGuid()));
    }

    private Friend getFriendInfo() {
        Friend friend = DaoUtils.getFriendManagerInstance().get(messageContent.getFromUserId());
        if (friend == null) {
            friend = DBFriendAction.convertToFriendInfo(messageContent.getFromUserId(),
                    messageContent.getFromUserName(), messageContent.getFromUserImage(),
                    false);

            DaoUtils.getFriendManagerInstance().save(friend);
        }

        return friend;
    }

    private void addGroupUser() {
        //检测组是否是系统组
        //检测用户是否存在组里面
        //如果不存在则将用户添加到组里面
        if (messageContent.getToGroupGuid().equals(UserPosSP.getGroupGuid())) {
            GroupUserInfo userInfo = DaoUtils.getGroupUserInfoManagerInstance().getUserInfo
                    (messageContent.getToGroupGuid(), messageContent
                            .getFromUserId());
            if (userInfo == null) {
                userInfo = DBGroupAction.convertToGroupUserInfo(messageContent.getToGroupGuid(),
                        messageContent.getFromUserId());
                DaoUtils.getGroupUserInfoManagerInstance().add(userInfo);
//                if (groupUserImages.size() < DBConstant.GROUP_IMAGE_COUNT) {
//                    groupUserImages.add(userInfo.getUserHeadImage());
//                    DaoUtils.getGroupInfoManagerInstance().add(groupInfo);
//                }
            }
        }

    }

    @Override
    public ImChatGroupMessageResponse getResponse(ImMessageResponse response) {
        return response.getChatGroupMessageResponse();
    }

}