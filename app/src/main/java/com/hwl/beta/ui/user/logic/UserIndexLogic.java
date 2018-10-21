package com.hwl.beta.ui.user.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.user.NetUserInfo;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.DeleteFriendResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.beta.ui.ebus.EventBusUtil;
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
    public void deleteFriend(final long userId, final DefaultCallback callback) {
        UserService.deleteFriend(userId)
                .subscribe(new NetDefaultObserver<DeleteFriendResponse>() {
                    @Override
                    protected void onSuccess(DeleteFriendResponse response) {
                        if (response.getStatus() == NetConstant.RESULT_SUCCESS || response
                                .getStatus() == NetConstant.RESULT_NONE) {
                            DaoUtils.getFriendManagerInstance().deleteFriend(userId);
                            UserSP.deleteOneFriendCount();
                            EventBusUtil.sendFriendDeleteEvent(userId);
                            callback.success(true);
//                            //删除好友聊天数据
//                            ChatRecordMessage record = DaoUtils
//                                    .getChatRecordMessageManagerInstance().deleteUserRecords
//                                            (UserSP.getUserId(), userBean
//                                                    .getUserId());
//                            if (record != null) {
//                                DaoUtils.getChatUserMessageManagerInstance()
//                                        .deleteUserMessages(UserSP.getUserId(), userBean
//                                                .getUserId());
//                                EventBus.getDefault().post(new EventActionChatRecord
//                                        (EventBusConstant.EB_TYPE_ACTINO_REMOVE, record));
//                            }
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
