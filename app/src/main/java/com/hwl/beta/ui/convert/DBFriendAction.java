package com.hwl.beta.ui.convert;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.net.user.NetUserFriendInfo;
import com.hwl.beta.net.user.UserDetailsInfo;
import com.hwl.beta.utils.StringUtils;

/**
 * Created by Administrator on 2018/4/1.
 */

public class DBFriendAction {
    public static Friend convertToFriendInfo(NetUserFriendInfo netFriendInfo) {
        Friend friend = new Friend();
        friend.setId(netFriendInfo.getId());
        friend.setSymbol(netFriendInfo.getSymbol());
        friend.setName(netFriendInfo.getName());
        friend.setSex(netFriendInfo.getSex());
//        friend.setRemark(netFriendInfo.getNameRemark());
        friend.setHeadImage(netFriendInfo.getHeadImage());
        friend.setCountry(netFriendInfo.getCountry());
        friend.setProvince(netFriendInfo.getProvince());
        friend.setUpdateTime(netFriendInfo.getUpdateTime());
        friend.setCircleBackImage(netFriendInfo.getCircleBackImage());
        friend.setLifeNotes(netFriendInfo.getLifeNotes());
        return friend;
    }

    public static Friend convertToFriendInfo(UserDetailsInfo userDetailsInfo) {
        Friend friend = new Friend();
        friend.setId(userDetailsInfo.getId());
        friend.setSymbol(userDetailsInfo.getSymbol());
        friend.setName(userDetailsInfo.getName());
        friend.setSex(userDetailsInfo.getSex());
        friend.setRemark(userDetailsInfo.getNameRemark());
        friend.setHeadImage(userDetailsInfo.getHeadImage());
        friend.setCountry(userDetailsInfo.getCountry());
        friend.setProvince(userDetailsInfo.getProvince());
        friend.setUpdateTime(userDetailsInfo.getUpdateTime());
        friend.setCircleBackImage(userDetailsInfo.getCircleBackImage());
        friend.setLifeNotes(userDetailsInfo.getLifeNotes());
        return friend;
    }

    public static boolean updateFriendNameAndImage(Friend friend, String newName, String newHeadImage) {
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
