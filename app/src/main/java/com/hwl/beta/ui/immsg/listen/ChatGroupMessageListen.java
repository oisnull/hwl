package com.hwl.beta.ui.immsg.listen;

import com.hwl.im.imaction.AbstractMessageListenExecutor;
import com.hwl.imcore.improto.ImChatGroupMessageResponse;
import com.hwl.imcore.improto.ImMessageResponse;
import com.hwl.imcore.improto.ImMessageType;

public class ChatGroupMessageListen extends AbstractMessageListenExecutor<ImChatGroupMessageResponse> {

    @Override
    public void executeCore(ImMessageType messageType, ImChatGroupMessageResponse response) {
        super.executeCore(messageType, response);
        System.out.println("ChatGroupMessageReceive success : " + response.getChatGroupMessageContent().toString());

//          GroupInfo groupInfo = DaoUtils.getGroupInfoManagerInstance().get(model.getGroupGuid());
//         if (groupInfo == null) return;
//         List<String> groupUserImages = model.getGroupUserImages() == null || model
//                 .getGroupUserImages().size() <= 0 ? groupInfo.getUserImages() : model
//                 .getGroupUserImages();

//         String fromUserName = model.getFromUserName();
//         Friend friend = DaoUtils.getFriendManagerInstance().get(model.getFromUserId());
//         if (friend != null) {
//             fromUserName = friend.getShowName();
//             if (DBFriendAction.updateFriendNameAndImage(friend, model.getFromUserName(), model
//                     .getFromUserHeadImage())) {
//                 EventBus.getDefault().post(friend);
//             }
//         }

//         //检测组是否是系统组
//         //检测用户是否存在组里面
//         //如果不存在则将用户添加到组里面
//         if (model.getGroupGuid().equals(UserPosSP.getGroupGuid())) {
//             GroupUserInfo userInfo = DaoUtils.getGroupUserInfoManagerInstance().getUserInfo(model
//                     .getGroupGuid(), model
//                     .getFromUserId());
//             if (userInfo == null) {
//                 userInfo = DBGroupAction.convertToGroupUserInfo(model);
//                 userInfo.setUserName(fromUserName);
//                 DaoUtils.getGroupUserInfoManagerInstance().add(userInfo);
//                 if (groupUserImages.size() < DBConstant.GROUP_IMAGE_COUNT) {
//                     groupUserImages.add(userInfo.getUserHeadImage());
//                     DaoUtils.getGroupInfoManagerInstance().add(groupInfo);
//                 }
//             }
//         }

//         ChatRecordMessage record = new ChatRecordMessage();
//         //record.setRecordId(1);
//         record.setRecordType(MQConstant.CHAT_RECORD_TYPE_GROUP);
//         record.setGruopGuid(model.getGroupGuid());
//         record.setGroupName(model.getGroupName());
//         record.setGroupUserImages(groupUserImages);
// //        record.setRecordImage(model.getGroupImage());
//         record.setFromUserId(model.getFromUserId());
//         record.setFromUserName(fromUserName);
//         record.setFromUserHeadImage(model.getFromUserHeadImage());
//         record.setTitle(model.getGroupName());
//         record.setContentType(model.getContentType());
//         record.setContent(StringUtils.isBlank(fromUserName) ? StringUtils.cutString(model
//                 .getContent(), 25) : (fromUserName + " : ") + StringUtils.cutString(model
//                 .getContent(), 25));
//         //record.setUnreadCount(1);
//         record.setSendTime(model.getSendTime());
//         record = DaoUtils.getChatRecordMessageManagerInstance().addOrUpdate(record);

//         ChatGroupMessage message = new ChatGroupMessage();
// //        message.setMsgId(1);
//         message.setGroupGuid(model.getGroupGuid());
//         message.setGroupName(model.getGroupName());
// //        message.setGroupImage(model.getGroupImage());
//         message.setFromUserId(model.getFromUserId());
//         message.setFromUserName(fromUserName);
//         message.setFromUserHeadImage(model.getFromUserHeadImage());
//         message.setContentType(model.getContentType());
//         message.setContent(model.getContent());
//         message.setLocalUrl("");
//         message.setPreviewUrl(model.getPreviewUrl());
//         message.setOriginalUrl(model.getOriginalUrl());
//         message.setImageWidth(model.getImageWidth());
//         message.setImageHeight(model.getImageHeight());
//         message.setPlayTime(model.getPlayTime());
//         message.setSendTime(model.getSendTime());
//         DaoUtils.getChatGroupMessageManagerInstance().save(message);

//         EventBus.getDefault().post(record);
//         EventBus.getDefault().post(message);
//         MessageNotifyManage.play(DaoUtils.getGroupInfoManagerInstance().getGroupSettingIsShield
//                 (record.getGruopGuid()));
    }

    @Override
    public ImChatGroupMessageResponse getResponse(ImMessageResponse response) {
        return response.getChatGroupMessageResponse();
    }

}