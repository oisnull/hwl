package com.hwl.beta.net;

/**
 * Created by Administrator on 2019/3/26.
 */

public class NetException extends Exception {
    private NetExceptionCode code;

    public NetException(NetExceptionCode code) {
        super(code.getDesc());
        this.code = code;
    }

    public NetException(NetExceptionCode code, String msg) {
        super(msg);
        this.code = code;
    }

    public NetExceptionCode getCode() {
        return code;
    }
}
