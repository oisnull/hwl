package com.hwl.beta.ui.immsg.listen;

import com.hwl.beta.net.general.NetAppVersionInfo;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.utils.AppUtils;
import com.hwl.im.imaction.AbstractMessageListenExecutor;
import com.hwl.imcore.improto.ImAppVersionContent;
import com.hwl.imcore.improto.ImAppVersionResponse;
import com.hwl.imcore.improto.ImMessageResponse;
import com.hwl.imcore.improto.ImMessageType;

public class AppVersionListen extends AbstractMessageListenExecutor<ImAppVersionResponse> {

    private ImAppVersionContent messageContent;

    @Override
    public void executeCore(ImMessageType messageType, ImAppVersionResponse response) {
        super.executeCore(messageType, response);
        this.messageContent = response.getAppVersionContent();

        //检测是否需要升级
        boolean isUp = AppUtils.isUpgrade(messageContent.getAppVersion(),
                AppUtils.getAppVersionName());
        if (!isUp) return;

        NetAppVersionInfo info = new NetAppVersionInfo();
        info.setAppName(messageContent.getAppName());
        info.setAppSize(messageContent.getAppSize());
        info.setAppVersion(messageContent.getAppVersion());
        info.setDownloadUrl(messageContent.getDownloadUrl());
        info.setUpgradeLog(messageContent.getUpgradeLog());
        MessageCountSP.setAppVersionInfo(info);
        EventBusUtil.sendAppVersionUpdateEvent();
    }

    @Override
    public ImAppVersionResponse getResponse(ImMessageResponse response) {
        return response.getAppVersionResponse();
    }

}