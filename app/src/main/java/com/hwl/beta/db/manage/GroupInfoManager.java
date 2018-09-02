package com.hwl.beta.db.manage;

import android.content.Context;

import com.hwl.beta.db.BaseDao;
import com.hwl.beta.db.dao.GroupInfoDao;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.utils.StringUtils;

import java.util.List;

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
        daoSession.getGroupInfoDao().insertOrReplace(groupInfo);
    }

    public void addList(List<GroupInfo> groupInfos) {
        if (groupInfos == null || groupInfos.size() <= 0) return;
        daoSession.getGroupInfoDao().insertInTx(groupInfos);
    }

    public List<String> getGroupUserImages(String groupGuid) {
        GroupInfo groupInfo = daoSession.getGroupInfoDao().queryBuilder()
                .where(GroupInfoDao.Properties.GroupGuid.eq(groupGuid))
                .unique();
        if (groupInfo != null) {
            return groupInfo.getUserImages();
        }
        return null;
    }

    public GroupInfo get(String groupGuid) {
        if (StringUtils.isBlank(groupGuid)) return null;

        return daoSession.getGroupInfoDao().queryBuilder().where(GroupInfoDao.Properties.GroupGuid.eq(groupGuid)).unique();
    }

    public boolean getGroupSettingIsShield(String groupGuid) {
        if (StringUtils.isBlank(groupGuid)) return false;
        GroupInfo setting = get(groupGuid);
        if (setting == null) return false;
        return setting.getIsShield();
    }

    public boolean updateUserCount(String groupGuid, int count) {
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

    public void deleteGroupInfo(GroupInfo groupInfo) {
        if(groupInfo==null) return;
        daoSession.getGroupInfoDao().delete(groupInfo);
    }
}
