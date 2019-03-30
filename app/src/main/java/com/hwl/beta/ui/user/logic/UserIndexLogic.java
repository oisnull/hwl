package com.hwl.beta.ui.user.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.user.NetUserInfo;
import com.hwl.beta.net.user.UserDetailsInfo;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.DeleteFriendResponse;
import com.hwl.beta.net.user.body.GetUserDetailsResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.rxext.RXDefaultObserver;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.ui.user.bean.UserIndexBean;
import com.hwl.beta.ui.user.standard.UserIndexStandard;

import io.reactivex.android.schedulers.AndroidSchedulers;

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
        userBean.setUpdateTime(friend.getUpdateTime());
        return userBean;
    }

    @Override
    public void loadServerUserInfo(long userId, final String updateTime, final
    DefaultCallback<UserDetailsInfo, String> callback) {
        if (userId == UserSP.getUserId()) return;

        UserService.getUserDetails(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RXDefaultObserver<GetUserDetailsResponse>() {
                    @Override
                    protected void onSuccess(GetUserDetailsResponse response) {
                        if (response.getUserDetailsInfo() != null) {
                            callback.success(response.getUserDetailsInfo());
                            if (response.getUserDetailsInfo().getUpdateTime() != null &&
                                    !response.getUserDetailsInfo().getUpdateTime().equals
                                            (updateTime)) {
                                Friend newInfo = DBFriendAction.convertToFriendInfo(response
                                        .getUserDetailsInfo());
                                DaoUtils.getFriendManagerInstance().save(newInfo);
                            }
                        }
                    }
                });
    }

    @Override
    public void deleteFriend(final long userId, final DefaultCallback callback) {
        UserService.deleteFriend(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RXDefaultObserver<DeleteFriendResponse>() {
                    @Override
                    protected void onSuccess(DeleteFriendResponse response) {
                        if (response.getStatus() == NetConstant.RESULT_SUCCESS || response
                                .getStatus() == NetConstant.RESULT_NONE) {
                            DaoUtils.getFriendManagerInstance().deleteFriend(userId);
                            UserSP.deleteOneFriendCount();
                            EventBusUtil.sendFriendDeleteEvent(userId);
                            callback.success(true);
                        } else {
                            onError("删除失败");
                        }
                    }

                    @Override
                    protected void onError(String resultMessage) {
                        super.onError(resultMessage);
                        callback.error(resultMessage);
                    }
                });
    }
}
