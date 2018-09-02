package com.hwl.beta.net;

/**
 * Created by Administrator on 2018/2/10.
 */

public class NetConstant {
    public static final String RESPONSE_CACHE = "response_cache";
    public static final long RESPONSE_CACHE_SIZE = 5 * 1024 * 1024;
    public static final long HTTP_CONNECT_TIMEOUT = 10 * 1000;
    public static final long HTTP_READ_TIMEOUT = 10 * 1000;

    public final static String RESPONSE_SUCCESS = "success";
    public final static String RESPONSE_FAILED = "failed";
    public final static String RESPONSE_RELOGIN = "relogin";

    public final static int RESULT_SUCCESS = 1;
    public final static int RESULT_FAILED = 2;
    public final static int RESULT_NONE = 3;

//    public final static int RESX_TYPE_CHATIMAGE = 0x1;
//    public final static int RESX_TYPE_CIRCLEBACK = 0x2;
//    public final static int RESX_TYPE_CIRCLEPOST = 0x3;
//    public final static int RESX_TYPE_CHATSOUND = 0x4;
//    public final static int RESX_TYPE_USERHEADIMAGE = 0x5;
//    public final static int RESX_TYPE_CHATVIDEO = 0x6;
//    public final static int RESX_TYPE_OTHER = 0x7;

    public final static int CIRCLE_CONTENT_NULL=0;
    public final static int CIRCLE_CONTENT_TEXT=1;
    public final static int CIRCLE_CONTENT_IMAGE=2;
    public final static int CIRCLE_CONTENT_Link=3;
    public final static int CIRCLE_CONTENT_TEXTIMAGE=4;

    public final static int SEND_STATUS_PROGRESSING = 0x1;
    public final static int SEND_STATUS_SUCCESS = 0x2;
    public final static int SEND_STATUS_COMPLETE = 0x3;
    public final static int SEND_STATUS_FAILED = 0x4;
}
