package com.hwl.beta.ui.imgselect.bean;

/**
 * Created by Administrator on 2018/4/4.
 */

public class ImageSelectType {
    public static final int USER_HEAD = 1;
    public static final int CHAT_PUBLISH = 2;
    public static final int CIRCLE_BACK_IMAGE = 3;

    public static boolean isShowCamera(int selectType) {
        switch (selectType) {
            case USER_HEAD:
            case CIRCLE_BACK_IMAGE:
                return true;
            default:
                return false;
        }
    }
}
