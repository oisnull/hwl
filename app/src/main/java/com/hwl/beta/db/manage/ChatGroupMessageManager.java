package com.hwl.beta.db.manage;

import android.content.Context;

import com.hwl.beta.db.BaseDao;
import com.hwl.beta.db.dao.ChatGroupMessageDao;
import com.hwl.beta.db.entity.ChatGroupMessage;
import com.hwl.beta.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/2/9.
 */

public class ChatGroupMessageManager extends BaseDao<ChatGroupMessage> {
    public ChatGroupMessageManager(Context context) {
        super(context);
    }

    public long save(ChatGroupMessage request) {
        if (request == null) return 0;

        return daoSession.getChatGroupMessageDao().insertOrReplace(request);
    }

    public List<ChatGroupMessage> getAll() {
        return daoSession.getChatGroupMessageDao().loadAll();
    }

    public List<ChatGroupMessage> getGroupMessages(String groupGuid) {
        return daoSession.getChatGroupMessageDao().queryBuilder()
                .where(ChatGroupMessageDao.Properties.GroupGuid.eq(groupGuid))
                .list();
    }

    public List<ChatGroupMessage> getGroupMessages(String groupGuid, long minMessageId, int pageSize) {
        return daoSession.getChatGroupMessageDao().queryBuilder()
                .orderDesc(ChatGroupMessageDao.Properties.MsgId)
                .where(ChatGroupMessageDao.Properties.GroupGuid.eq(groupGuid))
                .where(minMessageId <= 0 ? ChatGroupMessageDao.Properties.MsgId.gt(minMessageId) : ChatGroupMessageDao.Properties.MsgId.lt(minMessageId))
                .limit(pageSize)
                .list();
    }

    public void updateStatus(long msgId, int status) {
        if (msgId <= 0) return;
        ChatGroupMessage message = daoSession.getChatGroupMessageDao().loadByRowId(msgId);
        if (message == null) return;
        message.setSendStatus(status);
        save(message);
    }

    public boolean deleteMessage(long msgId) {
        if (msgId <= 0) return false;
        daoSession.getChatGroupMessageDao().deleteByKeyInTx(msgId);
        return true;
    }

    public boolean deleteMessage(ChatGroupMessage message) {
        if (message==null||message.getMsgId() <= 0) return false;
        daoSession.getChatGroupMessageDao().delete(message);
        return true;
    }

    public boolean deleteMessages(String groupGuid) {
        if (StringUtils.isBlank(groupGuid)) return false;
        List<ChatGroupMessage> messages = getGroupMessages(groupGuid);
        if (messages == null || messages.size() <= 0) return true;
        daoSession.getChatGroupMessageDao().deleteInTx(messages);
        return true;
    }

    public boolean updateLocalPath(long msgId, String localPath) {
        ChatGroupMessage message = daoSession.getChatGroupMessageDao().loadByRowId(msgId);
        if (message == null) return false;
        message.setLocalUrl(localPath);
        save(message);
        return true;
    }
}
