package com.hwl.beta.ui.chat.standard;

import com.hwl.beta.db.entity.ChatGroupMessage;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.ui.common.DefaultCallback;

import java.util.List;

public interface ChatGroupStandard {
    GroupInfo getChatGroupInfo(String groupGuid);

    List<ChatGroupMessage> getTopLocalMessages(String groupGuid);

    void loadLocalMessages(String groupGuid, long minMsgId, DefaultCallback<List<ChatGroupMessage>,
            String> callback);

    void clearRecordMessageCount(String groupGuid);
}
