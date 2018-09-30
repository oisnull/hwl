package com.hwl.beta.ui.entry.standard;

import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.entry.bean.MainBean;

public interface MainStandard {
    MainBean getMainBean();

    int getLocationStatus();
    void getLocation(DefaultCallback<String,String> callback);// DefaultConsumer<String> succCallback, DefaultConsumer<String> errorCallback);
}
