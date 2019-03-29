package com.hwl.beta.ui.entry.standard;

import com.hwl.beta.ui.entry.bean.MainBean;

public interface MainStandard {
    MainBean getMainBean();

    int getLocationStatus();
    Observable<String> getLocation();
}
