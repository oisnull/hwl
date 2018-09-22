package com.hwl.beta.ui.entry.standard;

import com.hwl.im.common.DefaultConsumer;
import com.hwl.beta.ui.entry.bean.MainBean;

public interface MainStandard {
    MainBean getMainBean();

    int getLocationStatus();
    void getLocation(DefaultConsumer<String> succCallback, DefaultConsumer<String> errorCallback);
}
