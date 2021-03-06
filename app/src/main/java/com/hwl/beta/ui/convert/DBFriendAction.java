package com.hwl.beta.ui.convert;

import com.annimon.stream.Stream;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.FriendRequest;
import com.hwl.beta.net.user.NetGroupUserInfo;
import com.hwl.beta.net.user.NetNearUserInfo;
import com.hwl.beta.net.user.NetUserFriendInfo;
import com.hwl.beta.net.user.NetUserInfo;
import com.hwl.beta.net.user.UserDetailsInfo;
import com.hwl.beta.utils.StringUtils;
import com.hwl.imcore.improto.ImAddFriendMessageResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/4/1.
 */

public class DBFriendAction {

    public static FriendRequest convertToFriendRequestInfo(ImAddFriendMessageResponse response) {
        FriendRequest request = new FriendRequest();
        request.setRemark(response.getAddFriendMessageContent().getContent());
        request.setFriendHeadImage(response.getAddFriendMessageContent().getFromUserHeadImage());
        request.setFriendName(response.getAddFriendMessageContent().getFromUserName());
        request.setFriendId(response.getAddFriendMessageContent().getFromUserId());
        request.setStatus(0);
        request.setRequestTime(new Date(response.getBuildTime()));
        return request;
    }

    public static List<Friend> convertToFriendInfos(List<NetUserFriendInfo> netUserFriendInfos) {
        if (netUserFriendInfos == null) return null;
        List<Friend> friends = new ArrayList<>(netUserFriendInfos.size());
        for (int i = 0; i < netUserFriendInfos.size(); i++) {
            friends.add(convertToFriendInfo(netUserFriendInfos.get(i)));
        }
        return friends;
    }

    public static Friend convertToFriendInfo(NetUserFriendInfo netFriendInfo) {
        Friend friend = new Friend();
        friend.setId(netFriendInfo.getId());
        friend.setSymbol(netFriendInfo.getSymbol());
        friend.setName(netFriendInfo.getName());
        friend.setSex(netFriendInfo.getSex());
        friend.setRemark(netFriendInfo.getNameRemark());
        friend.setHeadImage(netFriendInfo.getHeadImage());
        friend.setCountry(netFriendInfo.getCountry());
        friend.setProvince(netFriendInfo.getProvince());
        friend.setUpdateTime(netFriendInfo.getUpdateTime());
        friend.setCircleBackImage(netFriendInfo.getCircleBackImage());
        friend.setLifeNotes(netFriendInfo.getLifeNotes());
        friend.setIsFriend(true);
        return friend;
    }

    public static List<Friend> convertGroupUserToFriendInfos(List<NetGroupUserInfo> netGroupUserInfos) {
        if (netGroupUserInfos == null) return null;
        List<Friend> friends = new ArrayList<>(netGroupUserInfos.size());
        for (int i = 0; i < netGroupUserInfos.size(); i++) {
            friends.add(convertToFriendInfo(netGroupUserInfos.get(i).getUserId(),
                    netGroupUserInfos.get(i).getUserName(), netGroupUserInfos.get(i)
                            .getUserHeadImage()));
        }
        return friends;
    }

    public static Friend convertToFriendInfo(UserDetailsInfo userDetailsInfo) {
        Friend friend = new Friend();
        friend.setId(userDetailsInfo.getId());
        friend.setSymbol(userDetailsInfo.getSymbol());
        friend.setName(userDetailsInfo.getName());
        friend.setSex(userDetailsInfo.getSex());
        friend.setRemark(userDetailsInfo.getNameRemark());
        friend.setHeadImage(userDetailsInfo.getHeadImage());
        //friend.setCountry(userDetailsInfo.getCountry());
        //friend.setProvince(userDetailsInfo.getProvince());
        friend.setUpdateTime(userDetailsInfo.getUpdateTime());
        friend.setCircleBackImage(userDetailsInfo.getCircleBackImage());
        friend.setLifeNotes(userDetailsInfo.getLifeNotes());
        friend.setIsFriend(userDetailsInfo.isFriend());
        return friend;
    }

    public static Friend convertToFriendInfo(long userId, String userName, String userHeadImage) {
        return convertToFriendInfo(userId, userName, userHeadImage, false);
    }

    public static Friend convertToFriendInfo(long userId, String userName, String userHeadImage,
                                             String groupUserRemark) {
        return convertToFriend(userId, userName, userHeadImage, groupUserRemark, false);
    }

    public static Friend convertToFriendInfo(long userId, String userName, String userHeadImage,
                                             boolean isFriend) {
        return convertToFriend(userId, userName, userHeadImage, userName, isFriend);
    }

    public static Friend convertToFriendInfo(NetUserInfo netUserInfo) {
        Friend friend = new Friend();
        friend.setId(netUserInfo.getId());
        friend.setSymbol(netUserInfo.getSymbol());
        friend.setName(netUserInfo.getName());
        friend.setSex(SexAction.OTHER_2);
        friend.setRemark(netUserInfo.getName());
        friend.setHeadImage(netUserInfo.getHeadImage());
        friend.setCircleBackImage(netUserInfo.getCircleBackImage());
        friend.setLifeNotes(netUserInfo.getLifeNotes());
        friend.setIsFriend(false);
        return friend;
    }

    public static Friend convertToFriend(long userId,
                                         String userName,
                                         String userHeadImage,
                                         String groupUserRemark,
                                         boolean isFriend) {
        Friend friend = new Friend();
        friend.setId(userId);
        friend.setSymbol("");
        friend.setName(userName);
        friend.setSex(SexAction.OTHER_2);
        friend.setRemark(userName);
        friend.setHeadImage(userHeadImage);
        friend.setUpdateTime(new Date().toString());
        friend.setIsFriend(isFriend);
        friend.setGroupRemark(groupUserRemark);
        return friend;
    }

    public static List<Friend> convertToFriends(List<NetNearUserInfo> users,
                                                boolean isFriend) {
        if (users == null || users.size() <= 0) return null;

        return Stream.of(users)
                .map(u -> convertToFriend(u.getUserId(),
                        u.getUserName(),
                        u.getUserImage(),
                        u.getUserName(),
                        isFriend))
                .toList();
    }

    public static boolean updateFriendNameAndImage(Friend friend, String newName, String
            newHeadImage) {
        if (friend == null || StringUtils.isBlank(newName) || StringUtils.isBlank(newHeadImage))
            return false;

        boolean isUpdate = false;
        if (!friend.getName().equals(newName)) {
            friend.setName(newName);
            isUpdate = true;
        }
        if (!friend.getHeadImage().equals(newHeadImage)) {
            friend.setHeadImage(newHeadImage);
            isUpdate = true;
        }

        if (isUpdate) {
            DaoUtils.getFriendManagerInstance().save(friend);
        }
        return isUpdate;
    }
}
