package com.hwl.beta.db.manage;

import android.content.Context;

import com.hwl.beta.db.BaseDao;
import com.hwl.beta.db.DBConstant;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.dao.NearCircleMessageDao;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.NearCircleMessage;
import com.hwl.beta.utils.StringUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class NearCircleMessageManager extends BaseDao<NearCircleMessage> {
    public NearCircleMessageManager(Context context) {
        super(context);
    }

    public List<NearCircleMessage> getAll() {
        List<NearCircleMessage> messages = daoSession.getNearCircleMessageDao()
                .queryBuilder()
                .orderDesc(NearCircleMessageDao.Properties.Id)
                .list();

        List<Friend> friends = DaoUtils.getFriendManagerInstance().getList(getCommentUserIds(messages));
        setCircleMessageFriendInfo(messages, friends);

        if (messages == null) {
            messages = new ArrayList<>();
        }
        return messages;
    }

    public void setCircleMessageFriendInfo(List<NearCircleMessage> messages, List<Friend> friends) {
        if (messages == null || messages.size() <= 0) return;
        if (friends == null || friends.size() <= 0) return;
        for (int i = 0; i < friends.size(); i++) {
            for (int j = 0; j < messages.size(); j++) {
                if (friends.get(i).getId() == messages.get(j).getUserId()) {
                    messages.get(j).setUserName(friends.get(i).getShowName());
                }
                if (messages.get(j).getReplyUserId() > 0 && friends.get(i).getId() == messages.get(j).getReplyUserId()) {
                    messages.get(j).setReplyUserName(friends.get(i).getShowName());
                }
            }
        }
    }

    public List<Long> getCommentUserIds(List<NearCircleMessage> messages) {
        List<Long> userIds = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            userIds.add(messages.get(i).getUserId());
            if (messages.get(i).getReplyUserId() > 0)
                userIds.add(messages.get(i).getReplyUserId());
        }
        return userIds;
    }

    public boolean save(NearCircleMessage message) {
        if (message == null) return false;
        if (daoSession.getNearCircleMessageDao().insert(message) > 0) {
            return true;
        }
        return false;
    }

    public boolean deleteMessage(NearCircleMessage message) {
        if (message == null) return false;
        daoSession.getNearCircleMessageDao().delete(message);
        return true;
    }

    public void deleteAll() {
        daoSession.getNearCircleMessageDao().deleteAll();
    }

    public boolean updateDelete(long nearCircleId, int type, long fromUserId, String comment) {
        if (nearCircleId <= 0 || fromUserId <= 0) return false;
        QueryBuilder<NearCircleMessage> query = daoSession.getNearCircleMessageDao().queryBuilder()
                .where(NearCircleMessageDao.Properties.NearCircleId.eq(nearCircleId))
                .where(NearCircleMessageDao.Properties.Type.eq(type))
                .where(NearCircleMessageDao.Properties.Status.notEq(DBConstant.STAUTS_DELETE))
                .where(NearCircleMessageDao.Properties.UserId.eq(fromUserId));
        if (StringUtils.isNotBlank(comment)) {
            query = query.where(NearCircleMessageDao.Properties.Comment.eq(comment));
        }
        NearCircleMessage message = query.limit(1).unique();
        if (message != null) {
            message.setStatus(DBConstant.STAUTS_DELETE);
            daoSession.getNearCircleMessageDao().update(message);
            return true;
        }
        return false;
    }
}
