package com.hwl.beta.ui.common;

import com.hwl.beta.db.entity.ChatRecordMessage;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by Administrator on 2018/2/8.
 */

public class ChatRecordMessageComparator implements Comparator<ChatRecordMessage> {

    public int compare(ChatRecordMessage o1, ChatRecordMessage o2) {
        Date begin = o1.getSendTime();
        Date end = o2.getSendTime();
        if (begin == null || end == null) {
            return -1;
        }
        if (begin.after(end)) {
            return -1;
        } else {
            return 1;
        }
    }
}
