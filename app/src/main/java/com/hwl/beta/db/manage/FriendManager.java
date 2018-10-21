package com.hwl.beta.db.manage;

import android.content.Context;

import com.hwl.beta.db.BaseDao;
import com.hwl.beta.db.dao.FriendDao;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.utils.CharacterParser;
import com.hwl.beta.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/1/28.
 */

public class FriendManager extends BaseDao<Friend> {
    public FriendManager(Context context) {
        super(context);
    }

    public String getFriendName(Friend friend) {
        if (friend == null) return null;
        if (!StringUtils.isBlank(friend.getRemark())) {
            return friend.getRemark();
        } else if (!StringUtils.isBlank(friend.getName())) {
            return friend.getName();
        } else if (!StringUtils.isBlank(friend.getSymbol())) {
            return friend.getSymbol();
        }
        return "--";
    }

    public void save(Friend friend) {
        if (friend == null || friend.getId() <= 0) return;
        setFirstLetter(friend);
        daoSession.getFriendDao().insertOrReplace(friend);
    }

    public void save(List<Friend> friends) {
        if (friends == null || friends.size() <= 0) return;
        for (int i = 0; i < friends.size(); i++) {
            setFirstLetter(friends.get(i));
        }
        daoSession.getFriendDao().insertInTx(friends);
    }

    public void setFirstLetter(Friend friend) {
        if (!StringUtils.isBlank(friend.getRemark())) {
            friend.setFirstLetter(CharacterParser.getInstance().getFirstLetter(friend.getRemark()));
        } else if (!StringUtils.isBlank(friend.getName())) {
            friend.setFirstLetter(CharacterParser.getInstance().getFirstLetter(friend.getName()));
        } else if (!StringUtils.isBlank(friend.getSymbol())) {
            friend.setFirstLetter(CharacterParser.getInstance().getFirstLetter(friend.getSymbol()));
        }
    }

    public boolean deleteFriend(long friendId) {
        if (friendId <= 0) return false;
        if (isExists(friendId)) {
            daoSession.getFriendDao().deleteByKey(friendId);
            return true;
        }
        return false;
    }

    public boolean isExists(long friendId) {
        if (friendId <= 0) return false;

        Friend friend = get(friendId);
        if (friend != null && friend.getId() > 0) {
            return true;
        }
        return false;
    }

    public Friend get(long friendId) {
        if (friendId <= 0) return null;

        return daoSession.getFriendDao().loadByRowId(friendId);
    }

    public Friend updateRemark(long friendId, String remark) {
        Friend friend = get(friendId);
        if (friend == null) return null;
        friend.setRemark(remark);
        save(friend);
        return friend;
    }

    public List<Friend> getList(List<Long> friendIds) {
        if (friendIds == null || friendIds.size() <= 0) return null;
        return daoSession.getFriendDao().queryBuilder()
                .where(FriendDao.Properties.Id.in(friendIds))
                .list();
    }

    public List<Friend> getAll() {
        boolean isFriend = true;
        return daoSession.getFriendDao().queryBuilder()
                .where(FriendDao.Properties.IsFriend.eq(isFriend))
                .list();
    }

}
