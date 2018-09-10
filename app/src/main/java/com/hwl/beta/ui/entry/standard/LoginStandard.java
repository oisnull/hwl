package com.hwl.beta.ui.entry.standard;

import com.hwl.beta.ui.common.rxext.DefaultAction;
import com.hwl.beta.ui.common.rxext.DefaultConsumer;
import com.hwl.beta.ui.entry.bean.LoginBean;

public interface LoginStandard {

    LoginBean getLoginBean();

    void userLogin(LoginBean loginBean, DefaultAction succCallback, DefaultConsumer<String> errorCallback);
}