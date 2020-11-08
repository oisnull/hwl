package com.hwl.beta.net.general;

public class NetAppVersionInfo {
    private String AppName;
    private String AppVersion;
    private Long AppSize;
    private String DownloadUrl;
    private String UpgradeLog;

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public String getAppVersion() {
        return AppVersion;
    }

    public void setAppVersion(String appVersion) {
        AppVersion = appVersion;
    }

    public Long getAppSize() {
        return AppSize;
    }

    public void setAppSize(Long appSize) {
        AppSize = appSize;
    }

    public String getDownloadUrl() {
        return DownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        DownloadUrl = downloadUrl;
    }

    public String getUpgradeLog() {
        return UpgradeLog;
    }

    public void setUpgradeLog(String upgradeLog) {
        UpgradeLog = upgradeLog;
    }
}
