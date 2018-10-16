package com.hwl.beta.ui.chat.standard;

import com.hwl.beta.db.entity.ChatRecordMessage;

import java.util.List;

public interface RecordStandard {
    List<ChatRecordMessage> loadRecords();
}
