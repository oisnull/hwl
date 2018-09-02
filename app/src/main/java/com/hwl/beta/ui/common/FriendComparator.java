package com.hwl.beta.ui.common;

import com.hwl.beta.db.entity.Friend;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/12/29.
 */

public class FriendComparator implements Comparator<Friend> {

    public int compare(Friend o1, Friend o2) {
        if (o1.getFirstLetter().equals("@")
                || o2.getFirstLetter().equals("#")) {
            return -1;
        } else if (o1.getFirstLetter().equals("#")
                || o2.getFirstLetter().equals("@")) {
            return 1;
        } else {
            return o1.getFirstLetter().compareTo(o2.getFirstLetter());
        }
    }

}

