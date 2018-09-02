package com.hwl.beta.net;

/**
 * Created by Administrator on 2018/1/14.
 */

public class ResponseHead {
    public String ResultCode;
    public String ResultMessage;

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public String getResultMessage() {
        return ResultMessage;
    }

    public void setResultMessage(String resultMessage) {
        ResultMessage = resultMessage;
    }
}
