package com.hwl.beta;

public class AppConfig {
    //Global config

    //release env
//    public static String NET_API_HOST = "http://115.29.179.171:8013/";
//    public static String NET_RESX_HOST = "http://115.29.179.171:8015/";
//
//    public final static String IM_DEBUG_TAG = "hwl-im";
//    public final static String IM_HOST = "115.29.179.171";
//    public final static int IM_PORT = 8017;

    //local env
    public static String NET_API_HOST = "http://192.168.2.210:8030/";
    public static String NET_RESX_HOST = "http://192.168.2.210:8033/";

    public final static String IM_DEBUG_TAG = "hwl-im";
    public final static String IM_HOST = "192.168.2.210";
    public final static int IM_PORT = 8081;

    public final static String EMOTION_ENTRANCE_URL = "http://192.168.2.210:8032/emotion/index";

    //im release env
    //net api
    public final static String NET_API_DEBUG_TAG = "hwl-api";

    /**
     * chat group setting
     * chat group setting edit
     * add chat group user
     * chat user setting
     *
     * add im server disconncet message send
     *
     * */
}
