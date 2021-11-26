package com.hwl.beta.api;

/**
 * Created by Administrator on 2019/3/26.
 */

public class ApiException extends Exception {
    private ApiExceptionCode code;

    public ApiException(ApiExceptionCode code) {
        super(code.getDesc());
        this.code = code;
    }

    public ApiException(ApiExceptionCode code, String msg) {
        super(msg);
        this.code = code;
    }

    public ApiExceptionCode getCode() {
        return code;
    }
}
