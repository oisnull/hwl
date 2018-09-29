package com.hwl.beta;

public class AppConfig {
    //Global config

    //release env
//    public static String NET_API_HOST = "http://115.29.179.171:8013/";
//    public static String NET_RESX_HOST = "http://115.29.179.171:8015/";

    //local env
    public static String NET_API_HOST = "http://192.168.16.1:8030/";
    public static String NET_RESX_HOST = "http://192.168.16.1:8033/";

    //im env
    public final static String IM_DEBUG_TAG = "hwl-im";
    public final static String IM_HOST = "192.168.16.1";
    public final static int IM_PORT = 8081;
}
