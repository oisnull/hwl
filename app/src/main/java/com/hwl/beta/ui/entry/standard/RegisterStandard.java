package com.hwl.beta.ui.entry.standard;

import com.hwl.beta.ui.common.rxext.DefaultAction;
import com.hwl.beta.ui.common.rxext.DefaultConsumer;
import com.hwl.beta.ui.entry.bean.RegisterBean;

/**
 * RegisterStandard
 */
public interface RegisterStandard {

    RegisterBean getRegisterBean();

    void userRegister(RegisterBean registerBean, DefaultAction succCallback, DefaultConsumer<String> errorCallback);

    void sendCode(RegisterBean registerBean,DefaultAction succCallback,DefaultConsumer<String> errorCallback);
}