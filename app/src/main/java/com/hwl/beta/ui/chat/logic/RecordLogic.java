package com.hwl.beta.ui.chat.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.ui.chat.standard.RecordStandard;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.immsg.IMConstant;

import java.util.List;

public class RecordLogic implements RecordStandard {
    @Override
    public List<ChatRecordMessage> loadRecords() {
        return DaoUtils.getChatRecordMessageManagerInstance().getRecords();
    }

    @Override
    public void deleteRecord(ChatRecordMessage recordMessage, DefaultCallback<Boolean, String>
            callback) {
        boolean succ = DaoUtils.getChatRecordMessageManagerInstance().deleteRecord(recordMessage);
        if (succ) {
            callback.success(true);
        } else {
            callback.error("删除记录失败");
        }
    }

    @Override
    public void clearMessages(ChatRecordMessage record, DefaultCallback<Boolean, String>
            callback) {
        if (DaoUtils.getChatRecordMessageManagerInstance().deleteRecord(record)) {
            boolean succ = false;
            if (record.getRecordType() == IMConstant.CHAT_RECORD_TYPE_GROUP) {
                succ = DaoUtils.getChatGroupMessageManagerInstance().deleteMessages(record
                        .getGroupGuid());
                //如果群组的状态为解散状态，删除消息的时候直接删除群组相关信息
                GroupInfo groupInfo = DaoUtils.getGroupInfoManagerInstance().get(record
                        .getGroupGuid());
                if (groupInfo != null && groupInfo.getIsDismiss()) {
                    DaoUtils.getGroupInfoManagerInstance().deleteGroupInfo(groupInfo);
                    DaoUtils.getGroupUserInfoManagerInstance().deleteGroupUserInfo(groupInfo
                            .getGroupGuid());
                }
            } else if (record.getRecordType() == IMConstant.CHAT_RECORD_TYPE_USER) {
                succ = DaoUtils.getChatUserMessageManagerInstance().deleteUserMessages(
                        record.getFromUserId(),
                        record.getToUserId());
            }
            if (succ) {
                callback.success(true);
            } else {
                callback.error("清空聊天数据失败");
            }
        } else {
            callback.error("清空聊天数据失败");
        }
    }
}
