package com.hwl.beta.db.manage;

import android.content.Context;

import com.hwl.beta.db.BaseDao;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.dao.ChatRecordMessageDao;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.immsg.IMConstant;
import com.hwl.beta.utils.StringUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/8.
 */

public class ChatRecordMessageManager extends BaseDao<ChatRecordMessage> {
    public ChatRecordMessageManager(Context context) {
        super(context);
    }

    public ChatRecordMessage getGroupRecord(String groupGuid) {
        if (StringUtils.isBlank(groupGuid)) return null;
        return daoSession.getChatRecordMessageDao().queryBuilder()
                .where(ChatRecordMessageDao.Properties.GroupGuid.eq(groupGuid))
                .unique();
    }

    public ChatRecordMessage getUserRecord(long myUserId, long fromUserId) {
        if (fromUserId <= 0) return null;
        return daoSession.getChatRecordMessageDao().queryBuilder()
                .whereOr(ChatRecordMessageDao.Properties.FromUserId.eq(fromUserId),
                        ChatRecordMessageDao.Properties.FromUserId.eq(myUserId))
                .whereOr(ChatRecordMessageDao.Properties.ToUserId.eq(fromUserId),
                        ChatRecordMessageDao.Properties.ToUserId.eq(myUserId))
                .unique();
    }

    public ChatRecordMessage updateUserRecordTitle(long myUserId, long fromUserId, String
            userName) {
        if (fromUserId <= 0) return null;
        ChatRecordMessage record = getUserRecord(myUserId, fromUserId);
        if (record == null) return null;
        if (StringUtils.isNotBlank(userName)) {
            record.setTitle(userName);
        }
//        if (StringUtils.isNotBlank(userImage))
//            record.setRecordImage(userImage);
        daoSession.getChatRecordMessageDao().update(record);
        return record;
    }

    public ChatRecordMessage deleteGroupRecord(String groupGuid) {
        ChatRecordMessage message = getGroupRecord(groupGuid);
        if (message == null) return null;
        daoSession.getChatRecordMessageDao().delete(message);
        return message;
    }

    public ChatRecordMessage save(ChatRecordMessage request) {
        if (request == null) return null;
        ChatRecordMessage existsRecord = null;
        QueryBuilder<ChatRecordMessage> query = daoSession.getChatRecordMessageDao().queryBuilder()
                .where(ChatRecordMessageDao.Properties.RecordType.eq(request.getRecordType()));
        if (request.getRecordType() == IMConstant.CHAT_RECORD_TYPE_USER) {
            //existsRecord = query.where(ChatRecordMessageDao.Properties.FromUserId.eq(request
            // .getFromUserId())).build().unique();
            existsRecord = daoSession.getChatRecordMessageDao().queryBuilder()
                    .whereOr(ChatRecordMessageDao.Properties.FromUserId.eq(request.getFromUserId
                            ()), ChatRecordMessageDao.Properties.FromUserId.eq(request
                            .getToUserId()))
                    .whereOr(ChatRecordMessageDao.Properties.ToUserId.eq(request.getFromUserId())
                            , ChatRecordMessageDao.Properties.ToUserId.eq(request.getToUserId()))
                    .unique();
        } else if (request.getRecordType() == IMConstant.CHAT_RECORD_TYPE_GROUP) {
            existsRecord = query.where(ChatRecordMessageDao.Properties.GroupGuid.eq(request
                    .getGroupGuid())).unique();
        }

        if (existsRecord != null) {
            existsRecord.setTitle(request.getTitle());
            existsRecord.setContentType(request.getContentType());
            existsRecord.setContent(request.getContent());

            existsRecord.setFromUserId(request.getFromUserId());
            existsRecord.setFromUserName(request.getFromUserName());
            existsRecord.setFromUserHeadImage(request.getFromUserHeadImage());

            existsRecord.setToUserId(request.getToUserId());
            existsRecord.setToUserName(request.getToUserName());
            existsRecord.setToUserHeadImage(request.getToUserHeadImage());

            existsRecord.setGroupName(request.getGroupName());
            existsRecord.setGroupUserImages(request.getGroupUserImages());

            if (request.getFromUserId() != UserSP.getUserId()) {
                existsRecord.setUnreadCount(existsRecord.getUnreadCount() + 1);
            } else {
                existsRecord.setUnreadCount(existsRecord.getUnreadCount());
            }
            existsRecord.setSendTime(request.getSendTime());
            daoSession.getChatRecordMessageDao().update(existsRecord);
            return existsRecord;
        } else {
            if (request.getFromUserId() != UserSP.getUserId()) {
                request.setUnreadCount(1);
            } else {
                request.setUnreadCount(0);
            }
            request.setRecordId(daoSession.getChatRecordMessageDao().insert(request));
            return request;
        }
    }

    public ChatRecordMessage deleteUserRecords(long myUserId, long friendUserId) {
        if (myUserId <= 0 || friendUserId <= 0 || myUserId == friendUserId) return null;
        ChatRecordMessage record = daoSession.getChatRecordMessageDao().queryBuilder()
                .whereOr(ChatRecordMessageDao.Properties.FromUserId.eq(myUserId),
                        ChatRecordMessageDao.Properties.FromUserId.eq(friendUserId))
                .whereOr(ChatRecordMessageDao.Properties.ToUserId.eq(myUserId),
                        ChatRecordMessageDao.Properties.ToUserId.eq(friendUserId))
                .unique();
        if (record == null) return null;
        daoSession.getChatRecordMessageDao().delete(record);
        return record;
    }

    public List<ChatRecordMessage> getAll() {
        return daoSession.getChatRecordMessageDao().loadAll();
    }

    public List<ChatRecordMessage> getRecords() {
        List<ChatRecordMessage> recordMessages = daoSession.getChatRecordMessageDao()
                .queryBuilder().orderDesc(ChatRecordMessageDao.Properties.SendTime).list();
        if (recordMessages != null && recordMessages.size() > 0) {
            for (int i = 0; i < recordMessages.size(); i++) {
                if (recordMessages.get(i).getRecordType() == IMConstant.CHAT_RECORD_TYPE_USER) {
                    long userId = UserSP.getUserId() == recordMessages.get(i).getFromUserId() ?
                            recordMessages.get(i).getToUserId() : recordMessages.get(i)
                            .getFromUserId();
                    recordMessages.get(i).setShield(DaoUtils
                            .getChatUserMessageManagerInstance().getChatUserSettingIsShield
                                    (userId));
                } else if (recordMessages.get(i).getRecordType() == IMConstant
                        .CHAT_RECORD_TYPE_GROUP) {
                    GroupInfo groupInfo = DaoUtils.getGroupInfoManagerInstance().get
                            (recordMessages.get(i).getGroupGuid());
                    if (groupInfo != null) {
                        recordMessages.get(i).setTitle(groupInfo.getGroupName());
                        recordMessages.get(i).setShield(groupInfo.getIsShield());
                        recordMessages.get(i).setGroupUserImages(groupInfo.getUserImages());
                    }
                }
            }
            return recordMessages;
        }
        return new ArrayList<>();
    }

    public void clearUnreadCount(ChatRecordMessage record) {
        if (record == null) return;
        record.setUnreadCount(0);
        daoSession.getChatRecordMessageDao().insertOrReplace(record);
    }

    public ChatRecordMessage clearUnreadCount(long recordId) {
        if (recordId <= 0) return null;
        ChatRecordMessage record = daoSession.getChatRecordMessageDao().load(recordId);
        if (record == null) return null;
        clearUnreadCount(record);
        return record;
    }

    public boolean deleteRecord(long recordId) {
        if (recordId <= 0) return false;
        daoSession.getChatRecordMessageDao().deleteByKeyInTx(recordId);
        return true;
    }

    public boolean deleteRecord(ChatRecordMessage record) {
        if (record == null || record.getRecordId() <= 0) return false;
        daoSession.getChatRecordMessageDao().delete(record);
        return true;
    }

}
