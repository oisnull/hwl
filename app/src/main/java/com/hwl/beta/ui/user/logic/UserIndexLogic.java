package com.hwl.beta.ui.user.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.net.user.NetUserInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.beta.ui.user.bean.UserIndexBean;
import com.hwl.beta.ui.user.standard.UserIndexStandard;

public class UserIndexLogic implements UserIndexStandard {
    @Override
    public UserIndexBean getUserInfo(long userId, String userName, String userImage) {
        if (userId <= 0) return null;
        UserIndexBean userBean = new UserIndexBean();
        if (userId == UserSP.getUserId()) {
            NetUserInfo netUserInfo = UserSP.getUserInfo();
            userBean.setUserId(netUserInfo.getId());
            userBean.setIdcard(UserIndexBean.IDCARD_MINE);
            userBean.setUserImage(netUserInfo.getHeadImage());
            userBean.setSymbol(netUserInfo.getSymbol());
            userBean.setUserName(netUserInfo.getName());
            userBean.setRegisterAddress(netUserInfo.getRegisterAddress());
            userBean.setUserCircleBackImage(netUserInfo.getCircleBackImage());
            userBean.setUserLifeNotes(netUserInfo.getLifeNotes());
            return userBean;
        }

        Friend friend = DaoUtils.getFriendManagerInstance().get(userId);
        if (friend == null) {
            friend = DBFriendAction.convertToFriendInfo(userId, userName, userImage);
        }
        userBean.setIdcard(friend.getIsFriend() ? UserIndexBean.IDCARD_FRIEND : UserIndexBean
                .IDCARD_OTHER);
        userBean.setUserId(friend.getId());
        userBean.setUserName(friend.getName());
        userBean.setUserImage(friend.getHeadImage());
        userBean.setRegisterAddress(friend.getCountry());
        userBean.setRemark(friend.getRemark());
        userBean.setSymbol(friend.getSymbol());
        userBean.setUserCircleBackImage(friend.getCircleBackImage());
        userBean.setUserLifeNotes(friend.getLifeNotes());
        return userBean;
    }

    @Override
    public void loadServerUserInfo(long userId, DefaultCallback<UserIndexBean, String> callback) {

    }

    @Override
    public void deleteFriend(long userId, DefaultCallback callback) {

    }
}
