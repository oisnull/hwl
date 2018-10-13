package com.hwl.beta.ui.immsg.listen;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.im.imaction.AbstractMessageListenExecutor;
import com.hwl.im.improto.ImAddFriendMessageResponse;
import com.hwl.im.improto.ImMessageResponse;
import com.hwl.im.improto.ImMessageType;

public class AddFriendMessageListen extends
        AbstractMessageListenExecutor<ImAddFriendMessageResponse> {

    @Override
    public void executeCore(ImMessageType messageType, ImAddFriendMessageResponse
            response) {
        //存储消息到本地数据库中
        if (DaoUtils.getFriendRequestManagerInstance().isExists(response
                .getAddFriendMessageContent().getFromUserId())) {
            DaoUtils.getFriendRequestManagerInstance().save(DBFriendAction
                    .convertToFriendRequestInfo(response));
            return;
        } else {
            DaoUtils.getFriendRequestManagerInstance().save(DBFriendAction
                    .convertToFriendRequestInfo(response));

            //将数量存储到sp中
            MessageCountSP.setFriendRequestCount(MessageCountSP.getFriendRequestCount() + 1);
            EventBusUtil.sendFriendRequestEvent();
        }

    }

    @Override
    public ImAddFriendMessageResponse getResponse(ImMessageResponse response) {
        return response.getAddFriendMessageResponse();
    }
}
