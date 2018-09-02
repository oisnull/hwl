package com.hwl.beta.net.general.body;

public class CheckVersionResponse {

    private boolean IsNewVersion;
    private String NewVersion;
    private String DownLoadUrl;

    public boolean isNewVersion() {
        return IsNewVersion;
    }

    public void setNewVersion(boolean newVersion) {
        IsNewVersion = newVersion;
    }

    public String getNewVersion() {
        return NewVersion;
    }

    public void setNewVersion(String newVersion) {
        NewVersion = newVersion;
    }

    public String getDownLoadUrl() {
        return DownLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        DownLoadUrl = downLoadUrl;
    }
}
