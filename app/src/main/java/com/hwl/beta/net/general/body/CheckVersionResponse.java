package com.hwl.beta.net.general.body;

import com.hwl.beta.net.general.NetAppVersionInfo;

public class CheckVersionResponse {

    private boolean IsNewVersion;
    private NetAppVersionInfo AppVersionInfo;

    public NetAppVersionInfo getAppVersionInfo() {
        return AppVersionInfo;
    }

    public void setAppVersionInfo(NetAppVersionInfo appVersionInfo) {
        AppVersionInfo = appVersionInfo;
    }

    public boolean isNewVersion() {
        return IsNewVersion;
    }

    public void setNewVersion(boolean newVersion) {
        IsNewVersion = newVersion;
    }

}
