package com.hwl.beta.api;

/**
 * Created by Administrator on 2019/3/26.
 */

public enum ApiExceptionCode {
    TokenInvalid(1, "Token is invalid, need to login again.");

    private String desc;
    private int index;

    ApiExceptionCode(int index, String desc) {
        this.desc = desc;
        this.index = index;
    }

    public static String getDesc(int index) {
        for (ApiExceptionCode c : ApiExceptionCode.values()) {
            if (c.getIndex() == index) {
                return c.desc;
            }
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static boolean isTokenInvalid(Throwable e) {
        return e instanceof ApiException && ((ApiException) e).getCode() == ApiExceptionCode.TokenInvalid;
    }
}
