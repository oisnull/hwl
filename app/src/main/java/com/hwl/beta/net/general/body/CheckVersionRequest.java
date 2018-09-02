package com.hwl.beta.net.general.body;

public class CheckVersionRequest {
    private long UserId;
    private String OldVersion;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public String getOldVersion() {
        return OldVersion;
    }

    public void setOldVersion(String oldVersion) {
        OldVersion = oldVersion;
    }
}
