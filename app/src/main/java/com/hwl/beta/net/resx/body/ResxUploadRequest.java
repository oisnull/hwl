package com.hwl.beta.net.resx.body;

public class ResxUploadRequest {
    private boolean Token;
	private MultipartBody.Part Files;
    private long UserId;
    private int ResxType;
	
    public void setToken(boolean token) {
        Token = token;
    }
	
    public void setFiles(MultipartBody.Part files) {
        Files = files;
    }
	
    public void setUserId(long userId) {
        UserId = userId;
    }
	
    public void setResxType(int resxType) {
        ResxType = resxType;
    }
}

