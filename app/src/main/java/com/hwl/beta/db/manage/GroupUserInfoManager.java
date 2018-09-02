package com.hwl.beta.db.manage;

import android.content.Context;
import android.database.Cursor;

import com.hwl.beta.db.BaseDao;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.dao.GroupUserInfoDao;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/2/10.
 */

public class GroupUserInfoManager extends BaseDao<GroupUserInfo> {
    public GroupUserInfoManager(Context context) {
        super(context);
    }

    public List<String> getTopUserImages(String groupGuid) {
        int count = 10;
        String sql = "select " + GroupUserInfoDao.Properties.UserHeadImage.columnName + " from " + GroupUserInfoDao.TABLENAME
                + " where " + GroupUserInfoDao.Properties.GroupGuid.columnName + " = '" + groupGuid + "'"
                + " limit " + count;
        Cursor cursor = daoSession.getDatabase().rawQuery(sql, null);
        List<String> images = new ArrayList<>(count);
        while (cursor.moveToNext()) {
            images.add(cursor.getString(0));
        }
        return images;
    }

    public List<Long> getUserIdList(String groupGuid) {
        String sql = "select " + GroupUserInfoDao.Properties.UserId.columnName + " from " + GroupUserInfoDao.TABLENAME + " where "
                + GroupUserInfoDao.Properties.GroupGuid.columnName + " = '" + groupGuid + "'";
        List<Long> ids = new ArrayList<>();
        Cursor cursor = daoSession.getDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            ids.add(cursor.getLong(0));
        }
        return ids;
    }

    public GroupUserInfo get(String groupGuid, long userId) {
        return daoSession.getGroupUserInfoDao().queryBuilder()
                .where(GroupUserInfoDao.Properties.GroupGuid.eq(groupGuid))
                .where(GroupUserInfoDao.Properties.UserId.eq(userId)).unique();
    }

    public GroupUserInfo getUserInfo(String groupGuid, long userId) {
        return daoSession.getGroupUserInfoDao().queryBuilder()
                .where(GroupUserInfoDao.Properties.UserId.eq(userId))
                .where(GroupUserInfoDao.Properties.GroupGuid.eq(groupGuid))
                .unique();
    }

    public long add(GroupUserInfo userInfo) {
        GroupUserInfo user = get(userInfo.getGroupGuid(), userInfo.getUserId());
        if (user != null) {
            return 0;
        }
        return daoSession.getGroupUserInfoDao().insert(userInfo);
    }

    public void addListAsync(List<GroupUserInfo> userInfos) {
        if (userInfos == null || userInfos.size() <= 0) return;
        Observable.fromIterable(userInfos)
                .map(new Function<GroupUserInfo, GroupUserInfo>() {
                    @Override
                    public GroupUserInfo apply(GroupUserInfo groupUserInfo) throws Exception {
                        if (get(groupUserInfo.getGroupGuid(), groupUserInfo.getUserId()) == null) {
                            return groupUserInfo;
                        }
                        return new GroupUserInfo();
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<GroupUserInfo>() {
                    @Override
                    public void accept(GroupUserInfo userInfo) throws Exception {
                        if (userInfo != null && userInfo.getUserId() > 0)
                            daoSession.getGroupUserInfoDao().insertOrReplace(userInfo);
                    }
                });
    }

    public List<GroupUserInfo> getUsers(String groupGuid) {
        if (StringUtils.isBlank(groupGuid)) return null;
        List<GroupUserInfo> groupUserInfos = daoSession.getGroupUserInfoDao().queryBuilder()
                .where(GroupUserInfoDao.Properties.GroupGuid.eq(groupGuid))
                .list();

        List<Long> userIds = new ArrayList<>(groupUserInfos.size());
        for (int i = 0; i < groupUserInfos.size(); i++) {
            userIds.add(groupUserInfos.get(i).getUserId());
        }

        List<Friend> friends = DaoUtils.getFriendManagerInstance().getList(userIds);
        if (friends != null && friends.size() > 0) {
            for (int i = 0; i < friends.size(); i++) {
                for (int j = 0; j < groupUserInfos.size(); j++) {
                    if (friends.get(i).getId() == groupUserInfos.get(j).getUserId()) {
                        groupUserInfos.get(j).setUserName(friends.get(i).getShowName());
                    }
                }
            }
        }

        return groupUserInfos;
    }

    public GroupUserInfo setUserName(String groupGuid, long userId, String userName) {
        GroupUserInfo userInfo = get(groupGuid, userId);
        if (userInfo != null) {
            userInfo.setUserName(userName);
            daoSession.getGroupUserInfoDao().update(userInfo);
        }
        return userInfo;
    }

    public void deleteGroupUserInfo(String groupGuid) {
        String sql = "delete from " + GroupUserInfoDao.TABLENAME +
                " where " + GroupUserInfoDao.Properties.GroupGuid.columnName + " = '" + groupGuid + "'";
        daoSession.getDatabase().execSQL(sql);
    }

    public void deleteGroupUserInfo(String groupGuid, long userId) {
        String sql = "delete from " + GroupUserInfoDao.TABLENAME +
                " where " + GroupUserInfoDao.Properties.GroupGuid.columnName + " = '" + groupGuid + "'" +
                " and " + GroupUserInfoDao.Properties.UserId.columnName + "=" + userId;
        daoSession.getDatabase().execSQL(sql);
    }
}
