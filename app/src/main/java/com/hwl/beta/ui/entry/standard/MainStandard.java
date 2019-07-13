package com.hwl.beta.ui.entry.standard;

import com.hwl.beta.location.LocationModel;
import com.hwl.beta.ui.entry.bean.MainBean;

import io.reactivex.Observable;

public interface MainStandard {
    MainBean getMainBean();

    Observable<String> setLocation(LocationModel model);

    //Observable<String> getLocation();
}
