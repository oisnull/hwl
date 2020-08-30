package com.hwl.beta;

public class AppConfig {
    //Global config
    public static final String APP_DEFAULT_NAME = "知了";
    public static final Boolean ENABLE_DEBUG = true;
    public final static String EMOTION_ENTRANCE_URL = "http://111.229.252.205:8082/emotion/index";

    //release env
//    public static String NET_API_HOST = "http://111.229.252.205:8083/";
//    public static String NET_RESX_HOST = "http://111.229.252.205:8085/";
//
//    public final static String IM_DEBUG_TAG = "zl-im";
//    public final static String IM_HOST = "111.229.252.205";
//    public final static int IM_PORT = 8081;

    //local env
    public static String NET_API_HOST = "http://192.168.2.210:8030/";
    public static String NET_RESX_HOST = "http://192.168.2.210:8033/";

    public final static String IM_DEBUG_TAG = "zl-im";
    public final static String IM_HOST = "192.168.2.210";
    public final static int IM_PORT = 8081;
}
