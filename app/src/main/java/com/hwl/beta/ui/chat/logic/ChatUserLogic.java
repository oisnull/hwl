package com.hwl.beta.ui.chat.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.ui.chat.standard.ChatUserStandard;

import java.util.List;

public class ChatUserLogic implements ChatUserStandard {
    static int pageSize = 10;
    @Override
    public Friend getChatUserInfo(long userId,String userName,String userHeadImage) {
        if(userId<=0) return null;
        Friend user = DaoUtils.getFriendManagerInstance().get(userId);
        if(user==null){
            user=DBFriendAction.convertToFriendInfo(userId,userName,userHeadImage,false);
        }
        return user;
    }

    @Override
    public List<ChatUserMessage> getTopLocalMessages(long userId) {
        return DaoUtils.getChatUserMessageManagerInstance().getFromUserMessages(UserSP.getUserId(), userId, 0, pageSize);
    }

    @Override
    public void loadLocalMessages(long userId,long minMsgId,DefaultCallback<List<ChatUserMessage>, String> callback) {
    }
}
