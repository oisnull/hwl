package com.hwl.beta.ui.user.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.FriendRequest;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.AddFriendResponse;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.ui.user.standard.NewFriendStandard;
import com.hwl.beta.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class NewFriendLogic implements NewFriendStandard {
    @Override
    public List<FriendRequest> getFriendRequestInfos() {
        List<FriendRequest> friendRequests = DaoUtils.getFriendRequestManagerInstance().getAll();
        if (friendRequests == null) {
            friendRequests = new ArrayList<>();
        } else {
            MessageCountSP.setFriendRequestCount(0);//清空好友请求数据
            EventBusUtil.sendFriendRequestEvent();
        }

        return friendRequests;
    }

    @Override
    public void addFriend(final FriendRequest friendRequest, final DefaultCallback<Boolean, String>
            callback) {
        if (friendRequest == null || friendRequest.getFriendId() <= 0) {
            callback.error("friendId is empty");
            return;
        }
        if (StringUtils.isBlank(friendRequest.getFriendName())) {
            callback.error("friendName is empty");
            return;
        }
        UserService.addFriend(friendRequest.getFriendId(), friendRequest.getFriendName())
                .subscribe(new NetDefaultObserver<AddFriendResponse>() {
                    @Override
                    protected void onSuccess(AddFriendResponse response) {
                        if (response != null && response.getStatus() == NetConstant
                                .RESULT_SUCCESS) {
                            Friend friend = DBFriendAction.convertToFriendInfo(response
                                    .getFriendInfo());
                            DaoUtils.getFriendManagerInstance().save(friend);
                            DaoUtils.getFriendRequestManagerInstance().delete(friendRequest);
                            MessageCountSP.setFriendRequestReductionCount();
                            EventBusUtil.sendFriendEvent(friend);
                            callback.success(true);
//                            sendSuccessMessage(friend);//发送好友添加成功的聊天消息
                        }
                    }

                    @Override
                    protected void onError(String resultMessage) {
                        super.onError(resultMessage);
                        callback.error(resultMessage);
                    }

                    @Override
                    protected void onRelogin() {
                        callback.relogin();
                    }
                });
    }

    @Override
    public void deleteFriendRequest(FriendRequest info) {
        DaoUtils.getFriendRequestManagerInstance().delete(info);
        MessageCountSP.setFriendRequestReductionCount();
    }
}
