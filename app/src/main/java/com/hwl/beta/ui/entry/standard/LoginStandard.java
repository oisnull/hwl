package com.hwl.beta.ui.entry.standard;

import com.hwl.im.common.DefaultAction;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.beta.ui.entry.bean.LoginBean;

public interface LoginStandard {

    LoginBean getLoginBean();

    void userLogin(LoginBean loginBean, DefaultAction succCallback, DefaultConsumer<String> errorCallback);
}