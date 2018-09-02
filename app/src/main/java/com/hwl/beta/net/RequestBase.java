package com.hwl.beta.net;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/14.
 */

public class RequestBase<T> implements Serializable {

    @SerializedName("Head")
    protected RequestHead requestHead;
    @SerializedName("Body")
    protected T requestBody;

    public RequestBase() {
        requestHead = new RequestHead();
        requestHead.setClientIp("127.0.0.1");
        requestHead.setSessionId("");
        requestHead.setTimestamp("20180114");
        requestHead.setToken("");
        requestHead.setVersion("1.0.0");
    }

    public RequestBase(T requestBody) {
        this();
        this.requestBody = requestBody;
    }


    public RequestBase(String token, T requestBody) {
        this(requestBody);
        requestHead.setToken(token);
    }

    public void setHeadToken(String token) {
        requestHead.setToken(token);
    }

    public RequestHead getRequestHead() {
        return requestHead;
    }

    public T getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(T requestBody) {
        this.requestBody = requestBody;
    }
}
