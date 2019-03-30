package com.hwl.beta.ui.entry.standard;

import com.hwl.beta.ui.entry.bean.RegisterBean;

import io.reactivex.Observable;

/**
 * RegisterStandard
 */
public interface RegisterStandard {

    RegisterBean getRegisterBean();

    Observable<Boolean> userRegister(RegisterBean registerBean);

    Observable<Boolean> sendCode(RegisterBean registerBean);
}