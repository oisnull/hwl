package com.hwl.beta.net;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/14.
 */

public class ResponseBase<T> implements Serializable {

    @SerializedName("Head")
    protected ResponseHead responseHead;
    @SerializedName("Body")
    protected T responseBody;

    public ResponseHead getResponseHead() {
        return responseHead;
    }

    public void setResponseHead(ResponseHead responseHead) {
        this.responseHead = responseHead;
    }

    public T getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(T responseBody) {
        this.responseBody = responseBody;
    }
}
