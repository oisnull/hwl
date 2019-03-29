package com.hwl.beta.ui.user.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.ChatUserMessage;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.FriendRequest;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.AddFriendResponse;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.convert.DBChatMessageAction;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.ui.immsg.IMClientEntry;
import com.hwl.beta.ui.immsg.IMDefaultSendOperateListener;
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
            //clear friend message request count
            MessageCountSP.setFriendRequestCount(0);
            EventBusUtil.sendFriendRequestEvent();
        }

        return friendRequests;
    }

    @Override
    public Observable addFriend(final FriendRequest friendRequest) {
        // if (friendRequest == null || friendRequest.getFriendId() <= 0) {
            // callback.error("friendId is empty");
            // return;
        // }
        // if (StringUtils.isBlank(friendRequest.getFriendName())) {
            // callback.error("friendName is empty");
            // return;
        // }

        // if (DaoUtils.getFriendManagerInstance().isExistsFriend(friendRequest.getFriendId())) {
            // DaoUtils.getFriendRequestManagerInstance().delete(friendRequest);
            // callback.success(true);
            // return;
        // }
       return UserService.addFriend(friendRequest.getFriendId(), friendRequest.getFriendName())
				.doOnNext(new Consumer<AddFriendResponse>(){
					@Override
                    public void accept(AddFriendResponse response) throws Exception {
						if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
                            Friend friend = DBFriendAction.convertToFriendInfo(response
                                    .getFriendInfo());
                            DaoUtils.getFriendManagerInstance().save(friend);
                            DaoUtils.getFriendRequestManagerInstance().delete(friendRequest);
                            MessageCountSP.setFriendRequestReductionCount();
                            EventBusUtil.sendFriendAddEvent(friend);
                            sendChatUserMessage(friend);
                        }
					}
				});
    }

    private void sendChatUserMessage(final Friend friend) {
        final String content = "我们已经成为好友了";
        IMClientEntry.sendChatUserTextMessage(friend.getId(), content, true, new
                IMDefaultSendOperateListener("ChatUserMessage(AddFriendSuccess)") {
                    @Override
                    public void success1() {
                        ChatUserMessage chatUserMessage = DBChatMessageAction.convertToTextMessage
                                (friend, content);
                        ChatRecordMessage chatRecordMessage = DBChatMessageAction
                                .convertToRecordMessage
                                        (friend, content);

                        DaoUtils.getChatUserMessageManagerInstance().save(chatUserMessage);
                        DaoUtils.getChatRecordMessageManagerInstance().save(chatRecordMessage);

                        EventBusUtil.sendChatUserMessageEvent(chatUserMessage);
                        EventBusUtil.sendChatRecordMessageSortEvent(chatRecordMessage);
                    }
                });
    }

    @Override
    public void deleteFriendRequest(FriendRequest info) {
        DaoUtils.getFriendRequestManagerInstance().delete(info);
        MessageCountSP.setFriendRequestReductionCount();
    }
}
