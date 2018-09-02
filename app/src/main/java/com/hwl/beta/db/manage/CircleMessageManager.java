package com.hwl.beta.db.manage;

import android.content.Context;

import com.hwl.beta.db.BaseDao;
import com.hwl.beta.db.DBConstant;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.dao.CircleMessageDao;
import com.hwl.beta.db.entity.CircleMessage;
import com.hwl.beta.db.entity.Friend;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class CircleMessageManager extends BaseDao<CircleMessage> {
    public CircleMessageManager(Context context) {
        super(context);
    }

    public List<CircleMessage> getAll() {
        List<CircleMessage> messages = daoSession.getCircleMessageDao()
                .queryBuilder()
                .orderDesc(CircleMessageDao.Properties.Id)
                .list();
        List<Friend> friends = DaoUtils.getFriendManagerInstance().getList(getCommentUserIds(messages));
        setCircleMessageFriendInfo(messages, friends);

        if (messages == null) {
            messages = new ArrayList<>();
        }
        return messages;
    }

    public void setCircleMessageFriendInfo(List<CircleMessage> messages, List<Friend> friends) {
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

    public List<Long> getCommentUserIds(List<CircleMessage> messages) {
        List<Long> userIds = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            userIds.add(messages.get(i).getUserId());
            if (messages.get(i).getReplyUserId() > 0)
                userIds.add(messages.get(i).getReplyUserId());
        }
        return userIds;
    }

    public boolean save(CircleMessage message) {
        if (message == null) return false;
        if (daoSession.getCircleMessageDao().insert(message) > 0) {
            return true;
        }
        return false;
    }

    public boolean deleteMessage(CircleMessage message) {
        if (message == null) return false;
        daoSession.getCircleMessageDao().delete(message);
        return true;
    }

    public void deleteAll() {
        daoSession.getCircleMessageDao().deleteAll();
    }

    public boolean updateDelete(long nearCircleId, int type, long fromUserId, int commentId) {
        if (nearCircleId <= 0 || fromUserId <= 0) return false;
        QueryBuilder<CircleMessage> query = daoSession.getCircleMessageDao().queryBuilder()
                .where(CircleMessageDao.Properties.CircleId.eq(nearCircleId))
                .where(CircleMessageDao.Properties.Type.eq(type))
                .where(CircleMessageDao.Properties.Status.notEq(DBConstant.STAUTS_DELETE))
                .where(CircleMessageDao.Properties.UserId.eq(fromUserId));
        if (commentId > 0) {
            query = query.where(CircleMessageDao.Properties.CommentId.eq(commentId));
        }
        CircleMessage message = query.limit(1).unique();
        if (message != null) {
            message.setStatus(DBConstant.STAUTS_DELETE);
            daoSession.getCircleMessageDao().update(message);
            return true;
        }
        return false;
    }
}
