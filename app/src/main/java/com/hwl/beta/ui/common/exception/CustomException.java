package com.hwl.beta.ui.common.exception;

public class CustomException extends Exception {
    private ExceptionCode code;

    public CustomException(ExceptionCode code) {
        super(code.getDesc());
        this.code = code;
    }

    public CustomException(ExceptionCode code, String msg) {
        super(msg);
        this.code = code;
    }

    public ExceptionCode getCode() {
        return code;
    }
}
