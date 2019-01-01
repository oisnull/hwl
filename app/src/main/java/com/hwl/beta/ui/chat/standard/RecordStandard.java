package com.hwl.beta.ui.chat.standard;

import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.ui.common.DefaultCallback;

import java.util.List;

public interface RecordStandard {
    List<ChatRecordMessage> loadRecords();

    void deleteRecord(ChatRecordMessage recordMessage, DefaultCallback<Boolean, String> callback);

    void clearMessages(ChatRecordMessage recordMessage, DefaultCallback<Boolean, String> callback);
}
