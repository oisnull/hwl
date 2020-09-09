package com.hwl.beta.db.manage;

import com.annimon.stream.Stream;
import com.hwl.beta.db.BaseDao;
import com.hwl.beta.db.dao.FriendDao;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.utils.CharacterParser;
import com.hwl.beta.utils.StringUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/1/28.
 */

public class FriendManager extends BaseDao<Friend> {

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

    public void saveList(List<Friend> friends) {
        if (friends == null || friends.size() <= 0) return;
        for (int i = 0; i < friends.size(); i++) {
            save(friends.get(i));
        }
    }

    public void addList(List<Friend> friends) {
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
        if (isExistsFriend(friendId)) {
            daoSession.getFriendDao().deleteByKey(friendId);
            return true;
        }
        return false;
    }

    public boolean isExistsFriend(long friendId) {
        if (friendId <= 0) return false;

        Friend friend = get(friendId);
        if (friend != null && friend.getId() > 0 && friend.getIsFriend()) {
            return true;
        }
        return false;
    }

    public boolean isExistsUser(long friendId) {
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

    public List<Friend> getAllFriends() {
        boolean isFriend = true;
        return daoSession.getFriendDao().queryBuilder()
                .where(FriendDao.Properties.IsFriend.eq(isFriend))
                .list();
    }

    public void addListAsync(List<Friend> friends) {
        if (friends == null || friends.size() <= 0) return;
        Observable.fromIterable(friends)
                .filter(new Predicate<Friend>() {
                    @Override
                    public boolean test(Friend friend) {
                        return !isExistsUser(friend.getId());
                    }
                })
                .doOnNext(new Consumer<Friend>() {
                    @Override
                    public void accept(Friend friend) {
                        save(friend);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void addFriends(List<Friend> friends) {
        if (friends == null || friends.size() <= 0) return;
        List<Long> fids = Stream.of(friends).map(f -> f.getId()).distinct().toList();
        List<Friend> oldFriends = daoSession.getFriendDao().queryBuilder()
                .where(FriendDao.Properties.Id.in(fids))
                .list();
        if (oldFriends == null || oldFriends.size() <= 0) {
            daoSession.getFriendDao().insertInTx(friends);
            return;
        }

        List<Friend> newFriends = Stream.of(friends).filter(u -> !oldFriends.contains(u)).toList();
        if (newFriends != null && newFriends.size() > 0) {
            daoSession.getFriendDao().insertInTx(newFriends);
        }
    }

}
