package com.hwl.beta.db.manage;

import android.content.Context;

import com.hwl.beta.db.BaseDao;
import com.hwl.beta.db.dao.GroupInfoDao;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.utils.StringUtils;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/2/10.
 */

public class GroupInfoManager extends BaseDao<GroupInfo> {
    public GroupInfoManager(Context context) {
        super(context);
    }

    public void add(GroupInfo groupInfo) {
        if (groupInfo == null || StringUtils.isBlank(groupInfo.getGroupGuid()))
            return;

        if (groupInfo.getId() != null && groupInfo.getId() > 0) {
            daoSession.getGroupInfoDao().insertOrReplace(groupInfo);
        } else if (get(groupInfo.getGroupGuid()) == null)
            daoSession.getGroupInfoDao().insert(groupInfo);
    }

    public void addList(List<GroupInfo> groupInfos) {
        if (groupInfos == null || groupInfos.size() <= 0) return;
        for (int i = 0; i < groupInfos.size(); i++) {
            this.add(groupInfos.get(i));
        }
    }

    public List<String> getGroupUserImages(String groupGuid) {
        GroupInfo groupInfo = daoSession.getGroupInfoDao().queryBuilder()
                .where(GroupInfoDao.Properties.GroupGuid.eq(groupGuid))
                .unique();
        if (groupInfo != null) {
            return groupInfo.getGroupImages();
        }
        return null;
    }

    public GroupInfo get(String groupGuid) {
        if (StringUtils.isBlank(groupGuid)) return null;

        return daoSession.getGroupInfoDao().queryBuilder()
                .where(GroupInfoDao.Properties.GroupGuid.eq(groupGuid))
                .unique();
    }

    public void setLoadUserStatus(String groupGuid, boolean isLoaded) {
        if (StringUtils.isBlank(groupGuid)) return;

        String updateSql = String.format("update %s set %s='%s' where %s='%s'",
                GroupInfoDao.TABLENAME
                , GroupInfoDao.Properties.IsLoadUser.columnName, isLoaded ? 1 : 0,
                GroupInfoDao.Properties.GroupGuid.columnName, groupGuid);
        daoSession.getDatabase().execSQL(updateSql);
    }

    public boolean getGroupSettingIsShield(String groupGuid) {
        if (StringUtils.isBlank(groupGuid)) return false;
        GroupInfo setting = get(groupGuid);
        if (setting == null) return false;
        return setting.getIsShield();
    }

    public boolean setUserCount(String groupGuid, int count) {
        if (StringUtils.isBlank(groupGuid)) return false;

        GroupInfo group = get(groupGuid);
        if (group != null) {
            group.setGroupUserCount(count);
            add(group);
            return true;
        }
        return false;
    }

    public List<GroupInfo> getAll() {
        return daoSession.getGroupInfoDao().loadAll();
    }

    public GroupInfo setGroupNote(String groupGuid, String content) {
        GroupInfo groupInfo = get(groupGuid);
        groupInfo.setGroupNote(content);
        add(groupInfo);
        return groupInfo;
    }

    public GroupInfo setGroupName(String groupGuid, String content) {
        GroupInfo groupInfo = get(groupGuid);
        groupInfo.setGroupName(content);
        add(groupInfo);
        return groupInfo;
    }

    public GroupInfo setGroupMyName(String groupGuid, String myUserName) {
        GroupInfo groupInfo = get(groupGuid);
        groupInfo.setMyUserName(myUserName);
        add(groupInfo);
        return groupInfo;
    }

    public GroupInfo setGroupDismiss(String groupGuid) {
        GroupInfo groupInfo = get(groupGuid);
        groupInfo.setIsDismiss(true);
        add(groupInfo);
        return groupInfo;
    }

    public GroupInfo setGroupImages(String groupGuid, List<String> images) {
        GroupInfo groupInfo = get(groupGuid);
        groupInfo.setGroupImages(images);
        add(groupInfo);
        return groupInfo;
    }

    public void setGroupInfo(String groupGuid, Consumer<GroupInfo> setCall) {
        if (setCall == null) return;
        GroupInfo groupInfo = get(groupGuid);
        if (groupInfo != null) {
            try {
                setCall.accept(groupInfo);
                add(groupInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void deleteGroupInfo(GroupInfo groupInfo) {
        if (groupInfo == null) return;
        daoSession.getGroupInfoDao().delete(groupInfo);
    }

    public void deleteGroupInfo(String groupGuid) {
        GroupInfo groupInfo = get(groupGuid);
        if (groupInfo == null) return;
        daoSession.getGroupInfoDao().delete(groupInfo);
    }
}
