package com.hwl.beta.db.manage;

import android.database.Cursor;
import android.text.TextUtils;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.hwl.beta.db.BaseDao;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.dao.GroupUserInfoDao;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.utils.StringUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/2/10.
 */

public class GroupUserInfoManager extends BaseDao<GroupUserInfo> {

    public List<Long> getUserIdList(String groupGuid) {
        String sql = "select " + GroupUserInfoDao.Properties.UserId.columnName + " from " +
                GroupUserInfoDao.TABLENAME + " where "
                + GroupUserInfoDao.Properties.GroupGuid.columnName + " = '" + groupGuid + "'";
        List<Long> ids = new ArrayList<>();
        Cursor cursor = daoSession.getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            ids.add(cursor.getLong(0));
        }
        return ids;
    }

    public List<String> getUserImages(String groupGuid) {
        List<Long> userIds = getUserIdList(groupGuid);
        if (userIds == null || userIds.size() <= 0) return null;

        List<Friend> friends = DaoUtils.getFriendManagerInstance().getList(userIds);
        if (friends == null || friends.size() <= 0) return null;

        List<String> imgs = new ArrayList<>(friends.size());
        for (int i = 0; i < friends.size(); i++) {
            imgs.add(friends.get(i).getHeadImage());
        }
        return imgs;
    }

    public GroupUserInfo getUserInfo(String groupGuid, long userId) {
        if (TextUtils.isEmpty(groupGuid) || userId <= 0) return null;

        return daoSession.getGroupUserInfoDao().queryBuilder()
                .where(GroupUserInfoDao.Properties.UserId.eq(userId))
                .where(GroupUserInfoDao.Properties.GroupGuid.eq(groupGuid))
                .unique();
    }

    public long add(GroupUserInfo userInfo) {
        GroupUserInfo user = getUserInfo(userInfo.getGroupGuid(), userInfo.getUserId());
        if (user != null) {
            return 0;
        }
        return daoSession.getGroupUserInfoDao().insert(userInfo);
    }

    public void addGroupUsers(String groupGuid, List<GroupUserInfo> users) {
        if (StringUtils.isBlank(groupGuid)) return;
        if (users == null || users.size() <= 0) return;

        List<Long> userIds = Stream.of(users).map(u -> u.getUserId()).distinct().toList();
        List<GroupUserInfo> oldUsers = daoSession.getGroupUserInfoDao().queryBuilder()
                .where(GroupUserInfoDao.Properties.UserId.in(userIds))
                .where(GroupUserInfoDao.Properties.GroupGuid.eq(groupGuid))
                .list();
        if (oldUsers == null || oldUsers.size() <= 0) {
            daoSession.getGroupUserInfoDao().insertInTx(users);
            return;
        }

        List<GroupUserInfo> newUsers = Stream.of(users).filter(u -> !oldUsers.contains(u)).toList();
        if (newUsers != null && newUsers.size() > 0) {
            daoSession.getGroupUserInfoDao().insertInTx(newUsers);
        }

    }

    public void addList(List<GroupUserInfo> users) {
        if (users == null || users.size() <= 0) return;

        for (int i = 0; i < users.size(); i++) {
            if (getUserInfo(users.get(i).getGroupGuid(), users.get(i).getUserId()) == null) {
                daoSession.getGroupUserInfoDao().insert(users.get(i));
            }
        }
    }

    public void addListAsync(List<GroupUserInfo> userInfos) {
        if (userInfos == null || userInfos.size() <= 0) return;
        Observable.fromIterable(userInfos)
                .filter(new Predicate<GroupUserInfo>() {
                    @Override
                    public boolean test(GroupUserInfo groupUserInfo) {
                        return getUserInfo(groupUserInfo.getGroupGuid(),
                                groupUserInfo.getUserId()) != null;
                    }
                })
                .doOnNext(new Consumer<GroupUserInfo>() {
                    @Override
                    public void accept(GroupUserInfo groupUserInfo) {
                        daoSession.getGroupUserInfoDao().insert(groupUserInfo);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public List<GroupUserInfo> getUsers(String groupGuid) {
        return getUsers(groupGuid, 0);
    }

    public List<GroupUserInfo> getUsers(String groupGuid, int count) {
        if (StringUtils.isBlank(groupGuid)) return null;
        QueryBuilder<GroupUserInfo> query = daoSession.getGroupUserInfoDao().queryBuilder()
                .where(GroupUserInfoDao.Properties.GroupGuid.eq(groupGuid));
        if (count > 0) {
            query = query.limit(count);
        }
        List<GroupUserInfo> groupUserInfos = query.list();
        if (groupUserInfos == null || groupUserInfos.size() <= 0)
            return null;

        List<Long> userIds = Stream.of(groupUserInfos).map(u -> u.getUserId()).toList();
        List<Friend> friends = DaoUtils.getFriendManagerInstance().getList(userIds);
        if (friends == null || friends.size() <= 0)
            return groupUserInfos;

        Stream<Friend> friendStream = Stream.of(friends);
        Stream.of(groupUserInfos)
                .forEach(g -> {
                    Optional<Friend> friend =
                            friendStream.filter(f -> f.getId() == g.getUserId()).findFirst();
                    if (friend.isPresent()) {
                        g.setUserName(friend.get().getShowName());
                        g.setUserImage(friend.get().getHeadImage());
                    }
                });

        return groupUserInfos;
    }

    public void deleteGroupUserInfo(String groupGuid) {
        String sql = "delete from " + GroupUserInfoDao.TABLENAME +
                " where " + GroupUserInfoDao.Properties.GroupGuid.columnName + " = '" + groupGuid
                + "'";
        daoSession.getDatabase().execSQL(sql);
    }

    public void deleteGroupUserInfo(String groupGuid, long userId) {
        String sql = "delete from " + GroupUserInfoDao.TABLENAME +
                " where " + GroupUserInfoDao.Properties.GroupGuid.columnName + " = '" + groupGuid
                + "'" +
                " and " + GroupUserInfoDao.Properties.UserId.columnName + "=" + userId;
        daoSession.getDatabase().execSQL(sql);
    }
}
