package com.hwl.beta.ui.chat.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.ui.chat.standard.RecordStandard;

import java.util.List;

public class RecordLogic implements RecordStandard {
    @Override
    public List<ChatRecordMessage> loadRecords() {
        return DaoUtils.getChatRecordMessageManagerInstance().getRecords();
    }
}
