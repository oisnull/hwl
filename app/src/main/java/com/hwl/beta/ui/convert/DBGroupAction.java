package com.hwl.beta.ui.convert;

import com.annimon.stream.Stream;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.net.group.NetGroupInfo;
import com.hwl.beta.net.user.NetGroupUserInfo;
import com.hwl.beta.net.user.NetNearUserInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/3/31.
 */

public class DBGroupAction {
    public static final int GROUP_IMAGE_COUNT = 9;

    public static List<GroupInfo> convertToGroupInfos(List<NetGroupInfo> groupInfos) {
        if (groupInfos == null || groupInfos.size() <= 0) return null;
        List<GroupInfo> groups = new ArrayList<>(groupInfos.size());
        for (int i = 0; i < groupInfos.size(); i++) {
            GroupInfo groupInfo = new GroupInfo();
            groupInfo.setGroupGuid(groupInfos.get(i).getGroupGuid());
            groupInfo.setGroupNote(groupInfos.get(i).getGroupNote());
            groupInfo.setGroupName(groupInfos.get(i).getGroupName());
            groupInfo.setBuildUserId(groupInfos.get(i).getBuildUserId());
            groupInfo.setGroupBackImage("");
            groupInfo.setBuildTime(groupInfos.get(i).getBuildDate());
            groupInfo.setUpdateTime(groupInfos.get(i).getUpdateDate());
            groupInfo.setGroupUserCount(groupInfos.get(i).getGroupUserCount());
            groupInfo.setMyUserName(UserSP.getUserShowName());
            groupInfo.setGroupImages(new ArrayList<String>());
            for (int j = 0; j < groupInfos.get(i).getGroupUsers().size(); j++) {
                groupInfo.getGroupImages().add(groupInfos.get(i).getGroupUsers().get(j)
                        .getUserHeadImage());
                if (j >= 8) break;
            }
            groups.add(groupInfo);
        }
        return groups;
    }

    public static GroupInfo convertToNearGroupInfo(String groupGuid,
                                                   String groupName,
                                                   int groupUserCount,
                                                   List<String> groupUserImages,
                                                   boolean isLoadUsers) {
        return convertToGroupInfo(groupGuid,
                groupName,
                null,
                0,
                groupUserCount,
                groupUserImages,
                new Date(),
                true,
                isLoadUsers);
    }

    public static GroupInfo convertToGroupInfo(String groupGuid,
                                               String groupName,
                                               String groupNote,
                                               long buildUserId,
                                               int groupUserCount,
                                               List<String> groupUserImages,
                                               Date buildTime,
                                               boolean isSystem,
                                               boolean isLoadUsers) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setGroupGuid(groupGuid);
        groupInfo.setGroupName(groupName);
        groupInfo.setGroupNote(groupNote);
        groupInfo.setBuildUserId(buildUserId);
        groupInfo.setBuildTime(buildTime);
        groupInfo.setGroupUserCount(groupUserCount);
        groupInfo.setGroupImages(groupUserImages);
        groupInfo.setMyUserName(UserSP.getUserShowName());
        groupInfo.setIsSystem(isSystem);
        groupInfo.setIsLoadUser(isLoadUsers);
        return groupInfo;
    }

    public static List<GroupUserInfo> convertToGroupUserInfos(List<NetGroupUserInfo> userInfos) {
        if (userInfos == null || userInfos.size() <= 0) return null;
        List<GroupUserInfo> groupUserInfos = new ArrayList<>(userInfos.size());
        for (int i = 0; i < userInfos.size(); i++) {
            GroupUserInfo user = convertToGroupUserInfo(userInfos.get(i).getGroupGuid(), userInfos
                    .get(i).getUserId());
            user.setUserName(userInfos.get(i).getUserName());
            user.setUserImage(userInfos.get(i).getUserHeadImage());
            groupUserInfos.add(user);
        }
        return groupUserInfos;
    }

    public static GroupUserInfo convertToGroupUserInfo(String groupGuid, long userId) {
        return convertToGroupUserInfo(groupGuid, userId, 0);
    }

    public static GroupUserInfo convertToGroupUserInfo(String groupGuid,
                                                       long userId,
                                                       double distance) {
        GroupUserInfo userInfo = new GroupUserInfo();
        userInfo.setGroupGuid(groupGuid);
        userInfo.setUserId(userId);
        userInfo.setDistance(distance);
        userInfo.setAddTime(new Date());
        return userInfo;
    }

//    public static GroupUserInfo convertToGroupUserInfo(ChatGroupMessageBean groupMessageBean) {
//        GroupUserInfo userInfo = new GroupUserInfo();
//        userInfo.setUserId(groupMessageBean.getFromUserId());
//        userInfo.setUserName(groupMessageBean.getFromUserName());
//        userInfo.setUserHeadImage(groupMessageBean.getFromUserHeadImage());
//        userInfo.setGroupGuid(groupMessageBean.getGroupGuid());
//        userInfo.setAddTime(new Date());
//        return userInfo;
//    }

    public static List<GroupUserInfo> convertToGroupUserInfos(String groupGuid, List<Friend>
            friends) {
        if (StringUtils.isBlank(groupGuid)) return null;
        if (friends == null || friends.size() <= 0) return null;

        List<GroupUserInfo> groupUserInfos = new ArrayList<>(friends.size());
        for (int i = 0; i < friends.size(); i++) {
            GroupUserInfo userInfo = new GroupUserInfo();
            userInfo.setUserId(friends.get(i).getId());
            userInfo.setUserName(friends.get(i).getName());
            userInfo.setUserImage(friends.get(i).getHeadImage());
            userInfo.setGroupGuid(groupGuid);
            userInfo.setAddTime(new Date());
            groupUserInfos.add(userInfo);
        }

        return groupUserInfos;
    }

    public static List<String> getGroupUserImagesV2(List<NetGroupUserInfo> userInfos) {
        if (userInfos == null || userInfos.size() <= 0) return null;
        List<String> groupUserImages = new ArrayList<>();
        for (int i = 0; i < userInfos.size(); i++) {
            if (i < GROUP_IMAGE_COUNT)
                groupUserImages.add(userInfos.get(i).getUserHeadImage());
            else
                break;
        }
        return groupUserImages;
    }

    public static List<String> getGroupUserImagesV3(List<GroupUserInfo> userInfos) {
        if (userInfos == null || userInfos.size() <= 0) return null;
        List<String> groupUserImages = new ArrayList<>();
        for (int i = 0; i < userInfos.size(); i++) {
            if (i < GROUP_IMAGE_COUNT)
                groupUserImages.add(userInfos.get(i).getUserImage());
            else
                break;
        }
        return groupUserImages;
    }

    public static List<String> getGroupUserImages(List<NetNearUserInfo> userInfos) {
        if (userInfos == null || userInfos.size() <= 0) return null;
        List<String> groupUserImages = new ArrayList<>();
        for (int i = 0; i < userInfos.size(); i++) {
            if (i < GROUP_IMAGE_COUNT)
                groupUserImages.add(userInfos.get(i).getUserImage());
            else
                break;
        }
        return groupUserImages;
    }

    public static List<GroupUserInfo> convertToNearGroupUsers(String groupGuid,
                                                              List<NetNearUserInfo> userInfos) {
        if (StringUtils.isBlank(groupGuid)) return null;
        if (userInfos == null || userInfos.size() <= 0) return null;

        return Stream.of(userInfos)
                .map(u -> convertToGroupUserInfo(groupGuid, u.getUserId(), u.getDistance()))
                .toList();
    }
}
