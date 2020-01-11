package com.hwl.beta.net.resx.body;

import okhttp3.MultipartBody;

public class ResxUploadRequest {
    private String Token;
    private long UserId;
    private int ResxType;

    public void setToken(String token) {
        Token = token;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public void setResxType(int resxType) {
        ResxType = resxType;
    }
}

