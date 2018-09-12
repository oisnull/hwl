package com.hwl.beta.ui.entry.standard;

import com.hwl.beta.ui.common.rxext.DefaultAction;
import com.hwl.beta.ui.common.rxext.DefaultConsumer;
import com.hwl.beta.ui.entry.bean.MainBean;

public interface MainStandard {
    MainBean getMainBean();
    void getLocation(DefaultAction succCallback, DefaultConsumer<String> errorCallback);
}
