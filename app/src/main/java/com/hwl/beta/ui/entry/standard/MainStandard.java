package com.hwl.beta.ui.entry.standard;

import com.hwl.beta.ui.entry.bean.MainBean;

import io.reactivex.Observable;

public interface MainStandard {
    MainBean getMainBean();

    int getLocationStatus();
    Observable<String> getLocation();
}
