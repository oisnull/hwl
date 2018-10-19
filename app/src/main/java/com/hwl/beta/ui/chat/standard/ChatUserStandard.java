package com.hwl.beta.ui.chat.standard;

import com.hwl.beta.db.entity.ChatUserMessage;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.ui.common.DefaultCallback;

import java.util.List;

public interface ChatUserStandard {
    Friend getChatUserInfo(long userId, String userName, String userHeadImage);

    List<ChatUserMessage> getTopLocalMessages(long userId);

    void loadLocalMessages(long userId,long minMsgId,DefaultCallback<List<ChatUserMessage>, String> callback);

    void clearRecordMessageCount(long userId);
}
